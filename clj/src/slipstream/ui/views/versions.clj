(ns slipstream.ui.views.versions
  (:require [clojure.string :as s]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.models.versions :as mv]))

(defn- subtitle
  [category versions]
  (format "There %s of this %s."
    (if (= 1 (count versions))
      "is one version"
      (str "are " (count versions) " versions"))
    (s/lower-case (or category "{unkown module category}"))))

(defn page [metadata type]
  (let [{:keys [versions breadcrumbs module-name category]} (mv/parse metadata)
        icon (icons/icon-for category)]
    (base/generate
      {:metadata metadata
       :type type
       :header {:icon icon
                :title (str "History of '" module-name"'")
                :subtitle (subtitle category versions)}
       :breadcrumbs breadcrumbs
       :content [{:title "Versions"
                  :content (t/versions-table icon versions)}]})))
