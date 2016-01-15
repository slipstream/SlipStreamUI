(ns slipstream.ui.views.versions
  (:require [superstring.core :as s]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.versions :as mv]))

(localization/def-scoped-t)

(defn- subtitle
  [category versions]
  (let [category-label (u/t-module-category category s/lower-case)
        num-of-versions (count versions)]
    (if (= 1 num-of-versions)
      (t :header.subtitle.one-version category-label)
      (t :header.subtitle.multiple-versions num-of-versions category-label))))

(defn page
  [metadata]
  (let [{:keys [versions resource-uri module-name category]} (mv/parse metadata)
        icon (icons/icon-for category)]
    (base/generate
      {:header {:icon icon
                :title (t :header.title module-name)
                :subtitle (subtitle category versions)}
       :resource-uri resource-uri
       :content [{:title (t :content.title)
                  :content (t/versions-table icon versions)}]})))
