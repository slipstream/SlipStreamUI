(ns slipstream.ui.views.footer
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.user :as user]))

(def footer-sel [:#footer])

(html/defsnippet footer "slipstream/ui/views/footer.html" footer-sel
  [root edit? rel-version]
  [:#version] (html/content rel-version)
  common/interaction-sel (if (nil? root)
                           nil
                           (html/substitute
                             (common/header-buttons
                               {:buttons (common/buttons root edit?)}))))
