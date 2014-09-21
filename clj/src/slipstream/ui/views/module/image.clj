(ns slipstream.ui.views.module.image
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.tables :as t]
            ; [slipstream.ui.util.clojure :as uc]
            ; [slipstream.ui.views.tables :as t]
            ))

(localization/def-scoped-t)

(defmulti middle-section-content
  (fn [section-metadata metadata-key] metadata-key))

; Section "cloud-configuration"

(defn- category-section
  [{:keys [category parameters]}]
  {:title category
   :content (t/parameters-table parameters)})

(defmethod middle-section-content :cloud-configuration
  [section-metadata _]
  (map category-section section-metadata))

; Section "runs"

(defn- run-section
  [{:keys [cloud-name runs]}]
  {:title cloud-name
   :content (t/runs-table runs)})

(defmethod middle-section-content :runs
  [section-metadata _]
  (map run-section section-metadata))

;; Other table sections (e.g. cloud-image-details os-details)

(defn- table-fn-for
  [metadata-key]
  (->> metadata-key
       name
       (format "slipstream.ui.views.tables/%s-table")
       symbol
       resolve))

(defmethod middle-section-content :default
  [section-metadata metadata-key]
  (if-let [table-fn (table-fn-for metadata-key)]
    (table-fn section-metadata)
    (throw (IllegalArgumentException. (str "No table defined for" metadata-key)))))

(defn- middle-section
  [module metadata-key]
  (let [section-metadata (get module metadata-key)]
    {:title   (->> metadata-key name (format "section.%s.title") keyword t)
     :content (middle-section-content section-metadata metadata-key)}))

(defn middle-sections
  [module]
  (->> [:cloud-image-details
        :os-details
        :cloud-configuration
        :runs]
       (map (partial middle-section module))))
