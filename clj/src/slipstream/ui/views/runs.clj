(ns slipstream.ui.views.runs
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.models.runs :as runs]))

(localization/def-scoped-t)

(defn- section
  [runs-metadata]
  [{:title   (localization/section-title :runs)
    :content (if-let [runs (-> runs-metadata :runs not-empty)]
               (t/runs-table runs)
               (t :section.runs.empty-content-hint))}])

(defn page
  [metadata]
  (let [runs-metadata (runs/parse metadata)]
  (base/generate
    {:header {:icon icons/runs
              :title (t :header.title)
              :subtitle (t :header.subtitle)
              :second-subtitle (t :header.second-subtitle)}
     :resource-uri "/dashboard/runs"
     :content (section runs-metadata)})))
