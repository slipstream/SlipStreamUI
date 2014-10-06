(ns slipstream.ui.views.modal-dialogs
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.views.common :as common]))

(localization/def-scoped-t)

(def template-filename (common/get-template "modal_dialogs.html"))

(html/defsnippet ^:private save-dialog template-filename [:#ss-save-dialog]
  []
  identity)

(html/defsnippet ^:private delete-dialog template-filename [:#ss-delete-dialog]
  []
  identity)

(html/defsnippet ^:private module-chooser-dialog template-filename [:#ss-module-chooser-dialog]
  []
  identity)

(defn all
  []
  (concat
    (save-dialog)
    (delete-dialog)
    (module-chooser-dialog)))

; (def navbar-sel [:.navbar])

; (def modal-dialogs-unlogged-sel (concat navbar-sel [:.ss-modal-dialogs-unlogged]))
; (def input-username-sel [:.ss-input-username])
; (def input-password-sel [:.ss-input-password])

; (def action-login-sel [:.ss-action-login])
; (def action-reset-password-sel [:.ss-action-reset-password])
; (def action-documentation-sel [:.ss-action-documentation])
; (def action-contact-us-sel [:.ss-action-contact-us])
; (def action-dashboard-sel [:.ss-action-dashboard])
; (def action-configuration-sel [:.ss-action-configuration])
; (def action-system-sel [:.ss-action-system])
; (def action-users-sel [:.ss-action-users])
; (def action-logout-sel [:.ss-action-logout])
; (def action-profile-sel [:.ss-action-profile])

; (def modal-dialogs-logged-in-sel (concat navbar-sel [:.ss-modal-dialogs-logged-in]))
; (def super-user-item-sel [:.ss-modal-dialogs-super-user-item])
; (def username-sel [:#ss-modal-dialogs-username])
; (def user-profile-anchor-sel [:#ss-modal-dialogs-user-profile-anchor])

; (def modal-dialogs-unlogged-node
;   (let [snip (html/snippet template-filename modal-dialogs-unlogged-sel [] identity)]
;     (snip)))

; (def modal-dialogs-logged-in-node
;   (let [snip (html/snippet template-filename modal-dialogs-logged-in-sel [] identity)]
;     (snip)))

; (defmulti modal-dialogs
;   (fn [{:keys [type user]}]
;     (cond
;       (page-type/chooser?)  :chooser
;       (nil? user)           :unlogged
;       (:logged-in? user)    :logged-in
;       :else :un-logged)))

; (defmethod modal-dialogs :chooser
;   [_]
;   nil)

; (defmethod modal-dialogs :unlogged
;   [_]
;   (html/at modal-dialogs-unlogged-node
;             input-username-sel  (ue/set-placeholder (t :unlogged.input.username.placeholder))
;             input-password-sel  (ue/set-placeholder (t :unlogged.input.password.placeholder))
;             action-login-sel          (html/content (t :unlogged.action.login))
;             action-reset-password-sel (html/content (t :unlogged.action.reset-password))
;             action-documentation-sel  (html/content (t :unlogged.action.documentation))
;             action-contact-us-sel     (html/content (t :unlogged.action.contact-us))))

; (defmethod modal-dialogs :logged-in
;   [{:keys [user]}]
;   (html/at modal-dialogs-logged-in-node
;             super-user-item-sel       (ue/remove-if-not (:super? user))
;             username-sel              (html/content (:username user))
;             user-profile-anchor-sel   (ue/set-href (:uri user))
;             action-dashboard-sel      (html/content (t :logged-in.action.dashboard))
;             action-configuration-sel  (html/content (t :logged-in.action.configuration))
;             action-system-sel         (html/content (t :logged-in.action.system))
;             action-users-sel          (html/content (t :logged-in.action.users))
;             action-profile-sel        (html/content (t :logged-in.action.profile))
;             action-documentation-sel  (html/content (t :logged-in.action.documentation))
;             action-contact-us-sel     (html/content (t :logged-in.action.contact-us))
;             action-logout-sel         (html/content (t :logged-in.action.logout))))
