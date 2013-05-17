(ns slipstream.ui.views.knockknock
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.version :as version]))

(def knockknock-template-html "slipstream/ui/views/knockknock.html")

(html/defsnippet header-title-snip knockknock-template-html header/titles-sel
  [metadata]
  header/titles-sel identity)

(html/defsnippet header-snip knockknock-template-html header/header-sel
  [metadata]
  header/header-top-bar-sel (html/substitute
                              (header/header-top-only-snip metadata)))

(html/defsnippet content-snip knockknock-template-html common/content-sel
  [metadata]
  common/content-sel identity)

(html/deftemplate base knockknock-template-html
  [{:keys [title header content footer]}]
  [:#title]  (html/content title)
  header/header-sel (html/content header)
  common/content-sel (html/content content)
  footer/footer-sel (html/content footer)
)

(defn page [metadata]
  (base 
    {:title (common/title "Login/Register")
     :header (header-snip metadata)
     :content (content-snip metadata)
     :footer (footer/footer-snip @version/slipstream-release-version)}))
