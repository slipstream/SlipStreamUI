(ns slipstream.ui.views.welcome
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.utils :as u :refer [defn-memo]]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.models.welcome :as mw]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.configuration :as configuration-model]
            [slipstream.ui.models.service-catalog :as service-catalog-model]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.header :as header]
                        [slipstream.ui.views.project :as project]
            [slipstream.ui.views.service-catalog :as service-catalog]
            [slipstream.ui.views.common :as common]))

(def published-modules-sel [:#published-modules])
(def published-modules-header-sel [:#published-modules-header])

(def welcome-template-html (common/get-template "welcome.html"))

(html/defsnippet header-titles-snip welcome-template-html header/titles-sel
  []
  identity)

(html/defsnippet header-snip header/header-template-html header/header-sel
  [metadata]
  header/titles-sel (html/substitute (header-titles-snip))
  header/header-top-bar-sel (html/substitute
                                    (header/header-top-bar-snip
                                      (user-model/attrs metadata))))

(html/defsnippet published-children-snip welcome-template-html [published-modules-sel :> [:div html/first-of-type]]
  [children]
  (html/clone-for
    [child children
     :let [attrs (module-model/attrs child)
           slash-resourceuri (:resourceuri attrs)
           name (:name attrs)
           logolink (:logolink attrs)]]
    [:div :> [:div (html/nth-of-type 1)] :a] (if logolink
                                               (html/set-attr :href slash-resourceuri)
                                               nil)
    [:img] (if logolink
     (html/set-attr :src logolink)
     nil)
    [:button] (html/set-attr :onclick (str "location = '" slash-resourceuri "?showdialog=run-with-options-dialog'; return false;"))
    [:div :> [:div (html/nth-of-type 2)] :a] (html/do->
                                               (html/set-attr :href slash-resourceuri)
                                               (html/content name))
    [:div :> [:div (html/nth-of-type 3)] :> :span] (html/content (module-model/owner child))
    [:div :> [:div (html/nth-of-type 4)] :> :span] (html/content (common/ellipse-right (module-model/module-description child) 70))
    [:div :> [:div (html/nth-of-type 5)] :> :span] (html/content (module-model/module-version child))))

(html/defsnippet modules-snip welcome-template-html common/content-sel
  [root-projects published-modules]
  published-modules-sel
  (html/content (published-children-snip published-modules))

  #{published-modules-sel published-modules-header-sel}
  (if (empty? published-modules)
    nil
    identity)

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
  (if (configuration-model/service-catalog-enabled? modules)
    identity
    nil))

(def js-scripts-default
  ["/js/welcome.js" "/js/service_catalog.js"])

(defn js-scripts
  [type]
  (if (= "chooser" type)
    (concat js-scripts-default (module/js-scripts-chooser))
    js-scripts-default))

; (defn page [root-projects type]
;   (base/base
;     {:js-scripts (js-scripts type)
;      :title (common/title "Welcome")
;      :header (module-base/header root-projects type header-snip)
;      :content (content-snip root-projects)
;      :footer (module-base/footer type)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(def template-filename (common/get-template "app_store.html"))

(def app-thumbnail-group-sel [:.ss-app-thumbnail-group])
(def app-thumbnail-sel [:.ss-app-thumbnail])
(def app-image-container-sel [:.ss-app-image-container])
(def app-image-preloader-sel (concat app-image-container-sel [:.ss-app-image-preloader]))
(def app-name-deploy-btn-sel [:.ss-app-deploy-btn])
(def app-name-container-sel [:.ss-app-name-container])
(def app-name-sel [:.ss-app-name])
(def app-version-sel [:.ss-app-version-number])
(def app-description-sel [:dd.ss-app-description])
(def app-publisher-sel [:dd.ss-app-publisher])
(def app-publication-date-sel [:dd.ss-app-publication-date])

(def app-updated-cls "ss-app-updated")
(def app-new-cls "ss-app-new")

(defn app-thumbnail-nodes
  [app-metadata-list]
  (html/clone-for [{:keys [name
                           uri
                           updated?
                           new?
                           version
                           description
                           publisher
                           publication-date
                           image]
                    :as app} app-metadata-list]
    app-image-preloader-sel  (u/set-src image)
    app-image-container-sel  (u/when-add-class updated? app-updated-cls)
    app-image-container-sel  (u/when-add-class new? app-new-cls)
    app-name-deploy-btn-sel  (u/set-onclick "location = '" uri "?showdialog=run-with-options-dialog'; return false;")
    app-name-container-sel   (u/set-href uri)
    app-name-sel             (html/content (str name))
    app-version-sel          (html/content version)
    app-description-sel      (html/content description)
    app-publisher-sel        (html/content publisher)
    app-publication-date-sel (html/content publication-date)))

(html/defsnippet app-thumbnails-snip template-filename app-thumbnail-group-sel
  [app-thumbnails]
  app-thumbnail-sel (app-thumbnail-nodes app-thumbnails))



(defn page [metadata type]
  (base/generate
    {:template-filename template-filename
     :page-title "Welcome"
     :header {:icon icons/welcome
              :title "Welcome to SlipStream"
              :subtitle "The welcome page provides you with all currently published
                         modules and root modules, including yours and the ones
                         shared with you."}
     :secondary-menu-actions [action/new-project]
     :content [{:title "App Store"
                :content (app-thumbnails-snip (mw/published-apps metadata))
                :selected? true
                :type :default}
               {:title "Shared Projects"
                :content (t/shared-projects-table (mw/shared-projects metadata))}]
     :type type
     :metadata metadata
     }))
