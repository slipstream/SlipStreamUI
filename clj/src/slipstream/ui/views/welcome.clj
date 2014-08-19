(ns slipstream.ui.views.welcome
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.models.welcome :as mw]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(def template-filename (common/get-template "app_store.html"))

(def app-thumbnail-group-sel [:.ss-app-thumbnail-group])
(def app-thumbnail-sel [:.ss-app-thumbnail])
(def app-image-container-sel [:.ss-app-image-container])
(def app-image-preloader-sel (concat app-image-container-sel [:.ss-image-preloader]))
(def app-name-deploy-btn-sel [:.ss-app-deploy-btn])
(def app-name-container-sel [:.ss-app-name-container])
(def app-name-sel [:.ss-app-name])
(def app-version-sel [:.ss-app-version-number])
(def app-description-label-sel [:dt.ss-app-description])
(def app-description-sel [:dd.ss-app-description])
(def app-publisher-label-sel [:dt.ss-app-publisher])
(def app-publisher-sel [:dd.ss-app-publisher])
(def app-publication-date-label-sel [:dt.ss-app-publication-date])
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
    app-image-preloader-sel        (ue/set-src image)
    app-image-container-sel        (ue/enable-class updated? app-updated-cls)
    app-image-container-sel        (ue/enable-class new? app-new-cls)
    app-name-deploy-btn-sel        (html/content (t :app.label.deploy))
    app-name-deploy-btn-sel        (ue/set-onclick "location = '" uri "?showdialog=run-with-options-dialog'; return false;")
    app-name-container-sel         (ue/set-href uri)
    app-name-sel                   (html/content (str name))
    app-version-sel                (html/content version)
    app-description-sel            (html/content description)
    app-description-label-sel      (html/content (t :app.label.description))
    app-publisher-sel              (html/content publisher)
    app-publisher-label-sel        (html/content (t :app.label.publisher))
    app-publication-date-sel       (html/content publication-date)
    app-publication-date-label-sel (html/content (t :app.label.publication-date))))

(html/defsnippet app-thumbnails-snip template-filename app-thumbnail-group-sel
  [app-thumbnails]
  app-thumbnail-sel (app-thumbnail-nodes app-thumbnails))

(defn page [metadata type]
  (localization/with-lang-from-metadata
    (base/generate
      {:template-filename template-filename
       :page-title (t :page-title)
       :header {:icon icons/home
                :title (t :header.title)
                :subtitle (t :header.subtitle)}
       ; :alerts [{:msg "aie" :title "Tada!"}]
       :secondary-menu-actions [action/new-project]
       :content [{:title (t :section.app-store.title)
                  :content (app-thumbnails-snip (mw/published-apps metadata))
                  :selected? true
                  :type :default}
                 {:title (t :section.shared-projects.title)
                  :content (t/shared-projects-table (mw/shared-projects metadata))}]
       :type type
       :metadata metadata
       })))
