(ns slipstream.ui.models.modules
  (:require [net.cgrand.enlive-html :as html]))

(def sel-modules
  #{[:item]})

(defn modules [project-or-root]
  "Extract modules from root map (e.g. root module list or project)"
  (html/select project-or-root sel-modules))
