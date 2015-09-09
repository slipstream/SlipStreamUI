(ns slipstream.ui.models.usages
  (:require [slipstream.ui.models.pagination :as pagination]
            [slipstream.ui.util.clojure :as uc]))

(defn- parse-usage
  [usage]
  (-> usage
      (select-keys [:id :user :cloud :start_timestamp :end_timestamp :usage])
      (update :id uc/trim-prefix "usage/")))

(defn parse
  [metadata]
  {:pagination (pagination/parse-json :usages metadata (-> metadata meta :request :query-parameters))
   :usages     (map parse-usage (:usages metadata))})