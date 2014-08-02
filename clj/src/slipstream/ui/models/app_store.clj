(ns slipstream.ui.models.app-store
  (:require [net.cgrand.enlive-html :as html]))

(def ^:private app-sel [:modules [:item (html/attr-has :published "true")]])
(def ^:private publisher-sel [:authz])

(defn- app-publisher
  [app-node]
  (let [authz-node (first (html/select app-node publisher-sel))]
    (get-in authz-node [:attrs :owner])))

(defn- app-metadata
  [app-node]
  (-> (select-keys (:attrs app-node) [:description :name :version])
      (assoc :uri (get-in app-node [:attrs :resourceuri]))
      (assoc :image (get-in app-node [:attrs :logolink]))
      (assoc :publisher (app-publisher app-node))))

(defn published-apps
  [metadata]
  (let [apps-nodes (html/select metadata app-sel)]
    (map app-metadata apps-nodes)))
