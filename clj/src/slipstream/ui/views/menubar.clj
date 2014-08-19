(ns slipstream.ui.views.menubar
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.views.common :as common]))

(localization/def-scoped-t)

(def template-filename (common/get-template "menubar.html"))

(def navbar-sel [:.navbar])

(def menubar-unlogged-sel (concat navbar-sel [:.ss-menubar-unlogged]))
(def input-username-sel [:.ss-input-username])
(def input-password-sel [:.ss-input-password])

(def action-login-sel [:.ss-action-login])
(def action-reset-password-sel [:.ss-action-reset-password])
(def action-documentation-sel [:.ss-action-documentation])
(def action-contact-us-sel [:.ss-action-contact-us])
(def action-dashboard-sel [:.ss-action-dashboard])
(def action-configuration-sel [:.ss-action-configuration])
(def action-system-sel [:.ss-action-system])
(def action-users-sel [:.ss-action-users])
(def action-logout-sel [:.ss-action-logout])
(def action-profile-sel [:.ss-action-profile])

(def menubar-logged-in-sel (concat navbar-sel [:.ss-menubar-logged-in]))
(def super-user-item-sel [:.ss-menubar-super-user-item])
(def username-sel [:#ss-menubar-username])
(def user-profile-anchor-sel [:#ss-menubar-user-profile-anchor])

(def menubar-unlogged-node
  (let [snip (html/snippet template-filename menubar-unlogged-sel [] identity)]
    (snip)))

(def menubar-logged-in-node
  (let [snip (html/snippet template-filename menubar-logged-in-sel [] identity)]
    (snip)))

(defmulti menubar
  (fn [{:keys [type user]}]
    (cond
      (u/chooser? type)   :chooser
      (nil? user)         :unlogged
      (:logged-in? user)  :logged-in
      :else :un-logged)))

(defmethod menubar :chooser
  [_]
  nil)

(defmethod menubar :unlogged
  [_]
  (html/at menubar-unlogged-node
            input-username-sel  (ue/set-placeholder (t :unlogged.input.username.placeholder))
            input-password-sel  (ue/set-placeholder (t :unlogged.input.password.placeholder))
            action-login-sel          (html/content (t :unlogged.action.login))
            action-reset-password-sel (html/content (t :unlogged.action.reset-password))
            action-documentation-sel  (html/content (t :unlogged.action.documentation))
            action-contact-us-sel     (html/content (t :unlogged.action.contact-us))))

(defmethod menubar :logged-in
  [{:keys [user]}]
  (html/at menubar-logged-in-node
            super-user-item-sel       (ue/remove-if-not (:super? user))
            username-sel              (html/content (:username user))
            user-profile-anchor-sel   (ue/set-href (:uri user))
            action-dashboard-sel      (html/content (t :logged-in.action.dashboard))
            action-configuration-sel  (html/content (t :logged-in.action.configuration))
            action-system-sel         (html/content (t :logged-in.action.system))
            action-users-sel          (html/content (t :logged-in.action.users))
            action-profile-sel        (html/content (t :logged-in.action.profile))
            action-documentation-sel  (html/content (t :logged-in.action.documentation))
            action-contact-us-sel     (html/content (t :logged-in.action.contact-us))
            action-logout-sel         (html/content (t :logged-in.action.logout))))
