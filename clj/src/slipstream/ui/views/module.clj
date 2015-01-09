(ns slipstream.ui.views.module
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.module.image :as image]
            [slipstream.ui.views.module.project :as project]
            [slipstream.ui.views.module.deployment :as deployment]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(localization/with-prefixed-t :header
  (defn- header
    [summary]
    {:icon      (-> summary :category icons/icon-for)
     :title     (if (page-type/new?)
                  (t :title.new (:category summary))
                  (:short-name summary))
     :image-url (-> summary :logo-url)
     :subtitle  (if (page-type/new?)
                  (t :subtitle.new (-> summary :category s/lower-case))
                  (-> summary :description not-empty))
     :second-subtitle (if (page-type/new?)
                        (str "")
                        (t :subtitle
                           (str (:version summary)
                                (when-let [comment (-> summary :comment not-empty)]
                                  (str " - " comment)))))}))

(defn- old-version-alert
  [{:keys [category latest-version?]}]
  (when-not latest-version?
    {:type :warning
     :msg (t :alert.old-version.msg (u/t-module-category category s/lower-case))}))

(defn- edit-published-alert
  [{:keys [category published?]}]
  (when (and (page-type/edit?) published?)
    (let [category-name (u/t-module-category category s/lower-case)]
      {:type  :info
       :title (t :alert.edit-published.title  category-name)
       :msg   (t :alert.edit-published.msg    category-name)})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti html-dependencies (comp uc/keywordize :category :summary))

(defmethod html-dependencies :default
  [module]
  nil)

(defmethod html-dependencies :deployment
  [module]
  deployment/html-dependencies)

(defmethod html-dependencies :image
  [module]
  image/html-dependencies)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti middle-sections (comp uc/keywordize :category :summary))

(defmethod middle-sections :project
  [module]
  (project/middle-sections module))

(defmethod middle-sections :image
  [module]
  (image/middle-sections module))

(defmethod middle-sections :deployment
  [module]
  (deployment/middle-sections module))

(defn- sections
  [module]
  (let [summary-section {:title (t :section.summary.title)
                         :content (t/module-summary-table (:summary module))}
        auth-section    {:title (t :section.authorizations.title)
                         :content [(t/access-rights-table module)
                                   (t/group-members-table (-> module :authorization))]}]
    (-> [summary-section (middle-sections module) auth-section]
        flatten
        vec)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- actions
  [module]
  (case (-> module :summary :category uc/keywordize)
    :project     (project/actions module)
    :image       (image/actions module)
    :deployment  (deployment/actions module)))

(defn- num-of-main-secondary-menu-actions
  [module]
  (case (-> module :summary :category uc/keywordize)
    :project     nil
    :image       nil
    :deployment  (when (page-type/view?)
                   deployment/num-of-main-secondary-menu-actions)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn page
  [metadata]
  (let [module (module/parse metadata)
        summary (:summary module)]
    (base/generate
      {:parsed-metadata module
       :header (header summary)
       :alerts [(old-version-alert summary)
                (edit-published-alert summary)]
       :resource-uri (:uri summary)
       :html-dependencies (merge-with (comp vec concat)
                            {:internal-js-filenames ["module.js"]}
                            (html-dependencies module))
       :secondary-menu-actions (actions module)
       :num-of-main-secondary-menu-actions (num-of-main-secondary-menu-actions module)
       :content (sections module)})))
