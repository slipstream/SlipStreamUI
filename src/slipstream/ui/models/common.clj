(ns slipstream.ui.models.common
  (:require [net.cgrand.enlive-html :as html]))

(defn attrs [elem]
  (:attrs elem))

(defn elem-name [elem]
  (:name (attrs elem)))

(defn elem-value [elem]
  (:value (attrs elem)))

(defn resourceuri [elem]
  (:resourceuri (attrs elem)))

(defn parameters [elem]
  (html/select elem [:parameter]))

(defn parameter [elem parameter-name]
  (filter #(= parameter-name (:name (:attrs %))) (parameters elem)))

(defn parameter-mappings [elem]
  (html/select elem [:parameterMappings :nodeParameter]))

(defn content [elem]
  (first (:content elem)))

(defn value [elem]
  (first (:content (first (html/select elem [:value])))))

;; Children

(def sel-modules
  #{[:item]})

(defn children-with-filter
  "Extract items from root map and apply filter function"
  [with-items filter-fn]
  (filter filter-fn (html/select with-items sel-modules)))

(defn children 
  "Extract items from root map (e.g. root module list or versions)"
  [with-items]
  (children-with-filter with-items identity))

(defn first-child
  [with-items]
  (first (children with-items)))

;; Grouping and category

(defn group-by-key
  [_key parameters]
  (into (sorted-map) (group-by #(_key (:attrs %)) parameters)))

(defn group-by-category
  [parameters]
  (group-by-key :category parameters))

(defn group-by-group
  [parameters]
  (group-by-key :group parameters))

(defn filter-by-categories
  "Generate a list of filtered parameters"
  [parameters categories]
  (let [grouped (group-by-category parameters)]
    (remove 
      nil? 
      (flatten
        (reduce #(conj %1 (get grouped %2)) [] categories)))))

(defn filter-not-in-categories
  [parameters categories]
  (let [grouped (group-by-category parameters)]
    (apply dissoc grouped categories)))

(defn sort-by-name
  [list]
  (flatten 
    (vals 
      (into 
        (sorted-map) (group-by #(:name (:attrs %)) list)))))
  
  
  
