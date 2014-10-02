(ns slipstream.ui.models.configuration
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.user :as user]))

(def root-uri "configuration/")
(def root-uri-length (count root-uri))
(def metering-enable-param "slipstream.metering.enable")
(def quota-enable-param "slipstream.quota.enable")
(def service-catalog-enable-param "slipstream.service.catalog.enable")

(defn attrs
  [config]
  (common/attrs config))

(defn config-params
  [metadata]
  (html/select metadata [html/root :> :serviceConfiguration :parameter]))

(defn parameter
  [metadata name]
  (filter #(= name (:name (:attrs %))) (config-params metadata)))

(defn metering-enabled?
  [metadata]
  (common/true-value? (common/value (parameter metadata metering-enable-param))))

(defn quota-enabled?
  [metadata]
  (common/true-value? (common/value (parameter metadata quota-enable-param))))

(defn service-catalog-enabled?
  [metadata]
  (common/true-value? (common/value (parameter metadata service-catalog-enable-param))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parse
  [metadata]
  nil)