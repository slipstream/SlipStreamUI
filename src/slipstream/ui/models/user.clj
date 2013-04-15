(ns slipstream.ui.models.user
  (:require [net.cgrand.enlive-html :as html]))

(def sel-user 
  #{[:tag "user"]})

(defn user [root]
  "Extract user from root map (e.g. module, run)"
  (html/select root [:user]))

(defn attrs [root]
  "Extract user attrs from root map (e.g. module, run)"
  (-> root user first :attrs))

(defn super? [root]
  (= "true" (:issuper (attrs root))))
  
