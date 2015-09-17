(ns slipstream.ui.models.usage-test
  (:use [expectations])
  (:require [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.models.usages :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_usage.json"))

(expect
  {:id              "usage/9264f72c-668c-4227-8c88-1d6c99f7194b"
   :user            "zaza"
   :cloud           "azure"
   :start_timestamp "2015-09-25T00:00:00.000Z"
   :end_timestamp   "2015-09-26T00:00:00.000Z"
   :usage           {:ram {:unit_minutes 100.0}}}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse-usage))

