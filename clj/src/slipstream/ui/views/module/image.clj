(ns slipstream.ui.views.module.image
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.views.code-area :as code-area]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defmulti middle-section-content
  (fn [module section-title section-metadata metadata-key] metadata-key))

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
  [_ _ section-metadata _]
  (map category-section section-metadata))

; Section "image creation recipes"

(localization/with-prefixed-t :section.image-creation.subsection

  (defn- no-packages-hint
    [module-name section-title]
    (t :packages.empty-content-hint
       (u/module-uri module-name
                     :edit true
                     :hash [(-> section-title uc/keywordize name)
                            (-> :packages.title t uc/keywordize name)])))

  (defn- packages-subsection
    [packages]
    (when (or (page-type/edit-or-new?) (not-empty packages))
       (t/image-creation-packages-table packages)))

  (defmethod middle-section-content :image-creation
    [module section-title section-metadata _]
    [{:title    (t :prerecipe.title)
      :content [(t :prerecipe.description)
                (-> section-metadata :pre-recipe :code (code-area "prerecipe"))]}
     {:title    (t :packages.title)
      :content  (or
                  (packages-subsection (:packages section-metadata))
                  (no-packages-hint (-> module :summary :name) section-title))}
     {:title    (t :recipe.title)
      :content [(t :recipe.description)
                (-> section-metadata :recipe :code (code-area "recipe"))]}])

) ; End of prefixed-t scope

; Section "deployment recipes"

(localization/with-prefixed-t :section.deployment.subsection
  (defmethod middle-section-content :deployment
    [_ _ section-metadata _]
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
                (-> section-metadata :targets :on-vm-remove :code (code-area "onvmremove"))]}
     {:title    (t :pre-scale.title)
      :content [(t :pre-scale.description)
                (-> section-metadata :targets :pre-scale :code (code-area "prescale"))]}
     {:title    (t :post-scale.title)
      :content [(t :post-scale.description)
                (-> section-metadata :targets :post-scale :code (code-area "postscale"))]}]))

; Section "runs"

(defmethod middle-section-content :runs
  [_ _ section-metadata _]
  (if-let [runs (-> section-metadata :runs not-empty)]
    (t/runs-table runs (:pagination section-metadata))
    (t :section.runs.empty-content-hint)))

; Other table sections (e.g. cloud-image-details os-details)

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
           :image-creation
           :deployment]
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
   action/delete])
