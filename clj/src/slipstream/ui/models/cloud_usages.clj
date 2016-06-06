(ns slipstream.ui.models.cloud-usages
  (:require [slipstream.ui.models.pagination :as pagination]))

(defn parse-usage
  [usage]
  (select-keys usage [:id :frequency :start-timestamp :end-timestamp :usage]))

(defn parse
  [metadata]
  {:pagination (pagination/parse-json :usages metadata (-> metadata meta :request :query-parameters))
   :usages     (map parse-usage (:usages metadata))})
