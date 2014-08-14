(ns slipstream.ui.models.users
  (:require [net.cgrand.enlive-html :as html]))

(def ^:private user-sel [:list :item])

(defn- parse-user
  [user-node]
  (let [attrs (:attrs user-node)]
    {:username (:name attrs)
     :uri (:resourceuri attrs)
     :first-name (:firstname attrs)
     :last-name (:lastname attrs)
     :organization (:organization attrs)
     :state (:state attrs)
     :online? (= "true" (:online attrs))
     :last-online (:lastonline attrs)}))

(defn parse
  [metadata]
  (let [user-nodes (html/select metadata user-sel)]
    (->> user-nodes
         (map parse-user)
         (sort-by :username)
         ; (sort-by (complement :online?)) ;; All online users grouped at the top
         )))

