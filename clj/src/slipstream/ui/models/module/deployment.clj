(ns slipstream.ui.models.module.deployment
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

; ;; Runs metadata section

; (defn- parse-run
;   [run-metadata]
;   (let [attrs (:attrs run-metadata)]
;     (-> attrs
;         (select-keys [:tags
;                       :status
;                       :uuid
;                       :username])
;         (assoc        :start-time  (:starttime attrs))
;         (assoc        :module-uri  (:moduleresourceuri attrs))
;         (assoc        :uri         (:resourceuri attrs))
;         (assoc        :cloud-name  (:cloudservicename attrs)))))

; (defn- group-runs
;   [runs]
;   (uc/coll-grouped-by :cloud-name runs
;                       :items-keyword :runs))

; (defn- runs
;   [metadata]
;   (->> (html/select metadata [:runs :item])
;        (map parse-run)
;        group-runs))

(defn- parse-node
  [node-metadata]
  (let [attrs (:attrs node-metadata)]
    {:name                  (-> attrs :name)
     :reference-image       (-> attrs :imageuri)
     :default-multiplicity  (-> attrs :multiplicity uc/parse-pos-int)
     :default-cloud         (-> attrs :cloudservice)}))

(defn sections
  [metadata]
  {:nodes (->> (html/select metadata [:nodes :entry :node])
               (map parse-node))
   ; :runs (runs metadata)
   
   })