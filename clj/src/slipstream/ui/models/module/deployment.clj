(ns slipstream.ui.models.module.deployment
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.models.runs :as runs]))

(defn- append-template-node-in-edit-mode
  "To serve as template for new nodes. See tests for expectations."
  [items]
  (if (page-type/edit-or-new?)
    (conj (vec items) {:template-node? true})
    items))

(defn- parse-parameter-mapping
  [parameter-mapping-metadata]
  (let [attrs (:attrs parameter-mapping-metadata)]
    {:name          (-> attrs :name)
     :mapped-value? (-> attrs :isMappedValue uc/parse-boolean)
     :value         (-> parameter-mapping-metadata
                        (html/select [:value html/text])
                        first)}))

(defn- append-template-mapping
  "To serve as template for new nodes. See tests for expectations."
  [items]
  (conj (vec items) {:template-mapping? true}))

(defn- parse-node
  [cloud-names node-metadata]
  (let [attrs (:attrs node-metadata)
        template-node? (:template-node? node-metadata)]
    {:name                  (-> attrs :name)
     :template-node?        template-node?
     :reference-image       (-> attrs :imageUri (or "new") u/module-name)
     :default-multiplicity  (-> attrs :multiplicity uc/parse-pos-int (or 1))
     :default-cloud         (->> attrs :cloudService (or "default") (u/enum cloud-names :cloud-names))
     :output-parameters     (->> (html/select node-metadata [:image :parameters :entry [:parameter (html/attr-has :category "Output")]])
                                 (map (comp :name :attrs))
                                 sort)
     :mappings              (cond->> (html/select node-metadata [:parameterMappings :entry :parameter])
                              template-node?  append-template-mapping
                              :always         (mapv parse-parameter-mapping)
                              :always         (sort-by :name))}))

(defn- append-template-node-in-edit-mode
  "To serve as template for new nodes. See tests for expectations."
  [items]
  (if (page-type/edit-or-new?)
    (conj (vec items) {:template-node? true})
    items))

(defn sections
  [metadata]
  (let [cloud-names (html/select metadata [:cloudNames :string html/text])]
    {:nodes (->> (html/select metadata [:nodes :entry :node])
                 (sort-by (comp :name :attrs)) ; Sort before adding the blank template row.
                 append-template-node-in-edit-mode
                 (map (partial parse-node cloud-names)))
     :runs       (runs/parse metadata)}))