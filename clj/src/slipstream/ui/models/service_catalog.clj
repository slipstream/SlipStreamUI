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
  (let [service-catalogs (html/select metadata [:serviceCatalog])]
    {:items (->> service-catalogs
                 (map parse-service-catalog)
                 (filter (comp not-empty :parameters))
                 (remove nil?)
                 (sort-by :cloud))}))
