(ns slipstream.ui.main
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.error :as error]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.byebye :as byebye]
            [slipstream.ui.views.knockknock :as knockknock]
            [slipstream.ui.views.welcome :as welcome]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.utils :as utils]
            [slipstream.ui.models.version :as version]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [slipstream.ui.data.projects :as projects]
            [slipstream.ui.data.project :as project]
            [slipstream.ui.data.image :as image]
            [slipstream.ui.data.deployment :as deployment])
  (:use [net.cgrand.moustache :only [app]]))

;; =============================================================================
;; Pages
;; =============================================================================

(defn module-page [module edit?]
  (module/page module edit?))

(defn knockknock-page []
  (knockknock/page projects/xml-projects))

(defn byebye-page []
  (byebye/page projects/xml-projects))

(defn welcome-page []
  (welcome/page projects/xml-projects))

(defn error-page [message]
  (error/page (or message "Oops!!") 123 (user/user projects/xml-projects)))

(defn module-view [module]
  (module-page module "view"))

(defn module-edit [module]
  (module-page module "edit"))

(defn module-new [module]
  (module-page module "new"))

(defn module-projects-view []
  (module-view projects/xml-projects))

(defn module-projects-edit []
  (module-edit projects/xml-projects))

(defn module-project-view []
  (module-view project/xml-project))

(defn module-project-edit []
  (module-edit project/xml-project))

(defn module-project-new []
  (module-new project/xml-project))

(defn module-image-view []
  (module-view image/xml-image))

(defn module-image-edit []
  (module-edit image/xml-image))

(defn module-deployment-view []
  (module-view deployment/xml-deployment))

(defn module-deployment-edit []
  (module-edit deployment/xml-deployment))

;; =============================================================================
;; Routes
;; =============================================================================

(def routes
  (app
    ["logout"] (-> (byebye-page) ring.util.response/response constantly)
    ["login"] (-> (knockknock-page) ring.util.response/response constantly)
    ["welcome"] (-> (welcome-page) ring.util.response/response constantly)
    ["project-view"] (-> (module-project-view) ring.util.response/response constantly)
    ["project-edit"] (-> (module-project-edit) ring.util.response/response constantly)
    ["project-new"] (-> (module-project-new) ring.util.response/response constantly)
    ["image-view"] (-> (module-image-view) ring.util.response/response constantly)
    ["image-edit"] (-> (module-image-edit) ring.util.response/response constantly)
    ["deployment-view"] (-> (module-deployment-view) ring.util.response/response constantly)
    ["deployment-edit"] (-> (module-deployment-edit) ring.util.response/response constantly)
    ["error"] (-> (error-page) ring.util.response/response constantly)
    [&] (-> (error-page "Unknown page")
          ring.util.response/response constantly)))

;; =============================================================================
;; The App
;; =============================================================================

(defonce ^:dynamic *server* (utils/run-server routes))
