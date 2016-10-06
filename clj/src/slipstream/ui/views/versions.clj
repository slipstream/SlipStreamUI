(ns slipstream.ui.views.versions
  (:require [superstring.core :as s]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.versions :as mv]))

(localization/def-scoped-t)

(defn- subtitle
  [category versions]
  (let [category-label (u/t-module-category category s/lower-case)
        num-of-versions (count versions)]
    (if (= 1 num-of-versions)
      (t :header.subtitle.one-version category-label)
      (t :header.subtitle.multiple-versions num-of-versions category-label))))

(defn- actions
  []
  [action/delete-all])

(defn page
  [metadata]
    (let [{:keys [versions resource-uri module-short-name category] :as parsed-metadata} (mv/parse metadata)
        icon (icons/icon-for category)]
    (base/generate
      {:parsed-metadata parsed-metadata
       :header {:icon icon
                :title (t :header.title module-short-name)
                :subtitle (subtitle category versions)}
       :resource-uri resource-uri
       :secondary-menu-actions (actions)
       :num-of-main-secondary-menu-actions 1
       :content [{:title (t :content.title)
                  :content (t/versions-table icon versions)}]})))
