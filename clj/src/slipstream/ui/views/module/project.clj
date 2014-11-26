(ns slipstream.ui.views.module.project
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defn- new-submodules-hrefs
  [module]
  (let [pathname (-> module :summary :name (str "/new"))]
    (map #(u/module-uri pathname :hash "summary" :category %)
         ["Image" "Deployment" "Project"])))

(localization/with-prefixed-t :section.children
  (defn middle-sections
    [module]
    (when (page-type/view-or-chooser?)
      {:title   (t :title)
       :content (if-let [children (-> module :children not-empty)]
                  (t/project-children-table children)
                  (apply t :empty-content-hint (new-submodules-hrefs module)))
       :selected? true})))

(defn actions
  [module]
  [action/edit
   action/new-project
   action/new-image
   action/new-deployment
   action/import])