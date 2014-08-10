(ns slipstream.ui.models.users
  (:require [net.cgrand.enlive-html :as html]))

(def ^:private user-sel [:list [:item]])

(defn- user-metadata
  [user-node]
  (let [attrs (:attrs user-node)]
    {:username (:name attrs)
     :uri (:resourceuri attrs)
     :first-name (:firstname attrs)
     :last-name (:lastname attrs)
     :state (:state attrs)
     :online? (= "true" (:online attrs))
     :last-online (:lastonline attrs)}))

(defn users
  [metadata]
  (let [user-nodes (html/select metadata user-sel)]
    (map user-metadata user-nodes)))

