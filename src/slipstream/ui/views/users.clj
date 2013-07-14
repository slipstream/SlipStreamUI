(ns slipstream.ui.views.users
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.base :as base]))

(def users-template-html "slipstream/ui/views/users.html")

(def users-sel [:#users])

(html/defsnippet header-snip header/header-template-html header/header-sel
  [users]
  header/header-summary-sel 
  (html/substitute 
    (header/header-titles-snip
      "Users"
      "Administer your users"
      "This page provides you with an overview of the activities of all users"
      "Users"))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (common-model/attrs users))))

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
                    [[:td (html/nth-of-type 4)]] (html/content (:state attrs))))

(html/defsnippet breadcrumb-snip common/breadcrumb-template-html common/breadcrumb-sel
  [name]
  [[:li (html/nth-of-type 2)]] (html/content "users")
  [[:li (html/nth-of-type 3)]] nil)

(html/defsnippet content-snip users-template-html common/content-sel
  [users]
  common/breadcrumb-sel (html/substitute
                          (breadcrumb-snip
                            (:name (common-model/attrs users))))
  
  [:#users] (html/substitute (users-snip users)))

;; javascript inclusion

(def js-scripts-default
  [])

(defn js-scripts
  []
  (concat js-scripts-default ["/js/users.js"]))

(defn page [users]
  (base/base 
    {:js-scripts (js-scripts)
     :title (common/title "Users")
     :header (header-snip users)
     :content (content-snip users)
     :footer (footer/footer-snip)}))
