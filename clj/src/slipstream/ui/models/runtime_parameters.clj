(ns slipstream.ui.models.runtime-parameters
  (:refer-clojure :exclude [filter])
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private runtime-parameter-sel [:runtimeParameter])

(defn- runtime-parameters-section-type
  [section-name]
  (condp re-matches section-name
    #"^Global$"         :global
    #"^orchestrator-.*" :orchestrator
    #"^.*\.[0-9]+$"     :vm
    #"^machine$"        :vm
    :node))

(defn- parse-parameter
  [parameter]
  (let [group-name (->> parameter :attrs :group)
        node-name  (->> group-name (re-matches #"^([^.]*)(?:\.[0-9]+)?$") second)]
    (-> parameter
      :attrs
      (select-keys [:group
                    :type
                    ; :key          ;; NOTE: 'key' is used below as 'name'
                    ; :description  ;; NOTE: 'description' is used below as 'help-hint'
                    :creation])
      (assoc        :deleted?       (-> parameter :attrs :deleted uc/parse-boolean)
                    :set?           (-> parameter :attrs :isSet uc/parse-boolean)
                    :map-others?    (-> parameter :attrs :mapsOthers uc/parse-boolean)
                    :mapped-param-names (-> parameter :attrs :mappedRuntimeParameterNames)
                    :mapped-value?  (-> parameter :attrs :isMappedValue uc/parse-boolean)
                    :name           (-> parameter :attrs :key)
                    :value          (-> parameter (html/select [html/text-node]) first (or "") s/trim)
                    :order          (u/order parameter)
                    :help-hint      (-> parameter :attrs :description)
                    :node           node-name)
      (u/assoc-enum-details parameter))))

(def sections-order {:global 1 :orchestrator 2 :vm 3 :node 4})

(defn- group
  [runtime-parameters]
  (uc/coll-grouped-by :node runtime-parameters
                      :items-keyword :runtime-parameters
                      :items-sort-fn (juxt :order :name)
                      :group-type-fn runtime-parameters-section-type))

(defn parse
  [metadata]
  (->> (html/select metadata runtime-parameter-sel)
       (map parse-parameter)
       group
       (sort-by (comp sections-order :node-type))))

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

(defn filter
  [parameter-type runtime-parameters]
  (->> runtime-parameters
       (mapcat :runtime-parameters)
       (clojure.core/filter #(re-matches (re-pattern (str ".*" parameter-type "$")) (:name %)))))
