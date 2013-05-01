(ns slipstream.ui.views.knockknock
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.version :as version]))

(def knockknock-template-html "slipstream/ui/views/knockknock.html")

(html/defsnippet header-snip knockknock-template-html header/header-sel
  [metadata]
  header/header-sel identity)

(html/defsnippet content-snip knockknock-template-html common/content-sel
  [metadata]
  common/content-sel identity)

(defn page [metadata]
  (base/base 
    {:title (common/title "Login/Register")
     :header (header-snip metadata)
     :content (content-snip metadata)
     :footer (footer/footer-snip @version/slipstream-release-version)
     }))
