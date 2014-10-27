(ns slipstream.ui.views.modal-dialogs
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
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
    []
    title-sel                 (html/content (t :title))
    [:.modal-body :textarea]  (ue/set-placeholder (t :placeholder.commit-message))
    first-button-sel          (html/content (t :button.cancel))
    last-button-sel           (html/content (t :button.save))))

(localization/with-prefixed-t :delete-dialog
  (html/defsnippet ^:private delete-dialog template-filename [:#ss-delete-dialog]
    []
    title-sel                 (html/content (t :title))
    [:.modal-body]            (html/content (t :question))
    first-button-sel          (html/content (t :button.cancel))
    last-button-sel           (html/content (t :button.delete))))

(localization/with-prefixed-t :module-chooser-dialog
  (html/defsnippet ^:private module-chooser-dialog template-filename [:#ss-module-chooser-dialog]
    []
    title-sel                 (html/content (t :title))
    first-button-sel          (html/content (t :button.cancel))
    second-button-sel         (html/content (t :button.save))
    last-button-sel           (html/content (t :button.save-exact-version))))

(localization/with-prefixed-t :terminate-deployment-dialog
  (html/defsnippet ^:private terminate-deployment-dialog template-filename [:#ss-terminate-deployment-dialog]
    []
    title-sel                 (html/content (t :title))
    [:.modal-body]            (html/content (t :question))
    first-button-sel          (html/content (t :button.cancel))
    last-button-sel           (html/content (t :button.terminate))))

(defn all
  []
  (concat
    (save-dialog)
    (delete-dialog)
    (module-chooser-dialog)
    (terminate-deployment-dialog)))
