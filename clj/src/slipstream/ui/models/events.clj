(ns slipstream.ui.models.events
  (:require [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.pagination :as pagination]))

(defn- parse-event
  [event]
  {:id          (-> event :id (uc/trim-prefix "event/"))
   :target      (-> event :content :resource :href)
   :timestamp   (-> event :timestamp)
   :content     (-> event :content :state)
   :severity    (-> event :severity)
   :type        (-> event :type)})

(defn parse
  [metadata]
  {:pagination (pagination/parse-json metadata (-> metadata meta :request :query-parameters))
   :events     (map parse-event (:events metadata))})
