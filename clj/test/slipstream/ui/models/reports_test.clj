(ns slipstream.ui.models.reports-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.reports :as model]))

(def raw-metadata-str
  "{
    \"files\": [
        {
            \"date\": \"2014-07-05T080220Z\",
            \"name\": \"orchestrator-ec2-eu-west_report_2014-07-05T080220Z.tgz\",
            \"node\": \"orchestrator-ec2-eu-west\",
            \"type\": \"tgz\",
            \"uri\": \"http://slipstream.sixsq.com/reports/91aac79a-7090-4fb6-861d-9cbf6101b5b8/orchestrator-ec2-eu-west_report_2014-07-05T080220Z.tgz\"
        },
        {
            \"date\": \"2014-07-05T080217Z\",
            \"name\": \"wp.1_report_2014-07-05T080217Z.tgz\",
            \"node\": \"wp.1\",
            \"type\": \"tgz\",
            \"uri\": \"http://slipstream.sixsq.com/reports/91aac79a-7090-4fb6-861d-9cbf6101b5b8/wp.1_report_2014-07-05T080217Z.tgz\"
        }
    ]
}")

(expect
  [{:date "2014-07-05T080220Z"
    :name "orchestrator-ec2-eu-west_report_2014-07-05T080220Z.tgz"
    :node "orchestrator-ec2-eu-west"
    :type "tgz"
    :uri "http://slipstream.sixsq.com/reports/91aac79a-7090-4fb6-861d-9cbf6101b5b8/orchestrator-ec2-eu-west_report_2014-07-05T080220Z.tgz"
    :relative-uri "/reports/91aac79a-7090-4fb6-861d-9cbf6101b5b8/orchestrator-ec2-eu-west_report_2014-07-05T080220Z.tgz"}
   {:date "2014-07-05T080217Z"
    :name "wp.1_report_2014-07-05T080217Z.tgz"
    :node "wp.1"
    :type "tgz"
    :uri "http://slipstream.sixsq.com/reports/91aac79a-7090-4fb6-861d-9cbf6101b5b8/wp.1_report_2014-07-05T080217Z.tgz"
    :relative-uri "/reports/91aac79a-7090-4fb6-861d-9cbf6101b5b8/wp.1_report_2014-07-05T080217Z.tgz"}]
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))