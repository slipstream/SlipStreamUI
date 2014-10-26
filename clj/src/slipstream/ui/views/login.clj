(ns slipstream.ui.views.login
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]))

(localization/def-scoped-t)

(def template-filename (common/get-template "login.html"))

(def ^:private header-title-sel     [:.ss-header-title])
(def ^:private header-subtitle-sel  [:.ss-header-subtitle])
(def ^:private header-last-line-sel [:.ss-header-last-line])

(def ^:private input-pick-username-sel  [:.ss-input-pick-username])
(def ^:private input-email-sel          [:.ss-input-email])
(def ^:private bnt-signup-sel           [:.ss-btn-signup])
(def ^:private terms-hint-sel           [:.ss-terms-hint])

(html/defsnippet header-snip template-filename base/header-sel
  []
  header-title-sel          (html/content       (t :header.title))
  header-subtitle-sel       (html/html-content  (t :header.subtitle))
  header-last-line-sel      (html/html-content  (t :header.last-line))
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
     :page-title "Sign up"
     :header (header-snip)
     :content (content-snip)
     :type type
     :metadata metadata}))
