(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.header :as header]))

(html/deftemplate base "slipstream/ui/views/layout.html"
  [{:keys [title header content footer]}]
  [:#title]  (html/content title)
  header/header-sel (html/content header)
  common/content-sel (html/content content)
  footer/footer-sel (html/content footer)
)
