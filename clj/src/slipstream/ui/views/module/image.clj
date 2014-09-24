(ns slipstream.ui.views.module.image
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.code-area :as code-area]
            [slipstream.ui.views.tables :as t]))

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

; Section "image creation recipes"

; (defn- image-creation-section
;   [{:keys [cloud-name runs]}]
;   {:title cloud-name
;    :content (t/runs-table runs)})

(defmethod middle-section-content :image-creation
  [section-metadata _]
  [
  {:title   (t :section.image-creation.subsection.recipe.title)
   :content [(t :section.image-creation.subsection.recipe.description)
             (-> section-metadata :recipe :code (code-area/build false))]}
  {:title   (t :section.image-creation.subsection.packages.title)
   :content (-> section-metadata :packages t/image-creation-packages-table)}
  {:title   (t :section.image-creation.subsection.prerecipe.title)
   :content [(t :section.image-creation.subsection.prerecipe.description)
             (-> section-metadata :pre-recipe :code (code-area/build false))]}
   ])

; Section "deployment recipes"

(defn- merge-in-out-params
  [parameters]
  (concat (some #(and (= "Input"  (:category %)) (:parameters %)) parameters)
          (some #(and (= "Output" (:category %)) (:parameters %)) parameters)))

(defmethod middle-section-content :deployment
  [section-metadata _]
  [
  {:title   (t :section.deployment.subsection.execute.title)
   :content [(t :section.deployment.subsection.execute.description)
             (-> section-metadata :targets :execute :code (code-area/build false))]}
  {:title   (t :section.deployment.subsection.report.title)
   :content [(t :section.deployment.subsection.report.description)
             (-> section-metadata :targets :report :code (code-area/build false))]}
  {:title   (t :section.deployment.subsection.parameters.title)
   :content (-> section-metadata :parameters merge-in-out-params t/deployment-parameters-table)}
  {:title   (t :section.deployment.subsection.on-vm-add.title)
   :content [(t :section.deployment.subsection.on-vm-add.description)
             (-> section-metadata :targets :on-vm-add :code (code-area/build false))]}
  {:title   (t :section.deployment.subsection.on-vm-remove.title)
   :content [(t :section.deployment.subsection.on-vm-remove.description)
             (-> section-metadata :targets :on-vm-remove :code (code-area/build false))]}
   ])

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
    {:title   (->> metadata-key name (format "section.%s.title") keyword t)
     :content (middle-section-content section-metadata metadata-key)}))

(defn middle-sections
  [module]
  (->> [:cloud-image-details
        :os-details
        :cloud-configuration
        :image-creation
        :deployment
        :runs]
       (map (partial middle-section module))))
