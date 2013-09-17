(ns slipstream.ui.views.dashboard
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.dashboard :as dashboard-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.base :as base]))

(def dashboard-template-html "slipstream/ui/views/dashboard.html")

(def runs-sel [:#runs])
(def vms-sel [:#vms])
(def runs-fragment-sel [:#fragment-runs-somecloud])
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

(defmulti type-to-icon
  (fn [type]
    type))

(defmethod type-to-icon :default
  [type]
  (type-to-icon "Deployment"))

(defmethod type-to-icon "Build"
  [type]
  "icon-legal")

(defmethod type-to-icon "Deployment"
  [type]
  "icon-cloud")

(defmethod type-to-icon "Run"
  [type]
  "icon-rocket")

(html/defsnippet runs-for-cloud-snip dashboard-template-html [runs-fragment-sel :> :table]
  [runs _]
  [:tbody :> :tr] (html/clone-for
                    [run runs
                     :let 
                     [attrs (module-model/attrs run)]]
                    [[:td (html/nth-of-type 1)] :> :i] (html/set-attr :class (type-to-icon (:type attrs)))
                    [[:td (html/nth-of-type 2)] :> :a] (html/do->
                                                         (html/set-attr :href (str "/" (:resourceuri attrs)))
                                                         (html/content (:uuid attrs)))
                    [[:td (html/nth-of-type 3)] :> :a] (html/do->
                                                         (html/set-attr :href (str "/" (:moduleresourceuri attrs)))
                                                         (html/content (apply str (drop 7 (:moduleresourceuri attrs)))))
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
                      (common/emtpy-section "No deployment, run or build currently available")
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
                     (common/emtpy-section "No virtual machine currently available")
                     (common/tab-sections grouped-by-cloud "vms" vms-for-cloud-snip))
  [:#fragment-vms-cloudb] nil)

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
        (dashboard-model/vms dashboard)))))

;; javascript inclusion

(def js-scripts-default
  [])

(defn js-scripts
  []
  (concat js-scripts-default []))

(defn page [dashboard]
  (base/base 
    {:js-scripts (js-scripts)
     :title (common/title "Dashboard")
     :header (header-snip dashboard)
     :content (content-snip dashboard)
     :footer (footer/footer-snip)}))
