(ns slipstream.ui.views.knockknock
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.header :as header]
                        [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.version :as version]))

(def knockknock-template-html (common/get-template "knockknock.html"))

(html/defsnippet header-snip knockknock-template-html header/header-sel
  [metadata]
  header/header-top-bar-sel (html/substitute
                              (header/header-top-only-snip metadata)))

(defn header
  [metadata type]
  (if (module-base/ischooser? type)
    nil
    (header-snip metadata)))

(defn footer
  [type]
  (if (module-base/ischooser? type)
    nil
    nil))

(html/deftemplate base knockknock-template-html
  [{:keys [js-scripts header content footer]}]
  header/header-sel (html/substitute header))

(html/defsnippet head knockknock-template-html base/head-sel
  []
  base/head-sel identity)

(html/defsnippet content knockknock-template-html base/content-sel
  []
  base/content-sel identity)

;; CSS inclusion

(def css-stylesheets-default
  ["/external/jquery/css/jquery.bxslider.css" "/css/knockknock.css"])

;; javascript inclusion

(def js-scripts-default
  ["/external/jquery/js/jquery.bxSlider.min.js" "/js/knockknock.js"])

(defn js-scripts
  [type]
  (if (= "chooser" type)
    (concat js-scripts-default ["/js/module-chooser.js"])
    js-scripts-default))

(defn page [metadata type]
  (base/base
    {:css-stylesheets css-stylesheets-default
     :js-scripts (js-scripts type)
     :title (common/title "Login/Register")
     :header (header metadata type)
     :content (content)
     :footer (footer type)}))
