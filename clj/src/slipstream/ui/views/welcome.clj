(ns slipstream.ui.views.welcome
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.views.service-catalog :as service-catalog]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.models.welcome :as mw]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "app_store.html"))

(def app-thumbnail-group-sel [:.ss-app-thumbnail-group])
(def app-thumbnail-sel [:.ss-app-thumbnail])
(def app-image-container-sel [:.ss-app-image-container])
(def app-image-preloader-sel (concat app-image-container-sel [:.ss-image-preloader]))
(def app-name-deploy-btn-sel [:.ss-app-deploy-btn])
(def app-name-deploy-btn-label-sel [:.ss-app-deploy-btn-label])
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

(defn- suitable-for-tour?
  [app]
  (->> app :name (re-find #"(?i)wordpress")))

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
    ue/this                        (ue/when-add-class (suitable-for-tour? app) "ss-example-app-in-tour")
    app-image-preloader-sel        (ue/set-src image)
    app-image-container-sel        (ue/enable-class updated? app-updated-cls)
    app-image-container-sel        (ue/enable-class new? app-new-cls)
    app-name-deploy-btn-sel        (when (page-type/not-chooser?)
                                     (ue/set-onclick "location = '" (u/uri uri :action "run") "'; return false;"))
    app-name-deploy-btn-label-sel  (html/content (t :app.label.deploy))
    app-name-container-sel         (ue/set-href uri)
    app-name-sel                   (html/content (str name))
    app-version-sel                (html/content version)
    app-description-sel            (ue/when-content description)
    app-description-label-sel      (ue/when-content (not-empty description) (t :app.label.description))
    app-publisher-sel              (html/content publisher)
    app-publisher-label-sel        (html/content (t :app.label.publisher))
    app-publication-date-sel       (ue/when-content publication-date)
    app-publication-date-label-sel (ue/when-content (not-empty publication-date) (t :app.label.publication-date))))

(html/defsnippet app-thumbnails-snip template-filename app-thumbnail-group-sel
  [app-thumbnails]
  app-thumbnail-sel (app-thumbnail-nodes app-thumbnails))

(defn- app-store-section
  [welcome-metadata]
  {:title     (t :section.app-store.title)
   :content   (app-thumbnails-snip (:published-apps welcome-metadata))
   :selected? true
   :type      :default})

(defn- projects-section
  [welcome-metadata]
  {:title   (t :section.projects.title)
   :content (t/welcome-projects-table (:projects welcome-metadata))})

(defn- service-catalog-section
  [welcome-metadata]
  (when-let [service-catalog-items (-> welcome-metadata :service-catalog :items not-empty)]
    {:title   (t :section.service-catalog.title)
     :content (map service-catalog/item-section service-catalog-items)}))

(defn- sections
  [welcome-metadata]
  (cond-> []
    :always                   (conj (app-store-section       welcome-metadata))
    :always                   (conj (projects-section        welcome-metadata))
    (page-type/not-chooser?)  (conj (service-catalog-section welcome-metadata))))

(defn page
  [metadata]
  (let [welcome-metadata (mw/parse metadata)]
    (base/generate
        {:template-filename template-filename
         :page-title (t :page-title)
         :header {:icon icons/home
                  :title (t :header.title)
                  :subtitle (t :header.subtitle)}
         ; :alerts [{:msg "aie" :title "Tada!"}]
         :secondary-menu-actions [action/new-project]
         :content (sections welcome-metadata)
         :metadata metadata})))
