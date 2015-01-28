(ns slipstream.ui.models.runs
  (:require [slipstream.ui.models.run-items :as run-items]
            [slipstream.ui.models.pagination :as pagination]))

(defn parse
  [metadata]
  {:pagination  (pagination/parse metadata)
   :runs        (run-items/parse  metadata)})
