(ns slipstream.ui.models.run
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.module :as module]))

(defn user
  [run]
  (module/user run))

(defn attrs
  [run]
  (module/attrs run))

(defn user-attrs
  [run]
  (module/user-attrs run))

(defn module
  [run]
  (first
    (html/select run [:module])))

(defn module-name
  [run]
  (common/elem-name (module run)))

(defn runtime-parameters
  [run]
  (html/select run [:runtimeParameter]))

(defn runtime-parameter [run param-name]
  (first (filter #(= param-name (:key (:attrs %))) (runtime-parameters run))))

(defn runtime-parameter-value [run param-name]
  (first
    (:content
      (first
        (filter
          #(= param-name (:key (:attrs %)))
          (runtime-parameters run))))))

(defn parameters
  [run]
  (common/parameters run))

(defn group-by-cloud
  [runs]
  (common/group-by-key
      :cloudservicename
      (common/children runs)))

(def run-type-mapping
  {"Orchestration" "Deployment"
   "Machine" "Build"
	 "Run" "Simple Run"})

(defn convert-type
  [type]
  (run-type-mapping type))

(defn get-type
  [run]
  (or 
    (convert-type (:type (common/attrs run))) 
    "Unknown"))

