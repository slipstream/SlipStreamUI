(ns slipstream.ui.views.run
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.time :as ut]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.current-user :as current-user]
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

(defn- runtime-parameter-node-instance-section
  [parameter-group]
  {:title   (-> parameter-group :group name)
   :content (-> parameter-group :runtime-parameters t/runtime-parameters-table)})

(defn- runtime-parameter-node-section
  [parameter-group]
  (let [subsection-metadata (:node-instances parameter-group)
        section-type        (:node-type parameter-group)
        global?             (= section-type :global)]
    {:icon    (when-not global? (icons/icon-for section-type))
     :title   (if global? (t :section.global.title) (-> parameter-group :node name))
     :content (if (-> subsection-metadata count (= 1))
                (-> subsection-metadata first runtime-parameter-node-instance-section :content)
                (map runtime-parameter-node-instance-section subsection-metadata))}))

(defmethod section :runtime-parameters
  [run metadata-key]
  (let [section-metadata (get run metadata-key)]
    (map runtime-parameter-node-section section-metadata)))

(defn- url-events
  [run]
  (str "/event?offset=0&limit=20&filter=content/resource/href='" (-> run :summary :uri) "'"))

(defmethod section :events
  [run metadata-key]
  {:icon  icons/event
   :title (localization/section-title metadata-key)
   :content (ue/dynamic-content-snip
              :id      "events"
              :content-load-url (url-events run))})

(ue/def-blank-snippet reports-iframe-snip :iframe
  [run]
  ue/this (ue/set-class "ss-reports-iframe")
  ue/this (ue/set-src (->> run :summary :uuid (format "/reports/%s/"))))

(defmethod section :reports
  [run metadata-key]
  (let [section-metadata (get run metadata-key)]
    {:title   (localization/section-title metadata-key)
     :content (reports-iframe-snip run)}))

(defn- sections
  [large-run?]
  (cond-> []
    (not large-run?)  (conj :overview)
    :always           (conj :summary)
    :always           (conj :runtime-parameters)
    :always           (conj :events)
    :always           (conj :reports)))

(defn- html-dependencies
  [large-run?]
  {:css-filenames         ["run.css"]
   :external-js-filenames (when-not large-run? ["jit/js/jit.js"])
   :internal-js-filenames (cond-> ["run.js"]
                            (not large-run?) (into ["run_autoupdate.js"
                                                    "run_overview.js"]))})

(defn- subtitle
  [run]
  (let [summary (:summary run)
        mutable? (:mutable? summary)
        run-owner (:run-owner summary)
        t-key (-> summary
                  :original-type
                  (str "." (when-not mutable? "not-") "mutable")
                  (str (when (current-user/is? run-owner) ".own-run")))
        relative-start-timestamp (ut/format :relative (:start-time summary))]
    (t t-key run-owner relative-start-timestamp)))

(defn- ss-abort-alert
  [run]
  (when-let [ss-abort-msg (-> run :summary :abort-msg not-empty)]
    {:type :error
     :container :fixed
     :msg (str "<strong><code>ss:abort</code></strong>- " ss-abort-msg)}))

(defn- large-run-alert
  [large-run? counts]
  (when large-run?
    {:type :warning
     :container :fixed
     :title (t :large-run-alert.title)
     :msg   (t :large-run-alert.message (:total-instances counts))}))

(defn page
  [metadata]
  (let [run (run/parse metadata)
        short-run-uuid (-> run :summary :uuid (uc/trim-from \-))
        large-run? (-> run :summary :large-run?)]
    (base/generate
      {:html-dependencies (html-dependencies large-run?)
       :page-title     (t :page-title short-run-uuid)
       :header {:icon  (-> run :summary :type (or :run) icons/icon-for)
                :title (t :header.title
                         short-run-uuid
                         (-> run :summary :state))
                :subtitle (subtitle run)}
       :alerts [(ss-abort-alert run)
                (large-run-alert large-run? (-> run :summary :counts))]
       :resource-uri (-> run
                         :summary
                         :module-uri
                         (uc/ensure-suffix "/")
                         (uc/ensure-suffix (-> run
                                               :summary
                                               :uuid
                                               (uc/trim-from \-))))
       :secondary-menu-actions [action/terminate]
       :content (->> (sections large-run?)
                     (map (partial section run))
                     flatten)})))

