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

;; Templates

(def project-view-template-html "slipstream/ui/views/project-view.html")
(def project-edit-template-html "slipstream/ui/views/project-edit.html")
(def project-new-template-html "slipstream/ui/views/project-new.html")

;; Utility

(defn to-css-class
  [category]
  (str (string/lower-case category) "_category"))  

(defn ischooser?
  [type]
  (= "chooser" type))

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
                                (module-model/titles-with-version module)))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user/attrs module))))

(html/defsnippet module-summary-view-snip project-view-template-html module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-version] (html/html-content
                       (str (:version (module-model/attrs module))
                            "<span> (<a href='/"
                            (:parenturi (module-model/attrs module))
                            "/"
                            (:shortname (module-model/attrs module))
                            "/'>history</a>)</span>"))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-comment] (html/content (module-model/module-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet module-summary-new-snip project-new-template-html module-summary-sel
  [module]
  [:#module-description] (html/set-attr :value (:description (module-model/attrs module)))
  [:#module-category :> :span] (html/content (:category (module-model/attrs module)))
  [:#module-category :> :input] (html/set-attr :value (:category (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet module-summary-edit-snip project-edit-template-html module-summary-sel
  [module]
  [:#module-name] (common/hidden-input-elem (:name (module-model/attrs module)) "module-name")
  [:#module-version] (html/html-content
                       (str (:version (module-model/attrs module))
                            "<span> (<a href='/"
                            (:parenturi (module-model/attrs module))
                            "/"
                            (:shortname (module-model/attrs module))
                            "/'>history</a>)</span>"))
  [:#module-description] (html/set-attr :value (:description (module-model/attrs module)))
  [:#module-category :> :span] (html/content (:category (module-model/attrs module)))
  [:#module-category :> :input] (html/set-attr :value (:category (module-model/attrs module)))
  [:#module-comment] (html/set-attr :value (module-model/module-comment module))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(defn breadcrumb
  [module]
  (html/substitute
    (common/breadcrumb-snip
      (:name (module-model/attrs module))
      "module")))

(defn header
  [metadata type snip]
  (if (ischooser? type)
    nil
    (snip metadata)))
  
(defn footer
  [type]
  (if (ischooser? type)
    nil
    (footer/footer-snip)))

