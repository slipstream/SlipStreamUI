(ns slipstream.ui.models.module.project
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(defn- parse-child
  [project-root-uri child-metadata]
  (let [attrs (:attrs child-metadata)]
    {:category    (-> attrs :category)
     :name        (-> attrs :name)
     :description (-> attrs :description)
     :owner       (-> child-metadata (html/select [:authz]) first :attrs :owner)
     :version     (-> attrs :version uc/parse-pos-int)
     :uri         (str project-root-uri "/" (:name attrs) "/" (:version attrs))}))

(defn- children
  [metadata]
  (let [project-root-uri (-> metadata :attrs :resourceUri uc/trim-last-path-segment)]
    (->> (html/select metadata [:children :item])
         (map (partial parse-child project-root-uri))
         (sort-by :name))))

(defn sections
  [metadata]
  {:children (children metadata)})