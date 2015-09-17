(ns slipstream.ui.views.usage
  (:require [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

(defn page
  [metadata]
  (base/generate
    {:header {:icon     icons/usage
              :title    (t :header.title)
              :subtitle (t :header.subtitle)}
     :resource-uri "/usage"
     :content (ue/pprint-snip metadata)}))