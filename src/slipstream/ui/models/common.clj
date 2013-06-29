(ns slipstream.ui.models.common
  (:require [net.cgrand.enlive-html :as html]))

(defn attrs [elem]
  (:attrs elem))

(defn elem-name [elem]
  (:name (attrs elem)))

(defn parameters [elem]
  (html/select elem [:parameter]))

(defn content [elem]
  (html/select elem [:content]))


;; Grouping and category

(defn group-by-key
  [_key parameters]
  (sort-by key (group-by #(_key (:attrs %)) parameters)))

(defn group-by-category
  [parameters]
  (group-by-key :category parameters))

(defn group-by-group
  [parameters]
  (group-by-key :group parameters))

(defn filter-by-categories
  [parameters categories]
  (let [grouped (group-by-category parameters)]
    (flatten
      (reduce #(conj %1 (get grouped %2)) [] categories))))

(defn filter-not-in-categories
  [parameters categories]
  (let [grouped (group-by-category parameters)]
    (apply dissoc grouped categories)))
