(ns slipstream.ui.views.service-catalog
  (:require [slipstream.ui.util.localization :as localization]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.service-catalog :as service-catalog-model]
            [slipstream.ui.models.configuration :as configuration-model]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.project :as project]
            [slipstream.ui.util.core :as u]))

(def service-catalog-template-html (u/template-path-for "service_catalog.html"))

(def service-catalog-id "service-catalog")
(def service-catalog-sel [(keyword (str "#" service-catalog-id))])
(def service-catalog-header-id "service-catalog-header")
(def service-catalog-header-sel [(keyword (str "#" service-catalog-header-id))])

(html/defsnippet header-titles-snip service-catalog-template-html header/titles-sel
  []
  identity)

(html/defsnippet header-snip header/header-template-html header/header-sel
  [metadata]
  header/titles-sel (html/substitute (header-titles-snip))
  header/header-top-bar-sel (html/substitute
                                    (header/header-top-bar-snip
                                      (user-model/attrs metadata))))

(defn define-tabs-for-parameters
  [parameters-grouped-by-category]
  (common/tab-headers parameters-grouped-by-category "service-catalog"))

(defn define-tab-sections-for-parameters-view
  [parameters-grouped-by-category]
  (common/tab-sections-with-add-button-option
    parameters-grouped-by-category
    "service-catalog"
    common/parameters-view-with-name-and-category-snip
    false))

(defn define-tab-sections-for-parameters-edit
  [parameters-grouped-by-category]
  (common/tab-sections-with-add-button-option
    parameters-grouped-by-category
    "service-catalog"
    common/parameters-show-all-edit-only-value-snip
    true))

(html/defsnippet service-catalog-view-tabs-snip common/parameters-view-template-html common/parameters-sel
  [service-catalogs]
  html/this-node (html/set-attr :id service-catalog-id)
  [:ul :> :li] (define-tabs-for-parameters service-catalogs)
  [:#fragment-parameters-something]
  (define-tab-sections-for-parameters-view service-catalogs))

(html/defsnippet service-catalog-edit-tabs-snip common/parameters-edit-template-html common/parameters-sel
  [service-catalogs]
  html/this-node (html/set-attr :id service-catalog-id)
  [:ul :> :li] (define-tabs-for-parameters service-catalogs)
  [:#fragment-parameters-something]
    (define-tab-sections-for-parameters-edit service-catalogs))

(defn flatten-catalog
  [service-catalogs]
  (into {}
    (map
      #(hash-map
         (:cloud
           (module-model/attrs %))
         (common-model/sort-by-category
           (common-model/sort-by-name
             (seq
               (common-model/parameters %)))))
      service-catalogs)))

(html/defsnippet service-catalog-snip service-catalog-template-html service-catalog-sel
  [service-catalogs edit?]
  service-catalog-sel
  (html/substitute
    (if edit?
      (service-catalog-edit-tabs-snip
        (flatten-catalog service-catalogs))
      (service-catalog-view-tabs-snip
        (flatten-catalog service-catalogs)))))

(html/defsnippet content-snip service-catalog-template-html common/content-sel
  [modules]
  service-catalog-sel
  (html/substitute
    (service-catalog-snip
      (service-catalog-model/service-catalog-items modules)
      (user-model/super? modules)))
  )
;  [#{service-catalog-sel service-catalog-header-sel}]
;  (if (configuration-model/metering-enabled? modules)
;    identity
;    nil))


(def js-scripts-default
  ["/js/service_catalog.js"])

(defn js-scripts
  [type]
  (if (= "chooser" type)
    (concat js-scripts-default (module/js-scripts-chooser))
    js-scripts-default))

(defn page-legacy [root-projects type]
  (base/base
    {:js-scripts (js-scripts type)
     :title (common/title "Service Catalog")
     :header (module-base/header root-projects type header-snip)
     :content (content-snip root-projects)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn page
  [metadata]
  (base/generate
    {:metadata metadata
     :placeholder-page? true
     :header {:icon icons/config
              :title "Service Catalog"
              :subtitle "Provides detailed capability information for each available clouds"}
     :content nil}))
