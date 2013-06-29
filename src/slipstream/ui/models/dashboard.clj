(ns slipstream.ui.models.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]))

(defn runs 
  [dashboard]
  (html/select dashboard [:runs :> :item]))

(defn vms 
  [dashboard]
  (html/select dashboard [:vms :> :item]))
