(ns slipstream.ui.models.usages-test
  (:use [expectations])
  (:require [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.models.usages :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_usages.json"))

(expect
  {:offset      0
   :limit       20
   :count-shown 20
   :count-total 100}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :pagination))

(expect
  {:id              "6bdc86aa-8d29-4e13-8f90-74fcd64a732d"
   :user            "zaza"
   :cloud           "aws"
   :start_timestamp "2015-10-28T00:00:00.000Z"
   :end_timestamp   "2015-10-29T00:00:00.000Z"
   :usage           {:ram {:unit_minutes 100.0}}}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :usages first))

(expect
  {:id              "7f3cfc82-c6dc-4bb9-af64-03af2f7e586a"
   :user            "zaza"
   :cloud           "exo"
   :start_timestamp "2015-10-19T00:00:00.000Z"
   :end_timestamp   "2015-10-20T00:00:00.000Z"
   :usage           {:ram  {:unit_minutes 100.0}
                     :disk {:unit_minutes 1250.0}}}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :usages last))
