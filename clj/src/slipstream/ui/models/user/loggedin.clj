(ns slipstream.ui.models.user.loggedin
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private user-sel [html/root :> :user])

(defn parse
  [metadata]
  (let [user (first (html/select metadata user-sel))
        user-attrs (:attrs user)]
    (when user
      {:username    (:name user-attrs)
       :uri         (:resourceUri user-attrs)
       :super?      (uc/parse-boolean (:issuper user-attrs))
       :logged-in?  true})))
