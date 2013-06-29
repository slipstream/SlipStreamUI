(ns slipstream.ui.views.deployment
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.views.authz-view :as authz-view]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]))

(def deployment-view-template-html "slipstream/ui/views/deployment-view.html")
(def deployment-edit-template-html "slipstream/ui/views/deployment-edit.html")
(def deployment-new-template-html "slipstream/ui/views/deployment-new.html")

(def module-login-sel [:#module-login])

;; View

(html/defsnippet summary-view-snip deployment-view-template-html module-base/module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-version] (html/html-content
                         (str (:version (module-model/attrs module))
                              "<span> (<a href='"
                              (:parenturi (module-model/attrs module))
                              "/"
                              (:shortname (module-model/attrs module))
                              "/'>history</a>)</span>"))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-comment] (html/content (module-model/module-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet summary-edit-snip deployment-edit-template-html module-base/module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-comment] (html/content (module-model/module-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet summary-new-snip deployment-new-template-html module-base/module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet view-snip deployment-view-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb module)
  module-base/module-summary-sel (html/substitute 
                                   (summary-view-snip module))
   
  [:#build-form] (html/set-attr :value (:resourceuri (module-model/attrs module)))
  
  [:#publish-form] (html/set-attr :value (str 
                                           (:resourceuri (module-model/attrs module))
                                           "/publish"))
  
  authz/authorization-sel (html/substitute (authz-view/authz-snip module)))

(html/defsnippet new-snip deployment-new-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb module)
  module-base/module-summary-sel (html/substitute 
                                   (summary-new-snip module)))

(html/defsnippet edit-snip deployment-edit-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb module)
  module-base/module-summary-sel (html/substitute 
                                   (module-base/module-summary-edit-snip module)))
