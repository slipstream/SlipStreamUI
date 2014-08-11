(ns slipstream.ui.models.versions
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u] ))

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

(def ^:private breadcrumbs-root "module")

(defn- breadcrumbs
  "From some module resourceUri, will build up the breadcrumbs metadata.
  E.g. From this uri 'module/examples/tutorials/wordpress/wordpress/180'
    it will build this breadcrumbs:
    [{:text 'examples' :uri 'module/examples'}
     {:text 'tutorials' :uri 'module/examples/tutorials'}
     {:text 'wordpress' :uri 'module/examples/tutorials/wordpress'}
     {:text 'wordpress' :uri 'module/examples/tutorials/wordpress/wordpress'}]"
  [version-node]
  (let [module-path (-> version-node
                        (get-in [:attrs :resourceuri])
                        u/trim-last-path-segment)
        uris (->> module-path
                  (iterate u/trim-last-path-segment)
                  (take-while not-empty)
                  reverse)]
    (into [] (for [uri uris
                   :let [uri-name (u/get-last-path-segment uri)]]
               {:text uri-name
                :uri (when (not= breadcrumbs-root uri-name) uri)}))))

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
    :breadcrumbs (breadcrumbs first-version-node)
    :module-name (get-in first-version-node [:attrs :name])
    :category (get-in first-version-node [:attrs :category])}))