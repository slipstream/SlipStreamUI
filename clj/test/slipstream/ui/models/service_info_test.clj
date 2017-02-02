(ns slipstream.ui.models.service-info-test
  (:require
    [expectations :refer :all]
    [slipstream.ui.util.clojure :as uc]
    [slipstream.ui.util.core :as u]
    [slipstream.ui.models.service-info :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_service_infos.json"))

;; attributes definition as returned by attribute resource
(def raw-attributes-json
  (uc/slurp-resource "slipstream/ui/mockup_data/resource_attributes.json"))

(expect
   [{:uri        "http://schema.example.org/ramCapacityGiB"
     :type       "int",
     :name       {:en "ramCapacityGiB"},
     :description
                 {:en
                  "Maximum amount of RAM that is available on the supplier's infrastructure in Gibibytes (GiB)."},
     :categories {:en ["service" "location"]},
     :value      "6000",
     :nested     false}
    {:name   {:en "connector"},
     :value  {:href "uxx2"},
     :nested false}
    {:uri         "http://schema.example.org/maxStorageKB"
      :type        "number",
     :name        {:en "maxStorageKB"},
     :description {:en "The max storage expressed in KB."},
     :categories  {:en ["capacity"]},
     :value       2000000000000,
     :nested      false}
    {:name   {:en "http://schema.example.org/supplierUrl"},
     :value  "https://uxx2.com",
     :nested false}
    {:uri        "http://schema.example.org/composite"
     :type
                  ["http://schema.example.org/ramCapacityGiB"
                   "http://schema.example.org/country"],
     :name        {:en "composite"},
     :description {:en "A composite type"},
     :categories  {:en ["service" "location"]},
     :nested      true}
    {:uri         "http://schema.example.org/ramCapacityGiB"
     :type       "int",
     :name       {:en "ramCapacityGiB"},
     :description
                 {:en
                  "Maximum amount of RAM that is available on the supplier's infrastructure in Gibibytes (GiB)."},
     :categories {:en ["service" "location"]},
     :value      1500,
     :nested     true}
    {:uri        "http://schema.example.org/country"
     :type       "string",
     :name       {:en "country"},
     :description
                 {:en
                  "The ISO 3166-1 alpha-2 country code where the cloud infrastructure physically resides. See https://www.iso.org/obp/ui/#search."},
     :categories {:en ["location"]},
     :value      "IT",
     :nested     true}]
   (-> raw-metadata-str u/clojurify-raw-metadata-str (model/parse raw-attributes-json) :service-offer first :decorated-values))

(expect
  ["aa", "uxx2"]
  (->> (-> raw-metadata-str u/clojurify-raw-metadata-str (model/parse raw-attributes-json) :service-offer)
       (sort-by :id)
       (map :id)))
;
;;; left commented on purpose : requires a connection to API
;;;
;;(expect
;;  {:type "int"
;;   :name {:en "ramCapacityGiB"}
;;   :categories  {:en ["service" "location"]}
;;   :description {:en "Maximum amount of RAM that is available on the supplier's infrastructure in Gibibytes (GiB)."}
;;   :value "6000"
;;   :nested false}
;;  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :service-offer first :decorated-values first))
;
(expect
  {:http://schema.example.org/country
   {:type "string"
    :en   {:description "The ISO 3166-1 alpha-2 country code where the cloud infrastructure physically resides. See https://www.iso.org/obp/ui/#search."
           :name        "country"
           :categories  ["location"]}}
   :http://schema.example.org/ramCapacityGiB
   {:type "int"
    :en   {:name        "ramCapacityGiB"
           :description "Maximum amount of RAM that is available on the supplier's infrastructure in Gibibytes (GiB)."
           :categories  ["service" "location"]}}
   :http://schema.example.org/composite
    {:type ["http://schema.example.org/ramCapacityGiB" "http://schema.example.org/country"]
     :en   {:name        "composite"
            :description "A composite type"
            :categories  ["service" "location"]}}
   :http://schema.example.org/maxStorageKB
   {:type "number"
    :en {:name "maxStorageKB"
         :description "The max storage expressed in KB."
         :categories ["capacity"]}}}
  (model/attributes-to-map raw-attributes-json))

(expect
  [{:uri         "http://schema.example.org/ramCapacityGiB"
    :type        "int"
    :name        {:en "ramCapacityGiB"}
    :description {:en "Maximum amount of RAM that is available on the supplier's infrastructure in Gibibytes (GiB)."}
    :categories  {:en ["service" "location"]}
    :value       1024
    :nested      false}]
  (model/decorate (model/attributes-to-map raw-attributes-json) [:http://schema.example.org/ramCapacityGiB 1024]))

(expect
  [{:uri         "http://schema.example.org/composite"
    :type        ["http://schema.example.org/ramCapacityGiB" "http://schema.example.org/country"],
    :name        {:en "composite"},
    :description {:en "A composite type"},
    :categories  {:en ["service" "location"]}
    :nested      true}

   {:uri         "http://schema.example.org/ramCapacityGiB"
    :type        "int", :name {:en "ramCapacityGiB"},
    :description {:en "Maximum amount of RAM that is available on the supplier's infrastructure in Gibibytes (GiB)."},
    :categories  {:en ["service" "location"]},
    :value       1024,
    :nested      true}

   {:uri         "http://schema.example.org/country"
    :type        "string",
    :name        {:en "country"},
    :description {:en "The ISO 3166-1 alpha-2 country code where the cloud infrastructure physically resides. See https://www.iso.org/obp/ui/#search."},
    :categories  {:en ["location"]},
    :value       "NZ",
    :nested      true}]

  (model/decorate (model/attributes-to-map raw-attributes-json) [:http://schema.example.org/composite
                                                                 {:http://schema.example.org/ramCapacityGiB 1024
                                                                  :http://schema.example.org/country        "NZ"}]))


(expect
  [{:name        {:en "unknown-uri"}
    :value       1000
    :nested      false}]
  (model/decorate (model/attributes-to-map raw-attributes-json) [:unknown-uri 1000]))
