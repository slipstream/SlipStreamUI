(ns slipstream.ui.models.user.loggedin
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]))

(def ^:private user-sel [html/root :> :user])

(defn parse
  [metadata]
  (let [user (first (html/select metadata user-sel))
        user-attrs (:attrs user)]
    (when user
      {:username    (:name user-attrs)
       :uri         (:resourceuri user-attrs)
       :super?      (u/parse-boolean (:issuper user-attrs))
       :logged-in?  true})))
