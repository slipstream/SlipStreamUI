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
                      :super?     (-> attrs :issuper u/parse-boolean)
                      :deleted?   (-> attrs :deleted u/parse-boolean)
                      :loggedin?  (= (:name attrs) (:username loggedin-user))))))
