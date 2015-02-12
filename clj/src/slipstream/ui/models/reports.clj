(ns slipstream.ui.models.reports
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(defn- strip-domain
  [url]
  (if-let [[_ relative-uri] (re-matches #".*(/reports/.*)" url)]
    relative-uri
    url))

(def ^:private date-re #".*(20\d{2}-\d{2}-\d{2}T\d{6}Z).*") ; "2014-07-05T080220Z"
(def ^:private type-re #".*\.(.*)")
(def ^:private name-re #".*\/(.*)")
(def ^:private node-re #".*\/(.*?)_.*")

(defn- parse-href
  [href]
  {:date          (->> href (re-matches date-re) second)
   :name          (->> href (re-matches name-re) second)
   :node          (->> href (re-matches node-re) second)
   :type          (->> href (re-matches type-re) second)
   :uri           href
   :relative-uri  (strip-domain href)})

(defn parse
  [metadata]
  (->> (html/select metadata [:a])
       (map :attrs)
       (map :href)
       (map parse-href)
       (filter #(-> % :name not-empty))))
