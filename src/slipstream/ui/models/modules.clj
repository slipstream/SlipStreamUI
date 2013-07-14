(ns slipstream.ui.models.modules
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.module :as module]))

(def sel-modules
  #{[:item]})

(defn children-with-filter
  "Extract items from root map and apply filter function"
  [with-items filter-fn]
  (filter filter-fn (html/select with-items sel-modules)))

(defn children 
  "Extract items from root map (e.g. root module list or versions)"
  [with-items]
  (children-with-filter with-items identity))

(defn children-published
  "Extract items from root map and apply filter function"
  [with-items]
  (children-with-filter with-items #(= "true" (:published (common/attrs %)))))

(defn first-child
  [with-items]
  (first (children with-items)))

(defn titles-from-versions
  [versions]
  (let 
    [attrs (common/attrs (first-child versions))
     {:keys [name comment category version]} attrs]
    [name "Versions" comment category]))

