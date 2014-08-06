(ns slipstream.ui.models.image
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.module :as module]))

(defn cloud-image-ids
  [image]
  (html/select image [:cloudImageIdentifier]))

(defn cloud-names
  "Generate a list of cloud names, without default"
  [image]
  (filter
    #(not= module/default-cloud-name %)
    (flatten (map :content (html/select image [:cloudNames :> :string])))))

(defn cloud-image-id
  [image cloud-name]
  (:cloudimageidentifier
    (first
      (filter
        #(= (:cloudservicename %1) cloud-name)
        (map :attrs (cloud-image-ids image))))))

; Creation

(defn creation-recipe
  [image]
  (-> (html/select image [:recipe]) first :content first))

(defn creation-prerecipe
  [image]
  (-> (html/select image [:prerecipe]) first :content first))

(defn creation-packages
  [image]
  (-> (html/select image [:package])))

(defn creates?
  [image]
  (or
    (not
      (clojure.string/blank?
        (str
          (creation-recipe image)
          (creation-prerecipe image))))
    (not
      (empty?
        (creation-packages image)))))

; Deployment

(defn targets
  [image]
  (html/select image [:target]))

(defn target
  [image target-name]
  (first
    (filter
      #(= target-name (-> % :attrs :name))
      (targets image))))

(defn deployment-script
  [image target-name]
  (first (:content (target image target-name))))

(defn deployment-execute
  [image]
  (deployment-script image "execute"))

(defn deployment-report
  [image]
  (deployment-script image "report"))

(defn deployment-onvmadd
  [image]
  (deployment-script image "onvmadd"))

(defn deployment-onvmremove
  [image]
  (deployment-script image "onvmremove"))

(defn deploys?
  [image]
  (or
    (not
      (clojure.string/blank?
        (str
          (deployment-execute image)
          (deployment-report image)
          (deployment-onvmadd image)
          (deployment-onvmremove image))))
    (not
      (empty?
        (common/filter-by-categories
          (common/parameters image)
          ["Input" "Output"])))))

