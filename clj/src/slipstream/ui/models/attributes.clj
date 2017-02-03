(ns slipstream.ui.models.attributes
  (:require
    [slipstream.ui.util.model :as um]))

(defn move-up
  "Embedded map inside m keyed by k is moved up at first level"
  [m k]
  (let [embedded (k m)]
    (-> m
        (dissoc k)
        (merge embedded))))

(defn parse-attribute
  [attribute]
  (let [payload-attributes (um/dissoc-CIMI attribute)]
    {:uri   (:uri payload-attributes)
     :vals  (-> payload-attributes
                (dissoc :uri)
                (move-up :en))}))

(defn parse
  [metadata]
  {:attributes (map parse-attribute (:serviceAttributes metadata))})
