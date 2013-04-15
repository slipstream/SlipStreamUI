(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]))

(defn attrs [module]
  (:attrs module))

(defn author [module]
  (-> (user/attrs module) :username))


