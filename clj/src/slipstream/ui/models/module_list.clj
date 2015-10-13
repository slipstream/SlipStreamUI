(ns slipstream.ui.models.module-list
  (:require [net.cgrand.enlive-html :as html]))

(def ^:private app-sel [:list [:item (html/attr-has :published "true")]])
(def ^:private publisher-sel [:authz])
(def ^:private project-sel [:list [:item (html/but (html/attr-has :published "true"))]])

(defn- owner
  [app-node]
  (let [authz-node (first (html/select app-node publisher-sel))]
    (get-in authz-node [:attrs :owner])))

(defn- app-metadata
  [app-node]
  (-> app-node
      :attrs
      (select-keys [:description :name :version])
      (assoc :uri       (get-in app-node [:attrs :resourceUri])
             :image     (get-in app-node [:attrs :logoLink])
             :publisher (owner app-node))))

(defn- published-apps
  [metadata]
  (let [apps-nodes (html/select metadata app-sel)]
    (map app-metadata apps-nodes)))

(defn- project-metadata
  [project-node]
  (-> project-node
      :attrs
      (select-keys [:category :description :version :name])
      (assoc :uri (get-in project-node [:attrs :resourceUri])
             :owner (owner project-node))))

(defn- projects
  [metadata]
  (let [project-nodes (html/select metadata project-sel)]
    (map project-metadata project-nodes)))

(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  {:published-apps  (published-apps metadata)
   :projects        (projects metadata)})