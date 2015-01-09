(ns slipstream.ui.views.run
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.time :as ut]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.models.run :as run]))

(localization/def-scoped-t)

(defmulti section (comp second vector))

(defmethod section :overview
  [run metadata-key]
  {:title   (localization/section-title metadata-key)
   :content (ue/blank-node :div :id "infovis"
                                :class "ss-run-overview")})

(defmethod section :summary
  [run metadata-key]
  (let [section-metadata (get run metadata-key)]
    {:title   (localization/section-title metadata-key)
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
    {:title   (localization/section-title metadata-key)
     :content (reports-iframe-snip run)}))

(def ^:private sections
  [:overview
   :summary
   :runtime-parameters
   :reports])

(def ^:private html-dependencies
  {:css-filenames         ["run.css"]
   :external-js-filenames ["jit/js/jit.js"]
   :internal-js-filenames ["run.js" "run_overview.js"]})

(defn- subtitle
  [run]
  (let [summary (:summary run)
        mutable? (:mutable? summary)
        t-key (-> summary
                  :original-type
                  (str "." (when-not mutable? "not-") "mutable"))
        relative-start-timestamp (ut/format :relative (:start-time summary))]
    (t t-key (:owner summary) relative-start-timestamp)))

(defn page
  [metadata]
  (let [run (run/parse metadata)]
    (base/generate
      {:html-dependencies html-dependencies
       :header {:icon  (-> run :summary :type (or :run) icons/icon-for)
                :title (t :header.title
                         (-> run :summary :uuid (uc/trim-from \-))
                         (-> run :summary :state))
                :subtitle (subtitle run)}
       :resource-uri (-> run
                         :summary
                         :module-uri
                         (uc/ensure-suffix "/")
                         (uc/ensure-suffix (-> run
                                               :summary
                                               :uuid
                                               (uc/trim-from \-))))
       :secondary-menu-actions [action/terminate]
       :content (->> sections
                     (map (partial section run))
                     flatten)})))

