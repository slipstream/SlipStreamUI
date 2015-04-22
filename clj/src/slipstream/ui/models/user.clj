(ns slipstream.ui.models.user
  (:require [clojure.string :as s]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.parameters :as parameters]))

(defn- configured-clouds
  "List the names of the cloud (i.e. connectors) for which all parameters have a
  configured value."
  [parameters]
  (->> (parameters/categories-of-type parameters :global)
       ; TODO: Take into account only parameters with {:mandatory true}
       (uc/map-in [:parameters] :value)
       (filter #(->> % :parameters (not-any? nil?)))
       (mapv :category)
       not-empty))

(defn parse
  [metadata]
  (when (not-empty metadata)
    (let [attrs             (:attrs metadata)
          parameters        (parameters/parse metadata)]
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
                        :parameters     parameters
                        :configuration  {:configured-clouds (configured-clouds parameters)
                                         :available-clouds  (some-> parameters
                                                                    (parameters/value-for "General.default.cloud.service")
                                                                    (u/enum-update-name :available-clouds)
                                                                    (u/enum-sort-by :text)
                                                                    u/enum-flag-selected-as-default)
                                         :keep-running      (some-> parameters
                                                                    (parameters/value-for "General.keep-running")
                                                                    u/enum-selection
                                                                    :value
                                                                    keyword)
                                         :ssh-keys          (some-> parameters
                                                                    (parameters/value-for "General.ssh.public.key")
                                                                    s/trim
                                                                    not-empty)})))))