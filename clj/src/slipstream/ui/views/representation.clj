(ns slipstream.ui.views.representation
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
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
   "reports"          reports/page
   "configuration"    configuration/page
   "action"           action/page})

(def page-types
  {"reports"          :chooser
   "service_catalog"  :edit
   "configuration"    :edit})

(defn- render
  [pagename metadata]
  (if-let [render-fn (get pages pagename)]
    (render-fn metadata)
    (throw (IllegalStateException.
             (format "No render-fn found for pagename '%s'."
                     pagename)))))

(defmacro guard-exceptions
  [& body]
  `(if base/*dev?*
    ;; In dev mode let the stacktrace be printed on the browser:
    ~@body
    (try
      ;; In prod render a proper error page reporting the expection:
      ~@body
      (catch Throwable t#
        (error/page-uncaught-exception t#)))))

(defn -toHtml
  "Generate an HTML page from the metadata xml string"
  [raw-metadata-str pagename type]
  (let [metadata (u/clojurify-raw-metadata-str raw-metadata-str)]
    (page-type/with-page-type (get page-types pagename type)
      (localization/with-lang-from-metadata
        (current-user/with-user-from-metadata
          (guard-exceptions
            (render pagename metadata)))))))

(defn -toHtmlError
  "Generate an HTML error page"
  [raw-metadata-str message code]
  (let [metadata (u/clojurify-raw-metadata-str raw-metadata-str)]
    (localization/with-lang-from-metadata
      (current-user/with-user-from-metadata
        (error/page metadata message code)))))

(defn -setReleaseVersion
  "Set the application version"
  [version]
  (version-model/set-release-version version))
