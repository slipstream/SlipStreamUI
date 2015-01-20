(ns slipstream.ui.models.users
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private user-sel [:list :item])

(defn- parse-user
  [user-node]
  (let [attrs (:attrs user-node)]
    {:username      (-> attrs :name)
     :uri           (-> attrs :resourceUri)
     :first-name    (-> attrs :firstName)
     :last-name     (-> attrs :lastName)
     :organization  (-> attrs :organization)
     :state         (-> attrs :state)
     :online?       (-> attrs :online uc/parse-boolean)
     :super?        (-> attrs :issuper uc/parse-boolean)
     :last-online   (-> attrs :lastOnline)}))

(defn parse
  [metadata]
  (let [user-nodes (html/select metadata user-sel)]
    (->> user-nodes
         (map parse-user)
         (sort-by :username)
         ; (sort-by (complement :online?)) ;; All online users grouped at the top
         )))

