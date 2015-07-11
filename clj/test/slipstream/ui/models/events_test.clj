(ns slipstream.ui.models.events-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.events :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_events.json"))

(expect
  {:id        "083e13c6-2dcc-4711-b72e-2880d12b989d"
   :target    "run/7fa1e339-f863-4ecb-b8ac-ffe6b0e343e3"
   :timestamp "2015-06-11T10:16:07.457Z"
   :content   "Done"
   :severity  "medium"
   :type      "state"}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse first))

(expect
  {:id        "670d2def-7c49-4281-a03e-289fb0b1f9f4"
   :target    "run/7fa1e339-f863-4ecb-b8ac-ffe6b0e343e3"
   :timestamp "2015-06-11T00:10:14.339Z"
   :content   "Something high"
   :severity  "high"
   :type      "example"}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse last))
