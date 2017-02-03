(ns slipstream.ui.views.attributes
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.attributes :as attributes]))

(localization/def-scoped-t)

(defn- sub-section
  [attribute]
  {:title   (:uri attribute)
   :content (t/attributes-table (:vals attribute))})

(defn- section
  [metadata]
  (map sub-section (-> metadata attributes/parse :attributes)))

(defn page
  [metadata]
  (base/generate
    {:header {:icon     icons/service-catalog
              :title    (t :header.title)
              :subtitle (t :header.subtitle)}
     :content (section metadata)}))

