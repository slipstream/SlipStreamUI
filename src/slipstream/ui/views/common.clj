(ns slipstream.ui.views.common
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]))

(def content-sel [:#content])

(def interaction-sel [:.interaction])

(def error-template-html "slipstream/ui/views/error.html")

(html/defsnippet error-snip error-template-html [:#messages]
  [message code]
  [:#error] (html/content (str "Error (" code "): " message)))


(def interations-template-html "slipstream/ui/views/interations.html")

(def slipstream-with-trade "SlipStream™")

(defn title [value]
  (str slipstream-with-trade " | " value))
