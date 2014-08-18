(ns slipstream.ui.models.common
  (:require [net.cgrand.enlive-html :as html]))

(def parameter-sel [html/root :> :parameters :> :entry :> :parameter])

(defn attrs [elem]
  (:attrs elem))

(defn elem-name [elem]
  (:name (attrs elem)))

(defn elem-value [elem]
  (:value (attrs elem)))

(defn resourceuri [elem]
  (:resourceuri (attrs elem)))

(defn parameters [elem]
  (html/select elem parameter-sel))

(defn parameter [elem parameter-name]
  (filter #(= parameter-name (:name (:attrs %))) (parameters elem)))

(defn parameter-mappings [elem]
  (html/select elem [:parameterMappings :parameter]))

(defn content [elem]
  (first (:content elem)))

(defn value [elem]
  (first (:content (first (html/select elem [:value])))))

(defn instructions [elem]
  (-> elem (html/select [:instructions]) first :content first))

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

(defn map-on-vals
  [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn compare-by-key-fn
  [k]
  (fn [a b]
    (compare
      (str (:order (:attrs a)) (k (:attrs a)))
      (str (:order (:attrs b)) (k (:attrs b))))))

(defn- sort-by-k
  [l k]
  (sort (compare-by-key-fn k) l))

(defn sort-by-name
  [l]
  (sort-by-k l :name))

(defn sort-by-key
  [l]
  (sort-by-k l :key))

(defn sort-by-category
  [l]
  (sort-by-k l :category))

(defn sort-map-vals-by-name
  [m]
  (map-on-vals sort-by-name m))

(defn true-value?
  [value]
  (if (empty? value)
    false
    (or
      (true? value)
      (= "true" (clojure.string/trim value)))))


