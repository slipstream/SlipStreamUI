(ns slipstream.ui.models.user.core
  (:require [clojure.string :as s]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.current-user :as current-user]))

(defn- not-new
  "When setting up a new user, the server passes a blank metadata object with
  the username set to 'new', which has to be ignored."
  [username]
  (s/replace username #"^new$" ""))

(defn parse
  [metadata]
  (let [attrs (:attrs metadata)]
    (-> attrs
        (select-keys [:email
                      :organization
                      :state
                      :creation])
        (assoc        :username   (-> attrs :name not-new)
                      :first-name (:firstname attrs)
                      :last-name  (:lastname attrs)
                      :uri        (:resourceuri attrs)
                      :super?     (-> attrs :issuper uc/parse-boolean)
                      :deleted?   (-> attrs :deleted uc/parse-boolean)
                      :loggedin?  (= (:name attrs) (current-user/username))))))
