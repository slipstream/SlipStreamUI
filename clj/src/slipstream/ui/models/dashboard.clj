(ns slipstream.ui.models.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.run-items :as run-items]
            [slipstream.ui.models.vms :as vms]
            [slipstream.ui.models.configuration :as configuration]))

(defn- parse-usage
  [usage]
  {:cloud         (-> usage :cloud)
   :current-usage (-> usage :currentUsage uc/parse-pos-int)
   :quota         (-> usage :quota uc/parse-pos-int)})

(defn- usages
  [dashboard]
  (->> (html/select dashboard [:usage :> :usageElement])
       (map :attrs)
       (map parse-usage)
       (sort-by :cloud)))

(defn parse
  [metadata]
  (let [configuration (configuration/parse metadata)]
    {:runs      (run-items/parse metadata)
     :vms       (vms/parse metadata)
     :quota     {:enabled? (configuration/quota-enabled? configuration)
                 :usage    (usages metadata)}
     :metering  {:enabled? (configuration/metering-enabled? configuration)}}))
