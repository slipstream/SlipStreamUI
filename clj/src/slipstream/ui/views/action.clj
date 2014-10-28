(ns slipstream.ui.views.action
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.models.action :as action]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn page
  [metadata]
  (let [action (action/parse metadata)]
    (base/generate
      {:metadata metadata
       :header {:icon     icons/action-ok
                :title    (or (:title action) (t :header.title))
                :subtitle (:message action)}
       :content nil})))
