(ns slipstream.ui.views.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.image :as image]
            [slipstream.ui.views.deployment :as deployment]
            [slipstream.ui.views.project :as project]))

(def deployment-view-template-html (common/get-template "deployment-view.html"))
(def deployment-edit-template-html (common/get-template "deployment-edit.html"))
(def deployment-new-template-html (common/get-template "deployment-new.html"))

(defn header
  [module type]
  (if (module-base/ischooser? type)
    nil
    (module-base/header-snip module)))

(defn footer
  [type]
  (if (module-base/ischooser? type)
    nil
    nil))


;; View

(html/defsnippet deployment-view-snip deployment-view-template-html common/content-sel
  [module]
  identity)

(defmulti content-by-category-view
  (fn [module category]
    category))

(defmethod content-by-category-view "Image"
  [module category]
  common/content-sel (image/view-snip module))

(defmethod content-by-category-view "Deployment"
  [module category]
  common/content-sel (deployment/view-snip module))

(defmethod content-by-category-view "Project"
  [module category]
  common/content-sel (project/view-snip module))

;; Edit

(html/defsnippet deployment-edit-snip deployment-edit-template-html common/content-sel
  [module]
  identity)

(defmulti content-by-category-edit
  (fn [module category]
    category))

(defmethod content-by-category-edit "Image"
  [module category]
  common/content-sel (image/edit-snip module))

(defmethod content-by-category-edit "Deployment"
  [module category]
  common/content-sel (deployment/edit-snip module))

(defmethod content-by-category-edit "Project"
  [module category]
  common/content-sel (project/edit-snip module))

;; New

(html/defsnippet deployment-new-snip deployment-new-template-html common/content-sel
  [module]
  identity)

(defmulti content-by-category-new
  (fn [module category]
    category))

(defmethod content-by-category-new "Image"
  [module category]
  common/content-sel (image/new-snip module))

(defmethod content-by-category-new "Deployment"
  [module category]
  common/content-sel (deployment/new-snip module))

(defmethod content-by-category-new "Project"
  [module category]
  common/content-sel (project/new-snip module))

;; Dispatch function

(defmulti content
  "Multi method dispatched on type: view, edit, new, chooser"
  (fn [module type category]
    type))

(defmethod content "view"
  [module type category]
  (content-by-category-view module category))

(defmethod content "edit"
  [module type category]
  (content-by-category-edit module category))

(defmethod content "new"
  [module type category]
  (content-by-category-new module category))

(defmethod content "chooser"
  [module type category]
    (content-by-category-view module category))


;; javascript inclusion

(def js-scripts-default
  ["/external/jit/js/jit.js"
   "/external/jit/js/excanvas.js"
   "/js/sourcecode-editor.js"
   "/external/ace-editor/ace.js"
   "/external/ace-editor/ext-language_tools.js"
   "/external/ace-editor/mode-python.js"
   "/external/ace-editor/mode-sh.js"
   "/external/ace-editor/mode-javascript.js"
   "/external/ace-editor/mode-powershell.js"
   "/external/ace-editor/mode-perl.js"
   "/external/ace-editor/mode-ruby.js"
   "/external/ace-editor/mode-scheme.js"
   "/external/ace-editor/mode-plain_text.js"
   ])

(defmulti js-scripts
  (fn [type category]
    [type category]))

(defn js-scripts-chooser
  []
  (concat js-scripts-default ["/js/module-chooser.js"]))

(defn js-scripts-project-view
  []
  (concat js-scripts-default ["/js/project-view.js"]))

(defn js-scripts-image-view
  []
  (concat js-scripts-default ["/js/image-view.js"]))

(defn js-scripts-deployment-view
  []
  (concat js-scripts-default ["/js/deployment-view.js"]))

(defmethod js-scripts ["view" "Project"]
  [type category]
  (js-scripts-project-view))

(defmethod js-scripts ["edit" "Project"]
  [type category]
  (concat js-scripts-default ["/js/module.js" "/js/module-edit.js" "/js/project-edit.js"]))

(defmethod js-scripts ["new" "Project"]
  [type category]
  (concat js-scripts-default ["/js/module.js" "/js/module-new.js" "/js/project-new.js"]))

(defmethod js-scripts ["chooser" "Project"]
  [type category]
  (js-scripts-chooser))

(defmethod js-scripts ["view" "Image"]
  [type category]
  (js-scripts-image-view))

(defmethod js-scripts ["edit" "Image"]
  [type category]
  (concat js-scripts-default ["/js/module.js" "/js/module-edit.js" "/js/image-edit.js"]))

(defmethod js-scripts ["new" "Image"]
  [type category]
  (concat js-scripts-default ["/js/module.js" "/js/module-new.js" "/js/image-edit.js"]))

(defmethod js-scripts ["chooser" "Image"]
  [type category]
  (js-scripts-chooser))

(defmethod js-scripts ["view" "Deployment"]
  [type category]
  (js-scripts-deployment-view))

(defmethod js-scripts ["edit" "Deployment"]
  [type category]
  (concat js-scripts-default ["/js/module.js" "/js/module-edit.js" "/js/deployment-edit.js"]))

(defmethod js-scripts ["new" "Deployment"]
  [type category]
  (concat js-scripts-default ["/js/module.js" "/js/module-new.js" "/js/deployment-edit.js"]))

(defmethod js-scripts ["chooser" "Deployment"]
  [type category]
  (js-scripts-chooser))


;; Main function

(defn page-legacy [module type]
  (let
    [category (module-model/module-category module)]
    (base/base
      {:js-scripts (js-scripts type category)
       :title (common/title (module-model/module-name module))
       :header (header module type)
       :content (content module type category)
       :footer (footer type)})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- header
  [module]
  {:icon (icons/icon-for (:category module))
   :title (format "%s: %s"
                 (:category module)
                 (:short-name module))
   :subtitle (str "Version: " (:version module)
               (when-let [desc (:description module)]
                 (str " - " desc)))})

(defn- old-version-alert
  [module]
  (when-not (:latest-version? module)
    {:type :warning
     :msg "You are not on the latest version of this module."}))

(defn page [metadata type]
  (let [module (module-model/parse metadata)]
    (base/generate
      {:metadata metadata
       :header (header module)
       :alerts [(old-version-alert module)]
       :resource-uri (:uri module)
       :secondary-menu-actions [action/edit
                                action/new-project
                                action/new-image
                                action/new-deployment
                                action/import]
       :content [{:title "Summary"
                  :selected? true
                  :content (t/module-summary-table module)}
                 {:title "Authorizations"
                  :content [(t/access-rights-table (-> module :authorization :access-rights))
                            (t/group-members-table (-> module :authorization))]}]})))
