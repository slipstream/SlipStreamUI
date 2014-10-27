(ns slipstream.ui.models.parameters
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
                    :help-hint (help-hint parameter))
      u/normalize-value
      (u/assoc-enum-details parameter)))

(def ^:private deployment-categories #{"Input" "Output"})

(defn- group
  [parameters]
  (uc/coll-grouped-by :category parameters
                      :group-type-fn #(if (deployment-categories %) :deployment :global)
                      :items-keyword :parameters
                      :items-sort-fn (juxt :order :name)))

(defn- parameter-categories
  [metadata]
  (->> (html/select metadata parameter-sel)
       (map parse-parameter)
       group))

(defn parse
  [metadata]
  (parameter-categories metadata))

;; Parameter util methods

(defn map->parameter-list
  "Convert a map of keys and values into a parameter list, in the form used above.
   E.g. [{:name name, :type type, :description description :value value} ... ]"
  [m & conversion-hints]
  (for [[k {:keys [description help-hint type as-parameter editable? hidden?] :as hints}]
        (partition 2 conversion-hints)]
    (cond-> {:name (name k)
             :type type
             :description (or description (-> as-parameter (or k) name (str ".description") keyword t))
             :help-hint (or help-hint (-> as-parameter (or k) name (str ".help-hint") keyword t))
             :value (get m k)}
      (contains? hints :editable?) (assoc :editable? editable?)
      (contains? hints :hidden?)   (assoc :hidden? hidden?))))

(defn categories-of-type
  [parameters & types]
  (filter #((set types) (get % :category-type)) parameters))

(defn- value-when-named
  [parameter-name parameter]
  (when (= parameter-name (:name parameter))
    (:value parameter)))

(defn value-for
  [parameters parameter-name]
  (->> parameters
       (mapcat :parameters)
       (some (partial value-when-named parameter-name))))
