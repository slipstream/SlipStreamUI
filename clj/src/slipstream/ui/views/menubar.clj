(ns slipstream.ui.views.menubar
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.exception :as ex]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.configuration :as configuration]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "menubar.html"))

(def navbar-sel                 [:.navbar])

(def menubar-unlogged-sel (concat navbar-sel [:.ss-menubar-unlogged]))
(def input-username-sel         [:.ss-input-username])
(def input-password-sel         [:.ss-input-password])

(def action-login-sel           [:.ss-action-login])
(def action-reset-password-sel  [:.ss-action-reset-password])
(def action-start-tour-sel      [:.ss-action-start-tour])
(def action-start-tour-action-sel [:.ss-action-start-tour :a])
(def action-label-sel           [:.ss-action-label])
(def action-documentation-sel   [:.ss-action-documentation])
(def action-knowledge-base-sel  [:.ss-action-knowledge-base])
(def action-contact-us-sel      [:.ss-action-contact-us])
(def action-dashboard-sel       [:.ss-action-dashboard])
(def action-configuration-sel   [:.ss-action-configuration])
(def action-service-catalog-menu-item-sel [:.ss-action-service-catalog-menu-item])
(def action-service-catalog-sel [:.ss-action-service-catalog])
(def action-system-sel          [:.ss-action-system])
(def action-users-sel           [:.ss-action-users])
(def action-help-sel            [:.ss-action-help])
(def action-events-sel          [:.ss-action-events])
(def action-logout-sel          [:.ss-action-logout])
(def action-profile-sel         [:.ss-action-profile])

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
  (fn [_]
    (cond
      (page-type/chooser?)      :chooser
      (current-user/logged-in?) :logged-in
      :else :unlogged)))

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
            action-knowledge-base-sel (html/content (t :unlogged.action.knowledge-base))
            action-contact-us-sel     (html/content (t :unlogged.action.contact-us))))

(defmethod menubar :logged-in
  [context]
  (ex/guard "set up logged-in menubar"
    (html/at menubar-logged-in-node
              super-user-item-sel         (ue/remove-if-not (current-user/super?))
              username-sel                (html/content (current-user/username))
              user-profile-anchor-sel     (ue/set-href (current-user/uri))
              action-dashboard-sel        (html/content (t :logged-in.action.dashboard))
              action-configuration-sel    (html/content (t :logged-in.action.configuration))
              action-system-sel           (html/content (t :logged-in.action.system))
              action-service-catalog-menu-item-sel (ue/remove-if-not (-> context :configuration configuration/service-catalog-enabled?))
              action-service-catalog-sel  (html/content (t :logged-in.action.service-catalog))
              action-users-sel            (html/content (t :logged-in.action.users))
              action-help-sel             (html/content (t :logged-in.action.help))
              action-profile-sel          (html/content (t :logged-in.action.profile))
              action-start-tour-sel       (ue/at-match
                                              action-label-sel (html/content (t :logged-in.action.start-tour))
                                              action-start-tour-action-sel (ue/toggle-href    (-> context :view-name #{"welcome"} not) "?start-tour=yes")
                                              action-start-tour-action-sel (ue/toggle-onclick (-> context :view-name #{"welcome"})     "SlipStream.util.tour.askToStart()"))
              action-documentation-sel    (html/content (t :logged-in.action.documentation))
              action-knowledge-base-sel   (html/content (t :logged-in.action.knowledge-base))
              action-contact-us-sel       (html/content (t :logged-in.action.contact-us))
              action-events-sel           (html/content (t :logged-in.action.events))
              action-logout-sel           (html/content (t :logged-in.action.logout)))))
