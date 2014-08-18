(ns slipstream.ui.models.parameters
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private parameter-sel [html/root :> :parameters :> :entry :> :parameter])

(defn- value
  [parameter]
  (-> parameter
      (html/select [:value html/text-node])
      first))

(defn- order
  [parameter]
  (-> parameter
      (get-in [:attrs :order])
      uc/parse-pos-int
      (or Integer/MAX_VALUE)))

(defn- parse-parameter
  [parameter]
  (-> parameter
      :attrs
      (select-keys [:name
                    :type
                    :description])
      (assoc        :value (value parameter)
                    :order (order parameter))))

(defn- parameter-categories
  [metadata]
  (let [params (html/select metadata parameter-sel)
        params-by-cat (group-by #(get-in % [:attrs :category]) params)]
    (for [[cat params] params-by-cat]
      {:category cat
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
  (for [[k {:keys [description help-hint type]}] (partition 2 conversion-hints)]
     {:name (name k)
      :type type
      :description description
      :help-hint help-hint
      :value (get m k)}))
