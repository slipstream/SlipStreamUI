(ns slipstream.ui.main
  (:require [clojure.string :as s]
            [ring.util.response :as resp]
            [ring.middleware.resource :as resource]
            [slipstream.ui.util.dev :as ud]
            [slipstream.ui.utils :as utils]
            [slipstream.ui.views.representation :as representation])
  (:use [net.cgrand.moustache :only [app]]))

;; =============================================================================
;; Local test routes
;; =============================================================================

(def ^:private full-metadata-ns
  (partial format "slipstream.ui.models.%s-test"))

(defn- raw-metadata-str
  [raw-metadata-ns]
  (when raw-metadata-ns
    (-> raw-metadata-ns full-metadata-ns symbol require)
    (if-let [raw-metadata-symbol (-> (full-metadata-ns raw-metadata-ns)
                                     (symbol "raw-metadata-str")
                                     resolve)]
      (var-get raw-metadata-symbol)
      (throw (IllegalArgumentException.
               (format "metadata: var '%s/raw-metadata-str not found for raw-metadata-ns '%s'"
                       (full-metadata-ns raw-metadata-ns)
                       raw-metadata-ns))))))

(defn- render
  [& {:keys [raw-metadata-ns pagename type]}]
  (ud/with-dev-environment
    (-> raw-metadata-ns
        raw-metadata-str
        (representation/-toHtml pagename {"type" type})
        resp/response
        constantly
        (resource/wrap-resource "public"))))

(defn- render-error
  [& {:keys [raw-metadata-ns message code]}]
  (ud/with-dev-environment
    (-> raw-metadata-ns
        raw-metadata-str
        (representation/-toHtmlError message code)
        resp/response
        constantly)))

(defn- render-file
  [file-data-file]
  (ud/with-dev-environment
    (-> (str "test/slipstream/ui/mockup_data/" file-data-file)
        slurp
        resp/response
        (resp/content-type (->> file-data-file (re-matches #".*\.(.*)") second keyword))
        constantly)))

(def routes
  (app
    ["login"]                 (render :pagename "login")

    ["login-chooser"]         (render :pagename "login" :type "chooser")

    ["logout"]                (render :pagename "logout")

    ["documentation"]         (render :pagename "documentation")

    ["welcome"]               (render :pagename "welcome"         :raw-metadata-ns "welcome" :type "view")
    ["welcome-chooser"]       (render :pagename "welcome"         :raw-metadata-ns "welcome" :type "chooser")

    ["service_catalog"]       (render :pagename "service_catalog" :raw-metadata-ns "service-catalog")

    ["project-view"]          (render :pagename "module"          :raw-metadata-ns "module.project"     :type "view")
    ["project-chooser"]       (render :pagename "module"          :raw-metadata-ns "module.project"     :type "chooser")
    ["project-edit"]          (render :pagename "module"          :raw-metadata-ns "module.project"     :type "edit")
    ["project-new"]           (render :pagename "module"          :raw-metadata-ns "module.project-new" :type "new")

    ; TODO: Project-root was historically used for the now "welcome" page.
    ;       This note is to flag this project-root-* pages as stale, to delete them
    ;       and their corresponding test data at a later point.
    ["project-root-view"]     (render :pagename "module"          :raw-metadata-ns "module.project-root"      :type "view")
    ["project-root-edit"]     (render :pagename "module"          :raw-metadata-ns "module.project-root"      :type "edit")
    ["project-root-new"]      (render :pagename "module"          :raw-metadata-ns "module.project-root-new"  :type "new")

    ["image-view"]            (render :pagename "module"          :raw-metadata-ns "module.image"     :type "view")
    ["image-chooser"]         (render :pagename "module"          :raw-metadata-ns "module.image"     :type "chooser")
    ["image-edit"]            (render :pagename "module"          :raw-metadata-ns "module.image"     :type "edit")
    ["image-new"]             (render :pagename "module"          :raw-metadata-ns "module.image-new" :type "new")

    ["deployment-view"]       (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "view")
    ["deployment-view-super"] (render :pagename "module"          :raw-metadata-ns "module.deployment-super"  :type "view")
    ["deployment-chooser"]    (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "chooser")
    ["deployment-edit"]       (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "edit")
    ["deployment-new"]        (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "new")

    ["versions"]              (render :pagename "versions"        :raw-metadata-ns "versions" :type "view")
    ["versions-chooser"]      (render :pagename "versions"        :raw-metadata-ns "versions" :type "chooser")

    ["dashboard"]             (render :pagename "dashboard"       :raw-metadata-ns "dashboard")
    ["runs"]                  (render :pagename "runs"             :raw-metadata-ns "runs")
    ["vms"]                   (render :pagename "vms"             :raw-metadata-ns "vms")
    ["metrics" "render"]      (render-file "metrics.json")
    ; ["metrics" "render"]      (render-file "metrics_2_lots_of_clouds.json")

    ["user-view"]             (render :pagename "user"            :raw-metadata-ns "user" :type "view")
    ["user-edit"]             (render :pagename "user"            :raw-metadata-ns "user" :type "edit")
    ["user-new"]              (render :pagename "user"            :raw-metadata-ns "user" :type "new")
    ["users"]                 (render :pagename "users"           :raw-metadata-ns "users")

    ["run"]                   (render :pagename "run"             :raw-metadata-ns "run")
    ["run-25-instances"]      (render :pagename "run"             :raw-metadata-ns "run-25-instances")
    ["run-1000-instances"]    (render :pagename "run"             :raw-metadata-ns "run-1000-instances")

    ["reports" &]             (render :pagename "reports"         :raw-metadata-ns "reports")

    ["configuration"]         (render :pagename "configuration"   :raw-metadata-ns "configuration")

    ["action"]                (render :pagename "action"          :raw-metadata-ns "action")

    ["error"]                 (render-error :raw-metadata-ns "module.project"
                                            :message "Oops!! Kaboom!! <a href='http://sixsq.com'>home</a>"
                                            :code 500)
    ["error-401"]             (render-error :raw-metadata-ns "module.project"
                                            :message "I'm afraid you are not allowed to do this."
                                            :code 401)
    [&] (fn [req] (resp/file-response (req :uri) {:root "resources/static_content"}))
    ; [&]                       (render-error :raw-metadata-ns "module.project"
    ;                                         :code 404)
    ))

;; =============================================================================
;; Local test app
;; =============================================================================

(defonce run-test-server
  (delay (utils/run-server routes)))
