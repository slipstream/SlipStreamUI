(ns slipstream.ui.models.image
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common-model]))

(defn cloud-image-ids 
  [image]
  (html/select image [:cloudImageIdentifier]))

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

(defn deploys? 
  [image]
  (or
    (not
      (clojure.string/blank?
        (str
          (deployment-execute image)
          (deployment-report image))))
    (not
      (empty?
        (common-model/filter-by-categories 
          (common-model/parameters image) 
          ["Input" "Output"])))))

