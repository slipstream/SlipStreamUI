(ns slipstream.ui.views.run
  (:require [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.run :as run]))

(localization/def-scoped-t)

;; We only need a template to include the CSS and JS files.
;; Everything else is composed and generated as the other pages with accordions.
(def run-template-html (common/get-template "run.html"))

(defn- section-title-for
  [metadata-key]
  (->> metadata-key name (format "section.%s.title") keyword t))

(defmulti section (comp second vector))

(defmethod section :overview
  [run metadata-key]
  {:title   (section-title-for metadata-key)
   :content (ue/blank-node :div :id "infovis"
                                :class "ss-run-overview")})

(defmethod section :summary
  [run metadata-key]
  (let [section-metadata (get run metadata-key)]
    {:title   (section-title-for metadata-key)
     :content (t/run-summary-table section-metadata)}))

(defn- runtime-parameter-section
  [parameter-group]
  {:title   (-> parameter-group :group name)
   :content (-> parameter-group :runtime-parameters t/runtime-parameters-table)})

(defmethod section :runtime-parameters
  [run metadata-key]
  (let [section-metadata (get run metadata-key)]
    (map runtime-parameter-section section-metadata)))

(ue/def-blank-snippet reports-iframe-snip :iframe
  [run]
  ue/this (ue/set-class "ss-reports-iframe")
  ue/this (ue/set-src (->> run :summary :uuid (format "/reports/%s/"))))

(defmethod section :reports
  [run metadata-key]
  (let [section-metadata (get run metadata-key)]
    {:title   (section-title-for metadata-key)
     :content (reports-iframe-snip nil)}))

(defn page
  [metadata]
  (localization/with-lang-from-metadata
   (let [run (run/parse metadata)]
     (base/generate
         {:template-filename run-template-html
          :metadata metadata
          :header {:icon icons/run
                   :title (t :header.title
                             (-> run :summary :uuid (uc/trim-from \-))
                             (-> run :summary :state))
                   :subtitle (-> run :summary :module-uri)}
          :resource-uri (-> run :summary :uri)
          :secondary-menu-actions [action/terminate]
          :content (->> [:overview
                         :summary
                         :runtime-parameters
                         :reports]
                        (map (partial section run))
                        flatten)}))))

