(ns slipstream.ui.models.run-ready-abort-test
  (:require [slipstream.ui.util.clojure :as uc]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_run_ready_abort.xml"))
