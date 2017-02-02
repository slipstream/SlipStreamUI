(ns slipstream.ui.views.service-info
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.service-info :as service-info]))

(localization/def-scoped-t)

(defn- sub-section
  [service-info]
  {:title   (:id service-info)
   :content (t/service-info-table (:decorated-values service-info))})

(defn- section
  [metadata]
  (map sub-section (->> metadata service-info/parse :service-offer (sort-by :id))))

(defn page
  [metadata]
  (base/generate
    {:header {:icon     icons/service-catalog
              :title    (t :header.title)
              :subtitle (t :header.subtitle)}
     :content (section metadata)}))
