(ns slipstream.ui.models.versions
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private version-sel [:versionList :item])
(def ^:private version-list-sel [:versionList])
(def ^:private commit-sel [:commit])
(def ^:private commit-comment-sel [:comment])

(defn- commit-metadata
  [version-node]
  (let [commit-node (first (html/select version-node commit-sel))
        commit-comment-node (first (html/select commit-node commit-comment-sel))]
    {:author (get-in commit-node [:attrs :author])
     :comment (first (html/unwrap commit-comment-node))
     :date (get-in version-node [:attrs :lastModified])}))

(defn- version-metadata
  [version-node]
  (let [attrs (:attrs version-node)]
    {:version (uc/parse-pos-int (:version attrs))
     :uri (:resourceUri attrs)
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
  (let [version-nodes       (html/select metadata version-sel)
        first-version-node  (first version-nodes)
        version-list        (first (html/select metadata version-list-sel))]
    {:versions          (versions version-nodes)
     :resource-uri      (-> first-version-node
                            (get-in [:attrs :resourceUri])
                            uc/trim-last-path-segment)
     :module-short-name (get-in first-version-node [:attrs :name])
     :module-name       (get-in version-list [:attrs :moduleName])
     :category          (get-in version-list [:attrs :moduleCategory])}))
