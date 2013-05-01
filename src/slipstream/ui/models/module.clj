(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]))

(defn attrs [module]
  (:attrs module))

(defn module-name [module]
  ((:name :attrs module)))

(defn author [module]
  (-> (user/attrs module) :username))

(defn module-comment [module]
  html/select module [:comment])


