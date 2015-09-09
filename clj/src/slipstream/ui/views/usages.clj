(ns slipstream.ui.views.usages
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defn page
  [metadata]
  (base/generate
    {:header {:icon icons/users
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :resource-uri "/usages"
     :content [{:title (t :content.title)
                :content "dummy"}]}))
