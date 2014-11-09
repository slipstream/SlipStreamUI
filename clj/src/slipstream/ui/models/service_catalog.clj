(ns slipstream.ui.models.service-catalog
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.parameters :as parameters]))

(defn- parse-service-catalog
  [service-catalog]
  {:cloud       (-> service-catalog :attrs :cloud)
   :creation    (-> service-catalog :attrs :creation)
   :uri         (-> service-catalog :attrs :resourceuri)
   :parameters  (-> service-catalog parameters/parse parameters/flatten)})

(defn parse
  [metadata]
  (let [service-catalogues (html/select metadata [:serviceCatalog])]
    {:items (->>  service-catalogues
                  (map parse-service-catalog)
                  (sort-by :cloud))}))
