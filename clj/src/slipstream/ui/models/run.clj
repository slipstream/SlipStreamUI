(ns slipstream.ui.models.run
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.runtime-parameters :as runtime-parameters]
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
  {"Orchestration"  "Deployment"
   "Machine"        "Build"
	 "Run"             "Simple Run"})

(defn convert-type
  [type]
  (run-type-mapping type))

(defn get-type
  [run]
  (or
    (convert-type (:type (common/attrs run)))
    "Unknown"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def run-type-mapping
  {"Orchestration"  "Deployment"
   "Machine"        "Build"
   "Run"             "Simple Run"})

(defn- summary
  [metadata]
  (let [attrs (:attrs metadata)]
    {:category    (-> attrs :category)
     :creation    (-> attrs :creation)
     :start-time  (-> attrs :starttime)
     :end-time    (-> attrs :endtime)
     :deleted?    (-> attrs :deleted uc/parse-boolean)
     :user        (-> attrs :user)
     :state       (-> attrs :state)
     :status      (-> attrs :status)
     :uuid        (-> attrs :uuid)
     :type        (-> attrs :type run-type-mapping)
     :uri         (-> attrs :resourceuri)
     :owner       (-> metadata (html/select [:authz]) first :attrs :owner)
     :module-uri  (-> attrs :moduleresourceuri)}))

(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  (let [runtime-parameters (runtime-parameters/parse metadata)]
    (-> {}
        (assoc :summary (summary metadata))
        ; (assoc :parameters (parameters/parse metadata))
        (assoc :runtime-parameters runtime-parameters)
        (assoc-in [:summary :tags] (runtime-parameters/value-for runtime-parameters "ss:tags"))
        )))


; :category     ='Deployment'
; :deleted      ='false'
; :creation     ='2013-06-12 15:39:55.575 CEST'
; :starttime    ='2013-06-12 15:39:55.575 CEST'
; :resourceuri  ='run/06f207d4-d25b-4597-a23a-f79a07b2f791'
; :uuid         ='06f207d4-d25b-4597-a23a-f79a07b2f791'
; :moduleresourceuri     ='module/Public/Tutorials/HelloWorld/client_server/11'
; :user         ='meb'
; :state        ='Terminal'
; :status       ='Done'



; :type         ='Orchestration'
; :cloudservicename     ='interoute'
; :nodenames    ='orchestrator-interoute, testclient1.1, apache1.1, '
; :groups       ='interoute:testclient1, interoute:apache1, '>