(ns slipstream.ui.views.versions
  (:require [clojure.string :as s]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.models.versions :as mv]))

(defn- subtitle
  [category versions]
  (format "There %s of this %s."
    (if (= 1 (count versions))
      "is one version"
      (str "are " (count versions) " versions"))
    (s/lower-case (or category "{unkown module category}"))))

(defn page [metadata type]
  (let [{:keys [versions resource-uri module-name category]} (mv/parse metadata)
        icon (icons/icon-for category)]
    (base/generate
      {:metadata metadata
       :type type
       :header {:icon icon
                :title (str "History of '" module-name"'")
                :subtitle (subtitle category versions)}
       :resource-uri resource-uri
       :content [{:title "Versions"
                  :content (t/versions-table icon versions)}]})))
