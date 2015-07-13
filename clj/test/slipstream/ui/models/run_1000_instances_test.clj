(ns slipstream.ui.models.run-1000-instances-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.run :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_run_1000_instances.xml"))

(def parsed-metadata
  (localization/with-lang :en
    (-> raw-metadata-str
        u/clojurify-raw-metadata-str
        model/parse)))

(expect
  #{:runtime-parameters :summary}
  (-> parsed-metadata
      keys
      set))

(expect
  {:end-time nil
   :status nil
   :last-state-change "2015-01-19 12:31:18.869 CET"
   :deleted? false
   :state "Initializing"
   :counts {:total-orchestrators 1
            :total-nodes 2
            :total-instances 1001}
   :creation "2015-01-19 12:31:18.869 CET"
   :module-uri "module/examples/tutorials/service-testing/system/72"
   :original-type "orchestration"
   :large-run? true
   :mutable? false
   :module-owner "super"
   :start-time "2015-01-19 12:31:18.869 CET"
   :uri "run/3a5b0160-547c-4c1a-b877-4498f9164ced"
   :abort-msg "sh: /usr/bin/nuvlabox-run-instances: Aucun fichier ou dossier de ce type"
   :uuid "3a5b0160-547c-4c1a-b877-4498f9164ced"
   :type :deployment-run
   :localized-type "Deployment Run"
   :run-owner "super"
   :category "Deployment"
   :tags ""}
  (-> parsed-metadata
      :summary))

(expect
  [:global :orchestrator :node :node]
  (->> parsed-metadata
       :runtime-parameters
       (mapv :node-type)))

(expect
  ["testclient.998" "testclient.999" "testclient.1000"]
  (->> parsed-metadata
       :runtime-parameters
       last
       :node-instances
       (map :group)
       distinct
       (take-last 3)))