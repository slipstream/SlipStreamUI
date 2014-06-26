(ns slipstream.ui.views.module-base
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.module :as module-model]))

;; Selectors

(def module-summary-sel [:#module-summary])
(def module-login-sel [:#module-login])
(def module-interaction-sel [:.interaction])
(def module-interaction-top-sel [:#interaction-top])
(def module-interaction-bottom-sel [:#interaction-bottom])
(def module-publish-button-top [:#publish-button-top])
(def module-publish-button-bottom [:#publish-button-bottom])
(def module-unpublish-button-top [:#unpublish-button-top])
(def module-unpublish-button-bottom [:#unpublish-button-bottom])
(def run-with-options-dialog-sel [:#run-with-options-dialog])
(def build-with-options-dialog-sel [:#build-with-options-dialog])

;; Templates

(def project-view-template-html "slipstream/ui/views/project-view.html")
(def project-edit-template-html "slipstream/ui/views/project-edit.html")
(def project-new-template-html "slipstream/ui/views/project-new.html")

;; Utility

(defn module-resource-uri-to-name
  [moduleresourceuri]
  (apply str (drop module-model/module-root-uri-length moduleresourceuri)))
  
(defn set-a
  [moduleresourceuri]
  (html/do->
    (html/content (module-resource-uri-to-name moduleresourceuri))
    (html/set-attr :href (str "/" moduleresourceuri))))

(defn to-css-class
  [category]
  (common/to-css-class category))

(defn ischooser?
  [type]
  (= "chooser" type))

(defn titles-with-version
  [module]
  (if (true? (module-model/new? module))
    [(str "New " (module-model/module-category module) "...") 
     "" 
     "" 
     (module-model/module-category module)]
    (module-model/titles-with-version module)))

(def js-scripts-default
  ["/external/jit/js/jit.js" 
   "/external/jit/js/excanvas.js" ])

(defn js-scripts-chooser
  []
  (concat js-scripts-default ["/js/module-chooser.js"]))

;; Snippets

(html/defsnippet header-snip header/header-template-html header/header-sel
  [module]
  header/header-summary-sel (html/substitute
                              (apply
                                header/header-titles-snip 
                                (titles-with-version module)))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user/attrs module)))
  [:#module-logo] (let [link (module-model/module-logo-link module)]
                    (if link 
                      (html/do->
                        (html/set-attr :src (module-model/module-logo-link module))
                        (html/remove-class "hidden"))
                      identity)))

(html/defsnippet module-summary-view-snip project-view-template-html module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-version] (html/html-content
                         (str "<span>" (:version (module-model/attrs module)) "</span>"
                              "<span> (<a href='/module/"
                              (:name (module-model/attrs module))
                              "/'>history</a>)</span>"))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-comment] (html/content (module-model/module-commit-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(defn module-summary-trans
  [module]
  (html/transformation
    [:#parent-module-name] (html/content (module-model/parent-name module))
    [:#module-category :> :span] (html/content (:category (module-model/attrs module)))
    [:#module-category :> :input] (html/set-attr :value (:category (module-model/attrs module)))
    [:#module-owner] (html/content (module-model/owner module))))

(html/defsnippet module-summary-new-snip project-new-template-html module-summary-sel
  [module]
  (module-summary-trans module))

(html/defsnippet module-summary-edit-snip project-edit-template-html module-summary-sel
  [module]
  [:#logo-link-input] (html/set-attr :value (module-model/module-logo-link module))
  [:#parent-module-name] (html/content (module-model/parent-name module))
  [:#module-name :> :span] (html/content (:name (module-model/attrs module)))
  [:#module-name :> :input] (html/set-attr :value (:name (module-model/attrs module)))
  [:#module-version] (html/html-content
                       (str "<span id='version'>" (:version (module-model/attrs module)) "</span>"
                            "<span> (<a href='/"
                            (:parenturi (module-model/attrs module))
                            "/"
                            (:shortname (module-model/attrs module))
                            "/'>history</a>)</span>"))
  [:#module-description] (html/set-attr :value (:description (module-model/attrs module)))
  [:#module-category :> :span] (html/content (:category (module-model/attrs module)))
  [:#module-category :> :input] (html/set-attr :value (:category (module-model/attrs module)))
  [:#module-comment] (html/set-attr :value (module-model/module-commit-comment module))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(defn breadcrumb
  [module-name]
  (html/substitute
    (common/breadcrumb-snip
      module-name
      "module")))

(defn header
  [metadata type snip-fn]
  (if (ischooser? type)
    nil
    (snip-fn metadata)))
  
(defn footer
  [type]
  (if (ischooser? type)
    nil
    (footer/footer-snip)))

