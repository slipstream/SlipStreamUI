(ns slipstream.ui.views.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.run :as run-model]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.dashboard :as dashboard-model]
            [slipstream.ui.models.configuration :as configuration-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.messages :as messages]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.run :as run]
            [slipstream.ui.views.base :as base]))

(def dashboard-template-html "slipstream/ui/views/dashboard.html")

(def metering-sel [:#metering])
(def metering-header-sel [:#metering-header])
(def metrics-sel [:#metering :div.metric])

(def usage-sel [:#usage])
(def usage-header-sel [:#usage-header])

(def runs-sel [:#runs])
(def runs-fragment-sel [:#fragment-runs-somecloud])

(def vms-sel [:#vms])
(def vms-fragment-sel [:#fragment-vms-somecloud])

(html/defsnippet header-snip header/header-template-html header/header-sel
  [dashboard]
  header/header-summary-sel
  (html/substitute
    (header/header-titles-snip
      "Dashboard"
      "Control and monitor your cloud activity"
      "This page provides you with an overview of the activities on each cloud you have access to"
      "Dashboard"))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user-model/attrs dashboard))))

(html/defsnippet runs-for-cloud-snip dashboard-template-html [runs-fragment-sel :> :table]
  [runs _]
  [:tbody :> :tr] (html/clone-for
                    [run runs
                     :let
                     [attrs (module-model/attrs run)]]
                    [[:td (html/nth-of-type 1)]] (html/set-attr :class (common/type-to-icon-class (run-model/convert-type (:type attrs))))
                    [[:td (html/nth-of-type 2)] :> :a] (html/do->
                                                         (html/set-attr :href (str "/" (:resourceuri attrs)))
                                                         (html/content (run/shorten-runid (:uuid attrs))))
                    [[:td (html/nth-of-type 3)] :> :a] (html/do->
                                                         (html/set-attr :href (str "/" (:moduleresourceuri attrs)))
                                                         (html/content (apply str (drop common/drop-module-slash-no-of-chars (:moduleresourceuri attrs)))))
                    [[:td (html/nth-of-type 4)]] (html/content (:status attrs))
                    [[:td (html/nth-of-type 5)]] (html/content (:starttime attrs))
                    [[:td (html/nth-of-type 6)]] (html/content (:username attrs))
                    [[:td (html/nth-of-type 7)]] (html/content (:tags attrs))))

(html/defsnippet vms-for-cloud-snip dashboard-template-html [vms-fragment-sel :> :table]
  [vms _]
  [:tbody :> :tr] (html/clone-for
                    [vm vms
                     :let
                     [attrs (module-model/attrs vm)]]
                    [[:a]] (html/do->
                             (html/set-attr :href (str "/run/" (:runuuid attrs)))
                             (html/content (:runuuid attrs)))
                    [[:td (html/nth-of-type 2)]] (html/content (:status attrs))
                    [[:td (html/nth-of-type 3)]] (html/content (:instanceid attrs))
                    [[:td (html/nth-of-type 4)]] (html/content (:cloud attrs))))

(html/defsnippet runs-snip dashboard-template-html runs-sel
  [grouped-by-cloud]
  [:ul :> [:li html/last-of-type]] nil
  [:ul :> :li]
  (common/tab-headers grouped-by-cloud "runs")
  [:ul] (if (empty? grouped-by-cloud)
          nil
          identity)

  runs-fragment-sel (if (empty? grouped-by-cloud)
                      (common/emtpy-section (:msg-no-run-all messages/msg))
                      (common/tab-sections grouped-by-cloud "runs" runs-for-cloud-snip))
  [:#fragment-runs-cloudb] nil)

(html/defsnippet vms-snip dashboard-template-html vms-sel
  [grouped-by-cloud]
  [:ul :> [:li html/last-of-type]] nil
  [:ul :> :li]
  (common/tab-headers grouped-by-cloud "vms")
  [:ul] (if (empty? grouped-by-cloud)
          nil
          identity)

  vms-fragment-sel (if (empty? grouped-by-cloud)
                     (common/emtpy-section (:msg-no-vm messages/msg))
                     (common/tab-sections grouped-by-cloud "vms" vms-for-cloud-snip))
  [:#fragment-vms-cloudb] nil)

(html/defsnippet usage-snip dashboard-template-html usage-sel
  [usages]
  [:div :> [:div html/last-of-type]] nil
  [:div :> :div] (html/clone-for
                   [usage usages
                    :let
                    [attrs (dashboard-model/attrs usage)]]
                   (html/do->
                     (html/set-attr :id (str "gauge-" (:cloud attrs)))
                     (html/set-attr :data-quota-title (:cloud attrs))
                     (html/set-attr :data-quota-max (:quota attrs))
                     (html/set-attr :data-quota-current (:currentusage attrs)))))

(html/defsnippet content-snip dashboard-template-html common/content-sel
  [dashboard]
  runs-sel
  (html/substitute
    (runs-snip
      (common-model/group-by-key
        :cloudservicename
        (dashboard-model/runs dashboard))))

  vms-sel
  (html/substitute
    (vms-snip
      (common-model/group-by-key
        :cloud
        (dashboard-model/vms dashboard))))

  [#{metering-sel metering-header-sel}]
  (if (configuration-model/metering-enabled? dashboard)
    identity
    nil)

  metrics-sel
  (if (configuration-model/metering-enabled? dashboard)
    (html/replace-vars {:username (clojure.string/replace
                                    (user-model/username dashboard) "." "_")})
    nil)

  usage-header-sel
  (if (configuration-model/quota-enabled? dashboard)
    identity
    nil)

  usage-sel
  (if (configuration-model/quota-enabled? dashboard)
    (html/substitute
      (usage-snip
        (dashboard-model/usages dashboard)))
    nil))

;  usage-sel
;  (usage-snip
;    (dashboard-model/usages dashboard)))

;  [#{usage-sel usage-header-sel}]
;  (if (configuration-model/quota-enabled? dashboard)
;    (usage-snip (dashboard-model/usages dashboard))
;    nil))

;; CSS inclusion

(defn css-stylesheets
  []
  ["/css/dashboard.css"])

;; javascript inclusion

(def js-scripts-default
  ["/external/jquery-flot/js/jquery.flot.min.js"
   "/external/jquery-flot/js/jquery.flot.pie.min.js"
   "/external/jquery-flot/js/jquery.flot.time.min.js"
   "/external/jquery-flot/js/jquery.flot.stack.min.js"
   "/external/jquery-flot/js/jquery.flot.tooltip.min.js"
   "/external/justgage/js/raphael.2.1.0.min.js"
   "/external/justgage/js/justgage.1.0.1.min.js"
   "/js/metering.js"
   "/js/dashboard.js"])

(defn js-scripts
  []
  (concat js-scripts-default []))

(defn page [dashboard]
  (base/base
    {:css-stylesheets (css-stylesheets)
     :js-scripts (js-scripts)
     :title (common/title "Dashboard")
     :header (header-snip dashboard)
     :content (content-snip dashboard)
     :footer (footer/footer-snip)}))
