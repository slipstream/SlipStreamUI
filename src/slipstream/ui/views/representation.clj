(ns slipstream.ui.views.representation
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.version :as version-model]
            [slipstream.ui.views.welcome :as welcome]
            [slipstream.ui.views.knockknock :as knockknock]
            [slipstream.ui.views.byebye :as byebye]
            [slipstream.ui.views.action :as action]
            [slipstream.ui.views.error :as error]
            [slipstream.ui.views.user :as user]
            [slipstream.ui.views.users :as users]
            [slipstream.ui.views.run :as run]
            [slipstream.ui.views.runs :as runs]
            [slipstream.ui.views.vms :as vms]
            [slipstream.ui.views.reports :as reports]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.configuration :as configuration]
            [slipstream.ui.views.dashboard :as dashboard]
            [slipstream.ui.views.versions :as versions]
            [slipstream.ui.views.service-catalog :as service-catalog]
            [slipstream.ui.views.documentation :as documentation])
  (:gen-class
    :name slipstream.ui.views.Representation
    :methods [#^{:static true
                 :doc "Takes: metadata pagename type"}
                [toHtml [String String String] String]
              #^{:static true
                 :doc "Takes: user message code"}
                [toHtmlError [String String String] String]
              #^{:static true
                 :doc "Takes: version"}
                [setReleaseVersion [String] void]]))

(defn- xml-string-to-map [metadata]
  (first (html/html-snippet metadata)))

(defn render [t]
  (apply str t))

(defmulti gen-page
  (fn [metadata pagename type]
    pagename))

(defmethod gen-page "welcome"
  [metadata pagename type]
    (render (welcome/page (xml-string-to-map metadata) type)))

(defmethod gen-page "configuration"
  [metadata pagename type]
    (render (configuration/page (xml-string-to-map metadata))))

(defmethod gen-page "service_catalog"
  [metadata pagename type]
    (render (service-catalog/page (xml-string-to-map metadata) type)))

(defmethod gen-page "dashboard"
  [metadata pagename type]
    (render (dashboard/page (xml-string-to-map metadata))))

(defmethod gen-page "login"
  [metadata pagename type]
    (render (knockknock/page (xml-string-to-map metadata) type)))

(defmethod gen-page "logout"
  [metadata pagename type]
    (render (byebye/page (xml-string-to-map metadata))))

(defmethod gen-page "action"
  [message pagename type]
    (render (action/page (xml-string-to-map message))))

(defmethod gen-page "module"
  [metadata pagename type]
    (render (module/page (xml-string-to-map metadata) type)))

(defmethod gen-page "versions"
  [metadata pagename type]
    (render (versions/page (xml-string-to-map metadata) type)))

(defmethod gen-page "user"
  [metadata pagename type]
    (render (user/page (xml-string-to-map metadata) type)))

(defmethod gen-page "users"
  [metadata pagename type]
    (render (users/page (xml-string-to-map metadata))))

(defmethod gen-page "run"
  [metadata pagename type]
    (render (run/page (xml-string-to-map metadata))))

(defmethod gen-page "runs"
  [metadata pagename type]
    (render (runs/page (xml-string-to-map metadata))))

(defmethod gen-page "vms"
  [metadata pagename type]
    (render (vms/page (xml-string-to-map metadata))))

(defmethod gen-page "reports"
  [metadata pagename type]
    (render (reports/page (xml-string-to-map metadata))))

(defmethod gen-page "documentation"
  [user pagename type]
    (render (documentation/page (xml-string-to-map user))))

(defn -toHtml
  "Generate an HTML page from the metadata xml string"
  [metadata pagename type]
  (gen-page metadata pagename type))

(defn gen-error-page
  [user message code]
    (render (error/page message code (xml-string-to-map user))))

(defn -toHtmlError
  "Generate an HTML error page"
  [user message code]
  (gen-error-page user message code))

(defn -setReleaseVersion
  "Set the application version"
  [version]
  (version-model/set-release-version! version))
