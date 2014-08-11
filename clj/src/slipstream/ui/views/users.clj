(ns slipstream.ui.views.users
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.users :as mu]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header]
                        [slipstream.ui.views.base :as base]))

(def users-template-html (common/get-template "users.html"))

(def users-sel [:#users])

(html/defsnippet header-snip header/header-template-html header/header-sel
  [users]
  header/header-summary-sel 
  (html/substitute 
    (header/header-titles-snip
      "Users"
      "Administer your users"
      "This page provides you with an overview of the activities of all users"
      "users"))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user-model/attrs users))))

(html/defsnippet users-snip users-template-html [:#users]
  [users]
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr (html/nth-of-type 1)]]
                  (html/clone-for
                    [user (modules-model/children users)
                     :let [attrs (module-model/attrs user)]]
                    [[:a]] (html/do->
                             (html/set-attr :href (:resourceuri attrs))
                             (html/content (:name attrs)))
                    [[:td (html/nth-of-type 2)]] (html/content (:firstname attrs))
                    [[:td (html/nth-of-type 3)]] (html/content (:lastname attrs))
                    [[:td (html/nth-of-type 4)]] (html/content (:state attrs))
                    [[:td (html/nth-of-type 5)] :> :i] (if (common-model/true-value? (:online attrs))
                                                         (html/add-class "online")
                                                         (html/remove-class "online"))))

(html/defsnippet breadcrumb-snip common/breadcrumb-template-html common/breadcrumb-sel
  [name]
  [[:li (html/nth-of-type 2)]] (html/content "users")
  [[:li (html/nth-of-type 3)]] nil)

(html/defsnippet content-snip users-template-html common/content-sel
  [users]
  [:#users] (html/substitute (users-snip users)))

;; javascript inclusion

(def js-scripts-default
  [])

(defn js-scripts
  []
  (concat js-scripts-default ["/js/users.js"]))

(defn page-legacy [users]
  (base/base 
    {:js-scripts (js-scripts)
     :title (common/title "Users")
     :header (header-snip users)
     :content (content-snip users)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn page [users]
  (base/generate
    {:metadata users
     :header {:icon icons/users
              :title "Users"
              :subtitle "Configure the users in the SlipStream service."}
     :breadcrumbs [{:text "Users"}]
     :secondary-menu-actions [action/new-user]
     :content [{:title "Users"
                :selected? true
                :content (t/users-table (mu/users users))}]}))

;; TODO: Make sections non-collapsible
;; TODO: Remove config menu for non-admins
