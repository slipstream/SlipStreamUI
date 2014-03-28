(ns slipstream.ui.views.user
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

(def user-view-template-html "slipstream/ui/views/user-view.html")
(def user-edit-template-html "slipstream/ui/views/user-edit.html")
(def user-new-template-html "slipstream/ui/views/user-new.html")

(def summary-sel [:#user-summary])
(def parameters-sel [:#parameters])

;; View

(html/defsnippet header-snip header/header-template-html header/header-sel
  [user]
  header/header-summary-sel 
  (html/substitute 
    (let [attrs (common-model/attrs user)
            firstname (:firstname attrs)
            lastname (:lastname attrs)]
      (header/header-titles-snip
        (str firstname " " lastname)
        (if (true? (user-model/super? user))
        "Privileged User"
        "Regular User")
        (str "Status: " (string/lower-case (:state attrs)))
        "User")))
  header/header-top-bar-sel
  (html/substitute
    (header/header-top-bar-snip
      (common-model/attrs (user-model/logged-in user)))))

(defn parameters-view-snip
  [parameters-by-category]
  (for [grouped (common-model/sort-map-vals-by-name parameters-by-category)]
    (list
      (html/html-snippet (str "\n    <h3>" (string/replace (key grouped) #"_" " ") "</h3>"))
      (common/parameters-view-snip (val grouped) nil))))

(defn parameters-edit-snip
  [parameters-by-category]
  (for [grouped (common-model/sort-map-vals-by-name parameters-by-category)]
    (list 
      (html/html-snippet (str "\n    <h3>" (string/replace (key grouped) #"_" " ") "</h3>"))
      (common/parameters-edit-snip (val grouped) false))))

(html/defsnippet breadcrumb-snip user-view-template-html common/breadcrumb-sel
  [name super?]
  [[:li (html/nth-of-type 2)]] (if super?
                                 identity
                                 (html/content "users"))
  [[:li (html/nth-of-type 3)]] (common/clone-breadcrumbs name "user"))

(html/defsnippet summary-view-snip user-view-template-html summary-sel
  [user]
  [:#username] (html/content (:name (common-model/attrs user)))
  [:#firstname] (html/content (:firstname (common-model/attrs user)))
  [:#lastname] (html/content (:lastname (common-model/attrs user)))
  [:#email] (html/content (:email (common-model/attrs user)))
  [:#organization] (html/content (:organization (common-model/attrs user)))
  [:#issuper :> :td :> :input] (if (user-model/super? user)
                                 (html/set-attr :checked "checked")
                                 (html/remove-attr :checked))
  [:#created] (html/content (:creation (common-model/attrs user)))
  [:#state] (html/content (:state (common-model/attrs user))))

(html/defsnippet summary-edit-snip user-edit-template-html summary-sel
  [user]
  [:#name :> :span] (html/content (:name (common-model/attrs user)))
  [:#name :> :input] (html/set-attr :value (:name (common-model/attrs user)))
  [:#username] (html/content (:name (common-model/attrs user)))
  [:#firstname] (html/set-attr :value (:firstname (common-model/attrs user)))
  [:#lastname] (html/set-attr :value (:lastname (common-model/attrs user)))
  [:#email] (html/set-attr :value (:email (common-model/attrs user)))
  [:#organization] (html/set-attr :value (:organization (common-model/attrs user)))
  [:#issuper] (if (user-model/super? user)
                (html/set-attr :checked "checked")
                (do 
                  (html/remove-attr :checked)
                  (html/set-attr :disabled "disabled"))))

(html/defsnippet content-view-snip user-view-template-html common/content-sel
  [user]
  common/breadcrumb-sel (html/substitute
                          (breadcrumb-snip
                            (:name (common-model/attrs user))
                            (user-model/super? user)))

  summary-sel (html/substitute
                (summary-view-snip user))

  summary-sel (html/after
                (parameters-view-snip
                  (common-model/group-by-category (common-model/parameters user))))
  
  [:#parameters-header] nil
  [:#parameters] nil)

(html/defsnippet content-edit-snip user-edit-template-html common/content-sel
  [user]
  common/breadcrumb-sel (html/substitute
                          (breadcrumb-snip
                            (:name (common-model/attrs user))
                            (user-model/super? user)))

  summary-sel (html/substitute
                (summary-edit-snip user))

  summary-sel (html/after
                (parameters-edit-snip (common-model/group-by-category (common-model/parameters user))))
  
  [:#parameters-header] nil
  [:#parameters] nil)

(html/defsnippet content-new-snip user-new-template-html common/content-sel
  [user]
  common/breadcrumb-sel (html/substitute
                          (breadcrumb-snip
                            (:name (common-model/attrs user))
                            (user-model/super? user)))

  summary-sel (html/after
                (parameters-edit-snip (common-model/group-by-category (common-model/parameters user))))
  
  [:#parameters-header] nil
  [:#parameters] nil)


;; Dispatch function

(defmulti content
  "Multi method dispatched on type: view, edit"
  (fn [user type]
    type))

(defmethod content "view" 
  [user type]
  (content-view-snip user))

(defmethod content "edit" 
  [user type]
  (content-edit-snip user))

(defmethod content "new" 
  [user type]
  (content-new-snip user))


;; javascript inclusion

(def js-scripts-default
  [])

(defmulti js-scripts
  (fn [type]
    [type]))

(defmethod js-scripts ["view"]
  [type]
  (concat js-scripts-default ["/js/user-view.js"]))

(defmethod js-scripts ["edit"]
  [type]
  (concat js-scripts-default ["/js/user-edit.js"]))

(defmethod js-scripts ["new"]
  [type]
  (concat js-scripts-default ["/js/user-new.js"]))


(defn page [user type]
  (base/base 
    {:js-scripts (js-scripts type)
     :title (common/title (common-model/elem-name user))
     :header (header-snip  user)
     :content (content user type)
     :footer (footer/footer-snip)}))
