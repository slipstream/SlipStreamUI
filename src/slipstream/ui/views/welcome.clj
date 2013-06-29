(ns slipstream.ui.views.welcome
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header-views]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.project :as project]
            [slipstream.ui.views.common :as common]))

(def welcome-template-html "slipstream/ui/views/welcome.html")

(html/defsnippet header-titles-snip welcome-template-html header-views/titles-sel
  []
  header-views/titles-sel identity)

(html/defsnippet header-snip header-views/header-template-html header-views/header-sel
  [metadata]
  header-views/header-sel identity
  header-views/titles-sel (html/substitute (header-titles-snip))
  header-views/header-top-bar-sel (html/substitute
                                    (header-views/header-top-bar-snip
                                      (user/attrs metadata))))

(html/defsnippet content-snip welcome-template-html common/content-sel
  [projects]
  [:#root-projects] (html/content (project/children-snip projects)))

(defn page [root-projects type]
  (base/base 
    {:js-scripts ["/js/welcome.js"]
     :title (common/title "Welcome")
     :header (module-base/header root-projects type header-snip)
     :content (content-snip root-projects)
     :footer (module-base/footer type)}))
