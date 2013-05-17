(ns slipstream.ui.views.common
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]))

(def content-sel [:#content])

(def interaction-sel [:.interaction])

(def interations-template-html "slipstream/ui/views/interations.html")

(def slipstream-with-trade "SlipStream™")

(defn title [value]
  (str slipstream-with-trade " | " value))
