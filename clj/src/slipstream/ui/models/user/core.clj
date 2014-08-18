(ns slipstream.ui.models.user.core
  (:require [slipstream.ui.util.clojure :as uc]
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
                      :super?     (-> attrs :issuper uc/parse-boolean)
                      :deleted?   (-> attrs :deleted uc/parse-boolean)
                      :loggedin?  (= (:name attrs) (:username loggedin-user))))))
