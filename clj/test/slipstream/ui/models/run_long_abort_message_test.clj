(ns slipstream.ui.models.run-long-abort-message-test
  (:require
   [expectations :refer :all]
   [slipstream.ui.util.clojure :as uc]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_run_long_abort_message.xml"))
