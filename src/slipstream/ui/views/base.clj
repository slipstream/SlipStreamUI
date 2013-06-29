(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.header :as header]))

(def layout-template-html "slipstream/ui/views/layout.html")

(def head-sel [:head])

(html/defsnippet head-snip layout-template-html head-sel
  [js-scripts css-stylesheets]
  head-sel identity
  [head-sel [:script html/last-of-type]] (html/clone-for
                                           [js js-scripts]
                                           (html/set-attr :src js)))
;  [head-sel [:link html/last-of-type]] (html/clone-for
;                                         [css css-stylesheets]
;                                         (html/set-attr :href css)))

(html/deftemplate base layout-template-html
  [{:keys [title header content footer js-scripts css-stylesheets]}]
  head-sel (html/substitute (head-snip js-scripts css-stylesheets))
  [:#title] (html/content title)
  header/header-sel (html/substitute header)
  common/content-sel (html/substitute content)
  footer/footer-sel (html/substitute footer))
