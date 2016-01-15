(ns slipstream.ui.views.configuration
  (:require [superstring.core :as s]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.configuration :as configuration]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn- section
  [{:keys [category parameters]}]
  {:title   (s/replace category "_" " ")
   :content (t/parameters-table parameters)})

(defn page
  [metadata]
  (base/generate
    (let [config-params (-> metadata configuration/parse :parameters)]
      {:header {:icon     icons/config
                :title    (t :header.title)
                :subtitle (t :header.subtitle)}
       :secondary-menu-actions [action/edit]
       :content (map section config-params)})))
