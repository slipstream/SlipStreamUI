(ns slipstream.ui.views.dashboard
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.models.pagination :as pagination]
            [slipstream.ui.models.dashboard :as dashboard]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defmulti ^:private section (comp second vector))

;; Usage section

(ue/def-blank-snippet ^:private add-new-cloud-btn-snip [:div :div]
  []
  ue/this     (ue/set-class "ss-add-new-cloud-btn-container")
  ue/this     (html/prepend (icons/icon-snip :add-new-cloud))
  ue/this     (ue/set-data :href (u/user-uri (current-user/username) :edit true :hash "close-sections"))
  [:div :div] (ue/set-class "ss-add-new-cloud-btn-label")
  [:div :div] (html/content (t :add-a-new-cloud-button-label)))


; This is the snippet .ss-usage-gauge-detailed-counter for each one of the detailed
; infos (the icons below the gauge) on each gauge.
(ue/def-blank-snippet ^:private detailed-counter-snip :div
  [cloud-usage usage-key]
  ue/this     (ue/set-class (format "ss-usage-gauge-detailed-counter ss-usage-count-%s ss-usage-key-%s"
                                    (-> cloud-usage usage-key str)
                                    (name usage-key)))
  ue/this     (let [usage       (usage-key cloud-usage)
                    keyname     (name usage-key)
                    title       (t (format "detailed-counter.%s.popover.title" keyname) usage)
                    content     (t (format "detailed-counter.%s.popover.content" keyname))]
                (ue/add-popover title :content content))
  ue/this     (html/append (icons/icon-snip usage-key))
  ue/this     (html/append (ue/text-div-snip (-> cloud-usage usage-key str) :css-class "counter")))

; This is the snippet .ss-usage-gauge-container for each one of the gauges.
(ue/def-blank-snippet ^:private gauge-snip [:div :div]
  [cloud-usage]
  ue/this     (ue/set-class   "ss-usage-gauge-container")
  [:div :div] (ue/set-class   "ss-usage-gauge")
  [:div :div] (ue/set-id      "ss-usage-gauge-"   (:cloud         cloud-usage))
  [:div :div] (html/set-attr  :data-quota-title   (:cloud         cloud-usage))
  [:div :div] (html/set-attr  :data-vm-quota      (:vm-quota      cloud-usage))
  [:div :div] (html/set-attr  :data-user-vm-usage (:user-vm-usage cloud-usage))
  ue/this     (html/append    (detailed-counter-snip cloud-usage :user-run-usage))
  ue/this     (html/append    (detailed-counter-snip cloud-usage :user-inactive-vm-usage))
  ue/this     (html/append    (detailed-counter-snip cloud-usage :others-vm-usage))
  ue/this     (html/append    (detailed-counter-snip cloud-usage :pending-vm-usage))
  ue/this     (html/append    (detailed-counter-snip cloud-usage :unknown-vm-usage)))

; This is the snippet .ss-usage-container containing all gauges.
(ue/def-blank-snippet ^:private usage-snip [:div :div]
  [usage]
  ue/this (ue/set-class "ss-usage-container")
  ue/this (ue/content-for [:div :div] [cloud-usage usage]
              ue/this (html/substitute (gauge-snip cloud-usage)))
  ue/this (html/append (add-new-cloud-btn-snip)))

(defmethod section ::quota
  [dashboard metadata-key]
  {:title   (localization/section-title metadata-key)
   :content (-> dashboard :quota :usage usage-snip)
   :type    :flat-section})

;; Runs and VMs section

(defn ^:private dynamic-cloud-subsection-content-snip
  [metadata-key]
  (ue/dynamic-content-snip :content-load-url (pagination/url metadata-key)
                           :id (name metadata-key)))

(defn- cloud-detailed-info-subsection
  [metadata-key]
  {:title   (localization/section-title metadata-key)
   :content (dynamic-cloud-subsection-content-snip metadata-key)})

(defmethod section ::cloud-detailed-info
  [{:keys [clouds]} metadata-key]
  {:content "<div id=\"dashboard-tab\"></div>"
   :type    :flat-section})

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
   :cpu-nb
   :ram-mb
   :disk-gb
   ; :instance-type
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

(defmethod section ::metering
  [dashboard metadata-key]
  {:title   (localization/section-title metadata-key)
   :content (map metering-subsection metering-metrics)
   :type    :flat-section})

(defn- sections
  [dashboard]
  (cond-> []
    (-> dashboard :quota :enabled?)     (conj ::quota)
    :always                             (conj ::cloud-detailed-info)
    (-> dashboard :metering :enabled?)  (conj ::metering)))

(def ^:private html-dependencies
  {:css-filenames ["semantic-fix-conflicts.css" "dashboard.css" "semantic.min.css"]
   :external-js-filenames (concat
                            (map (partial format "jquery-flot/js/jquery.flot%s.min.js")
                                 ["" ".pie" ".time" ".stack" ".tooltip" ".resize"])
                            (map (partial format "justgage/js/%s.min.js")
                                 ["raphael.2.1.4" "justgage.1.1.0"]))
   :internal-js-filenames ["metering.js" "dashboard.js" "webui.js" "webui_init.js"]})

(defn page
  [metadata]
  (let [dashboard (dashboard/parse metadata)]
    (base/generate
      {:html-dependencies html-dependencies
       :header {:icon             icons/dashboard
                :title            (t :header.title)
                :subtitle         (if (current-user/super?)
                                    (t :header.subtitle.super)
                                    (t :header.subtitle))
                :second-subtitle  (when (current-user/super?)
                                    (t :header.second-subtitle.super))}
       :content (->> dashboard
                     sections
                     (map (partial section dashboard))
                     flatten)})))
