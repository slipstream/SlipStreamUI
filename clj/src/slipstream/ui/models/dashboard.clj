(ns slipstream.ui.models.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.models.configuration :as configuration]))

(localization/def-scoped-t)

(defn- parse-usage
  [usage]
  {:cloud         (-> usage :cloud)
   :current-usage (-> usage :currentUsage uc/parse-pos-int)
   :quota         (-> usage :quota uc/parse-pos-int)})

(defn- prepend-global-usage
  [cloud-usages]
  (cons
    {:cloud         (t :all-clouds.title)
     :global-usage? true
     :current-usage (->> cloud-usages (map :current-usage) (reduce +))
     :quota         (->> cloud-usages (map :quota)         (reduce +))}
    cloud-usages))

(defn- visible?
  [cloud-usage]
  (let [configured-clouds (current-user/configured-clouds)]
    (or
      (-> cloud-usage :global-usage?)
      (-> cloud-usage :current-usage pos?)
      (-> cloud-usage :cloud configured-clouds))))

(defn- filter-visible
  "Non-super users should only see the usage gauges for the clouds they have
  configured in their profile, or the ones they have some usage in."
  [cloud-usages]
  (if (current-user/super?)
    cloud-usages
    (filter visible? cloud-usages)))

(defn- usages
  [dashboard]
  (->> (html/select dashboard [:usage :> :usageElement])
       (map :attrs)
       (map parse-usage)
       prepend-global-usage
       filter-visible
       (sort-by :cloud)))

(defn- clouds
  [dashboard]
  (-> dashboard
      (html/select [:clouds :string html/text-node])
      sort))

(defn parse
  [metadata]
  (let [configuration (configuration/parse metadata)]
    {:clouds    (clouds metadata)
     :quota     {:enabled? (configuration/quota-enabled? configuration)
                 :usage    (usages metadata)}
     :metering  {:enabled? (configuration/metering-enabled? configuration)}}))
