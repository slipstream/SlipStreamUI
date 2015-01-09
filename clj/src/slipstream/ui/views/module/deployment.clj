(ns slipstream.ui.views.module.deployment
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(localization/with-prefixed-t :section.nodes
  (defn middle-sections
    [module]
    {:title   (t :title)
     :content (if-let [nodes (-> module :nodes not-empty)]
                (t/deployment-nodes-table nodes)
                (if (page-type/chooser?)
                    (t :empty-content-hint.on-chooser)
                    (t :empty-content-hint (-> module :summary :uri (u/module-uri :edit true)))))
     :selected? true}))

(def html-dependencies
  {:internal-js-filenames ["deployment.js"]})

(defn actions
  [module]
  [action/run
   action/edit
   action/copy
   (action/publish    :hidden? (-> module :summary :published?))
   (action/unpublish  :hidden? (-> module :summary :published? not))
   action/delete])

(def num-of-main-secondary-menu-actions
  2)