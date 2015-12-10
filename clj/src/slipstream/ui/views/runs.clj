(ns slipstream.ui.views.runs
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.models.runs :as runs]))

(localization/def-scoped-t)

(defn- section
  [runs-metadata include-inactive-runs?]
  [{:title   (localization/section-title :runs)
    :content (if-let [runs (-> runs-metadata :runs not-empty)]
               (t/runs-table runs (:pagination runs-metadata) include-inactive-runs?)
               (t :section.runs.empty-content-hint))}])

(defn page
  [metadata]
  (let [runs-metadata             (runs/parse metadata)
        include-inactive-runs?  (-> metadata meta :request :query-parameters :activeOnly (uc/parse-boolean false) not)]
    (base/generate
      {:header {:icon icons/runs
                :title (t :header.title)
                :subtitle (t :header.subtitle)
                :second-subtitle (t :header.second-subtitle)}
       :content (section runs-metadata include-inactive-runs?)})))
