(ns slipstream.ui.models.user
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.parameters :as parameters]))

(defn parse
  [metadata]
  (when (not-empty metadata)
    (let [attrs       (:attrs metadata)
          parameters  (parameters/parse metadata)]
      -
      (-> attrs
          (select-keys [:email
                        :organization
                        :state
                        :creation])
          (assoc        :username   (-> attrs :name u/not-default-new-name)
                        :first-name (:firstName attrs)
                        :last-name  (:lastName attrs)
                        :uri        (:resourceUri attrs)
                        :super?     (-> attrs :issuper uc/parse-boolean)
                        :deleted?   (-> attrs :deleted uc/parse-boolean)
                        :configuration {:cloud        (-> parameters
                                                          (parameters/value-for "General.default.cloud.service")
                                                          u/enum-selection
                                                          :value)
                                        :keep-running (-> parameters
                                                          (parameters/value-for "General.keep-running")
                                                          u/enum-selection
                                                          :value
                                                          keyword)
                                        :ssh-keys     (-> parameters
                                                         (parameters/value-for "General.ssh.public.key"))})))))