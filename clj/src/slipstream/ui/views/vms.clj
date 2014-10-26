(ns slipstream.ui.views.vms
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.models.vms :as vms]))

(localization/def-scoped-t)

(defn- vms-subsection
  [{:keys [cloud-name vms]}]
  {:title cloud-name
   :content (t/vms-table vms)})

(defn- section
  [vms]
  [{:title   (localization/section-title :vms)
    :content (map vms-subsection vms)}])

(defn page
  [metadata]
  (let [vms (vms/parse metadata)]
  (base/generate
    {:metadata metadata
     :header {:icon icons/vms
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :resource-uri "/dashboard/vms"
     :content (section vms)})))
