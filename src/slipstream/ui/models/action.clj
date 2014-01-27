(ns slipstream.ui.models.action
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]))

(defn message
 [metadata]
  (first (html/select metadata [html/root :> [html/text-node]])))
