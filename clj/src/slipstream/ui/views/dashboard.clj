(ns slipstream.ui.views.dashboard
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.dashboard :as dashboard]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

; The template is only needed to provide the JS requested.
; TODO: Allow to pass JS filenames when no template needed.
(def template-file (common/get-template "dashboard.html"))

(defmulti ^:private section (comp second vector))

;; Usage section

(ue/def-blank-snippet ^:private usage-snip [:div :div]
  [usage]
  ue/this (ue/set-class "ss-usage-container")
  ue/this (ue/content-for [:div :div] [cloud-usage usage]
              ue/this (ue/set-id "ss-usage-gauge-" (:cloud cloud-usage))
              ue/this (ue/set-class "ss-usage-gauge")
              ue/this (html/set-attr :data-quota-title (:cloud cloud-usage))
              ue/this (html/set-attr :data-quota-max (:quota cloud-usage))
              ue/this (html/set-attr :data-quota-current (:current-usage cloud-usage))))

(defmethod section :quota
  [dashboard metadata-key]
  {:title   (localization/section-title metadata-key)
   :content (-> dashboard :quota :usage usage-snip)})

;; Runs section

(defn- runs-subsection
  [{:keys [cloud-name runs]}]
  {:title cloud-name
   :content (t/runs-table runs)})

(defmethod section :runs
  [dashboard metadata-key]
  (let [section-metadata (get dashboard metadata-key)]
    {:title   (localization/section-title metadata-key)
     :content (map runs-subsection section-metadata)}))

;; VMS section

(def ^:private vms-div
  (ue/blank-node :div :id "vms"))

(defmethod section :vms
  [dashboard metadata-key]
  {:title   (localization/section-title metadata-key)
   :content vms-div})

;; Metering section

(localization/with-prefixed-t :metering-options
  (defn- metering-options
    []
    [{:value 3840,    :text (t :last-hour)}
     {:value 86640,   :text (t :last-day)}
     {:value 604800,  :text (t :last-7-days)}
     {:value 2592000, :text (t :last-30-days)}]))

(ue/def-blank-snippet ^:private metering-selector-snip [:select :option]
  [options]
  [:select] (ue/set-id "ss-metering-selector")
  [:select] (ue/content-for [:option] [{:keys [value text]} options]
              ue/this (ue/set-value value)
              ue/this (html/content (str text))))

(def ^:private metering-metrics
  [
   :instance
   ; :vcpus
   ; :memory
   ; :disk
   ])

(defn- data-metric-value
  [metric]
  (format "slipstream.%s.usage.%s.*"
          (s/replace (current-user/username) "." "_")
          (name metric)))

(ue/def-blank-snippet ^:private metering-subsection-snip :div
  [metric]
  ue/this (ue/set-class "ss-metering metric")
  ue/this (html/set-attr :data-metric (data-metric-value metric))
  ue/this (html/prepend (metering-selector-snip (metering-options))))

(localization/with-prefixed-t :metering.metric
  (defn- metering-subsection
    [metric]
    {:title (t metric)
     :content (metering-subsection-snip metric)}))

(defmethod section :metering
  [dashboard metadata-key]
  {:title   (localization/section-title metadata-key)
   :content (map metering-subsection metering-metrics)})

(defn- sections
  [dashboard]
  (cond-> []
    (-> dashboard :quota :enabled?)     (conj :quota)
    :always                             (conj :runs)
    :always                             (conj :vms)
    (-> dashboard :metering :enabled?)  (conj :metering)))

(defn page
  [metadata]
  (let [dashboard (dashboard/parse metadata)]
    (base/generate
      {:template-filename template-file
       :metadata metadata
       :header {:icon icons/dashboard
                :title (t :header.title)
                :subtitle (t :header.subtitle)}
       :resource-uri "/dashboard"
       :content (->> dashboard
                     sections
                     (map (partial section dashboard))
                     flatten)})))
