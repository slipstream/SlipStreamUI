(ns slipstream.ui.models.configuration
  (:require [slipstream.ui.models.parameters :as parameters]))

(defn parse
  [metadata]
  {:parameters (parameters/parse metadata)})

;; Configuration parameters utils

(defn metering-enabled?
  [configuration]
  (-> configuration
      :parameters
      (parameters/value-for "slipstream.metering.enable")))

(defn quota-enabled?
  [configuration]
  (-> configuration
      :parameters
      (parameters/value-for "slipstream.quota.enable")))

(defn service-catalog-enabled?
  [configuration]
  (-> configuration
      :parameters
      (parameters/value-for "slipstream.service.catalog.enable")))
