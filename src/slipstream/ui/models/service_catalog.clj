(ns slipstream.ui.models.service-catalog
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.user :as user-model]))

(defn service-catalog-items [catalogs]
  "Extract service-catalog entries"
  (sort-by
    #(:cloud (common-model/attrs %))
    (html/select catalogs [:serviceCatalog])))
