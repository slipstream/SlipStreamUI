(ns slipstream.ui.views.footer
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.user :as user]))

(def footer-sel [:#footer])

(html/defsnippet footer-snip "slipstream/ui/views/footer.html" footer-sel
  [rel-version]
  [:#version] (html/content rel-version))
