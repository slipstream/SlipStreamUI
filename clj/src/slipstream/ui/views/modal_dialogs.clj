(ns slipstream.ui.views.modal-dialogs
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "modal_dialogs.html"))

(def title-sel [:.modal-title])
(def first-button-sel [:.modal-footer [:button html/first-of-type]])
(def second-button-sel [:.modal-footer [:button (html/nth-of-type 2)]])
(def last-button-sel [:.modal-footer [:button html/last-of-type]])

(localization/with-prefixed-t :save-dialog
  (html/defsnippet ^:private save-dialog template-filename [:#ss-save-dialog]
    [resource-name]
    title-sel                   (html/content       (t :title resource-name))
    [:.modal-body :textarea]    (ue/set-placeholder (t :placeholder.commit-message))
    [:.ss-save-dialog-footnote] (html/html-content  (t :footnote))
    first-button-sel            (html/content       (t :button.cancel))
    last-button-sel             (html/content       (t :button.save resource-name))))

(localization/with-prefixed-t :delete-dialog
  (html/defsnippet ^:private delete-dialog template-filename [:#ss-delete-dialog]
    [resource-name resource-id]
    title-sel                 (html/content       (t :title resource-name))
    [:.modal-body]            (html/html-content  (t :question resource-name resource-id))
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.delete resource-name))))

(localization/with-prefixed-t :module-chooser-dialog
  (html/defsnippet ^:private module-chooser-dialog template-filename [:#ss-module-chooser-dialog]
    [reference-image]
    title-sel                 (html/content (t :title))
    [:iframe]                 (ue/when-set-src (not-empty reference-image) (u/module-uri reference-image) "?chooser=true")
    first-button-sel          (html/content (t :button.cancel))
    second-button-sel         (html/content (t :button.select))
    last-button-sel           (html/content (t :button.select-exact-version))))

(localization/with-prefixed-t :terminate-deployment-dialog
  (html/defsnippet ^:private terminate-deployment-dialog template-filename [:#ss-terminate-deployment-dialog]
    []
    title-sel                 (html/content (t :title))
    [:.modal-body]            (html/content (t :question))
    first-button-sel          (html/content (t :button.cancel))
    last-button-sel           (html/content (t :button.terminate))))

(localization/with-prefixed-t :resource-name
  (defn- resource-name
    [{:keys [view-name parsed-metadata]}]
    (case view-name
      "module" (-> parsed-metadata :summary :category u/t-module-category s/lower-case)
      (-> view-name uc/keywordize t s/lower-case))))

(defn- resource-id
  [{:keys [view-name parsed-metadata]}]
  (case view-name
    "user"    (-> parsed-metadata :username)
    "module"  (-> parsed-metadata :summary :name)
    ""))

(defn- chooser-required?
  [{:keys [view-name parsed-metadata]}]
  (and
    (page-type/edit-or-new?)
    (= "module" view-name)
    (-> parsed-metadata :summary :category uc/keywordize (= :image))))

(defn- terminate-required?
  [{:keys [view-name]}]
  (= "run" view-name))

(defn required
  [context]
  (let [resource-name (resource-name context)
        resource-id (resource-id context)]
    (cond-> []
      (page-type/edit?)             (conj (save-dialog resource-name))
      (page-type/edit?)             (conj (delete-dialog resource-name resource-id))
      (chooser-required? context)   (conj (-> context :parsed-metadata :cloud-image-details :reference-image module-chooser-dialog))
      (terminate-required? context) (conj (terminate-deployment-dialog)))))
