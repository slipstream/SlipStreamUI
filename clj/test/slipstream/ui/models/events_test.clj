(ns slipstream.ui.models.events-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.events :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_events.json"))

(expect
  {:offset        0
   :limit         20
   :count-shown   20
   :count-total   45}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :pagination))

(expect
  {:id        "event/268fd1b2-050c-4126-8a0b-2841555cce9b"
   :target    "run/26"
   :timestamp "2015-09-19T19:53:05.651Z"
   :content   "Started"
   :severity  "low"
   :type      "state"}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :events first))

(expect
  {:id        "event/4c56a355-f186-47fe-be6c-8259bd98463b"
   :target    "run/35"
   :timestamp "2015-09-15T22:19:12.466Z"
   :content   "Started"
   :severity  "high"
   :type      "state"}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :events last))
