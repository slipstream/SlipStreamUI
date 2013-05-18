(ns slipstream.ui.views.module
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.views.authz-view :as authz-view]
            [slipstream.ui.views.authz-edit :as authz-edit]
            [slipstream.ui.views.header :as header]))

(def project-view-template-html "slipstream/ui/views/project-view.html")
(def project-edit-template-html "slipstream/ui/views/project-edit.html")
(def project-new-template-html "slipstream/ui/views/project-new.html")

(def image-view-template-html "slipstream/ui/views/image-view.html")
(def image-edit-template-html "slipstream/ui/views/image-edit.html")
(def image-new-template-html "slipstream/ui/views/image-new.html")

(def deployment-view-template-html "slipstream/ui/views/deployment-view.html")
(def deployment-edit-template-html "slipstream/ui/views/deployment-edit.html")
(def deployment-new-template-html "slipstream/ui/views/deployment-new.html")

(def breadcrumb-template-html "slipstream/ui/views/breadcrumb.html")

; Breadcrumbs

(def breadcrumb-sel [:#breadcrumb])

(defn- breadcrumb-href
  "root-uri, e.g. 'module/' in the case of modules, or 'user/'
   in the case of users"
  [names index root-uri]
  (if (= "" (names index))
    (str "/" root-uri)
    (str "/"
      root-uri 
      (reduce 
        #(str %1 (if (= "" %1) "" "/") %2) 
        "" 
        (subvec names 0 (inc index))))))
                                 
(html/defsnippet breadcrumb-snip breadcrumb-template-html breadcrumb-sel
  [name root-uri]
  [[:li html/last-of-type]] nil
  [[:li (html/nth-of-type 3)]] (html/clone-for 
                                 [i (range (count (string/split name #"/")))] 
                                 [:a]
                                 (let 
                                   [names (string/split name #"/")
                                    href (breadcrumb-href names i root-uri)
                                    short-name (names i)]
                                   (html/do-> 
                                     (html/content (if (= "" short-name)
                                                     root-uri short-name))
                                     (html/set-attr :href href)))))

(html/defsnippet header-snip header/header-template-html header/header-sel
  [module]
  header/header-summary-sel 
  (html/substitute 
    (apply 
      header/header-titles-snip 
      (module-model/titles-with-version module)))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user/attrs module))))

(def module-summary-sel [:#module-summary])

(html/defsnippet module-summary-view-snip project-view-template-html module-summary-sel
  [module]
  [:#module-name] (html/content (:name (module-model/attrs module)))
  [:#module-version] (html/html-content
                         (str (:version (module-model/attrs module))
                              "<span> (<a href='"
                              (:parenturi (module-model/attrs module))
                              "/"
                              (:shortname (module-model/attrs module))
                              "/'>history</a>)</span>"))
  [:#module-description] (html/content (:description (module-model/attrs module)))
  [:#module-comment] (html/content (module-model/module-comment module))
  [:#module-category] (html/content (:category (module-model/attrs module)))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module))
  )

(defn hidden-input-elem [value id]
  "Returns a value + hidden input element"
  (html/html-content
    (str value "<input name='name' id='" id "' type='hidden' value='" value "'>")))

(html/defsnippet module-summary-edit-snip project-edit-template-html module-summary-sel
  [module]
  [:#module-name] (hidden-input-elem (:name (module-model/attrs module)) "module-name")
  [:#module-version] (html/html-content
                       (str (:version (module-model/attrs module))
                            "<span> (<a href='"
                            (:parenturi (module-model/attrs module))
                            "/"
                            (:shortname (module-model/attrs module))
                            "/'>history</a>)</span>"))
  [:#module-description] (html/set-attr :value (:description (module-model/attrs module)))
  [:#module-comment] (html/set-attr :value (module-model/module-comment module))
  [:#module-created] (html/content (:creation (module-model/attrs module)))
  [:#module-last-modified] (html/content (:lastmodified (module-model/attrs module)))
  [:#module-owner] (html/content (module-model/owner module)))

(html/defsnippet module-summary-new-snip project-new-template-html module-summary-sel
  [module]
  identity)

(def children-sel [:#children])

(html/defsnippet children-snip project-view-template-html [children-sel :> :table]
  [parent]
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr (html/nth-of-type 1)]] 
                  (html/clone-for 
                    [child (modules-model/modules parent)]
                    [[:a]] (html/do->
                             (html/set-attr :href (str "/" (:resourceuri (module-model/attrs child))))
                             (html/content (:name (module-model/attrs child))))
                    [[:td (html/nth-of-type 2)]] (html/content (:description (module-model/attrs child)))
                    [[:td (html/nth-of-type 3)]] (html/content (:owner (authz-model/attrs child)))
                    [[:td (html/nth-of-type 4)]] (html/content (:version (module-model/attrs child)))))


;; View

(html/defsnippet project-view-snip project-view-template-html common/content-sel
  [module]
  breadcrumb-sel (html/substitute
                   (breadcrumb-snip
                     (:name (module-model/attrs module))
                     "module/"))
  children-sel (html/content (children-snip module))
  module-summary-sel (html/substitute 
                       (module-summary-view-snip module))
  authz/authorization-sel (html/substitute (authz-view/authz-snip module)))

(html/defsnippet image-view-snip image-view-template-html common/content-sel
  [module]
  identity)

(html/defsnippet deployment-view-snip deployment-view-template-html common/content-sel
  [module]
  identity)

(defmulti content-by-category-view
  (fn [module category]
    category))

(defmethod content-by-category-view "Image"
  [module category]
  common/content-sel (image-view-snip module))

(defmethod content-by-category-view "Deployment"
  [module category]
  common/content-sel (deployment-view-snip module))

(defmethod content-by-category-view "Project"
  [module category]
  common/content-sel (project-view-snip module))


;; Edit

(html/defsnippet project-edit-snip project-edit-template-html common/content-sel
  [module]
  breadcrumb-sel (html/substitute
                   (breadcrumb-snip
                     (:name (module-model/attrs module))
                     "module/"))
  children-sel (html/content (children-snip module))
  module-summary-sel (html/substitute 
                       (module-summary-edit-snip module))
  authz/authorization-sel (html/substitute (authz-edit/authz-snip module)))

(html/defsnippet image-edit-snip image-edit-template-html common/content-sel
  [module]
  identity)

(html/defsnippet deployment-edit-snip deployment-edit-template-html common/content-sel
  [module]
  identity)

(defmulti content-by-category-edit
  (fn [module category]
    category))

(defmethod content-by-category-edit "Image"
  [module category]
  common/content-sel (image-edit-snip module))

(defmethod content-by-category-edit "Deployment"
  [module category]
  common/content-sel (deployment-edit-snip module))

(defmethod content-by-category-edit "Project"
  [module category]
  common/content-sel (project-edit-snip module))

;; New

(html/defsnippet project-new-snip project-new-template-html common/content-sel
  [module]
  breadcrumb-sel (html/substitute
                   (breadcrumb-snip
                     (:name (module-model/attrs module))
                     "module/"))
  children-sel (html/content (children-snip module))
  module-summary-sel (html/substitute 
                       (module-summary-new-snip module))
  authz/authorization-sel (html/substitute (authz-edit/authz-snip module)))

(html/defsnippet image-new-snip image-new-template-html common/content-sel
  [module]
  identity)

(html/defsnippet deployment-new-snip deployment-new-template-html common/content-sel
  [module]
  identity)

(defmulti content-by-category-new
  (fn [module category]
    category))

(defmethod content-by-category-new "Image"
  [module category]
  common/content-sel (image-new-snip module))

(defmethod content-by-category-new "Deployment"
  [module category]
  common/content-sel (deployment-new-snip module))

(defmethod content-by-category-new "Project"
  [module category]
  common/content-sel (project-new-snip module))


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


;; javascript inclusion

(def js-scripts-default
  ["/external/jit/js/jit.js" 
   "/external/jit/js/excanvas.js" ])

(defmulti js-scripts
  (fn [type category]
    [type category]))

(defmethod js-scripts ["view" "Project"]
  [type category]
  (concat js-scripts-default ["/js/project-view.js"]))

(defmethod js-scripts ["edit" "Project"]
  [type category]
  (concat js-scripts-default ["/js/project-edit.js"]))

(defmethod js-scripts ["new" "Project"]
  [type category]
  (concat js-scripts-default ["/js/project-new.js"]))

;; Main function

(defn page [module type]
  (let
    [category (module-model/module-category module)]
    (base/base 
      {:js-scripts (js-scripts type category)
       :title (str "SlipStream™ | " (module-model/module-name module))
       :header (header-snip module)
       :content (content module type category)
       :footer (footer/footer-snip @version/slipstream-release-version)})))
