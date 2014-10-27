(ns slipstream.ui.views.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.module.image :as image]
            [slipstream.ui.views.module.project :as project]
            [slipstream.ui.views.module.deployment :as deployment]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.models.module :as module]
            ; [slipstream.ui.models.modules :as modules-model]
            ; [slipstream.ui.models.version :as version]
            ; [slipstream.ui.views.common :as common]
            ; [slipstream.ui.views.module-base :as module-base]
            ; [slipstream.ui.views.header :as header]
            ; [slipstream.ui.views.image :as image-legacy]
            ; [slipstream.ui.views.deployment :as deployment-legacy]
            ; [slipstream.ui.views.project :as project-legacy]
            ))

(localization/def-scoped-t)

(defn- header
  [summary]
  {:icon      (-> summary :category icons/icon-for)
   :title     (t :header.title (:category summary) (:short-name summary))
   :image     (-> summary :image)
   :subtitle  (t :header.subtitle (str (:version summary)
                  (when-let [desc (:description summary)]
                    (str " - " desc))))})

(defn- old-version-alert
  [latest-version?]
  (when-not latest-version?
    {:type :warning
     :msg (t :alert.old-version.msg)}))

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
    :project     project/actions
    :image       image/actions
    :deployment  deployment/actions))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn page
  [metadata]
  (let [module (module/parse metadata)
        summary (:summary module)]
    (base/generate
      {:metadata metadata
       :header (header summary)
       :alerts [(-> summary :latest-version? old-version-alert)]
       :resource-uri (:uri summary)
       :secondary-menu-actions (actions module)
       :content (sections module)})))
