(ns slipstream.ui.models.runs
  "Parsing of run items, used in model/runs and model/module/image."
  (:require #_[slipstream.ui.models.run-items :as run-items]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.run :as run]
            [slipstream.ui.models.pagination :as pagination]))

(defn- parse-run-item
  [run-item-metadata]
  (let [attrs (:attrs run-item-metadata)]
    (-> attrs
        (select-keys [:tags
                      :status
                      :uuid
                      :username])
        (assoc        :start-time  (-> attrs :startTime))
        (assoc        :abort-msg   (-> attrs :abort))
        (assoc        :type        (-> attrs :type run/run-type-mapping))
        (assoc        :module-uri  (-> attrs :moduleResourceUri))
        (assoc        :uri         (-> attrs :resourceUri))
        (assoc        :cloud-names (-> attrs :cloudServiceNames)))))

(defn- parse-run-items
  [metadata]
  (->> (html/select metadata [:runs :item])
       (map parse-run-item)
       (sort-by :cloud-names)))

(defn parse
  [metadata]
  (let [runs-metadata (first (html/select metadata [:runs]))]
    {:pagination  (pagination/parse runs-metadata)
     :runs        (parse-run-items  runs-metadata)}))
