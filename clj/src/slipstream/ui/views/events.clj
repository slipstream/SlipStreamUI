(ns slipstream.ui.views.events
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.events :as events]))

(localization/def-scoped-t)

(defn- section
  [metadata]
  (let [parsed-metadata (events/parse metadata)]
    [{:title (t :content.title)
      :content (if (-> parsed-metadata :events empty?)
                 (t :no-events)
                 (t/events-table parsed-metadata))}]))

(defn page
  [metadata]
  (base/generate
    {:header {:icon icons/event
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :resource-uri "/events"
     :content (section metadata)}))

