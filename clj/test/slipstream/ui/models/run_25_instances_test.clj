(ns slipstream.ui.models.run-25-instances-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.run :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_run_25_instances.xml"))

(def parsed-metadata
  (localization/with-lang :en
    (-> raw-metadata-str
        u/clojurify-raw-metadata-str
        model/parse)))

(expect
  [:runtime-parameters :summary]
  (-> parsed-metadata
      keys))

(expect
  {:end-time nil
   :status nil
   :last-state-change "2015-02-03 13:36:35.370 CET"
   :deleted? false
   :state "Initializing"
   :counts {:total-orchestrators 1
            :total-nodes 1
            :total-instances 25}
   :creation "2015-02-03 13:36:35.370 CET"
   :module-uri "module/first-project/newdeployment/87"
   :original-type "orchestration"
   :large-run? true
   :mutable? false
   :module-owner "rob"
   :start-time "2015-02-03 13:36:35.374 CET"
   :uri "run/07596eb7-f1f7-4e45-8561-5dbbc55cc817"
   :uuid "07596eb7-f1f7-4e45-8561-5dbbc55cc817"
   :type :deployment-run
   :localized-type "Deployment Run"
   :run-owner "rob"
   :category "Deployment"
   :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/rob'> user account</a>"
   :tags ""}
  (-> parsed-metadata
      :summary))

(expect
  [:global :orchestrator :node]
  (->> parsed-metadata
       :runtime-parameters
       (mapv :node-type)))

(expect
  ["ndoe1.23" "ndoe1.24" "ndoe1.25"]
  (->> parsed-metadata
       :runtime-parameters
       last
       :node-instances
       (map :group)
       distinct
       (take-last 3)))