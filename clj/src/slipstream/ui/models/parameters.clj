(ns slipstream.ui.models.parameters
  (:refer-clojure :exclude [flatten])
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

(def ^:private parameter-sel [html/root :> :parameters :> :entry :> :parameter])

(defn- trim
  [s]
  (when s
    (s/trim s)))

(defn- value
  [parameter]
  (-> parameter
      (html/select [:value html/text-node])
      first
      trim))

(defn- help-hint
  [parameter]
  (-> parameter
      (html/select [:instructions html/text-node])
      first
      trim))

(defn- parse-parameter
  [parameter]
  (-> parameter
      :attrs
      (select-keys [:name
                    :type
                    :description
                    :category])
      (assoc        :value (value parameter)
                    :order (u/order parameter)
                    :read-only? (-> parameter :attrs :readonly uc/parse-boolean)
                    :mandatory? (-> parameter :attrs :mandatory uc/parse-boolean)
                    :help-hint (help-hint parameter))
      u/normalize-value
      (u/assoc-enum-details parameter)))

(defn- category-type
  [category]
  (case category
    ("Input" "Output")  :deployment
    "General"           :general
    :global))

(defn- group
  [parameters]
  (uc/coll-grouped-by :category parameters
                      :group-type-fn category-type
                      :items-keyword :parameters
                      :items-sort-fn (juxt :order :name)))

(defn- parameter-categories
  [metadata]
  (->> (html/select metadata parameter-sel)
       (map parse-parameter)
       group
       (sort-by :category-type)))

(defn parse
  "Parameters are always grouped by category. To extract an ungrouped list of
  parameters apply the fn flatten below to the result of this parsing. See tests
  for expectations."
  [metadata]
  (parameter-categories metadata))

;; Parameter util methods

(defn map->parameter-list
  "Convert a map of keys and values into a parameter list, in the form used above.
   E.g. [{:name name, :type type, :description description :value value} ... ]"
  [m & conversion-hints]
  (for [[k {:keys [description help-hint type as-parameter id-format-fn] :as hints}]
        (partition 2 conversion-hints)]
    (cond-> {:name (name k)
             :id-format-fn (or id-format-fn uc/dashless-str)
             :type type
             :description (or description (-> as-parameter (or k) name (str ".description") keyword t))
             :help-hint   (or help-hint   (-> as-parameter (or k) name (str ".help-hint")   keyword t))
             :value (get m k)
             :built-from-map? true}
      (contains? hints :required?)    (assoc :required?   (:required?   hints))
      (contains? hints :validation)   (assoc :validation  (:validation  hints))
      (contains? hints :editable?)    (assoc :editable?   (:editable?   hints))
      (contains? hints :read-only?)   (assoc :read-only?  (:read-only?  hints))
      (contains? hints :remove?)      (assoc :remove?     (:remove?     hints))
      (contains? hints :hidden?)      (assoc :hidden?     (:hidden?     hints)))))

(defn categories-of-type
  [parameters & types]
  (filter #((set types) (get % :category-type)) parameters))

(defn flatten
  "The parse function returns the parameters grouped by category.
  This flattens this list. The category is still in each parameters."
  [parameters-by-category]
  (mapcat :parameters parameters-by-category))

(defn- value-when-named
  [parameter-name parameter]
  (when (= parameter-name (:name parameter))
    (:value parameter)))

(defn value-for
  [parameters parameter-name]
  (->> parameters
       flatten
       (some (partial value-when-named parameter-name))))

(defn update
  [parameters parameters-to-update k v]
  (let [parameters-set (set parameters-to-update)
        update-fn (fn [parameter]
                    (if (-> parameter :name parameters-set)
                      (assoc parameter k v)
                      parameter))]
    (uc/map-in [:parameters] update-fn parameters)))