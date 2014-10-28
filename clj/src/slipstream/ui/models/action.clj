(ns slipstream.ui.models.action
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]))

(defn- parse-action-str
  [action-str]
  (let [[_ title message] (re-matches #"(?s)(.*?)\n(.*)" action-str)] ;; (?s) Dot matches all (including newline)
    {:title (when title (s/trim title))
     :message (if message
                (s/trim message)
                action-str)}))

(defn parse
  [metadata]
  (-> metadata
      (html/select [html/text-node])
      first
      (or "")
      s/trim
      parse-action-str))

