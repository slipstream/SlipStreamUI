(ns slipstream.ui.views.cloud-usages
  (:require [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.usages :as usages]))

(localization/def-scoped-t)

(defn- frequency-subsection
  [parsed-metadata include-content? cloud frequency]
  (let [subsection-id (str "ss-cloud-usages-table-" (name frequency))]
    {:title   (t (keyword (format "content.subsection.%s.title" (name frequency))))
     :content (if (-> parsed-metadata :usageSummaries empty?)
                (ue/text-div-snip (t :no-usages) :id subsection-id)
                (ue/dynamic-content-snip
                  :content-load-url (format "/cloud-usage/%s?offset=0&filter=frequency='%s'" cloud (name frequency))
                  :content-id       subsection-id
                  :content          (when include-content? (t/cloud-usages-table parsed-metadata))))}))

(def displayed-frequencies [:daily :weekly :monthly])

(defn- section
  [metadata]
  (let [parsed-metadata (usages/parse metadata)
        cloud (some->> parsed-metadata :usageSummaries first :cloud)
        frequency (some->> metadata meta :request :query-parameters :filter (re-find #"frequency='(daily|weekly|monthly)'") second keyword)]
    (if frequency
      [(frequency-subsection parsed-metadata true cloud frequency)]
      [{:title (t :content.title)
        :content (mapv (partial frequency-subsection parsed-metadata false cloud) displayed-frequencies)}])))

(defn page
  [metadata]
  (base/generate
    {:header {:icon icons/usage
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :content (section metadata)}))