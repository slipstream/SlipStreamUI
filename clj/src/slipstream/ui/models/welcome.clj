(ns slipstream.ui.models.welcome
  (:require [net.cgrand.enlive-html :as html]))

(def ^:private app-sel [:modules [:item (html/attr-has :published "true")]])
(def ^:private publisher-sel [:authz])
(def ^:private project-sel [:modules [:item (html/but (html/attr-has :published "true"))]])

(defn- owner
  [app-node]
  (let [authz-node (first (html/select app-node publisher-sel))]
    (get-in authz-node [:attrs :owner])))

(defn- app-metadata
  [app-node]
  (-> app-node
      :attrs
      (select-keys [:description :name :version])
      (assoc :uri       (get-in app-node [:attrs :resourceuri])
             :image     (get-in app-node [:attrs :logolink])
             :publisher (owner app-node))))

(defn published-apps
  [metadata]
  (let [apps-nodes (html/select metadata app-sel)]
    (map app-metadata apps-nodes)))

(defn- project-metadata
  [project-node]
  (-> project-node
      :attrs
      (select-keys [:category :description :version :name])
      (assoc :uri (get-in project-node [:attrs :resourceuri])
             :owner (owner project-node))))

(defn projects
  [metadata]
  (let [project-nodes (html/select metadata project-sel)]
    (map project-metadata project-nodes)))

