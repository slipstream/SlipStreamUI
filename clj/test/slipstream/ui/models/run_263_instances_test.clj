(ns slipstream.ui.models.run-263-instances-test
  (:require [slipstream.ui.util.clojure :as uc]))

;; Note: This is only to be used by test (headless) server in 'slipstream.ui.main'
(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_run_263_instances.xml"))

