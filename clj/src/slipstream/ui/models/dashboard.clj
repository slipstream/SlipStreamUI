(ns slipstream.ui.models.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.run-items :as run-items]
            [slipstream.ui.models.vms :as vms]
            [slipstream.ui.models.configuration :as configuration]
            [slipstream.ui.models.common :as common] ;; TODO: remove
            ))

(defn runs
  [dashboard]
  (html/select dashboard [:runs :> :item]))

(defn vms
  [dashboard]
  (html/select dashboard [:vms :> :item]))

(defn usages
  [dashboard]
  (html/select dashboard [:usage :> :usageElement]))

(defn attrs
  [dashboard]
  (common/attrs dashboard))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- parse-usage
  [usage]
  {:cloud         (-> usage :cloud)
   :current-usage (-> usage :currentusage uc/parse-pos-int)
   :quota         (-> usage :quota uc/parse-pos-int)})

(defn usages ; TODO: Make private
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
