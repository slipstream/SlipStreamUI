(ns slipstream.ui.views.module
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.module.image :as image]
            [slipstream.ui.views.module.project :as project]
            [slipstream.ui.views.module.deployment :as deployment]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(localization/with-prefixed-t :header
  (defn- header
    [summary]
    (let [category-name (-> summary :category u/t-module-category)]
      {:icon      (-> summary :category icons/icon-for)
       :title     (if (page-type/new?)
                    (t :title.new category-name)
                    (:short-name summary))
       :image-url (-> summary :logo-url)
       :subtitle  (if (page-type/new?)
                    (t :subtitle.new (s/lower-case category-name))
                    (or
                      (-> summary :description not-empty)
                      category-name))
       :second-subtitle (when-not (page-type/new?)
                          (t :subtitle
                             (str (:version summary)
                                  (when-let [comment (-> summary :comment not-empty)]
                                    (str " - " comment)))))})))

(defn- old-version-alert
  [{:keys [category latest-version? uri]}]
  (when-not latest-version?
    {:type :warning
     :container :fixed
     :data-context {:help-hints {:breadcrumb-to-last-version  (t :help-hint.breadcrumb-to-last-version)
                                 :link-to-history             (t :help-hint.link-to-history)}}
     :msg (t :alert.old-version.msg
             (u/t-module-category category s/lower-case)
             (uc/trim-last-path-segment uri))}))

(defn- edit-published-alert
  [category-name version]
  {:type      :info
   :container :fixed
   :title     (t :alert.edit-published.title  category-name version)
   :msg       (t :alert.edit-published.msg    category-name version)})

(defn- view-published-alert
  [category-name version]
  {:type      :info
   :container :fixed
   :title     (t :alert.view-published.title  category-name version)
   :msg       (t :alert.view-published.msg    category-name version)})

(defn- view-chooser-published-alert
  [category-name version]
  {:type      :info
   :container :fixed
   :title     (t :alert.view-chooser-published.title  category-name version)
   :msg       (t :alert.view-chooser-published.msg    category-name version)})

(defn- published-alert
  [{:keys [category published? version]}]
  (when published?
    (let [category-name (u/t-module-category category s/lower-case)]
      (cond
        (page-type/edit?)     (edit-published-alert         category-name version)
        (page-type/chooser?)  (view-chooser-published-alert category-name version)
        :else                 (view-published-alert         category-name version)))))

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

(defn- user-has?
  [access-right module]
  (or
    (current-user/super?)
    (-> module :summary :owner (current-user/is?))
    (-> module :authorization :access-rights access-right :public-access?)
    (and
      (-> module :authorization :access-rights access-right :group-access?)
      (-> module :authorization :group-members (get (current-user/username))))))

(def ^:private action-types
  {:edit            {:access-right  :put
                     :menu-actions  #{action/edit}}
   :build-run       {:access-right  :post
                     :menu-actions  #{action/run
                                      action/build}}
   :create-children {:access-right  :create-children
                     :menu-actions  #{action/new-project
                                      action/new-image
                                      action/new-deployment}}
   :delete          {:access-right  :delete
                     :menu-actions  #{action/delete}}})

(defn- disable-actions
  [menu-actions action]
  (if (get menu-actions action)
    (action :disabled? true
            :disabled-reason (t :action-disabled-reason.wrong-acls))
    action))

(defn- toggle-action
  [actions action-type module]
  (let [{:keys [access-right menu-actions]} (action-types action-type)]
    (if (user-has? access-right module)
      actions
      (map (partial disable-actions menu-actions) actions))))

(defn- module-actions
  [module]
  (case (-> module :summary :category uc/keywordize)
    :project     (project/actions module)
    :image       (image/actions module)
    :deployment  (deployment/actions module)))

(defn- actions
  [module]
  (-> module
      module-actions
      (toggle-action :edit            module)
      (toggle-action :build-run       module)
      (toggle-action :create-children module)
      (toggle-action :delete          module)
      (toggle-action :delete-all      module)))

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
                (published-alert summary)]
       :resource-uri (:uri summary)
       :html-dependencies (merge-with (comp vec concat)
                            {:internal-js-filenames ["module.js"]}
                            (html-dependencies module))
       :secondary-menu-actions (actions module)
       :num-of-main-secondary-menu-actions (num-of-main-secondary-menu-actions module)
       :content (sections module)})))
