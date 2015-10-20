(ns slipstream.ui.views.users
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.models.users :as users]))

(localization/def-scoped-t)

(defn page
  [metadata]
  (base/generate
    {:header {:icon icons/users
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :secondary-menu-actions [action/new-user action/export-users]
     :num-of-main-secondary-menu-actions 1
     :content [{:title (t :content.title)
                :content (t/users-table (users/parse metadata))}]}))
