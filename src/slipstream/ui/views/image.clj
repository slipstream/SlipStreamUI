(ns slipstream.ui.views.image
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.user :as user-model]
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

(html/defsnippet creation-snip image-view-template-html image-creation-sel
  [recipe prerecipe packages]

  [:ul :> :#fragment-creation-recipe-header]
  (if (string/blank? recipe)
    nil
    identity)
  [:ul :> :#fragment-creation-packages-header]
  (if (empty? packages)
    nil
    identity)
  [:ul :> :#fragment-creation-prerecipe-header]
  (if (string/blank? prerecipe)
    nil
    identity)

  [:#fragment-creation-recipe :> :table :tbody :tr :td :textarea]
  (html/content recipe)
  [:#fragment-creation-recipe]
  (if (string/blank? recipe)
    nil
    identity)

  [:#fragment-creation-packages :> :table :tbody :tr]
  (html/clone-for
    [package packages
     :let [attrs (module-model/attrs package)]]
    [[:td (html/nth-of-type 1)]] (html/content (:name attrs))
    [[:td (html/nth-of-type 2)]] (html/content (:repository attrs))
    [[:td (html/nth-of-type 3)]] (html/content (:key attrs)))
  [:#fragment-creation-packages]
  (if (empty? packages)
    nil
    identity)

  [:#fragment-creation-prerecipe :> :table :tbody :tr :td :textarea]
  (html/content prerecipe)
  [:#fragment-creation-prerecipe]
  (if (string/blank? prerecipe)
    nil
    identity))
  
(html/defsnippet deployment-snip image-view-template-html image-deployment-sel
  [execute report parameters]
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

  [:#fragment-deployment-execute :> :table :tbody :tr :td :textarea]
    (html/content execute)
  [:#fragment-deployment-execute]
    (if (string/blank? execute)
      nil
      identity)

  [:#fragment-deployment-report :> :table :tbody :tr :td :textarea]
    (html/content report)
  [:#fragment-deployment-report]
    (if (string/blank? report)
      nil
      identity)

  [:#fragment-deployment-parameters]
    (html/content (common/parameters-view-with-name-and-category-snip parameters))
  [:#fragment-deployment-parameters]
    (if (empty? parameters)
      nil
      identity))

(html/defsnippet summary-view-snip image-view-template-html module-base/module-summary-sel
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

(html/defsnippet summary-edit-snip image-edit-template-html module-base/module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-description] (html/set-attr :value (:description (module-model/attrs module)))
  [:#module-comment] (html/set-attr :value (module-model/module-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module))
  [:#module-is-base] (if (true? (:isbase (module-model/attrs module)))
                       (html/set-attr :checked "checked")
                       (html/remove-attr :checked)))

(html/defsnippet summary-new-snip image-new-template-html module-base/module-summary-sel
  [module]
  (module-base/module-summary-new-trans module))

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
        nil))))

(html/defsnippet view-interaction-snip image-view-template-html module-base/module-interaction-top-sel
  [module]
  (authz-buttons module))

(html/defsnippet view-snip image-view-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))
  module-base/module-summary-sel (html/substitute 
                                   (summary-view-snip module))
   
  [:#build-form] (html/set-attr :value (:resourceuri (module-model/attrs module)))
  
  [image-image-ids-sel :> [:div html/first-of-type]]
    (html/clone-for 
      [id-pair (image-model/cloud-image-ids module)
      :let
        [attrs (module-model/attrs id-pair)
         cloud-service-name (:cloudservicename attrs)
         cloud-image-identifier (:cloudimageidentifier attrs)]]
        (html/content (str cloud-service-name ": " cloud-image-identifier)))

  [image-is-base-sel]
    (if (module-model/base? module)
      (html/set-attr :checked "checked")
      (html/remove-attr :checked))
  
  [image-platform-sel :> :td] (html/content (:platform (module-model/attrs module)))
  
  [module-base/module-login-sel :> :td] (html/content (:loginuser (module-model/attrs module)))

  [image-reference-sel :> :td :> :a] (module-base/set-a (module-model/parent-uri module))
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
                       (creation-snip
                         (image-model/creation-recipe module)
                         (image-model/creation-prerecipe module)
                         (image-model/creation-packages module)))
  [#{image-creation-sel image-creation-header-sel}] 
    (if (image-model/creates? module)
      identity
      nil)

  image-deployment-sel (html/substitute
                         (deployment-snip 
                           (image-model/deployment-execute module)
                           (image-model/deployment-report module)
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
      (view-interaction-snip module))

  module-base/module-interaction-bottom-sel
    (html/substitute
      (view-interaction-snip module))

  authz/authorization-sel (html/substitute (authz/authz-view-snip module)))

(html/defsnippet new-snip image-new-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))
  
  module-base/module-summary-sel (html/substitute 
                                   (summary-new-snip module))

  authz/authorization-sel (html/substitute (authz/authz-edit-snip module)))

(html/defsnippet edit-interaction-snip image-edit-template-html module-base/module-interaction-top-sel
  [module]
  (authz-buttons module))

(html/defsnippet edit-snip image-edit-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))
  module-base/module-summary-sel (html/substitute 
                                   (summary-edit-snip module))

  [:#build-form] (html/set-attr :value (:resourceuri (module-model/attrs module)))
  
  [image-image-ids-sel :> [:div html/first-of-type]]
    (html/clone-for 
      [id-pair (image-model/cloud-image-ids module)
      :let
        [attrs (module-model/attrs id-pair)
         cloud-service-name (:cloudservicename attrs)
         cloud-image-identifier (:cloudimageidentifier attrs)]]
        (html/content (str cloud-service-name ": " cloud-image-identifier)))

  [image-is-base-sel]
    (if (module-model/base? module)
      (html/set-attr :checked "checked")
      (html/remove-attr :checked))
  
  [image-platform-sel :> :td] (html/content 
                                (common/gen-select
                                  "platform"
                                  platforms
                                  (:platform (module-model/attrs module))))

  [module-base/module-login-sel :> :td] (html/content (:loginuser (module-model/attrs module)))

  [image-reference-sel :> :td :> :a] (module-base/set-a (module-model/parent-uri module))
  
  image-cloud-configuration-sel
    (html/substitute 
      (common/parameters-edit-tabs-by-category-snip 
        (common-model/filter-not-in-categories
          (common-model/parameters module)
          deployment-parameter-categories)))

  image-creation-sel (html/substitute 
                       (creation-snip
                         (image-model/creation-recipe module)
                         (image-model/creation-prerecipe module)
                         (image-model/creation-packages module)))
  [#{image-creation-sel image-creation-header-sel}] 
    (if (image-model/creates? module)
      identity
      nil)

  image-deployment-sel (html/substitute
                         (deployment-snip 
                           (image-model/deployment-execute module)
                           (image-model/deployment-report module)
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







