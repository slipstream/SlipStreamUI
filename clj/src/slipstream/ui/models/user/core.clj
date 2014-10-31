(ns slipstream.ui.models.user.core
  (:require [clojure.string :as s]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.current-user :as current-user]))

(defn parse
  [metadata]
  (let [attrs (:attrs metadata)]
    (-> attrs
        (select-keys [:email
                      :organization
                      :state
                      :creation])
        (assoc        :username   (-> attrs :name u/not-default-new-name)
                      :first-name (:firstname attrs)
                      :last-name  (:lastname attrs)
                      :uri        (:resourceuri attrs)
                      :super?     (-> attrs :issuper uc/parse-boolean)
                      :deleted?   (-> attrs :deleted uc/parse-boolean)
                      :loggedin?  (= (:name attrs) (current-user/username))))))
