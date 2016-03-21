(ns slipstream.ui.models.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.models.configuration :as configuration]))

(localization/def-scoped-t)

(defn- parse-usage
  [usage]
  (-> usage
      (uc/update-kvs :keys-fn (comp uc/keywordize str) :vals-fn uc/parse-pos-int)
      (assoc :cloud (:cloud usage))))

(defn- visible?
  [cloud-usage]
  (let [configured-clouds (current-user/configured-clouds)]
    (or
      (->  cloud-usage :cloud configured-clouds)
      (->> (dissoc cloud-usage :cloud) vals (some pos?)))))

(defn- filter-visible
  "Non-super users should only see the usage gauges for the clouds they have
  configured in their profile, or the ones they have some usage in."
  [cloud-usages]
  (if (current-user/super?)
    cloud-usages
    (filter visible? cloud-usages)))

(defn- usages
  [dashboard]
  (->> (html/select dashboard [:cloudUsages :> :cloudUsage])
       (map :attrs)
       (map parse-usage)
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
