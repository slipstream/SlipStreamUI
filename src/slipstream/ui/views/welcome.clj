(ns slipstream.ui.views.welcome
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.service-catalog :as service-catalog-model]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.project :as project]
            [slipstream.ui.views.service-catalog :as service-catalog]
            [slipstream.ui.views.common :as common]))

(def welcome-template-html "slipstream/ui/views/welcome.html")

(html/defsnippet header-titles-snip welcome-template-html header/titles-sel
  []
  identity)

(html/defsnippet header-snip header/header-template-html header/header-sel
  [metadata]
  header/titles-sel (html/substitute (header-titles-snip))
  header/header-top-bar-sel (html/substitute
                                    (header/header-top-bar-snip
                                      (user-model/attrs metadata))))

(html/defsnippet modules-snip welcome-template-html common/content-sel
  [root-projects published-modules]
  [:#published-modules]
  (html/content (project/children-snip published-modules))

  #{[:#published-modules] [:#published-modules-header]}
  (if (empty? published-modules)
    nil
    identity)

  [:#published-modules :> :table :> :thead :> :tr :> [:th (html/nth-of-type 3)]]
  (html/content "Publisher")

  ; Root projects cannot be empty
  [:#root-projects] 
  (html/content (project/children-snip root-projects)))

(html/defsnippet content-snip welcome-template-html common/content-sel
  [modules]
  common/content-sel
  (html/substitute
    (modules-snip
      (modules-model/children-with-filter modules #(not= "true" (:published (module-model/attrs %))))
      (modules-model/children-with-filter modules #(= "true" (:published (module-model/attrs %))))))

  service-catalog/service-catalog-sel 
  (html/substitute
    (service-catalog/service-catalog-snip
      (service-catalog-model/service-catalog-items modules)
      false))
  
  [#{service-catalog/service-catalog-sel service-catalog/service-catalog-header-sel}] 
  (if (empty? (service-catalog-model/service-catalog-items modules))
    nil
    identity))

(def js-scripts-default
  ["/js/welcome.js" "/js/service_catalog.js"])

(defn js-scripts
  [type]
  (if (= "chooser" type)
    (concat js-scripts-default (module/js-scripts-chooser))
    js-scripts-default))

(defn page [root-projects type]
  (base/base 
    {:js-scripts (js-scripts type)
     :title (common/title "Welcome")
     :header (module-base/header root-projects type header-snip)
     :content (content-snip root-projects)
     :footer (module-base/footer type)}))
