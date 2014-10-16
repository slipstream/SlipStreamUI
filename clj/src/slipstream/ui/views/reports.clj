(ns slipstream.ui.views.reports
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.reports :as reports]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(ue/def-blank-snippet report-link-snip [:li :a]
  [report]
  [:a] (html/content (:name report))
  [:a] (ue/set-href (:relative-uri report))
  [:a] (ue/set-target "_blank"))

(ue/def-blank-snippet list-snip [:div :ul]
  [reports]
  ue/this (ue/set-class "ss-reports-list")
  [:ul]   (html/content (for [report reports]
                          (report-link-snip report))))

(ue/def-blank-snippet text-snip :div
  [text & {:keys [css-class]}]
  ue/this (ue/set-class css-class)
  ue/this (html/content (str text)))

(defn- reports-list
  [reports]
  (if (not-empty reports)
    (list-snip reports)
    (text-snip (t :no-reports-yet))))

(defn- footnote
  []
  (text-snip (t :no-need-to-refresh) :css-class "ss-report-footnote"))

(defn page
  [metadata]
  (localization/with-lang-from-metadata
    (let [reports (reports/parse metadata)]
      (base/generate
        {:content [(reports-list reports)
                   (footnote)]}))))
