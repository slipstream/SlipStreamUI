(ns slipstream.ui.models.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.vms :as vms]
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
     :current-usage (->> cloud-usages (map :current-usage) (reduce +))
     :quota         (->> cloud-usages (map :quota)         (reduce +))}
    cloud-usages))

(defn- usages
  [dashboard]
  (->> (html/select dashboard [:usage :> :usageElement])
       (map :attrs)
       (map parse-usage)
       (sort-by :cloud)
       prepend-global-usage))

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
