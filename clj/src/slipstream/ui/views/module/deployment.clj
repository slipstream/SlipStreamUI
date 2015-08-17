(ns slipstream.ui.views.module.deployment
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.pagination :as pagination]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defmulti middle-section-content (comp last vector))

; Section "nodes"

(localization/with-prefixed-t :section.nodes
  (defmethod middle-section-content :nodes
    [module section-metadata _]
    (if-let [nodes (not-empty section-metadata)]
      (t/deployment-nodes-table nodes)
      (if (page-type/chooser?)
          (t :empty-content-hint.on-chooser)
          (t :empty-content-hint (-> module :summary :uri (u/module-uri :edit true)))))))

; Section "runs"

(defmethod middle-section-content :runs
  [_ section-metadata _]
  (if-let [runs (-> section-metadata :runs not-empty)]
    (ue/dynamic-content-snip
      :id      "runs"
      :content-load-url (pagination/url :runs)
      :content (t/runs-table runs (:pagination section-metadata)))
    (t :section.runs.empty-content-hint)))

(defn- middle-section
  [module metadata-key]
  (let [section-title (localization/section-title metadata-key)
        section-metadata (get module metadata-key)]
    {:title   section-title
     :content (middle-section-content module section-metadata metadata-key)
     :selected? (= metadata-key :nodes)}))

(defn- visible-middle-sections
  []
  (cond-> [:nodes]
   (page-type/view?) (conj :runs)))

(defn middle-sections
  [module]
  (map (partial middle-section module) (visible-middle-sections)))

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