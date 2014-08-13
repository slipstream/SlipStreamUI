(ns slipstream.ui.views.welcome
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.utils :as u :refer [defn-memo]]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.models.welcome :as mw]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.base :as base]))


(def template-filename (common/get-template "app_store.html"))

(def app-thumbnail-group-sel [:.ss-app-thumbnail-group])
(def app-thumbnail-sel [:.ss-app-thumbnail])
(def app-image-container-sel [:.ss-app-image-container])
(def app-image-preloader-sel (concat app-image-container-sel [:.ss-app-image-preloader]))
(def app-name-deploy-btn-sel [:.ss-app-deploy-btn])
(def app-name-container-sel [:.ss-app-name-container])
(def app-name-sel [:.ss-app-name])
(def app-version-sel [:.ss-app-version-number])
(def app-description-sel [:dd.ss-app-description])
(def app-publisher-sel [:dd.ss-app-publisher])
(def app-publication-date-sel [:dd.ss-app-publication-date])

(def app-updated-cls "ss-app-updated")
(def app-new-cls "ss-app-new")

(defn app-thumbnail-nodes
  [app-metadata-list]
  (html/clone-for [{:keys [name
                           uri
                           updated?
                           new?
                           version
                           description
                           publisher
                           publication-date
                           image]
                    :as app} app-metadata-list]
    app-image-preloader-sel  (u/set-src image)
    app-image-container-sel  (u/enable-class updated? app-updated-cls)
    app-image-container-sel  (u/enable-class new? app-new-cls)
    app-name-deploy-btn-sel  (u/set-onclick "location = '" uri "?showdialog=run-with-options-dialog'; return false;")
    app-name-container-sel   (u/set-href uri)
    app-name-sel             (html/content (str name))
    app-version-sel          (html/content version)
    app-description-sel      (html/content description)
    app-publisher-sel        (html/content publisher)
    app-publication-date-sel (html/content publication-date)))

(html/defsnippet app-thumbnails-snip template-filename app-thumbnail-group-sel
  [app-thumbnails]
  app-thumbnail-sel (app-thumbnail-nodes app-thumbnails))



(defn page [metadata type]
  (base/generate
    {:template-filename template-filename
     :page-title "Welcome"
     :header {:icon icons/home
              :title "Welcome to SlipStream"
              :subtitle "The welcome page provides you with all currently published
                         modules and root modules, including yours and the ones
                         shared with you."}
     ; :alerts [{:msg "aie" :title "Tada!"}]
     :secondary-menu-actions [action/new-project]
     :content [{:title "App Store"
                :content (app-thumbnails-snip (mw/published-apps metadata))
                :selected? true
                :type :default}
               {:title "Shared Projects"
                :content (t/shared-projects-table (mw/shared-projects metadata))}]
     :type type
     :metadata metadata
     }))
