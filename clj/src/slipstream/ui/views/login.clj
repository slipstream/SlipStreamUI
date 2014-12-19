(ns slipstream.ui.views.login
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.configuration :as configuration]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "login.html"))

(def ^:private header-title-sel         [:.ss-header-title])
(def ^:private header-subtitle-sel      [:.ss-header-subtitle])
(def ^:private header-last-line-sel     [:.ss-header-last-line])

(def ^:private create-account-form-sel  [:#create-account-form])
(def ^:private input-pick-username-sel  [:.ss-input-pick-username])
(def ^:private input-email-sel          [:.ss-input-email])
(def ^:private bnt-signup-sel           [:.ss-btn-signup])
(def ^:private terms-hint-sel           [:.ss-terms-hint])

(html/defsnippet header-snip template-filename base/header-sel
  [self-registration-enabled?]
  header-title-sel          (html/content       (t :header.title))
  header-subtitle-sel       (html/html-content  (t :header.subtitle))
  header-last-line-sel      (html/html-content  (t :header.last-line))
  create-account-form-sel   (ue/remove-if-not   self-registration-enabled?)
  input-pick-username-sel   (ue/set-placeholder (t :input.pick-username.placeholder))
  input-email-sel           (ue/set-placeholder (t :input.email.placeholder))
  bnt-signup-sel            (html/content       (t :signup.button.label))
  terms-hint-sel            (html/html-content  (t :signup.terms-hint)))

(html/defsnippet content-snip template-filename base/content-sel
  []
  identity)

(defn page
  [metadata]
  (base/generate
    {:template-filename template-filename
     :page-title (t :page-title)
     :header  (-> metadata
                  configuration/parse
                  configuration/self-registration-enabled?
                  header-snip)
     :content (content-snip)
     :type type
     :metadata metadata}))
