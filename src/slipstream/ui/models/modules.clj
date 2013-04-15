(ns slipstream.ui.models.modules
  (:require [net.cgrand.enlive-html :as html]))

(def sel-modules
  #{[:item]})

(defn modules [root]
  "Extract modules from root map (e.g. root module list)"
  (html/select root sel-modules))
