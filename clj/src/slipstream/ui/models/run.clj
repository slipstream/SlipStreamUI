(ns slipstream.ui.models.run
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.runtime-parameters :as runtime-parameters]))

(def run-type-mapping
  {"Orchestration"  "Deployment"
   "Machine"        "Build"
   "Run"             "Simple Run"})

(defn- summary
  [metadata]
  (let [attrs (:attrs metadata)]
    {:category    (-> attrs :category)
     :creation    (-> attrs :creation)
     :start-time  (-> attrs :starttime)
     :end-time    (-> attrs :endtime)
     :deleted?    (-> attrs :deleted uc/parse-boolean)
     :user        (-> attrs :user)
     :state       (-> attrs :state)
     :status      (-> attrs :status)
     :uuid        (-> attrs :uuid)
     :type        (-> attrs :type run-type-mapping)
     :uri         (-> attrs :resourceuri)
     :owner       (-> metadata (html/select [:authz]) first :attrs :owner)
     :module-uri  (-> attrs :moduleresourceuri)}))

(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  (let [runtime-parameters (runtime-parameters/parse metadata)]
    (-> {}
        (assoc :summary (summary metadata))
        (assoc :runtime-parameters runtime-parameters)
        (assoc-in [:summary :tags] (runtime-parameters/value-for runtime-parameters "ss:tags")))))
