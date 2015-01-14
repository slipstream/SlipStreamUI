(ns slipstream.ui.models.run
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.runtime-parameters :as runtime-parameters]))

(localization/def-scoped-t)

(defn run-type-mapping
  "Also used in ns 'slipstream.ui.models.run-items'."
  [original-type]
  (case original-type
    "Machine"        (t :type.machine)
    "Run"            (t :type.run)
    "Orchestration"  (t :type.orchestration)
    nil))

(defn- summary
  [metadata]
  (let [attrs (:attrs metadata)]
    {:category      (-> attrs :category)
     :creation      (-> attrs :creation)
     :start-time    (-> attrs :starttime)
     :end-time      (-> attrs :endtime)
     :deleted?      (-> attrs :deleted uc/parse-boolean)
     :mutable?      (-> attrs :mutable uc/parse-boolean)
     :user          (-> attrs :user)
     :state         (-> attrs :state)
     :status        (-> attrs :status)
     :uuid          (-> attrs :uuid)
     :original-type (-> attrs :type s/lower-case)
     :type          (-> attrs :type run-type-mapping)
     :uri           (-> attrs :resourceuri)
     :owner         (-> metadata (html/select [:authz]) first :attrs :owner)
     :module-uri    (-> attrs :moduleresourceuri)}))

(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  (let [runtime-parameters (runtime-parameters/parse metadata)]
    (-> {}
        (assoc :summary (summary metadata))
        (assoc :runtime-parameters runtime-parameters)
        (assoc-in [:summary :tags] (runtime-parameters/value-for runtime-parameters "ss:tags"))
        (assoc-in [:summary :global-ss-abort] (runtime-parameters/value-for runtime-parameters "ss:abort")))))
