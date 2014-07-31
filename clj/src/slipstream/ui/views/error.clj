(ns slipstream.ui.views.error
  (:require [slipstream.ui.views.base :as base]))

(defn page [message code user]
  (base/generate
    {:title "Error"
     :error true
     :header {:status-code code
              :title nil
              :subtitle message}}))
