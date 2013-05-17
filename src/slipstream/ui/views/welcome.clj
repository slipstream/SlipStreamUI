(ns slipstream.ui.views.welcome
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.module :as module-view]
            [slipstream.ui.views.common :as common]))

(def welcome-template-html "slipstream/ui/views/welcome.html")

(html/defsnippet header-titles-snip welcome-template-html header/titles-sel
  []
  header/titles-sel identity)

(html/defsnippet header-snip header/header-template-html header/header-sel
  [metadata]
  header/header-sel identity
  header/titles-sel (html/substitute (header-titles-snip))
  header/header-top-bar-sel (html/substitute
                               (header/header-top-bar-snip
                                 (user/attrs metadata))))

(html/defsnippet content-snip welcome-template-html common/content-sel
  [projects]
  [:#root-projects] (html/content (module-view/children-snip projects)))

(defn page [root-projects]
  (base/base 
    {:js-scripts ["/js/welcome.js"]
     :title (common/title "Welcome")
     :header (header-snip root-projects)
     :content (content-snip root-projects)
     :footer (footer/footer-snip @version/slipstream-release-version)}))
