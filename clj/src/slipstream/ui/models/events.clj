(ns slipstream.ui.models.events
  (:require [slipstream.ui.util.clojure :as uc]))

(defn- parse-event
  [event]
  {:id          (-> event :id (uc/trim-prefix "Event/"))
   :target      (-> event :content :resource :href)
   :timestamp   (-> event :timestamp)
   :content     (-> event :content :state)
   :severity    (-> event :severity)
   :type        (-> event :type)})

(defn parse
  [metadata]
  (->> metadata
       :events
       (map parse-event)))
