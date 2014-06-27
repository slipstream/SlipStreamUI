(ns slipstream.ui.views.footer
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.version :as version]))

(def footer-sel [:#footer])

(html/defsnippet footer-snip (common/get-template "footer.html") footer-sel
  []
  [:#release-version] (html/content @version/slipstream-release-version))
