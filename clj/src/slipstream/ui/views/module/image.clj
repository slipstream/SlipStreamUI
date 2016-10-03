(ns slipstream.ui.views.module.image
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.models.pagination :as pagination]
            [slipstream.ui.views.module.util :as mu]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defmulti middle-section-content
  (fn [module section-title section-metadata metadata-key] metadata-key))

; Section :cloud-configuration

(defn- category-section
  [{:keys [category parameters]}]
  {:title category
   :content (t/parameters-table parameters)})

(defmethod middle-section-content :cloud-configuration
  [_ _ section-metadata _]
  (map category-section section-metadata))

; Section :targets

(defmethod middle-section-content :targets
  [_ _ section-metadata _]
  (map mu/target-subsection section-metadata))

; Section :runs

(defmethod middle-section-content :runs
  [_ _ section-metadata _]
  (if-let [runs (-> section-metadata :runs not-empty)]
    (ue/dynamic-content-snip
      :id      "runs"
      :content-load-url (pagination/url :runs :module-uri (some :module-uri runs)))
    (t :section.runs.empty-content-hint)))

; Other table sections (e.g. :cloud-image-details, :os-details and :deployment-parameters)

(defn- table-fn-for
  [metadata-key]
  (->> metadata-key
       name
       (format "slipstream.ui.views.tables/%s-table")
       symbol
       resolve))

(defmethod middle-section-content :default
  [_ _ section-metadata metadata-key]
  (if-let [table-fn (table-fn-for metadata-key)]
    (table-fn section-metadata)
    (throw (IllegalArgumentException. (str "No table defined for " metadata-key)))))

; Middle sections

(defn- middle-section
  [module metadata-key]
  (let [section-title (localization/section-title metadata-key)
        section-metadata (get module metadata-key)]
    {:title   section-title
     :content (middle-section-content module section-title section-metadata metadata-key)}))

(defn- visible-middle-sections
  []
  (cond-> [:cloud-image-details
           :os-details
           :cloud-configuration
           :deployment-parameters
           :targets]
   (page-type/view?) (conj :runs)))

(defn middle-sections
  [module]
  (map (partial middle-section module) (visible-middle-sections)))

(def html-dependencies
  {:internal-js-filenames ["image.js"]})

(defn actions
  [module]
  [action/run
   (action/build      :disabled?        (-> module :cloud-image-details :native-image?)
                      :disabled-reason  (t :action-disabled-reason.native-image-cannot-be-built))
   action/edit
   action/copy
   (action/publish    :hidden? (-> module :summary :published?))
   (action/unpublish  :hidden? (-> module :summary :published? not))
   action/delete
   action/delete-all])
