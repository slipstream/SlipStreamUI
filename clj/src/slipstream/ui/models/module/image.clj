(ns slipstream.ui.models.module.image
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.parameters :as parameters]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Runs metadata section

(defn- parse-run
  [run-metadata]
  (let [attrs (:attrs run-metadata)]
    (-> attrs
        (select-keys [:tags
                      :status
                      :uuid
                      :username])
        (assoc        :start-time  (:starttime attrs))
        (assoc        :module-uri  (:moduleresourceuri attrs))
        (assoc        :uri         (:resourceuri attrs))
        (assoc        :cloud-name  (:cloudservicename attrs)))))

(defn- group-runs
  [runs]
  (uc/coll-grouped-by :cloud-name runs
                      :items-keyword :runs))

(defn- runs
  [metadata]
  (->> (html/select metadata [:runs :item])
       (map parse-run)
       group-runs))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Image creation metadata section

(defn- parse-recipe
  [recipe-type metadata]
  {:code (-> metadata (html/select [recipe-type html/text-node]) first)})

(defn- parse-package
  [package-metadata]
  (let [attrs (:attrs package-metadata)]
    (-> attrs
        (select-keys [:key
                      :name
                      :repository]))))

(defn- image-creation
  [metadata]
  {:recipe      (parse-recipe :recipe metadata)
   :packages    (->> (html/select metadata [:package])
                     (map parse-package)
                     (sort-by :name))
   :pre-recipe  (parse-recipe :prerecipe metadata)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Deployment metadata section

(def ^:private target-names
  {:execute       "execute"
   :report        "report"
   :parameters    "parameters"
   :on-vm-add     "onvmadd"
   :on-vm-remove  "onvmremove"})

(defn- assoc-target
  [m target metadata]
  (let [target-metadata (-> metadata
                            (html/select [[:target (html/attr= :name (target-names target))]])
                            first)]
    (assoc-in m [:targets target]
        {:code              (-> target-metadata (html/select [html/text-node]) first)
         :run-in-background (-> target-metadata :attrs :runinbackground uc/parse-boolean)})))


(defn- deployment
  [metadata parameters]
  (-> {:parameters (parameters/categories-of-type parameters :deployment)}
      (assoc-target :execute metadata)
      (assoc-target :report metadata)
      (assoc-target :on-vm-add metadata)
      (assoc-target :on-vm-remove metadata)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Metadata section build

(defn sections
  [metadata]
  (let [parameters (parameters/parse metadata)]
    (-> {}
        (assoc-in [:summary :image] (-> metadata :attrs :logolink))

        (assoc-in [:cloud-image-details :native-image?]     (-> metadata :attrs :isbase uc/parse-boolean))
        (assoc-in [:cloud-image-details :cloud-identifiers] (->> (html/select metadata [:cloudImageIdentifier])
                                                                 (map :attrs)
                                                                 (map (juxt :cloudservicename :cloudimageidentifier))
                                                                 (into {})))
        (assoc-in [:cloud-image-details :reference-image]   (-> metadata :attrs :modulereferenceuri))

        (assoc-in [:os-details :platform]         (-> metadata :attrs :platform))
        (assoc-in [:os-details :login-username]   (-> metadata :attrs :loginuser))

        (assoc-in [:cloud-configuration]          (-> parameters
                                                      (parameters/categories-of-type :global)))

        (assoc-in [:image-creation]               (image-creation metadata))
        (assoc-in [:deployment]                   (deployment metadata parameters))
        (assoc-in [:runs]                         (runs metadata))
        )))

