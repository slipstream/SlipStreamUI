(ns slipstream.ui.models.user.core
  (:require [slipstream.ui.views.utils :as u]
            [slipstream.ui.models.user.loggedin :as l]))

(defn parse
  [metadata]
  (let [loggedin-user (l/parse metadata)
        attrs (:attrs metadata)]
    (-> attrs
        (select-keys [:email
                      :organization
                      :state
                      :creation])
        (assoc        :username   (:name attrs)
                      :first-name (:firstname attrs)
                      :last-name  (:lastname attrs)
                      :uri        (:resourceuri attrs)
                      :super?     (u/parse-boolean (:issuper attrs))
                      :deleted?   (u/parse-boolean (:deleted attrs))
                      :loggedin?  (= (:name attrs) (:username loggedin-user))))))
