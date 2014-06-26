(ns slipstream.ui.models.modules
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.module :as module]))

(defn children-with-filter
  [with-items filter-fn]
  (common/children-with-filter with-items filter-fn))

(defn children 
  [with-items]
  (common/children with-items))

(defn children-published
  "Extract items from root map and apply filter function"
  [with-items]
  (children-with-filter with-items #(= "true" (:published (common/attrs %)))))

(defn first-child
  [with-items]
  (common/first-child with-items))

(defn titles-from-versions
  [versions]
  (let 
    [attrs (common/attrs (first-child versions))
     {:keys [name comment category version]} attrs]
    [name "Versions" comment category]))
