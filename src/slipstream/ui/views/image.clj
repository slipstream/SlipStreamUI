(ns slipstream.ui.views.image
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.views.runs :as runs]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.run :as run-model]
            [slipstream.ui.models.image :as image-model]))

(def image-view-template-html "slipstream/ui/views/image-view.html")
(def image-edit-template-html "slipstream/ui/views/image-edit.html")
(def image-new-template-html "slipstream/ui/views/image-new.html")

(def image-reference-sel [:#module-reference])
(def image-image-ids-sel [:#module-image-ids])
(def image-is-base-sel [:#module-is-base])
(def image-platform-sel [:#module-platform])
(def image-cloud-configuration-sel [:#cloud-configuration])
(def image-creation-sel [:#creation])
(def image-creation-header-sel [:#creation-header])
(def image-deployment-sel [:#deployment])
(def image-deployment-header-sel [:#deployment-header])
(def deployment-parameter-categories ["Input" "Output"])
(def parameter-cloudservice-name "parameter--cloudservice")
(def cloudservice-sel [(keyword (str "#" parameter-cloudservice-name))])

(def platforms
  ["centos"
   "debian"
   "fedora"
   "opensuse"
   "redhat"
   "sles"
   "ubuntu"
   "windows"
   "other"])

;; View

(html/defsnippet run-with-options-dialog-snip image-view-template-html module-base/run-with-options-dialog-sel
  [module]
  [:select]
  (html/substitute
    (common/gen-select
      "parameter--cloudservice"
      (module-model/available-clouds module)
      (-> module user-model/default-cloud))))

(html/defsnippet build-with-options-dialog-snip image-view-template-html module-base/build-with-options-dialog-sel
  [module]
  [:select]
  (html/substitute
    (common/gen-select
      "parameter--cloudservice"
      (module-model/available-clouds module)
      (-> module user-model/default-cloud))))

(defn- creation-trans
  [recipe prerecipe packages view?]

  (html/transformation
    [:ul :> :#fragment-creation-recipe-header]
    (if (and
          view?
          (string/blank? recipe))
      nil
      identity)
    [:ul :> :#fragment-creation-packages-header]
    (if (and
          view?
          (empty? packages))
      nil
      identity)
    [:ul :> :#fragment-creation-prerecipe-header]
    (if (and
          view?
          (string/blank? prerecipe))
      nil
      identity)

    [:#recipe]
    (html/content recipe)
    [:#fragment-creation-recipe]
    (if (and
          view?
          (string/blank? recipe))
      nil
      identity)

    [:#packages :> :tbody :> :tr]
    (html/clone-for
      [i (range (count packages))
       :let [attrs (module-model/attrs
                     (nth
                       (common-model/sort-by-name packages) i))
             package-name (:name attrs)
             id (str "package--" i)
             repository (:repository attrs)
             key (:key attrs)]]
      html/this-node (html/set-attr :id id)
      [[:td (html/nth-of-type 1)] :> :span] (html/content package-name)
      [[:td (html/nth-of-type 1)] :> :input] (html/set-attr :value package-name)
      [[:td (html/nth-of-type 1)] :> :input] (html/set-attr :name (str id "--name"))
      [[:td (html/nth-of-type 2)] :> :span] (html/content repository)
      [[:td (html/nth-of-type 2)] :> :input] (html/set-attr :value repository)
      [[:td (html/nth-of-type 2)] :> :input] (html/set-attr :name (str id "--repository"))
      [[:td (html/nth-of-type 3)] :> :span] (html/content key)
      [[:td (html/nth-of-type 3)] :> :input] (html/set-attr :value key)
      [[:td (html/nth-of-type 3)] :> :input] (html/set-attr :name (str id "--key")))
    [:#fragment-creation-packages]
    (if (and
          view?
          (empty? packages))
      nil
      identity)

    [:#prerecipe]
    (html/content prerecipe)
    [:#fragment-creation-prerecipe]
    (if (and
          view?
          (string/blank? prerecipe))
      nil
      identity)))

(html/defsnippet creation-view-snip image-view-template-html image-creation-sel
  [recipe prerecipe packages]
  (creation-trans recipe prerecipe packages true))

(html/defsnippet creation-edit-snip image-edit-template-html image-creation-sel
  [recipe prerecipe packages]
  (creation-trans recipe prerecipe packages false))

(defn- deployment-trans
  [execute report onvmadd onvmremove parameters parameters-gen-fn]
  (html/transformation
    [:ul :> :#fragment-deployment-execute-header]
    (if (string/blank? execute)
      nil
      identity)
    [:ul :> :#fragment-deployment-report-header]
    (if (string/blank? report)
      nil
      identity)
    [:ul :> :#fragment-deployment-parameters-header]
    (if (empty? parameters)
      nil
      identity)
    [:ul :> :#fragment-deployment-onvmadd-header]
    (if (string/blank? onvmadd)
      nil
      identity)
    [:ul :> :#fragment-deployment-onvmremove-header]
    (if (string/blank? onvmremove)
      nil
      identity)

    [:#execute]
    (html/content execute)
    [:#fragment-deployment-execute]
    (if (string/blank? execute)
      nil
      identity)

    [:#report]
    (html/content report)
    [:#fragment-deployment-report]
    (if (string/blank? report)
      nil
      identity)

    [:#fragment-deployment-parameters]
    (html/content (parameters-gen-fn parameters true))
    [:#fragment-deployment-parameters]
    (if (empty? parameters)
      nil
      identity)

    [:#onvmadd]
    (html/content onvmadd)
    [:#fragment-deployment-onvmadd]
    (if (string/blank? onvmadd)
      nil
      identity)

    [:#onvmremove]
    (html/content onvmremove)
    [:#fragment-deployment-onvmremove]
    (if (string/blank? onvmremove)
      nil
      identity)))

(html/defsnippet deployment-view-snip image-view-template-html image-deployment-sel
  [execute report onvmadd onvmremove parameters]
  (deployment-trans execute report onvmadd onvmremove parameters common/parameters-view-with-name-and-category-snip))

(html/defsnippet deployment-edit-snip image-edit-template-html image-deployment-sel
  [execute report onvmadd onvmremove parameters]
  (deployment-trans execute report onvmadd onvmremove parameters common/parameters-edit-all-snip))

;; Edit

(defn authz-button
 [can?]
 (if can?
   (html/remove-attr :disabled)
   (html/set-attr :disabled "disabled")))

(defn remove-elem-if
  [remove?]
  (if remove?
    nil
    identity))

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
      #{[:#build-with-options-button-top] [:#build-with-options-button-bottom]}
      (authz-button can-post?)
      #{[:#run-with-options-button-top] [:#run-with-options-button-bottom]}
      (authz-button can-post?)
      #{[:#edit-button-top] [:#edit-button-bottom] [:#save-button-top] [:#save-button-bottom]}
      (authz-button can-put?)
      #{[:#delete-button-top] [:#delete-button-bottom]}
      (authz-button can-delete?)
      #{module-base/module-publish-button-top module-base/module-publish-button-bottom
        module-base/module-unpublish-button-top module-base/module-unpublish-button-bottom}
      (remove-elem-if (not super?))
      #{module-base/module-publish-button-top module-base/module-publish-button-bottom}
      (remove-elem-if published?)
      #{module-base/module-unpublish-button-top module-base/module-unpublish-button-bottom}
      (remove-elem-if (not published?)))))

(html/defsnippet view-interaction-snip image-view-template-html module-base/module-interaction-top-sel
  [module]
  (authz-buttons module))

(html/defsnippet view-snip image-view-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))
  module-base/module-summary-sel (html/substitute
                                   (module-base/module-summary-view-snip module))

  [[:input (html/attr-has :name "refqname")]] (html/set-attr :value (common-model/resourceuri module))

  module-base/run-with-options-dialog-sel
    (html/substitute
      (run-with-options-dialog-snip module))

  module-base/build-with-options-dialog-sel
    (html/substitute
      (build-with-options-dialog-snip module))

  [image-image-ids-sel :> [:div html/first-of-type]]
    (html/clone-for
      [id-pair (image-model/cloud-image-ids module)
      :let [attrs (module-model/attrs id-pair)
            cloud-service-name (:cloudservicename attrs)
            cloud-image-identifier (:cloudimageidentifier attrs)]]
      (html/content (str cloud-service-name ": " cloud-image-identifier)))

  [image-image-ids-sel] (if
                          (image-model/cloud-image-ids module)
                          identity
                          nil)

  [image-is-base-sel]
    (if (module-model/base? module)
      (html/set-attr :checked "checked")
      (html/remove-attr :checked))

  [image-platform-sel :> [:td html/first-of-type]] (html/content (:platform (module-model/attrs module)))

  [module-base/module-login-sel :> [:td html/first-of-type]] (html/content (:loginuser (module-model/attrs module)))

  [image-reference-sel :> [:td html/first-of-type] :> :a] (module-base/set-a (:modulereferenceuri (module-model/attrs module)))
  [image-reference-sel] (if (module-model/base? module)
                          nil
                          identity)

  image-cloud-configuration-sel
    (html/substitute
      (common/parameters-view-tabs-by-category-snip
        (common-model/filter-not-in-categories
          (common-model/parameters module)
          deployment-parameter-categories)))

  image-creation-sel (html/substitute
                       (creation-view-snip
                         (image-model/creation-recipe module)
                         (image-model/creation-prerecipe module)
                         (image-model/creation-packages module)))
  [#{image-creation-sel image-creation-header-sel}]
    (if (image-model/creates? module)
      identity
      nil)

  image-deployment-sel (html/substitute
                         (deployment-view-snip
                           (image-model/deployment-execute module)
                           (image-model/deployment-report module)
                           (image-model/deployment-onvmadd module)
                           (image-model/deployment-onvmremove module)
                           (common-model/filter-by-categories
                             (common-model/parameters module)
                             deployment-parameter-categories)))
  [#{image-deployment-sel image-deployment-header-sel}]
    (if (image-model/deploys? module)
      identity
      nil)

  [:#publish-form] (html/set-attr :action (str "/"
                                           (common-model/resourceuri module)
                                           "/publish"))

  ; Copy to
  [:#source_uri] (html/set-attr :value (common-model/resourceuri module))
  [:#target_name] (html/set-attr :value (:shortname (common-model/attrs module)))

  module-base/module-interaction-top-sel
    (html/substitute
      (view-interaction-snip module))

  module-base/module-interaction-bottom-sel
    (html/substitute
      (view-interaction-snip module))

  runs/runs-sel (html/content (runs/runs-snip (run-model/group-by-cloud module)))

  authz/authorization-sel (html/substitute (authz/authz-view-snip module)))

(html/defsnippet edit-interaction-snip image-edit-template-html module-base/module-interaction-top-sel
  [module]
  (authz-buttons module))

(html/defsnippet edit-snip image-edit-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))
  module-base/module-summary-sel (html/substitute
                                   (module-base/module-summary-edit-snip module))

  [image-image-ids-sel :> [:div html/first-of-type]]
    (html/clone-for
      [cloud-service-name (image-model/cloud-names module)
       :let [cloud-image-identifier (image-model/cloud-image-id module cloud-service-name)]]
      (html/content
        (html/html-snippet
          (str "<span>" cloud-service-name ": </span><input name='cloudimageid_imageid_" cloud-service-name "' type='text' value='" cloud-image-identifier "' />"))))
  [image-image-ids-sel :> :div :> :input]
    (common/set-disabled (not (module-model/base? module)))

  [image-is-base-sel]
    (common/set-checked (module-model/base? module))

  [image-platform-sel :> [:td html/first-of-type]]
    (html/content
      (common/gen-select
        "platform"
        platforms
        (:platform (module-model/attrs module))
        (not (module-model/base? module))))

  [module-base/module-login-sel]
    (html/do->
      (html/set-attr :value (:loginuser (module-model/attrs module)))
      (common/set-disabled (not (module-model/base? module))))

  [image-reference-sel] (html/set-attr
                          :value
                          (module-base/module-resource-uri-to-name
                            (:modulereferenceuri (module-model/attrs module))))
  [:#moduleReferenceChooser] (common/set-disabled (module-model/base? module))

  image-cloud-configuration-sel
  (html/substitute
    (common/parameters-edit-tabs-by-category-snip
      (common-model/filter-not-in-categories
        (common-model/parameters module)
        deployment-parameter-categories)))

  image-creation-sel (html/substitute
                       (creation-edit-snip
                         (image-model/creation-recipe module)
                         (image-model/creation-prerecipe module)
                         (image-model/creation-packages module)))

  image-deployment-sel (html/substitute
                         (deployment-edit-snip
                           (image-model/deployment-execute module)
                           (image-model/deployment-report module)
                           (image-model/deployment-onvmadd module)
                           (image-model/deployment-onvmremove module)
                           (common-model/filter-by-categories
                             (common-model/parameters module)
                             deployment-parameter-categories)))
  [#{image-deployment-sel image-deployment-header-sel}]
    (if (image-model/deploys? module)
      identity
      nil)

  [:#publish-form] (html/set-attr :action (str "/"
                                           (common-model/resourceuri module)
                                           "/publish"))

  module-base/module-interaction-top-sel
    (html/substitute
      (edit-interaction-snip module))

  module-base/module-interaction-bottom-sel
    (html/substitute
      (edit-interaction-snip module))

  authz/authorization-sel (html/substitute (authz/authz-edit-snip module)))

(html/defsnippet new-interaction-snip image-new-template-html module-base/module-interaction-top-sel
  [module]
  identity)

(html/defsnippet new-summary image-new-template-html module-base/module-summary-sel
  [module]
  (module-base/module-summary-trans module))

(html/defsnippet new-snip image-edit-template-html common/content-sel
  [module]
  html/this-node (html/substitute (edit-snip module))

  module-base/module-summary-sel (html/substitute (new-summary module))

  module-base/module-interaction-top-sel
    (html/substitute
      (new-interaction-snip module))

  module-base/module-interaction-bottom-sel
    (html/substitute
      (new-interaction-snip module)))
