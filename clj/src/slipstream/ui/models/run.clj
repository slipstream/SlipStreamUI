(ns slipstream.ui.models.run
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.runtime-parameters :as runtime-parameters]))

(localization/def-scoped-t)

(def ^:private large-run-threshold 19)

(defn run-type-localization-mapping
  "Also used in ns 'slipstream.ui.models.runs'."
  [original-type]
  (case original-type
    "Machine"        (t :type.machine)
    "Run"            (t :type.run)
    "Orchestration"  (t :type.orchestration)
    nil))

(defn run-type-mapping
  [original-type]
  (case original-type
    "Machine"        :image-build
    "Run"            :image-run
    "Orchestration"  :deployment-run
    nil))

(defn- summary
  [metadata runtime-parameters]
  (let [attrs (:attrs metadata)
        node-types (map :node-type runtime-parameters)
        total-orchestrators (->> node-types (filter #{:orchestrator}) count)
        total-nodes         (->> node-types (filter #{:node}) count)
        total-instances     (->> runtime-parameters
                                 (runtime-parameters/filter :multiplicity)
                                 (map :value)
                                 (remove empty?)
                                 (map uc/parse-pos-int)
                                 (remove nil?)
                                 (reduce +))]
    {:category      (-> attrs :category u/t-module-category)
     :creation      (-> attrs :creation)
     :start-time    (-> attrs :startTime)
     :end-time      (-> attrs :endTime)
     :deleted?      (-> attrs :deleted uc/parse-boolean)
     :mutable?      (-> attrs :mutable uc/parse-boolean)
     :run-owner     (-> attrs :user) ;; NOTE: User who started the build (for module owner, see below)
     :state         (-> attrs :state)
     :status        (-> attrs :status)
     :counts        {:total-orchestrators total-orchestrators
                     :total-nodes         total-nodes
                     :total-instances     total-instances}
     :large-run?    (> total-instances large-run-threshold)
     :uuid          (-> attrs :uuid)
     :original-type (-> attrs :type s/lower-case)
     :localized-type(-> attrs :type run-type-localization-mapping)
     :type          (-> attrs :type run-type-mapping)
     :uri           (-> attrs :resourceUri)
     :module-owner  (-> metadata (html/select [:module :authz]) first :attrs :owner)
     :module-uri    (-> attrs :moduleResourceUri)
     :last-state-change (-> attrs :lastStateChangeTime)}))


(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  (let [runtime-parameters (->> metadata
                                runtime-parameters/parse)]
    (-> {}
        (assoc :summary (summary metadata runtime-parameters))
        (assoc :runtime-parameters runtime-parameters)
        (assoc-in [:summary :tags] (runtime-parameters/value-for runtime-parameters "ss:tags"))
        (assoc-in [:summary :abort-msg] (runtime-parameters/value-for runtime-parameters "ss:abort")))))
