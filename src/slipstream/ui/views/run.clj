(ns slipstream.ui.views.run
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.run :as run-model]))

(def run-template-html "slipstream/ui/views/run.html")
(def runtime-parameters-template-html "slipstream/ui/views/runtime-parameters.html")

(def summary-sel [:#summary])
(def parameters-sel [:#parameters])
(def runtime-parameters-header-sel [:#runtime-parameters-header])
(def runtime-parameters-sel [:#runtime-parameters])

;; View

(html/defsnippet header-snip header/header-template-html header/header-sel
  [run]
  header/header-summary-sel 
  (html/substitute 
    (let [attrs (common-model/attrs run)
          id (apply str (take common/take-run-no-of-chars (:uuid attrs)))
          module (:moduleresourceuri attrs)
          state (:state attrs)
          category (:category attrs)]
      (header/header-titles-snip
        id
        module
        (str "State: " state)
        category)))

  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user-model/attrs run))))

(defn- clone-runtime-parameters
  [parameters]
  (html/clone-for
    [parameter parameters
     :let 
     [attrs (common-model/attrs parameter)
      name (:key attrs)
      description (:description attrs)
      value (common/runtime-parameter-value parameter)]]
    [[:td (html/nth-of-type 1)]] (html/content name)
    [[:td (html/nth-of-type 2)]] (html/content description)
    [[:td (html/nth-of-type 3)]] (html/do->
                                   (html/content value)
                                   (html/set-attr :id (clojure.string/replace name #":" "\\:")))))


(html/defsnippet runtime-parameters-snip runtime-parameters-template-html [:#fragment-parameters-something]
  [parameters]
  [:table :> :thead] identity
  [:table :> :tbody :> :tr] (clone-runtime-parameters parameters))

(defn runtime-parameters
  "In this case, grouped by vm name (e.g. testclient1.1)"
  [parameters-by-group]
  (for [grouped parameters-by-group]
    (list 
      (html/html-snippet (str "\n    <h3>" (key grouped) "</h3>"))
      (runtime-parameters-snip (val grouped)))))

(html/defsnippet summary-snip run-template-html summary-sel
  [run]
  [:#module :> :a] (module-base/set-a (:moduleresourceuri (common-model/attrs run)))
  [:#category] (html/content (:category (common-model/attrs run)))
  [:#description] (html/content (:description (common-model/attrs run)))
  [:#username] (html/content (:user (common-model/attrs run)))
  [:#account] (html/content (:user (common-model/attrs run)))
  [:#start] (html/content (:starttime (common-model/attrs run)))
  [:#end] (html/content (:endtime (common-model/attrs run)))
  [:#state] (html/content (:state (common-model/attrs run)))
  [:#runtype] (html/content (run-model/get-type run))
  [:#uuid] (html/content (:uuid (common-model/attrs run)))
  [:#tags] (html/set-attr :value (run-model/runtime-parameter-value run "ss:tags")))

(html/defsnippet content-snip run-template-html common/content-sel
  [run]
  common/breadcrumb-sel (html/substitute
                          (common/breadcrumb-snip
                            (:uuid (common-model/attrs run))
                            "run"))

  summary-sel (html/substitute
                (summary-snip run))

  summary-sel (html/after
                (runtime-parameters
                  (common-model/group-by-group
                    (run-model/runtime-parameters run))))

  runtime-parameters-header-sel nil
  runtime-parameters-sel nil

  [:#reports :> :iframe] (html/set-attr :src (str "/reports/" (:uuid (common-model/attrs run)))))

;; CSS inclusion

(defn css-stylesheets
  []
  ["/external/jit/css/base.css" "/external/jit/css/Spacetree.css" "/css/run-dashboard.css"])

;; javascript inclusion

(defn js-scripts
  []
  ["/js/common.js"
   "/js/run-parameters-update.js"
   "/external/jit/js/jit.js"
   "/js/run-dashboard.js"
   "/external/ui.watermark/js/ui.watermark.js"
   "/js/run.js"])

;; Main function

(defn page [run]
  (base/base 
    {:css-stylesheets (css-stylesheets)
     :js-scripts (js-scripts)
     :title (common/title (run-model/module-name run))
     :header (header-snip run)
     :content (content-snip run)
     :footer (footer/footer-snip)}))
