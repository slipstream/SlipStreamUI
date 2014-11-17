(ns slipstream.ui.views.byebye
  (:require [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]))

(defn page
  [metadata]
  (base/generate
    {:placeholder-page? true
     :header {:icon icons/action-log-out
              :title "Leaving? We hope to see you back soon..."
              :subtitle "Your gateway to multi-cloud automation"}
     :content nil}))
