(ns slipstream.ui.models.configuration
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.parameters :as parameters]))

(def ^:private non-editable-fields
  "The parameter attr 'readOnly' provided by the server is not yet used in a standard
  way, so that we manually flag here the fields that we know as non editable."
  ["slipstream.version"])

(defn parse
  [metadata]
  {:parameters (-> metadata
                   (html/select [:serviceConfiguration])
                   first
                   parameters/parse
                   (parameters/update non-editable-fields :editable? false))})

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

(defn self-registration-enabled?
  [configuration]
  (-> configuration
      :parameters
      (parameters/value-for "slipstream.registration.enable")))

(defn support-email
  [configuration]
  (-> configuration
      :parameters
      (parameters/value-for "slipstream.support.email")
      not-empty))
