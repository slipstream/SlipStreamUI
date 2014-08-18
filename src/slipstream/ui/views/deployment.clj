(ns slipstream.ui.views.deployment
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.runs :as runs]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.run :as run-model]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.module :as module-model]))

(def deployment-view-template-html "slipstream/ui/views/deployment-view.html")
(def deployment-edit-template-html "slipstream/ui/views/deployment-edit.html")
(def deployment-new-template-html "slipstream/ui/views/deployment-new.html")

(def module-login-sel [:#module-login])
(def nodes-sel [:#nodes])
(def parameters-mapping-sel [:#parameters-mapping])

;; View

(html/defsnippet run-parameters-edit-snip common/parameter-edit-template-html [:#run-parameters-edit]
  [node available-clouds]
  [:#run-parameters-edit [:input html/any-node]] (html/replace-vars (common-model/attrs node))
  [:#run-parameters-edit :>  :table :> :tbody :> [:tr (html/nth-of-type 2)] :> :td :> :select]
  (html/substitute
    (common/gen-select
      (str "parameter--node--" (common-model/elem-name node) "--cloudservice")
      available-clouds
      (:cloudservice (common-model/attrs node))))
  [:#run-parameters-edit :> :table :> :tbody :> [:tr html/last-of-type]]
  (html/clone-for
    [p (filter
         #(and
            (not
              (empty? (common-model/value %)))
            (not
              (nil? (re-find #"^[\"'].*[\"']$" (common-model/value %)))))
           (common-model/parameter-mappings node))
     :let [node-name (common-model/elem-name node)
           name (common-model/elem-name p)
           value (common-model/value p)]]
    [[:td html/first-of-type]] (html/content name)
    [[:td (html/nth-of-type 2)] :> :input] (html/do->
                                 (html/set-attr :name (str "parameter--node--" node-name "--" name))
                                 (html/set-attr :value value))))

(html/defsnippet run-with-options-dialog-snip deployment-view-template-html module-base/run-with-options-dialog-sel
  [deployment]
  [:form :> :div :> [:h3 html/last-of-type]] nil ; remove unwanted part
  [:form :> :div :> [:div html/last-of-type]] nil ; remove unwanted part
  [:form :> :div :> :#run-parameters-edit-global]
  (html/after
    (for [node (module-model/nodes deployment)]
      (list
        (html/html-snippet (str "\n    <h3>" (common-model/elem-name node) "</h3>"))
        (run-parameters-edit-snip
          node
          (module-model/available-clouds deployment))))))

(html/defsnippet parameters-mapping-snip deployment-edit-template-html parameters-mapping-sel
  [parameters node-index view?]
  ; make the header visible if there are parameters
  [parameters-mapping-sel :> :td :> :table :> :thead :> :tr]
  (if (pos? (count parameters))
    (html/remove-class "hidden")
    (html/add-class "hidden"))
  [parameters-mapping-sel :> :td :> :table :> :tbody :> :tr]
  (html/clone-for
    [i (range (count parameters))
     :let [parameter (nth parameters i)
           id-prefix (str "node--" node-index "--mappingtable--" i "--")]]
    html/this-node (html/remove-attr :id)
    [[:td (html/nth-of-type 1)] :> :input]
    (html/do->
      (html/set-attr :name (str id-prefix "input"))
      (html/set-attr :value (common-model/elem-name parameter)))
    [[:td (html/nth-of-type 2)] :> :input]
    (html/do->
      (html/set-attr :name (str id-prefix "output"))
      (html/set-attr :value (common-model/value parameter))))
  parameters-mapping-sel
  (if (and view? (zero? (count parameters)))
    nil
    identity)
  [parameters-mapping-sel :> :td :> :table] (html/set-attr :id (str "node--" node-index "--mappingtable")))

(defn node-trans
  [node i available-clouds attrs id-prefix image-uri view?]
  (html/transformation
    html/this-node (html/set-attr :id (str "node--" i))
    ; node name
    [[:td.nodename] :> :input]
    (html/do->
      (html/set-attr :value (common-model/elem-name node))
      (html/set-attr :name (str id-prefix "shortname")))

    ; reference
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 1)] :> :td :> :a]
    (module-base/set-a image-uri)
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 1)] :> :td :> :a]
    (if
      (empty? (module-model/image node))
      (html/after
        (html/html-snippet
          " <span><a href='#' title='Missing image'><i style='color:red' class='error icon-warning-sign' /></a></span>"))
    identity)

    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 1)] :> :td :> :input]
    (html/do->
      (html/set-attr :name (str id-prefix "imagelink"))
      (html/set-attr :value image-uri))

    ; multiplicity
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 2)] :> :td :> :input]
    (html/do->
      (html/set-attr :value (:multiplicity attrs))
      (html/set-attr :name (str id-prefix "multiplicity--value")))

    ; default cloud
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 3)] :> :td :> :select]
    (html/substitute
      (common/gen-select
        (str id-prefix "cloudservice--value")
        available-clouds
        (:cloudservice attrs)
        view?))

    ; parameter mapping
    parameters-mapping-sel
    (html/substitute
      (parameters-mapping-snip (common-model/parameter-mappings node) i view?))))

