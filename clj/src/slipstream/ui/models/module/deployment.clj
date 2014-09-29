(ns slipstream.ui.models.module.deployment
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(defn- parse-node
  [node-metadata]
  (let [attrs (:attrs node-metadata)]
    {:name                  (-> attrs :name)
     :reference-image       (-> attrs :imageuri)
     :default-multiplicity  (-> attrs :multiplicity uc/parse-pos-int)
     :default-cloud         (-> attrs :cloudservice)}))

(defn sections
  [metadata]
  {:nodes (->> (html/select metadata [:nodes :entry :node])
               (map parse-node))})