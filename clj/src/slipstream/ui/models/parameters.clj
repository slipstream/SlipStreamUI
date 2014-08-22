(ns slipstream.ui.models.parameters
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.clojure :as uc]))

(localization/def-scoped-t)

(def ^:private parameter-sel [html/root :> :parameters :> :entry :> :parameter])

(defn- value
  [parameter]
  (-> parameter
      (html/select [:value html/text-node])
      first))

(defn- value
  [parameter]
  (-> parameter
      (html/select [:value html/text-node])
      first))

(defn- help-hint
  [parameter]
  (-> parameter
      (html/select [:instructions html/text-node])
      first))

(defn- order
  [parameter]
  (-> parameter
      (get-in [:attrs :order])
      uc/parse-pos-int
      (or Integer/MAX_VALUE)))

(defn- assoc-enum-details
  [m parameter]
  (if (-> m :type (= "Enum"))
    (assoc m :value-default (-> parameter
                                :attrs
                                (html/select [:defaultValue html/text-node])
                                first)
             :value-options (-> parameter
                                (html/select [:enumValues :string html/text-node])
                                vec))
    m))

(defn- parse-parameter
  [parameter]
  (-> parameter
      :attrs
      (select-keys [:name
                    :type
                    :description
                    :category])
      (assoc        :value (value parameter)
                    :order (order parameter)
                    :read-only? (-> parameter :attrs :readonly uc/parse-boolean)
                    :help-hint (help-hint parameter))
      (assoc-enum-details parameter)))

(def ^:private deployment-categories #{"Input" "Output"})

(defn- parameter-categories
  [metadata]
  (let [params (html/select metadata parameter-sel)
        params-by-cat (group-by #(get-in % [:attrs :category]) params)]
    (for [[cat params] params-by-cat]
      {:category cat
       :category-type (if (deployment-categories cat) :deployment :global)
       :parameters (->> params
                        (map parse-parameter)
                        (sort-by (juxt :order :name)))})))

(defn parse
  [metadata]
  (->> metadata
       parameter-categories
       (sort-by :category)))

;; Parameter util method

(defn map->parameter-list
  "Convert a map of keys and values into a parameter list, in the form used above.
   E.g. [{:name name, :type type, :description description :value value} ... ]"
  [m & conversion-hints]
  (for [[k {:keys [description help-hint type as-parameter]}] (partition 2 conversion-hints)]
     {:name (name k)
      :type type
      :description (or description (-> as-parameter (or k) name (str ".description") keyword t))
      :help-hint (or help-hint (-> as-parameter (or k) name (str ".help-hint") keyword t))
      :value (get m k)}))

(defn categories-of-type
  [parameters type]
  (filter #(= type (get % :category-type)) parameters))