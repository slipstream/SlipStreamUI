(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.header :as header-views]))

(def layout-template-html "slipstream/ui/views/layout.html")

(def head-sel [:head])

(html/defsnippet head-snip layout-template-html head-sel
  [js-scripts css-stylesheets]
  head-sel identity
  [head-sel [:script html/last-of-type]] (html/after
             (for [js js-scripts]
                 (html/html-snippet (str "\n    <script src='" js "' type='text/javascript'></script>"))))
  [head-sel [:link html/last-of-type]] (html/after 
             (for [css css-stylesheets]
               (html/html-snippet (str "\n    <link rel='stylesheet' type='text/css' href='" css "' media='screen' />")))))

(html/deftemplate base layout-template-html
  [{:keys [title header content footer js-scripts css-stylesheets]}]
  head-sel (html/substitute (head-snip js-scripts css-stylesheets))
  [:#title] (html/content title)
  header-views/header-sel (html/substitute header)
  common/content-sel (html/substitute content)
  footer/footer-sel (html/substitute footer))
