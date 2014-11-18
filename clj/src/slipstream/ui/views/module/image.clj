(ns slipstream.ui.views.module.image
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.views.code-area :as code-area]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defmulti middle-section-content
  (fn [section-metadata metadata-key] metadata-key))

(defn- code-area
  [code id]
  (code-area/build code
                   :id id
                   :editable? (page-type/edit-or-new?)))

; Section "cloud-configuration"

(defn- category-section
  [{:keys [category parameters]}]
  {:title category
   :content (t/parameters-table parameters)})

(defmethod middle-section-content :cloud-configuration
  [section-metadata _]
  (map category-section section-metadata))

; Section "image creation recipes"

(defmethod middle-section-content :image-creation
  [section-metadata _]
  (localization/with-prefixed-t :section.image-creation.subsection
    [{:title    (t :recipe.title)
      :content [(t :recipe.description)
                (-> section-metadata :recipe :code (code-area "recipe"))]}
     {:title    (t :packages.title)
      :content (-> section-metadata :packages t/image-creation-packages-table)}
     {:title    (t :prerecipe.title)
      :content [(t :prerecipe.description)
                (-> section-metadata :pre-recipe :code (code-area "prerecipe"))]}]))

; Section "deployment recipes"

(defmethod middle-section-content :deployment
  [section-metadata _]
  (localization/with-prefixed-t :section.deployment.subsection
    [{:title    (t :execute.title)
      :content [(t :execute.description)
                (-> section-metadata :targets :execute :code (code-area "execute"))]}
     {:title    (t :report.title)
      :content [(t :report.description)
                (-> section-metadata :targets :report :code (code-area "report"))]}
     {:title    (t :parameters.title)
      :content (-> section-metadata :parameters t/deployment-parameters-table)}
     {:title    (t :on-vm-add.title)
      :content [(t :on-vm-add.description)
                (-> section-metadata :targets :on-vm-add :code (code-area "onvmadd"))]}
     {:title    (t :on-vm-remove.title)
      :content [(t :on-vm-remove.description)
                (-> section-metadata :targets :on-vm-remove :code (code-area "onvmremove"))]}]))

; Section "runs"

(defn- run-section
  [{:keys [cloud-name runs]}]
  {:title cloud-name
   :content (t/runs-table runs)})

(defmethod middle-section-content :runs
  [section-metadata _]
  (map run-section section-metadata))

; Other table sections (e.g. cloud-image-details os-details)

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
    (throw (IllegalArgumentException. (str "No table defined for " metadata-key)))))

(defn- middle-section
  [module metadata-key]
  (let [section-metadata (get module metadata-key)]
    {:title   (localization/section-title metadata-key)
     :content (middle-section-content section-metadata metadata-key)}))

(defn- visible-middle-sections
  []
  (cond-> [:cloud-image-details
           :os-details
           :cloud-configuration
           :image-creation
           :deployment]
   (page-type/view?) (conj :runs)))

(defn middle-sections
  [module]
  (map (partial middle-section module) (visible-middle-sections)))

(defn actions
  [module]
  [action/run
   action/build
   action/edit
   action/copy
   (action/publish    :hidden? (-> module :summary :published?))
   (action/unpublish  :hidden? (-> module :summary :published? not))])
