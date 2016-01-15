(ns slipstream.ui.models.user
  (:require [superstring.core :as s]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.parameters :as parameters]))

(localization/def-scoped-t)

(defn- credentials-param?
  [param]
  (->> param
       :name
       (re-find #"\.username|\.password|\.access\.id|\.secret\.key")
       boolean))

(defn- filter-credentials-params
  [params]
  (filter credentials-param? params))

(defn- configured-clouds
  "List the names of the cloud (i.e. connectors) for which credential parameters
  have a configured value."
  [parameters]
  (->> (parameters/categories-of-type parameters :global)
       (map #(update-in % [:parameters] filter-credentials-params))
       (uc/map-in [:parameters] :value)
       (filter #(->> % :parameters (not-any? nil?)))
       (mapv :category)
       set
       not-empty))

(defn- flag-configured-cloud
  [configured-clouds cloud]
  {:pre [(-> configured-clouds nil? not)]}
  (let [configured? (-> cloud :value configured-clouds boolean)]
    (cond-> cloud
       :always           (assoc :configured? configured?)
       (not configured?) (update-in [:text] str " " (t :cloud-not-configured.label)))))

(defn- flag-configured-clouds
  [available-clouds-enum configured-clouds]
  (map (partial flag-configured-cloud (or configured-clouds #{})) available-clouds-enum))

(defn parse
  [metadata]
  (when (not-empty metadata)
    (let [attrs             (:attrs metadata)
          parameters        (parameters/parse metadata)
          configured-clouds (configured-clouds parameters)]
      (-> attrs
          (select-keys [:email
                        :organization
                        :state
                        :creation
                        :roles])
          (assoc        :username     (-> attrs :name u/not-default-new-name)
                        :first-name   (:firstName attrs)
                        :last-name    (:lastName attrs)
                        :uri          (:resourceUri attrs)
                        :super?       (-> attrs :issuper uc/parse-boolean)
                        :deleted?     (-> attrs :deleted uc/parse-boolean)
                        :parameters     parameters
                        :configuration  {:configured-clouds configured-clouds
                                         :available-clouds  (some-> parameters
                                                                    (parameters/value-for "General.default.cloud.service")
                                                                    (flag-configured-clouds configured-clouds)
                                                                    (u/enum-update-name :available-clouds)
                                                                    (u/enum-sort-by :text)
                                                                    u/enum-flag-selected-as-default
                                                                    (u/enum-filter-by :configured?)
                                                                    not-empty)
                                         :keep-running      (some-> parameters
                                                                    (parameters/value-for "General.keep-running")
                                                                    u/enum-selection
                                                                    :value
                                                                    keyword)
                                         :ssh-keys          (some-> parameters
                                                                    (parameters/value-for "General.ssh.public.key")
                                                                    s/trim
                                                                    not-empty)})))))
