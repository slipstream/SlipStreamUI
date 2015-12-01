(ns slipstream.ui.models.usage-test
  (:use [expectations])
  (:require [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.models.usages :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_usage.json"))

(expect
  {:id              "usage/33c6765c-a9a9-4ac8-883e-0855e60a39ae"
   :user            "zaza"
   :cloud           "aws"
   :start_timestamp "2016-02-22T00:00:00.000Z"
   :end_timestamp   "2016-02-29T00:00:00.000Z"
   :frequency       "weekly"
   :usage           { :ram  {:unit_minutes 571859200}
                      :disk {:unit_minutes 5000.67}
                      :cpu  {:unit_minutes 52500}}}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse-usage))

