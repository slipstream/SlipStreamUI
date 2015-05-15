(ns slipstream.ui.main
(:use slipstream.ui.util.dev-traces)
(:require [clojure.string :as s]
            [ring.util.response :as resp]
            [ring.middleware.resource :as resource]
            [slipstream.ui.util.mode :as mode]
            [slipstream.ui.util.clojure :as uc]
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
    (-> raw-metadata-ns full-metadata-ns symbol (require :reload-all))
    (if-let [raw-metadata-symbol (-> (full-metadata-ns raw-metadata-ns)
                                     (symbol "raw-metadata-str")
                                     resolve)]
      (var-get raw-metadata-symbol)
      (throw (IllegalArgumentException.
               (format "metadata: var '%s/raw-metadata-str not found for raw-metadata-ns '%s'"
                       (full-metadata-ns raw-metadata-ns)
                       raw-metadata-ns))))))

(defn- render
  [& {:keys [raw-metadata-ns pagename type query-parameters]}]
  (mode/with-headless-environment
    (-> raw-metadata-ns
        raw-metadata-str
        (representation/-toHtml pagename {:type type :request {:query-parameters query-parameters}})
        resp/response
        constantly
        (resource/wrap-resource "public"))))

(defn- render-error
  [& {:keys [raw-metadata-ns message code]}]
  (mode/with-headless-environment
    (-> raw-metadata-ns
        raw-metadata-str
        (representation/-toHtmlError message code nil)
        resp/response
        constantly)))

(defn- render-file
  [file-data-file]
  (mode/with-headless-environment
    (-> (str "test/slipstream/ui/mockup_data/" file-data-file)
        slurp
        resp/response
        (resp/content-type (->> file-data-file (re-matches #".*\.(.*)") second keyword))
        constantly)))

(defmacro app-routes
  [& routes]
  (let [index-page (->> routes
                        (take-nth 2)
                        flatten
                        (remove #{'&})
                        (partition-by first)
                        (uc/mmap #(str "<div><a href='/" % "'>" % "</a></div>"))
                        (interpose ["<br>"])
                        flatten
                        s/join)]
    `(def ~(symbol "routes")
       (app
         [""] (fn [req#]  (resp/response ~index-page))
         ~@routes))))

(app-routes
    ["login"]                 (render :pagename "login")

    ["login-chooser"]         (render :pagename "login" :type "chooser")

    ["logout"]                (render :pagename "logout")

    ["welcome"]               (render :pagename "welcome"         :raw-metadata-ns "welcome" :type "view")
    ["welcome-with-tour"]     (render :pagename "welcome"         :raw-metadata-ns "welcome" :type "view" :query-parameters {:start-tour "yes"})
    ["welcome-regular-user"]  (render :pagename "welcome"         :raw-metadata-ns "welcome-regular-user" :type "view")
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
    ["deployment-view-tour-intro-without-connectors"] (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "view" :query-parameters {:action "run" :tour "alice.intro-without-connectors.deploying-wordpress"})
    ["deployment-view-super"] (render :pagename "module"          :raw-metadata-ns "module.deployment-super"  :type "view")
    ["deployment-chooser"]    (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "chooser")
    ["deployment-edit"]       (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "edit")
    ["deployment-new"]        (render :pagename "module"          :raw-metadata-ns "module.deployment"  :type "new")

    ["versions"]              (render :pagename "versions"        :raw-metadata-ns "versions" :type "view")
    ["versions-chooser"]      (render :pagename "versions"        :raw-metadata-ns "versions" :type "chooser")

    ["dashboard"]             (render :pagename "dashboard"       :raw-metadata-ns "dashboard")
    ["runs"]                  (render :pagename "runs"            :raw-metadata-ns "runs")
    ["runs-paginated"]        (render :pagename "runs"            :raw-metadata-ns "runs-paginated")
    ["vms-super"]             (render :pagename "vms"             :raw-metadata-ns "vms-super")
    ["vms-regular-user"]      (render :pagename "vms"             :raw-metadata-ns "vms-regular-user")
    ["metrics" "render"]      (render-file "metrics.json")
    ; ["metrics" "render"]      (render-file "metrics_2_lots_of_clouds.json")

    ["user-view"]                 (render :pagename "user"            :raw-metadata-ns "user" :type "view")
    ["user-view-without-clouds"]  (render :pagename "user"            :raw-metadata-ns "user-without-clouds" :type "view")
    ["user-view-tour-intro-without-connectors"] (render :pagename "user"            :raw-metadata-ns "user" :type "view" :query-parameters {:tour "alice.intro-without-connectors.navigate-back-to-welcome"})
    ["user-edit"]             (render :pagename "user"            :raw-metadata-ns "user" :type "edit")
    ["user-edit-without-clouds"]  (render :pagename "user"            :raw-metadata-ns "user-without-clouds" :type "edit")
    ["user-edit-tour-intro-without-connectors"] (render :pagename "user"            :raw-metadata-ns "user" :type "edit" :query-parameters {:tour "alice.intro-without-connectors.edit-profile"})
    ["user-new"]              (render :pagename "user"            :raw-metadata-ns "user" :type "new")
    ["users"]                 (render :pagename "users"           :raw-metadata-ns "users")

    ["run"]                   (render :pagename "run"             :raw-metadata-ns "run")
    ["run-25-instances"]      (render :pagename "run"             :raw-metadata-ns "run-25-instances")
    ["run-200-instances"]     (render :pagename "run"             :raw-metadata-ns "run-200-instances")
    ["run-263-instances"]     (render :pagename "run"             :raw-metadata-ns "run-263-instances")
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
    )

;; =============================================================================
;; Local test app
;; =============================================================================

(def ^:private ^:dynamic *test-server-port*
  8082)

(defonce test-server
  (delay
    (if-let [test-server (utils/run-server routes :port *test-server-port*)]
      (do
        (println
          (str
           "\n"
           "\033[1;32mThe headless test server has started successfully on port " *test-server-port* ".\033[0m\n"
           "Go to following URL for a list of existent test pages:\n"
           "\n"
           "  http://localhost:" *test-server-port* "/\n"
           "\n"
           "If code changes (in clojure or HTML) are not taken into account, just reload the headless app."))
        test-server)
      (throw (IllegalStateException. "run-test-server failed to start")))))

(defn reload-headless-app
  [& {:keys [port]}]
  (when-let [^org.eclipse.jetty.server.Server server (and
                                                       (realized? test-server)
                                                       @test-server)]
    (let [current-port (-> server .getConnectors first .getPort)]
      (printf "Test server already running on port %s.\n" current-port)
      (if (and port (not= port current-port))
        (printf "\033[1;33mPort %s ignored.\033[0m\n" port)))
    (println "Restarting...")
    (flush)
    (.stop  server)
    (.start server)
    (println "\033[1;32mRestarted successfully.\033[0m"))
  (binding [*test-server-port* (or port *test-server-port*)]
    @test-server))
