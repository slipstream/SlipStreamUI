(ns slipstream.ui.views.configuration
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.header :as header]
                        [slipstream.ui.views.base :as base]))

(def configuration-template-html (common/get-template "configuration.html"))

(def parameters-sel [:#parameters])

(html/defsnippet header-titles-snip configuration-template-html header/titles-sel
  []
  identity)

(html/defsnippet header-snip header/header-template-html header/header-sel
  [metadata]
  header/titles-sel (html/substitute (header-titles-snip))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user-model/attrs metadata))))

(defn parameters-snip
  [parameters-by-category]
  (for [grouped parameters-by-category]
    (list
      (html/html-snippet (str "\n    <h3>" (string/replace (key grouped) #"_" " ") "</h3>"))
      (common/parameters-edit-snip (val grouped) false))))

(html/defsnippet content-snip configuration-template-html common/content-sel
  [configuration]
  parameters-sel (html/content
                   (parameters-snip (common-model/group-by-category (common-model/parameters configuration)))))


;; javascript inclusion

(def js-scripts-default
  [])

(defn js-scripts
  []
  (concat js-scripts-default ["/js/configuration.js"]))


(defn page-legacy
  [configuration]
  (base/base
    {:js-scripts (js-scripts)
     :title (common/title "Configuration")
     :header (header-snip (user-model/user configuration))
     :content (content-snip configuration)}))

(defn page [metadata]
  (base/generate
    {:metadata metadata
     :placeholder-page? true
     :header {:icon icons/config
              :title "System Configuration"
              :subtitle "Configure the SlipStream service and its cloud connectors"}
     :content nil}))

