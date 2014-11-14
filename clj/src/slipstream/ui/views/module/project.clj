(ns slipstream.ui.views.module.project
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defn middle-sections
  [module]
  (when (page-type/view-or-chooser?)
    {:title   (t :section.children.title)
     :content (-> module :children t/project-children-table)
     :selected? true}))

(defn actions
  [module]
  [action/edit
   action/new-project
   action/new-image
   action/new-deployment
   action/import])