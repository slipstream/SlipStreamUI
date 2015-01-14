(ns slipstream.ui.models.runtime-parameters
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private runtime-parameter-sel [:runtimeParameter])

(defn- parse-parameter
  [parameter]
  (-> parameter
      :attrs
      (select-keys [:group
                    :type
                    ; :key          ;; NOTE: 'key' is used below as 'name'
                    ; :description  ;; NOTE: 'description' is used below as 'help-hint'
                    :creation])
      (assoc        :deleted?       (-> parameter :attrs :deleted uc/parse-boolean)
                    :set?           (-> parameter :attrs :isset uc/parse-boolean)
                    :map-others?    (-> parameter :attrs :mapsothers uc/parse-boolean)
                    :mapped-param-names (-> parameter :attrs :mappedruntimeparameternames)
                    :mapped-value?  (-> parameter :attrs :ismappedvalue uc/parse-boolean)
                    :name           (-> parameter :attrs :key)
                    :value          (-> parameter (html/select [html/text-node]) first (or "") s/trim)
                    :order          (u/order parameter)
                    :help-hint      (-> parameter :attrs :description))
      (u/assoc-enum-details parameter)))

(defn- group
  [runtime-parameters]
  (uc/coll-grouped-by :group runtime-parameters
                      :items-keyword :runtime-parameters
                      :items-sort-fn (juxt :order :name)))

(defn parse
  [metadata]
  (->> (html/select metadata runtime-parameter-sel)
       (map parse-parameter)
       group))

;; Runtime-parameter util methods

(defn- value-when-named
  [parameter-name parameter]
  (when (= parameter-name (:name parameter))
    (:value parameter)))

(defn value-for
  [runtime-parameters parameter-name]
  (->> runtime-parameters
       (mapcat :runtime-parameters)
       (some (partial value-when-named parameter-name))))
