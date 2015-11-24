(ns slipstream.ui.views.usages
  (:require [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.usages :as usages]))

(localization/def-scoped-t)

(defn- frequency-subsection
  [parsed-metadata include-content? frequency]
  {:title   (str "TODO" frequency)
   :content (if (-> parsed-metadata :usages empty?)
              (t :no-usages)
              (ue/dynamic-content-snip
                :content-load-url (format "/usage?offset=0&filter=frequency='%s'" (name frequency))
                :content-id (str "ss-usages-table-" (name frequency))
                :content (when include-content? (t/usages-table parsed-metadata))))})

(def displayed-frequencies [:daily :weekly :monthly])

(defn- section
  [metadata]
  (let [parsed-metadata (usages/parse metadata)
        frequency (slipstream.ui.util.dev-traces/>>> some->> metadata meta :request :query-parameters :filter (re-find #"frequency='(daily|weekly|monthly)'") second keyword)]
    (println (some->> metadata meta :request :query-parameters :filter))
    (println frequency)
    (if frequency
      [(frequency-subsection parsed-metadata true frequency)]
      [{:title (t :content.title)
        :content (mapv (partial frequency-subsection parsed-metadata false) displayed-frequencies)}])))

(defn page
  [metadata]
  (base/generate
    {:header {:icon icons/usage
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :content (section metadata)}))
