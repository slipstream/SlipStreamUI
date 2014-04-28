(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.messages :as messages]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.header :as header]))

(def layout-template-html "slipstream/ui/views/layout.html")

(def head-sel [:head])
(def title-sel [head-sel :> :title])
(def content-sel common/content-sel)

(html/defsnippet head-snip layout-template-html head-sel
  [title js-scripts css-stylesheets]
  title-sel (html/content title)
  [head-sel [:script html/last-of-type]] (html/clone-for
                                           [js js-scripts]
                                           (html/set-attr :src js))
  [head-sel [:link html/last-of-type]] (html/clone-for
                                           [css css-stylesheets]
                                           (html/set-attr :href css)))

(html/deftemplate base layout-template-html
  [{:keys [title header content footer js-scripts css-stylesheets]}]
  head-sel (html/substitute (head-snip title js-scripts css-stylesheets))
  header/header-sel (html/substitute header)
  common/content-sel (html/substitute content)
  [:span html/text-node] (html/replace-vars messages/all-messages)
  footer/footer-sel (html/substitute footer))
