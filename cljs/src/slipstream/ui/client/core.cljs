(ns slipstream.ui.client.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :refer [<! put! chan]])
  (:import [goog.net Jsonp]
           [goog Uri]))

(defn init
  "Log something"
  []
  (.log js/console "Hello from cljs"))

(init)
