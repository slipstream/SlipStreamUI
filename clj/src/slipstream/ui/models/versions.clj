(ns slipstream.ui.models.versions
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]))

(def ^:private version-sel [:versionList :item])
(def ^:private commit-sel [:commit])
(def ^:private commit-comment-sel [:comment])

(defn- commit-metadata
  [version-node]
  (let [commit-node (first (html/select version-node commit-sel))
        commit-comment-node (first (html/select commit-node commit-comment-sel))]
    {:author (get-in commit-node [:attrs :author])
     :comment (first (html/unwrap commit-comment-node))
     :date (get-in version-node [:attrs :lastmodified])}))

(defn- version-metadata
  [version-node]
  (let [attrs (:attrs version-node)]
    {:version (u/parse-pos-int (:version attrs))
     :uri (:resourceuri attrs)
     :commit (commit-metadata version-node)}))

(defn- versions
  [version-nodes]
  (->> version-nodes
       (map version-metadata)
       (sort-by :version)))

(defn- module-name
  [version-nodes]
  (-> version-nodes
      first
      (get-in [:attrs :name])))

(defn parse
  [metadata]
  (let [version-nodes (html/select metadata version-sel)
        first-version-node (first version-nodes)]
   {:versions (versions version-nodes)
    :resource-uri (-> first-version-node
                      (get-in [:attrs :resourceuri])
                      u/trim-last-path-segment)
    :module-name (get-in first-version-node [:attrs :name])
    :category (get-in first-version-node [:attrs :category])}))