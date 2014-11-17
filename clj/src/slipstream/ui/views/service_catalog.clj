(ns slipstream.ui.views.service-catalog
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.service-catalog :as service-catalog]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn item-section
  "NB: Also used by the welcome page, as subsection."
  [service-catalog-item]
  {:title (:cloud service-catalog-item)
   :content (t/service-catalog-parameters-table (:parameters service-catalog-item))})

(defn page
  [metadata]
  (let [service-catalog (service-catalog/parse metadata)]
    (base/generate
      {:header {:icon     icons/service-catalog
                :title    (t :header.title)
                :subtitle (t :header.subtitle)}
       :content (map item-section (:items service-catalog))})))
