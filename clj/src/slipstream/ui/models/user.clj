(ns slipstream.ui.models.user
  (:require [net.cgrand.enlive-html :as html]))

(def sel-user
  #{[:tag "user"]})

(defn user [metadata]
  "Extract user from metadata map (e.g. module, run)"
  (first (html/select metadata #{[html/root :> :user] [:user]})))

(defn logged-in [metadata]
  "Extract logged-in user from metadata map (e.g. module, run)"
  (first (html/select metadata [html/root :> :user])))

(defn attrs [metadata]
  "Extract user attrs from root map (e.g. module, run)"
  (-> metadata user :attrs))

(defn super? [metadata]
  (= "true" (:issuper (attrs metadata))))

(defn username [metadata]
  (:name (attrs metadata)))

(defn user-map
  [metadata]
  (let [logged-in-user (first (html/select metadata [html/root :> :user]))
        user (or logged-in-user
                 (first (html/select metadata [:user])))
        user-attrs (:attrs user)]
    (when user
      {:name (username metadata)
       :uri (:resourceuri user-attrs)
       :super? (= "true" (:issuper user-attrs))
       :logged-in? (boolean logged-in-user)})))

(defn default-cloud [metadata]
  (-> metadata user attrs :defaultcloud))

