(ns slipstream.ui.models.attributes-test
  (:require
    [expectations :refer :all]
    [slipstream.ui.util.clojure :as uc]
    [slipstream.ui.util.core :as u]
    [slipstream.ui.models.attributes :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_attributes.json"))

(expect
  [{:uri  "http://schema.example.org/price"
    :vals {:minor-version 1
           :major-version 2
           :authority     "http://helix-nebula.eu"
           :type          "number"
           :name          "price"
           :description   "The price per unit of the VM or VM component."
           :patch-version 0
           :normative     true}}
   {
    :uri  "http://schema.example.org/country"
    :vals {:minor-version 1
           :major-version 2
           :authority     "http://helix-nebula.eu"
           :type          "string"
           :name          "country"
           :description   "The ISO 3166-1 alpha-2 country code."
           :categories    ["location"]
           :patch-version 0
           :normative     true}}
   {
    :uri  "http://schema.example.org/priceCurrency"
    :vals {:minor-version 1
           :major-version 2
           :authority     "http://helix-nebula.eu"
           :type          "string"
           :name          "priceCurrency"
           :description   "The currency for the price."
           :patch-version 0
           :normative     true}}
   {
    :uri  "http://schema.example.org/priceModel"
    :vals {:minor-version 1
           :major-version 2
           :authority     "http://helix-nebula.eu"
           :type          ["http://schema.example.org/price" "http://schema.example.org/priceCurrency"]
           :name        "ramCapacityGiB"
           :description "Note that these attributes can be used directly at the \"offer\" level."
           :patch-version 0
           :normative     true}}
   {
    :uri  "http://schema.example.org/ramCapacityGiB"
    :vals {:minor-version 1
           :major-version 2
           :authority     "http://helix-nebula.eu"
           :type          "int"
           :name          "ramCapacityGiB"
           :description   "Maximum amount of RAM that is available on the supplier's infrastructure in Gibibytes (GiB)."
           :categories    ["service" "location"]
           :patch-version 0
           :normative     true}}
   {
    :uri  "http://schema.example.org/cpuPriceModel"
    :vals {:minor-version 1
           :major-version 2
           :authority     "http://helix-nebula.eu"
           :type          "http://schema.example.org/priceModel"
           :name          "cpuPriceModel"
           :description   "Alias to Composite type."
           :patch-version 0
           :normative     true}}]

  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :attributes))



