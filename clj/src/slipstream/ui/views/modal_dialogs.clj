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
(def body-sel [:.modal-body])
(def body-text-sel [:.ss-dialog-body-text])
(def footnote-sel [:.ss-dialog-footnote])
(def first-button-sel [:.modal-footer [:button html/first-of-type]])
(def second-button-sel [:.modal-footer [:button (html/nth-of-type 2)]])
(def last-button-sel [:.modal-footer [:button html/last-of-type]])

(localization/with-prefixed-t :save-dialog
  (html/defsnippet ^:private save-dialog template-filename [:#ss-save-dialog]
    [resource-name]
    title-sel                   (html/content       (t :title resource-name))
    [:.modal-body :textarea]    (ue/set-placeholder (t :placeholder.commit-message))
    footnote-sel                (html/html-content  (t :footnote))
    first-button-sel            (html/content       (t :button.cancel))
    last-button-sel             (html/content       (t :button.save resource-name))))

(localization/with-prefixed-t :delete-dialog
  (html/defsnippet ^:private delete-dialog template-filename [:#ss-delete-dialog]
    [resource-name resource-id]
    title-sel                 (html/content       (t :title resource-name))
    body-sel                  (html/html-content  (t :question resource-name resource-id))
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
    body-sel                  (html/content (t :question))
    first-button-sel          (html/content (t :button.cancel))
    last-button-sel           (html/content (t :button.terminate))))

(localization/with-prefixed-t :publish-module-confirmation-dialog
  (html/defsnippet ^:private publish-module-confirmation-dialog template-filename [:#ss-publish-module-confirmation-dialog]
    [resource-name resource-id module-version]
    title-sel                 (html/content       (t :title resource-name))
    body-text-sel             (html/html-content  (t :question resource-name resource-id module-version))
    footnote-sel              (html/html-content  (t :footnote resource-name))
    [:code]                   (html/add-class     "text-primary")
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.publish resource-name))))

(localization/with-prefixed-t :unpublish-module-confirmation-dialog
  (html/defsnippet ^:private unpublish-module-confirmation-dialog template-filename [:#ss-unpublish-module-confirmation-dialog]
    [resource-name resource-id module-version]
    title-sel                 (html/content       (t :title resource-name))
    body-text-sel             (html/html-content  (t :question resource-name resource-id module-version))
    footnote-sel              (html/html-content  (t :footnote resource-name))
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.unpublish resource-name))))

(localization/with-prefixed-t :copy-module-dialog
  (html/defsnippet ^:private copy-module-dialog template-filename [:#ss-copy-module-dialog]
    [resource-name resource-id module-version]
    title-sel                 (html/content       (t :title resource-name))
    [:iframe]                 (ue/when-set-src (not-empty resource-id) (-> resource-id u/module-uri uc/trim-last-path-segment) "?chooser=true")
    [:#ss-module-copy-source-uri] (ue/set-value (-> resource-id u/module-uri (uc/trim-prefix "/") (str "/" module-version)))
    [:#ss-module-copy-target-name-label] (html/content (t :new-module-name.label resource-name))
    [:#ss-module-copy-target-name] (ue/set-value (-> resource-id uc/last-path-segment (str (t :new-module-name.default-suffix module-version))))
    [:#ss-module-copy-target-name] (ue/set-placeholder (t :new-module-name.placeholder))
    footnote-sel              (html/html-content  (t :footnote resource-name))
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.copy resource-name))))

(localization/with-prefixed-t :resource-name
  (defn- resource-name
    [{:keys [view-name parsed-metadata]}]
    (case view-name
      "module" (-> parsed-metadata :summary :category u/t-module-category s/lower-case)
      (-> view-name uc/keywordize t s/lower-case))))

(defn- module-version
  [{:keys [parsed-metadata]}]
  (-> parsed-metadata :summary :version))

(defn- resource-id
  [{:keys [view-name parsed-metadata]}]
  (case view-name
    "user"    (-> parsed-metadata :username)
    "module"  (-> parsed-metadata :summary :name)
    ""))

(defn- module-category?
  [{:keys [view-name parsed-metadata]} & categories]
  (and
    (= "module" view-name)
    (-> parsed-metadata :summary :category uc/keywordize hash-set (some categories))))

(defn- chooser-required?
  [context]
  (and
    (page-type/edit-or-new?)
    (module-category? context :image)))

(defn- publish-required?
  [context]
  (and
    (page-type/view?)
    (module-category? context :image :deployment)))

(def ^:private copy-required?
  publish-required?)

(defn- terminate-required?
  [{:keys [view-name]}]
  (= "run" view-name))

(defn required
  [context]
  (let [resource-name (resource-name context)
        resource-id (resource-id context)
        module-version (module-version context)]
    (cond-> []
      (page-type/edit?)               (conj (save-dialog resource-name)
                                            (delete-dialog resource-name resource-id))
      (chooser-required? context)     (conj (-> context :parsed-metadata :cloud-image-details :reference-image module-chooser-dialog))
      (terminate-required? context)   (conj (terminate-deployment-dialog))
      (publish-required? context)     (conj (publish-module-confirmation-dialog   resource-name resource-id module-version)
                                            (unpublish-module-confirmation-dialog resource-name resource-id module-version))
      (copy-required? context)        (conj (copy-module-dialog resource-name resource-id module-version)))))
