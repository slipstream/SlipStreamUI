(ns slipstream.ui.main
  (:require [slipstream.ui.utils :as utils]
            [slipstream.ui.views.representation :as representation])
  (:use [net.cgrand.moustache :only [app]]))

;; =============================================================================
;; Local test routes
;; =============================================================================

(def ^:private full-metadata-ns
  (partial format "slipstream.ui.models.%s-test"))

(defn- raw-metadata
  [raw-metadata-ns]
  (when raw-metadata-ns
    (-> raw-metadata-ns full-metadata-ns symbol require)
    (if-let [raw-metadata-symbol (-> (full-metadata-ns raw-metadata-ns)
                                     (symbol "raw-metadata")
                                     resolve)]
      (var-get raw-metadata-symbol)
      (throw (IllegalArgumentException.
               (format "metadata: var '%s/raw-metadata not found for raw-metadata-ns '%s'"
                       (full-metadata-ns raw-metadata-ns)
                       raw-metadata-ns))))))

(defmacro render
  [& {:keys [raw-metadata-ns pagename type]}]
  `(-> (representation/-toHtml ~(raw-metadata raw-metadata-ns) ~pagename ~type)
        ring.util.response/response
        constantly))

(defmacro render-error
  [& {:keys [raw-metadata-ns message code]}]
  `(-> (representation/-toHtmlError ~(raw-metadata raw-metadata-ns) ~message ~code)
        ring.util.response/response
        constantly))

(def routes
  (app
    ["login"]                 (render :pagename "login")

    ["login-chooser"]         (render :pagename "login" :type "chooser")

    ["logout"]                (render :pagename "logout")

    ["documentation"]         (render :pagename "documentation")

    ["welcome"]               (render :pagename "welcome"         :raw-metadata-ns "welcome" :type "view")
    ["welcome-chooser"]       (render :pagename "welcome"         :raw-metadata-ns "welcome" :type "chooser")

    ["project-view"]          (render :pagename "module"          :raw-metadata-ns "module.project"     :type "view")
    ["project-edit"]          (render :pagename "module"          :raw-metadata-ns "module.project"     :type "edit")
    ["project-new"]           (render :pagename "module"          :raw-metadata-ns "module.project-new" :type "new")

    ; TODO: Project-root was historically used for the now "welcome" page.
    ;       This note is to flag this pages as stale, to delete (with their corresponding test data) at a later point.
    ["project-root-view"]     (render :pagename "module"          :raw-metadata-ns "module.project-root"      :type "view")
    ["project-root-edit"]     (render :pagename "module"          :raw-metadata-ns "module.project-root"      :type "edit")
    ["project-root-new"]      (render :pagename "module"          :raw-metadata-ns "module.project-root-new"  :type "new")

    ["image-view"]            (render :pagename "module"          :raw-metadata-ns "module.image"     :type "view")
    ["image-chooser"]         (render :pagename "module"          :raw-metadata-ns "module.image"     :type "chooser")
    ["image-edit"]            (render :pagename "module"          :raw-metadata-ns "module.image"     :type "edit")
    ["image-new"]             (render :pagename "module"          :raw-metadata-ns "module.image-new" :type "new")

    ["deployment-view"]       (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "view")
    ["deployment-edit"]       (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "edit")
    ["deployment-new"]        (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "new")

    ["versions"]              (render :pagename "versions"        :raw-metadata-ns "versions" :type "view")
    ["versions-chooser"]      (render :pagename "versions"        :raw-metadata-ns "versions" :type "chooser")

    ["dashboard"]             (render :pagename "dashboard"       :raw-metadata-ns "dashboard")

    ["user-view"]             (render :pagename "user"            :raw-metadata-ns "user" :type "view")
    ["user-edit"]             (render :pagename "user"            :raw-metadata-ns "user" :type "edit")
    ["users"]                 (render :pagename "users"           :raw-metadata-ns "users")

    ["run"]                   (render :pagename "run"             :raw-metadata-ns "run")
    ["runs"]                  (render :pagename "runs"            :raw-metadata-ns "runs")

    ["reports"]               (render :pagename "reports"         :raw-metadata-ns "reports")

    ["configuration-view"]    (render :pagename "configuration"   :raw-metadata-ns "configuration" :type "view")
    ["configuration-edit"]    (render :pagename "configuration"   :raw-metadata-ns "configuration" :type "edit")

    ["service-catalog-view"]  (render :pagename "service_catalog" :raw-metadata-ns "service-catalog"  :type "view")
    ["service-catalog-edit"]  (render :pagename "service_catalog" :raw-metadata-ns "service-catalog"  :type "edit")

    ["action"]                (render :pagename "action"          :raw-metadata-ns "action")

    ["error"]                 (render-error :raw-metadata-ns "module.project"
                                            :message "Oops!! Kaboom!! <a href='http://sixsq.com'>home</a>"
                                            :code 500)
    [&]                       (render-error :raw-metadata-ns "module.project"
                                            :code 404)))

;; =============================================================================
;; Local test app
;; =============================================================================

(defonce run-test-server
  (delay (utils/run-server routes)))
