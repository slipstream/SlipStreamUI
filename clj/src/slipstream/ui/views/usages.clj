(ns slipstream.ui.views.usages
  (:require [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.usages :as usages]))

(localization/def-scoped-t)

(defn- section
  [metadata]
  (let [parsed-metadata (usages/parse metadata)]
    [{:title (t :content.title)
      :content (if (-> parsed-metadata :usages empty?)
                 (t :no-usages)
                 (ue/dynamic-content-snip
                   :content-load-url  "/usage?offset=0"
                   :content-id        "ss-usages-table"
                   :content           (t/usages-table parsed-metadata)))}]))

(defn page
  [metadata]
  (base/generate
    {:header {:icon icons/usage
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :resource-uri "/usages"
     :content (section metadata)}))
