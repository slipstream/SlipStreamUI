(ns slipstream.ui.views.users
  (:require [slipstream.ui.views.base :as base]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.users :as mu]))

(defn page [users]
  (base/generate
    {:metadata users
     :header {:icon icons/users
              :title "Users"
              :subtitle "Configure the users in the SlipStream service."}
     :breadcrumbs [{:text "Users"}]
     :secondary-menu-actions [action/new-user]
     :content [{:title "Users"
                :content (t/users-table (mu/users users))}]}))
