(ns slipstream.ui.views.configuration
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.base :as base]))

(def configuration-template-html "slipstream/ui/views/configuration.html")

(def parameters-sel [:#parameters])

(html/defsnippet header-snip configuration-template-html header/header-sel
  [user]
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user-model/attrs user))))

(defn parameters-snip
  [parameters-by-category]
  (for [grouped parameters-by-category]
    (list
      (html/html-snippet (str "\n    <h3>" (key grouped) "</h3>"))
      (common/parameters-edit-snip (val grouped)))))

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


(defn page
  [configuration]
  (base/base 
    {:js-scripts (js-scripts)
     :title (common/title "Configuration")
     :header (header-snip (user-model/user configuration))
     :content (content-snip configuration)
     :footer (footer/footer-snip)}))
