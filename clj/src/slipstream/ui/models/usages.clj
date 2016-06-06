(ns slipstream.ui.models.usages
  (:require [slipstream.ui.models.pagination :as pagination]))

(defn parse-usage
  [usage]
  (select-keys usage [:id :user :frequency :cloud :start-timestamp :end-timestamp :usage]))

(defn parse
  [metadata]
  {:pagination (pagination/parse-json :usages metadata (-> metadata meta :request :query-parameters))
   :usages     (map parse-usage (:usages metadata))})
