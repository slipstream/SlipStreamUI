(ns slipstream.ui.views.menubar
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
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
(def action-signup-sel          [:.ss-action-signup])
(def action-reset-password-sel  [:.ss-action-reset-password])
(def action-start-tour-sel      [:.ss-action-start-tour])
(def action-start-tour-action-sel [:.ss-action-start-tour :a])
(def action-label-sel           [:.ss-action-label])
(def action-documentation-sel   [:.ss-action-documentation])
(def action-knowledge-base-sel  [:.ss-action-knowledge-base])
(def menu-item-support-sel      [:.ss-menu-item-support])
(def action-support-link-sel    [:.ss-menu-item-support :a])
(def action-support-sel         [:.ss-action-support])
(def action-dashboard-sel       [:.ss-action-dashboard])
(def action-appstore-sel        [:.ss-action-appstore])
(def action-modules-sel         [:.ss-action-modules])
(def action-configuration-sel   [:.ss-action-configuration])
(def action-system-sel          [:.ss-action-system])
(def action-users-sel           [:.ss-action-users])
(def action-nuvlabox-sel        [:.ss-action-nuvlabox-admin])
(def action-help-sel            [:.ss-action-help])
(def action-events-sel          [:.ss-action-events])
(def action-usage-sel           [:.ss-action-usage])
(def action-logout-sel          [:.ss-action-logout])
(def action-profile-sel         [:.ss-action-profile])

(def active-menu-sel            [:.navbar-left :.active])

(def menubar-logged-in-sel (concat navbar-sel [:.ss-menubar-logged-in]))
(def super-user-item-sel [:.ss-menubar-super-user-item])
(def non-super-user-item-sel [:.ss-menubar-non-super-user-item])
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

(defn- configure-support-menu-item
  [context current-user-status]
  (localization/with-prefixed-t current-user-status
    (let [support-email (-> context :configuration configuration/support-email)]
      (when support-email
        (ue/at-match
          [:a] (ue/set-href (format "mailto:%s?subject=%s"
                              (uc/url-encode support-email)
                              (uc/url-encode (t :action.support.email.subject
                                                (current-user/username)))))
          action-support-sel  (html/content (t :action.support)))))))

(defmethod menubar :unlogged
  [context]
  (html/at menubar-unlogged-node
            input-username-sel        (ue/set-placeholder (t :unlogged.input.username.placeholder))
            input-password-sel        (ue/set-placeholder (t :unlogged.input.password.placeholder))
            action-login-sel          (html/content (t :unlogged.action.login))
            action-signup-sel         (html/content (t :unlogged.action.signup))
            action-reset-password-sel (html/content (t :unlogged.action.reset-password))
            action-documentation-sel  (html/content (t :unlogged.action.documentation))
            action-knowledge-base-sel (html/content (t :unlogged.action.knowledge-base))
            menu-item-support-sel     (configure-support-menu-item context :unlogged)))

(defn- current-active-menu-sel
  [context]
  (->> context
       :view-name
       (str ".ss-action-")
       keyword
       vector))

(defmethod menubar :logged-in
  [context]
  (ex/guard "set up logged-in menubar"
    (html/at menubar-logged-in-node
              super-user-item-sel         (ue/remove-if-not (current-user/super?))
              non-super-user-item-sel     (ue/remove-if     (current-user/super?))
              username-sel                (html/content (current-user/username))
              user-profile-anchor-sel     (ue/set-href (current-user/uri))
              action-dashboard-sel        (html/content (t :logged-in.action.dashboard))
              action-appstore-sel         (html/content (t :logged-in.action.appstore))
              action-modules-sel          (html/content (t :logged-in.action.modules))
              action-configuration-sel    (html/content (t :logged-in.action.configuration))
              action-system-sel           (html/content (t :logged-in.action.system))
              action-users-sel            (html/content (t :logged-in.action.users))
              action-nuvlabox-sel         (ue/remove-if-not (= (System/getProperty "slipstream.ui.util.ss-nuvlabox") "ss-nuvlabox"))
              action-help-sel             (html/content (t :logged-in.action.help))
              action-profile-sel          (html/content (t :logged-in.action.profile))
              active-menu-sel             (html/remove-class "active")
              (current-active-menu-sel context) (html/add-class "active")
              action-start-tour-sel       (ue/at-match
                                              action-label-sel (html/content (t :logged-in.action.start-tour))
                                              action-start-tour-action-sel (ue/toggle-href    (-> context :view-name #{"appstore"} not) "/appstore?start-tour=yes")
                                              action-start-tour-action-sel (ue/toggle-onclick (-> context :view-name #{"appstore"})     "SlipStream.util.tour.askToStart()"))
              action-documentation-sel    (html/content (t :logged-in.action.documentation))
              action-knowledge-base-sel   (html/content (t :logged-in.action.knowledge-base))
              menu-item-support-sel       (configure-support-menu-item context :logged-in)
              action-events-sel           (html/content (t :logged-in.action.events))
              action-usage-sel            (html/content (t :logged-in.action.usage))
              action-logout-sel           (html/content (t :logged-in.action.logout)))))
