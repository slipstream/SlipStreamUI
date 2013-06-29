(ns slipstream.ui.models.modules
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]))

(def sel-modules
  #{[:item]})

(defn children 
  "Extract items from root map (e.g. root module list or versions)"
  [with-items]
  (html/select with-items sel-modules))

(defn first-child
  [with-items]
  (first (children with-items)))

(defn titles-from-versions
  [versions]
  (let 
    [attrs (common/attrs (first-child versions))
     {:keys [name comment category version]} attrs]
    [name "Versions" comment category]))

