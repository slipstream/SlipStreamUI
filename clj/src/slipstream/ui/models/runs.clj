(ns slipstream.ui.models.runs
  "Parsing of run items, used in model/runs and model/module/image."
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.run :as run]
            [slipstream.ui.models.pagination :as pagination]))

(defn- display-status
  [status abort-flag?]
  (case [status abort-flag?]
    ["Initializing"     true ] :run-with-abort-flag-set
    ["Initializing"     false] :run-in-transitional-state
    ["Provisioning"     true ] :run-with-abort-flag-set
    ["Provisioning"     false] :run-in-transitional-state
    ["Executing"        true ] :run-with-abort-flag-set
    ["Executing"        false] :run-in-transitional-state
    ["Sending reports"  true ] :run-with-abort-flag-set
    ["Sending reports"  false] :run-in-transitional-state
    ["Ready"            true ] :run-with-abort-flag-set
    ["Ready"            false] :run-successfully-ready
    ["Finalizing"       true ] :run-with-abort-flag-set
    ["Finalizing"       false] :run-in-transitional-state
    ["Done"             true ] :run-with-abort-flag-set
    ["Done"             false] :run-terminated
    ["Aborted"          true ] :run-terminated
    ["Aborted"          false] :run-terminated
    ["Cancelled"        true ] :run-terminated
    ["Cancelled"        false] :run-terminated
    ;; TODO: nil will be returned if an unexpected state appear.
    ;;       Should properly log this.
    nil))

(def ^:private terminated-states
  #{"Finalizing"
    "Done"
    "Aborted"
    "Cancelled"})

(defn- parse-run-item
  [run-item-metadata]
  (let [attrs       (:attrs run-item-metadata)
        abort-msg   (-> attrs :abort (or "") s/trim not-empty)
        abort-flag? (boolean abort-msg)]
    (-> attrs
        (select-keys [:tags
                      :status
                      :uuid
                      :username])
        (assoc        :start-time     (-> attrs :startTime))
        (assoc        :abort-msg      abort-msg)
        (assoc        :abort-flag?    abort-flag?)
        (assoc        :display-status (display-status (:status attrs) abort-flag?))
        (assoc        :type           (-> attrs :type run/run-type-mapping))
        (assoc        :terminable?    (-> attrs :status terminated-states not))
        (assoc        :module-uri     (-> attrs :moduleResourceUri))
        (assoc        :uri            (-> attrs :resourceUri))
        (assoc        :service-url    (-> attrs :serviceUrl (or "") s/trim not-empty))
        (assoc        :cloud-names    (-> attrs :cloudServiceNames)))))

(defn- parse-run-items
  [metadata]
  (->> (html/select metadata [:runs :item])
       (map parse-run-item)))

(defn parse
  [metadata]
  (let [runs-metadata (first (html/select metadata [:runs]))]
    {:pagination  (pagination/parse runs-metadata)
     :runs        (parse-run-items  runs-metadata)}))
