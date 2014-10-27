(ns slipstream.ui.views.legacy.footer
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.version :as version]))

(def footer-sel [:#footer])

(def footer-template (u/template-path-for "footer.html"))

(html/defsnippet footer-snip footer-template footer-sel
  []
  [:#release-version] (html/content @version/slipstream-release-version))
