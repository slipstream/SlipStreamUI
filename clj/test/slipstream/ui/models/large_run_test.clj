(ns slipstream.ui.models.large-run-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.run :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_run_1000_instance.xml"))

(expect
  [:runtime-parameters :summary]
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse keys)))

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
   :owner "super"
   :start-time "2015-01-19 12:31:18.869 CET"
   :uri "run/3a5b0160-547c-4c1a-b877-4498f9164ced"
   :global-ss-abort "sh: /usr/bin/nuvlabox-run-instances: Aucun fichier ou dossier de ce type"
   :uuid "3a5b0160-547c-4c1a-b877-4498f9164ced"
   :type :deployment-run
   :localized-type "Deployment Run"
   :user "super"
   :category "Deployment"
   :tags ""}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :summary)))

(expect
  [:global :orchestrator :node :node]
  (localization/with-lang :en
    (->> raw-metadata-str u/clojurify-raw-metadata-str model/parse :runtime-parameters (mapv :node-type))))
