(ns slipstream.ui.views.representation
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.interop :as ui]
            [slipstream.ui.util.mode :as mode]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.login :as login]
            [slipstream.ui.views.byebye :as byebye]
            [slipstream.ui.views.service-catalog :as service-catalog]
            [slipstream.ui.views.module-list :as module-list]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.versions :as versions]
            [slipstream.ui.views.dashboard :as dashboard]
            [slipstream.ui.views.vms :as vms]
            [slipstream.ui.views.runs :as runs]
            [slipstream.ui.views.user :as user]
            [slipstream.ui.views.users :as users]
            [slipstream.ui.views.run :as run]
            [slipstream.ui.views.reports :as reports]
            [slipstream.ui.views.configuration :as configuration]
            [slipstream.ui.views.action :as action]
            [slipstream.ui.views.events :as events]
            [slipstream.ui.views.usages :as usages]
            [slipstream.ui.views.usage :as usage]
            [slipstream.ui.views.error :as error]
            [slipstream.ui.models.version :as version-model])
  (:gen-class
    :name slipstream.ui.views.Representation
    :methods [#^{:static true
                 :doc "Takes: metadata pagename type"}
                [toHtml [String String java.util.Map] String]
              #^{:static true
                 :doc "Takes: user message code"}
                [toHtmlError [String String String java.util.Map] String]
              #^{:static true
                 :doc "Set alternative namespace for HTML template. Must be slash separated."}
                [setHtmlTemplateNamespace [String] String]
              #^{:static true
                 :doc "Takes: version"}
                [setReleaseVersion [String] void]]))

(def pages
  {"login"            login/page
   "logout"           byebye/page
   "service_catalog"  service-catalog/page
   "appstore"         module-list/appstore-page
   "projects"         module-list/projects-page
   "chooser"          module-list/chooser-page
   "module"           module/page
   "versions"         versions/page
   "dashboard"        dashboard/page
   "vms"              vms/page
   "runs"             runs/page ;; NOTE: Not to be mixed with '/run'
   "user"             user/page
   "users"            users/page
   "run"              run/page
   "reports"          reports/page
   "configuration"    configuration/page
   "action"           action/page
   "events"           events/page
   "usages"           usages/page
   "usage"            usage/page})

(defn page-types
  [pagename]
  (case pagename
    "reports"          "reports-frame"
    "service_catalog"  (if (current-user/super?) "edit" "view")
    "configuration"    (if (current-user/super?) "edit" "view")
    nil))

(defn get-page-type
  [pagename options]
  (or (page-types pagename)
      (-> options :request :query-parameters :chooser-type)
      (-> options :type)))

(defn- render-html
  "Enlive templates (used in page-fn's) return a seq of string which must be
  joined to a valid HTML doc."
  [page-fn & args]
  (->> args
       (apply page-fn)
       (apply str)))

(defn- render-page
  [pagename metadata]
  (if-let [page-fn (get pages pagename)]
    (render-html page-fn metadata)
    (throw (IllegalStateException.
             (format "No page-fn found for pagename '%s'."
                     pagename)))))

(defmacro guard-exceptions
  [& body]
  `(if (mode/headless?)
    ; In headless mode let the stacktrace be printed on the browser:
    (do
      ~@body)
    (try
      ; In prod render a proper error page reporting the expection:
      ~@body
      (catch Throwable t#
        ; TODO: Properly log exception
        (println)
        (println ">>>> Uncaught exception during the UI generation (clj side):")
        (println)
        (.printStackTrace t#)
        (render-html error/page-uncaught-exception t#)))))

(defn -toHtml
  "Generate an HTML page from the metadata xml string"
  [raw-metadata-str pagename args-map]
  (let [options (ui/->clj args-map)
        page-type-sent-by-server (:type options "UNDEFINED")
        lang (-> options :request :query-parameters :lang)
        metadata (u/clojurify-raw-metadata-str raw-metadata-str)]
    (mode/when-dev
      ; NOTE: In dev mode, save a file into SlipStreamServer/war/raw-metadata-str.xml
      ;       with the XML metadata received from the server.
      ;       These XMLs files can then be used as mockups for UI tests. They can be
      ;       saved with this command:
      ;       $ cp war/raw-metadata-str.xml ../SlipStreamUI/clj/test/slipstream/ui/mockup_data/metadata_{$NAME_OF_THE_METADATA}.xml
      (spit (format "raw-metadata-str-page_%s-type_%s.xml" pagename page-type-sent-by-server) raw-metadata-str))
    (localization/with-lang lang
      (current-user/with-current-user
        (page-type/with-page-type (get-page-type pagename options)
          (guard-exceptions
            (render-page pagename (some-> metadata (with-meta options)))))))))

(defn- lang-from-request
  [request]
  (get-in request ["query-parameters" "lang"]))

(defn -toHtmlError
  "Generate an HTML error page"
  ; NOTE: :strs directive used instead of :keys, because keys in args-map map are strings.
  [raw-metadata-str message code {:strs [type request] :as args-map}]
  (let [lang (lang-from-request request)
        metadata (u/clojurify-raw-metadata-str raw-metadata-str)]
    (localization/with-lang lang
      (current-user/with-current-user
        (render-html error/page metadata message code)))))

(defn -setReleaseVersion
  "Set the application version"
  [version]
  (version-model/set-release-version version))
