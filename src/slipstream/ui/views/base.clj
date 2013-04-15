(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate base "slipstream/ui/views/layout.html"
  [{:keys [header content footer]}]
  [:#header]  (html/content header)
  [:#content] (html/content content)
  [:#footer] (html/content footer))
