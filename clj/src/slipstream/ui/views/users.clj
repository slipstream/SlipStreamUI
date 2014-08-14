(ns slipstream.ui.views.users
  (:require [slipstream.ui.views.base :as base]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.users :as users]))

(defn page [metadata]
  (base/generate
    {:metadata metadata
     :header {:icon icons/users
              :title "Users"
              :subtitle "Configure the users in the SlipStream service."}
     :resource-uri "/users"
     :secondary-menu-actions [action/new-user]
     :content [{:title "Users"
                :content (t/users-table (users/parse metadata))}]}))