(html/defsnippet nodes-snip deployment-edit-template-html nodes-sel
  [nodes available-clouds view?]
  [:#cloudServiceNamesList] (html/substitute
                              (common/gen-select
                                "cloudServiceNamesList"
                                available-clouds
                                false))
  [nodes-sel :> :table :> :tbody :> :tr]
  (html/clone-for
    [i (range (count nodes))
     :let [node (nth nodes i)
           attrs (common-model/attrs node)
           id-prefix (str "node--" i "--")
           image-uri (:imageuri attrs)]]
    (node-trans node i available-clouds attrs id-prefix image-uri view?)))

(html/defsnippet nodes-view-snip deployment-view-template-html nodes-sel
  [nodes available-clouds]
  nodes-sel (html/substitute (nodes-snip nodes available-clouds true))
  [nodes-sel :button] (html/substitute nil)
  [nodes-sel [#{[:td] [:th]} (html/nth-of-type 3)]] (html/substitute nil)
  [:input] (html/set-attr :disabled "disabled"))

(html/defsnippet nodes-edit-snip deployment-view-template-html nodes-sel
  [nodes available-clouds]
  nodes-sel (html/substitute (nodes-snip nodes available-clouds false)))

(defn authz-buttons
  [module]
  (let
    [authz (authz-model/authz module)
     user (user-model/user module)
     can-get? (authz-model/can-get? authz user)
     can-put? (authz-model/can-put? authz user)
     can-delete? (authz-model/can-delete? authz user)
     can-post? (authz-model/can-post? authz user)
     can-createchildren? (authz-model/can-createchildren? authz user)
     super? (user-model/super? user)
     published? (module-model/published? module)]
    (html/transformation
      #{[:#build-button-top] [:#build-button-bottom]}
      (if can-post?
        (html/remove-attr :disabled)
        (html/set-attr :disabled "disabled"))
      #{[:#edit-button-top] [:#edit-button-bottom]}
      (if can-put?
        (html/remove-attr :disabled)
        (html/set-attr :disabled "disabled"))
      #{module-base/module-publish-button-top module-base/module-publish-button-bottom
        module-base/module-unpublish-button-top module-base/module-unpublish-button-bottom}
      (if super?
        (html/remove-attr :disabled)
        (html/set-attr :disabled "disabled"))
      #{module-base/module-publish-button-top module-base/module-publish-button-bottom}
      (if published?
        nil
        identity)
      #{module-base/module-unpublish-button-top module-base/module-unpublish-button-bottom}
      (if published?
        identity
        nil)
      )))

(html/defsnippet view-interaction-snip deployment-view-template-html module-base/module-interaction-top-sel
  [module]
  (authz-buttons module))

(html/defsnippet view-snip deployment-view-template-html common/content-sel
  [deployment]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name deployment))
  module-base/module-summary-sel (html/substitute
                                   (module-base/module-summary-view-snip deployment))
  nodes-sel (html/substitute
              (nodes-view-snip
                (module-model/nodes deployment)
                (module-model/available-clouds deployment)))

  [[:input (html/attr-has :name "refqname")]] (html/set-attr :value (common-model/resourceuri deployment))

  module-base/run-with-options-dialog-sel (html/substitute (run-with-options-dialog-snip deployment))

  [:#publish-form] (html/set-attr :action (str "/"
                                           (common-model/resourceuri deployment)
                                           "/publish"))

  ; Copy to
  [:#source_uri] (html/set-attr :value (common-model/resourceuri deployment))
  [:#target_name] (html/set-attr :value (:shortname (common-model/attrs deployment)))

  module-base/module-interaction-top-sel
  (html/substitute
    (view-interaction-snip deployment))

  module-base/module-interaction-bottom-sel
  (html/substitute
    (view-interaction-snip deployment))

  runs/runs-sel (html/content (runs/runs-snip (run-model/group-by-cloud deployment)))

  authz/authorization-sel (html/substitute (authz/authz-view-snip deployment)))

(html/defsnippet new-summary-snip deployment-new-template-html module-base/module-summary-sel
  [module]
  (module-base/module-summary-trans module))

(html/defsnippet new-snip deployment-edit-template-html common/content-sel
  [deployment]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name deployment))
  module-base/module-summary-sel (html/substitute
                                   (new-summary-snip deployment))

  nodes-sel (html/substitute
              (nodes-edit-snip
                (module-model/nodes deployment)
                (module-model/available-clouds deployment)))

  authz/authorization-sel (html/substitute (authz/authz-edit-snip deployment)))


(html/defsnippet edit-snip deployment-edit-template-html common/content-sel
  [deployment]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name deployment))
  module-base/module-summary-sel (html/substitute
                                   (module-base/module-summary-edit-snip deployment))
  nodes-sel (html/substitute
              (nodes-edit-snip
                (module-model/nodes deployment)
                (module-model/available-clouds deployment)))

  authz/authorization-sel (html/substitute (authz/authz-edit-snip deployment)))
