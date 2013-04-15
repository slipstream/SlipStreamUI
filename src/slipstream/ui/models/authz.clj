(ns slipstream.ui.models.authz
  (:require [net.cgrand.enlive-html :as html]))

(defn authz [module]
  "Extract authz map from module"
  (-> (html/select module [html/root :> :authz]) first))

(defn attrs [module]
  (:attrs (authz module)))
