(ns slipstream.ui.models.module.image
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.run-items :as run-items]
            [slipstream.ui.models.parameters :as parameters]))


;; Logo image info

(defn- logo-image
  [metadata]
  {:image (-> metadata :attrs :logolink)})


;; Cloud image section

(defn- cloud-image-details
  [metadata]
  {:native-image?     (-> metadata :attrs :isbase uc/parse-boolean)
   :cloud-identifiers (->> (html/select metadata [:cloudImageIdentifier])
                           (map :attrs)
                           (map (juxt :cloudservicename :cloudimageidentifier))
                           (into {}))
   :reference-image   (-> metadata :attrs :modulereferenceuri)})


;; Os details section

(def ^:private platforms
  [:centos
   :debian
   :fedora
   :opensuse
   :redhat
   :sles
   :ubuntu
   :windows
   :other])

(defn- os-details
  [metadata]
  {:platform         (->> metadata :attrs :platform (u/enum platforms))
   :login-username   (-> metadata :attrs :loginuser)})


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


;; Metadata section build

(defn sections
  [metadata]
  (let [parameters (parameters/parse metadata)]
    {:summary              (logo-image metadata)
     :cloud-image-details  (cloud-image-details metadata)
     :os-details           (os-details metadata)
     :cloud-configuration  (parameters/categories-of-type parameters :global)
     :image-creation       (image-creation metadata)
     :deployment           (deployment metadata parameters)
     :runs                 (run-items/parse metadata)}))

