(ns slipstream.ui.views.vms
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.models.vms :as vms]))

(localization/def-scoped-t)

(defn- section
  [vms-metadata]
  [{:title   (localization/section-title :vms)
    :content (if-let [vms (-> vms-metadata :vms not-empty)]
               (t/vms-table vms (:pagination vms-metadata))
               (t :section.vms.empty-content-hint))}])

(defn page
  [metadata]
  (let [vms-metadata (vms/parse metadata)]
  (base/generate
    {:header {:icon icons/vms
              :title (t :header.title)
              :subtitle (t :header.subtitle)
              :second-subtitle (t :header.second-subtitle)}
     :content (section vms-metadata)})))
