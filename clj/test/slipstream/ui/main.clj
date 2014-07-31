(ns slipstream.ui.main
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
                        [slipstream.ui.views.error :as error]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.byebye :as byebye]
            [slipstream.ui.views.knockknock :as knockknock] ;; TODO: To remove
            [slipstream.ui.views.singin :as singin]
            [slipstream.ui.views.welcome :as welcome]
            [slipstream.ui.views.action :as action]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.versions :as versions]
            [slipstream.ui.views.users :as users]
            [slipstream.ui.views.user :as user]
            [slipstream.ui.views.run :as run]
            [slipstream.ui.views.runs :as runs]
            [slipstream.ui.views.vms :as vms]
            [slipstream.ui.views.reports :as reports]
            [slipstream.ui.views.service-catalog :as service-catalog]
            [slipstream.ui.views.dashboard :as dashboard]
            [slipstream.ui.views.configuration :as configuration]
            [slipstream.ui.views.documentation :as documentation]
            [slipstream.ui.views.representation :as representation]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.utils :as utils]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.dashboard :as dashboard-model]
            [slipstream.ui.data.message :as message]
            [slipstream.ui.data.configuration :as configuration-data]
            [slipstream.ui.data.run :as run-data]
            [slipstream.ui.data.reports :as reports-data]
            [slipstream.ui.data.users :as users-data]
            [slipstream.ui.data.user :as user-data]
            [slipstream.ui.data.projects :as projects]
            [slipstream.ui.data.project :as project]
            [slipstream.ui.data.project-new :as project-new]
            [slipstream.ui.data.project-root :as project-root]
            [slipstream.ui.data.project-root-new :as project-root-new]
            [slipstream.ui.data.welcome :as welcome-data]
            [slipstream.ui.data.image :as image]
            [slipstream.ui.data.image-new :as image-new]
            [slipstream.ui.data.versions :as versions-data]
            [slipstream.ui.data.dashboard :as dashboard-data]
            [slipstream.ui.data.service-catalog :as service-catalog-data]
            [slipstream.ui.data.vms :as vms-data]
            [slipstream.ui.data.runs :as runs-data]
            [slipstream.ui.data.action :as action-data]
            [slipstream.ui.data.deployment :as deployment])
  (:use [net.cgrand.moustache :only [app]]))

;; =============================================================================
;; Pages
;; =============================================================================

(defn action-page []
  (action/page action-data/xml-action))

(defn module-page [module type]
  (module/page module type))

(defn configuration-page []
  (configuration/page configuration-data/xml-configuration))

(defn service-catalog-page []
  (service-catalog/page service-catalog-data/xml-service-catalog "view"))

(defn documentation-page []
  (documentation/page user-data/xml-user))

(defn reports-page []
  (reports/page reports-data/xml-reports))

(defn singin-page []
  (singin/page nil nil))

(defn singin-chooser-page []
  (singin/page projects/xml-projects "chooser"))

(defn knockknock-page []
  (knockknock/page projects/xml-projects nil))

(defn knockknock-chooser-page []
  (knockknock/page projects/xml-projects "chooser"))

(defn run-page []
  (run/page run-data/xml-run))

(defn runs-page []
  (runs/page runs-data/xml-runs))

(defn vms-page []
  (vms/page vms-data/xml-vms))

(defn byebye-page []
  (byebye/page projects/xml-projects))

(defn welcome-page []
  (welcome/page welcome-data/xml-welcome "view"))

(defn welcome-page-chooser []
  (welcome/page welcome-data/xml-welcome "chooser"))

(defn dashboard-page []
  (dashboard/page dashboard-data/xml-dashboard))

(defn users-page []
  (users/page users-data/xml-users))

(defn user-view-page []
  (user/page user-data/xml-user "view"))

(defn user-edit-page []
  (user/page user-data/xml-user "edit"))

(defn error-page [message code]
  (error/page message code (user-model/user projects/xml-projects)))

(defn module-view [module]
  (module-page module "view"))

(defn module-edit [module]
  (module-page module "edit"))

(defn module-new [module]
  (module-page module "new"))

(defn module-chooser [module]
  (module-page module "chooser"))

(defn module-projects-view []
  (module-view projects/xml-projects))

(defn module-projects-edit []
  (module-edit projects/xml-projects))

(defn module-project-view []
  (module-view project/xml-project))

(defn module-project-edit []
  (module-edit project/xml-project))

(defn module-project-new []
  (module-new project-new/xml-project))

(defn module-project-root-view []
  (module-view project-root/xml-project))

(defn module-project-root-edit []
  (module-edit project-root/xml-project))

(defn module-project-root-new []
  (module-new project-root-new/xml-project))

(defn module-image-view []
  (module-view image/xml-image))

(defn module-image-edit []
  (module-edit image/xml-image))

(defn module-image-new []
  (module-new image-new/xml-image))

(defn module-image-chooser []
  (module-chooser image/xml-image))

(defn module-deployment-view []
  (module-view deployment/xml-deployment))

(defn module-deployment-edit []
  (module-edit deployment/xml-deployment))

(defn module-deployment-new []
  (module-new deployment/xml-deployment))

(defn module-versions-view []
  (versions/page versions-data/xml-versions "view"))

(defn module-versions-chooser []
  (versions/page versions-data/xml-versions "chooser"))

;; =============================================================================
;; Routes
;; =============================================================================

(defmacro render
  [page]
  `(-> (~page) ring.util.response/response constantly))

(def routes
  (app
    ["logout"] (render byebye-page)
    ["login"] (render singin-page)
    ["login-chooser"] (render singin-chooser-page)
    ["login-legacy"] (render knockknock-page)
    ["login-chooser-legacy"] (render knockknock-chooser-page)
    ["service_catalog"] (render service-catalog-page)
    ["welcome"] (render welcome-page)
    ["welcome-chooser"] (render welcome-page-chooser)
    ["project-view"] (render module-project-view)
    ["project-edit"] (render module-project-edit)
    ["project-new"] (render module-project-new)
    ["project-root-view"] (render module-project-root-view)
    ["project-root-edit"] (render module-project-root-edit)
    ["project-root-new"] (render module-project-root-new)
    ["image-view"] (render module-image-view)
    ["image-edit"] (render module-image-edit)
    ["image-new"] (render module-image-new)
    ["image-chooser"] (render module-image-chooser)
    ["deployment-view"] (render module-deployment-view)
    ["deployment-edit"] (render module-deployment-edit)
    ["deployment-new"] (render module-deployment-new)
    ["versions"] (render module-versions-view)
    ["versions-chooser"] (render module-versions-chooser)
    ["dashboard"] (render dashboard-page)
    ["users"] (render users-page)
    ["user-view"] (render user-view-page)
    ["user-edit"] (render user-edit-page)
    ["run"] (render run-page)
    ["runs"] (render runs-page)
    ["vms"] (render vms-page)
    ["reports"] (render reports-page)
    ["configuration"] (render configuration-page)
    ["documentation"] (render documentation-page)
    ["action"] (render action-page)
    ["error"] (-> (error-page "Oops!! Kaboom!! <a href='http://sixsq.com'>home</a>" 500) ring.util.response/response constantly)
    [&] (-> (error-page nil 404)
          ring.util.response/response constantly)))

;; =============================================================================
;; The App
;; =============================================================================

(defonce run-test-server
  (delay (utils/run-server routes)))
