(ns slipstream.ui.views.representation
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.views.login :as login]
            [slipstream.ui.views.byebye :as byebye]
            [slipstream.ui.views.documentation :as documentation]
            [slipstream.ui.views.service-catalog :as service-catalog]
            [slipstream.ui.views.welcome :as welcome]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.versions :as versions]
            [slipstream.ui.views.dashboard :as dashboard]
            [slipstream.ui.views.vms :as vms]
            [slipstream.ui.views.user :as user]
            [slipstream.ui.views.users :as users]
            [slipstream.ui.views.run :as run]
            [slipstream.ui.views.runs :as runs]
            [slipstream.ui.views.reports :as reports]
            [slipstream.ui.views.configuration :as configuration]
            [slipstream.ui.views.action :as action]
            [slipstream.ui.views.error :as error]
            [slipstream.ui.models.version :as version-model])
  (:gen-class
    :name slipstream.ui.views.Representation
    :methods [#^{:static true
                 :doc "Takes: metadata pagename type"}
                [toHtml [String String String] String]
              #^{:static true
                 :doc "Takes: user message code"}
                [toHtmlError [String String String] String]
              #^{:static true
                 :doc "Set alternative namespace for HTML template. Must be slash separated."}
                [setHtmlTemplateNamespace [String] String]
              #^{:static true
                 :doc "Takes: version"}
                [setReleaseVersion [String] void]]))

(defn- render
  [t]
  (apply str t))

(def pages
  {"login"            login/page
   "logout"           byebye/page
   "documentation"    documentation/page
   "service_catalog"  service-catalog/page
   "welcome"          welcome/page
   "module"           module/page
   "versions"         versions/page
   "dashboard"        dashboard/page
   "vms"              vms/page
   "user"             user/page
   "users"            users/page
   "run"              run/page
   "runs"             runs/page ; TODO: Is this page still needed?
   "reports"          reports/page
   "configuration"    configuration/page
   "action"           action/page})

(def page-types
  {"reports"          :chooser})

(defn -toHtml
  "Generate an HTML page from the metadata xml string"
  [raw-metadata-str pagename type]
  (page-type/is (get page-types pagename type)
    (-> raw-metadata-str
        u/clojurify-raw-metadata-str
        ((get pages pagename))
        render)))

(defn -toHtmlError
  "Generate an HTML error page"
  [raw-metadata-str message code]
  (-> raw-metadata-str
      u/clojurify-raw-metadata-str
      (error/page message code)
      render))

(defn -setReleaseVersion
  "Set the application version"
  [version]
  (version-model/set-release-version version))
