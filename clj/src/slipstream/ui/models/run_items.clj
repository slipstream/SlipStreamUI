(ns slipstream.ui.models.run-items
  "Parsing of run items, used in dashboard and module/image models."
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
        (assoc        :start-time  (-> attrs :starttime))
        (assoc        :type        (-> attrs :type run/run-type-mapping))
        (assoc        :module-uri  (-> attrs :moduleresourceuri))
        (assoc        :uri         (-> attrs :resourceuri))
        (assoc        :cloud-name  (-> attrs :cloudservicename)))))

(defn- group-run-items
  [run-items]
  (uc/coll-grouped-by :cloud-name run-items
                      :items-keyword :runs))

(defn parse
  [metadata]
  (->> (html/select metadata [:runs :item])
       (map parse-run-item)
       group-run-items))
