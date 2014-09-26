(ns slipstream.ui.views.module.deployment
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defn middle-sections
  [module]
  {:title   (t :section.nodes.title)
   :content (-> module :nodes t/deployment-nodes-table)
   :selected? true})

(def actions
  [action/run
   action/edit
   action/copy
   action/publish])