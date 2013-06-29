(ns slipstream.ui.views.knockknock
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.version :as version]))

(def knockknock-template-html "slipstream/ui/views/knockknock.html")

(html/defsnippet header-snip knockknock-template-html header/header-sel
  [metadata]
  header/header-top-bar-sel (html/substitute
                       (header/header-top-only-snip metadata)))

(html/deftemplate base knockknock-template-html
  [{:keys [header content footer]}]
  header/header-sel (html/substitute header)
  footer/footer-sel (html/substitute footer))

(defn page [metadata]
  (base 
    {:header (header-snip metadata)
     :footer (footer/footer-snip)}))
