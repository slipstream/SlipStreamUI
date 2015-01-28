(ns slipstream.ui.models.run-items
  "Parsing of run items, used in model/runs and model/module/image."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.run :as run]))

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

(defn parse
  [metadata]
  (->> (html/select metadata [:runs :item])
       (map parse-run-item)
       (sort-by :cloud-names)))
