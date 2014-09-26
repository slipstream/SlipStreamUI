(ns slipstream.ui.views.module.project
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defn middle-sections
  [module]
  {:title   (t :section.children.title)
   :content (-> module :children t/project-children-table)
   :selected? true})

(def actions
  [action/edit
   action/new-project
   action/new-image
   action/new-deployment
   action/import])