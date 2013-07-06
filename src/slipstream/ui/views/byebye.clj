(ns slipstream.ui.views.byebye
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.version :as version]))

(def byebye-template-html "slipstream/ui/views/byebye.html")

(html/defsnippet header-titles-snip byebye-template-html header/header-titles-sel
  [metadata]
  header/header-titles-sel identity)

(html/defsnippet header-snip header/header-template-html header/header-sel
  [metadata]
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user/attrs metadata)))
  header/header-titles-sel header-titles-snip metadata)

(html/defsnippet content-snip byebye-template-html common/content-sel
  [metadata]
  common/content-sel identity)

(defn page [metadata]
  (base/base 
    {:title (common/title "Login/Register")
     :header (header-snip metadata)
     :content (content-snip metadata)
     :footer (footer/footer-snip)}))
