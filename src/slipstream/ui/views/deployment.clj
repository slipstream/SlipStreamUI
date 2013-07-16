(ns slipstream.ui.views.deployment
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.module :as module-model]))

(def deployment-view-template-html "slipstream/ui/views/deployment-view.html")
(def deployment-edit-template-html "slipstream/ui/views/deployment-edit.html")
(def deployment-new-template-html "slipstream/ui/views/deployment-new.html")

(def module-login-sel [:#module-login])
(def run-with-options-dialog-sel [:#run-with-options-dialog])
(def nodes-sel [:#nodes])
(def parameters-mapping-sel [:#parameters-mapping])

;; View

(html/defsnippet run-with-options-dialog-snip deployment-view-template-html [run-with-options-dialog-sel]
  [deployment]
  [:> :div] (html/content
              (for [node (module-model/nodes deployment)]
                (list
                  (html/html-snippet (str "\n    <h3>" (common-model/elem-name node) "</h3>"))
                  (common/parameters-edit-snip 
                    (common-model/filter-by-categories
                      (common-model/parameters node)
                      ["Input"]))))))

(html/defsnippet summary-view-snip deployment-view-template-html module-base/module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-version] (html/html-content
                         (str (:version (module-model/attrs module))
                              "<span> (<a href='/module/"
                              (:name (module-model/attrs module))
                              "/'>history</a>)</span>"))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-comment] (html/content (module-model/module-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet summary-edit-snip deployment-edit-template-html module-base/module-summary-sel
  [module]
  [:#module-name :> :span] (html/content (:name (module-model/attrs module)))
  [:#module-name :> :input] (html/set-attr :value (:name (module-model/attrs module)))
  [:#module-description :> :input] (html/set-attr :value (:description (module-model/attrs module)))
  [:#module-comment] (html/content (module-model/module-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet summary-new-snip deployment-new-template-html module-base/module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet parameters-mapping-snip deployment-edit-template-html parameters-mapping-sel
  [parameters node-index]
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
    [[:td (html/nth-of-type 1)] :> :input]
    (html/do->
      (html/set-attr :name (str id-prefix "input"))
      (html/set-attr :value (common-model/elem-name parameter)))
    [[:td (html/nth-of-type 2)] :> :input]
    (html/do->
      (html/set-attr :name (str id-prefix "output"))
      (html/set-attr :value (common-model/value parameter)))))

(html/defsnippet nodes-edit-snip deployment-edit-template-html nodes-sel
  [nodes available-clouds]
  [nodes-sel :> :table :> :tbody :> :tr] 
  (html/clone-for
    [i (range (count nodes))
     :let [node (nth nodes i)
           attrs (common-model/attrs node)
           id-prefix (str "node--" i "--")
           image-uri (:imageuri attrs)]]
    [html/this-node] (html/set-attr :id (str "node--" i))
    ; node name
    [[:td.node_name] :> :input]
    (html/do->
      (html/set-attr :value (common-model/elem-name node))
      (html/set-attr :name (str id-prefix "shortname")))

    ; reference
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 1)] :> :td :> :a]
    (module-base/set-a image-uri)
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 1)] :> :td :> :input]
    (html/do->
      (html/set-attr :name (str id-prefix "imagelink"))
      (html/set-attr :value (str "/" image-uri)))

    ; multiplicity
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 2)] :> :td :> :input]
    (html/do->
      (html/set-attr :value (:multiplicity attrs))
      (html/set-attr :name (str id-prefix "multiplicity--value")))

    ; default cloud
    [:td :> [:table.image_link] :> :tbody :> [:tr (html/nth-of-type 3)] :> :td :> :select]
    (html/substitute
      (html/html-snippet
        (str "<select name='" id-prefix "cloudservice--value'>\n")
             (apply str
                    (for [cloud available-clouds]
                      (str 
                        "<option value='" cloud "'"
                        (if (= cloud (:cloudservice attrs))
                          " selected"
                          "")
                        ">"
                        cloud
                        "</option>\n"
                        )))
             "</select>\n"))
    
    ; parameter mapping
    parameters-mapping-sel
    (html/substitute
      (parameters-mapping-snip (common-model/parameter-mappings node) i))))

(html/defsnippet nodes-view-snip deployment-view-template-html nodes-sel
  [nodes available-clouds]
  nodes-sel (html/substitute (nodes-edit-snip nodes available-clouds))
  [:input] (html/set-attr :disabled "disabled"))

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
                                   (summary-view-snip deployment))
  nodes-sel (html/substitute
              (nodes-view-snip
                (module-model/nodes deployment)
                (module-model/available-clouds deployment)))

  [:.refqname] (html/set-attr :value (common-model/resourceuri deployment))

  run-with-options-dialog-sel (html/substitute (run-with-options-dialog-snip deployment))

  [:#build-form] (html/set-attr :value (common-model/resourceuri deployment))
  
  [:#publish-form] (html/set-attr :action (str "/"
                                           (common-model/resourceuri deployment)
                                           "/publish"))
  
  module-base/module-interaction-top-sel
  (html/substitute
    (view-interaction-snip deployment))
  
  module-base/module-interaction-bottom-sel
  (html/substitute
    (view-interaction-snip deployment))
  
  authz/authorization-sel (html/substitute (authz/authz-view-snip deployment)))

(html/defsnippet new-snip deployment-new-template-html common/content-sel
  [deployment]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name deployment))
  module-base/module-summary-sel (html/substitute 
                                   (summary-new-snip deployment)))

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
