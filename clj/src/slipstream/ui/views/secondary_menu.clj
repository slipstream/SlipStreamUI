(ns slipstream.ui.views.secondary-menu
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]))

(def ^:private disabled-cls "disabled")

(def ^:private main-action-sel [:.ss-secondary-menu-main-action])
(def ^:private extra-actions-container-sel [:#ss-secondary-menu :> :div :> :ul])
(def ^:private extra-action-sel [[:.ss-secondary-menu-extra-action (ue/first-of-class "ss-secondary-menu-extra-action")]])
(def ^:private action-icon-sel [[:span (html/nth-of-type 1)]])
(def ^:private action-name-sel [[:span html/last-of-type]])
(def ^:private extra-action-anchor-sel [:a])

(defn- setup-action
  [{:keys [icon name uri] enabled? ::enabled? :or {enabled? true}}]
  (fn [action-node]
    (html/at action-node
             ue/this           (ue/when-set-onclick enabled? "window.location = '" uri "';")
             ue/this           (ue/enable-class (not enabled?) disabled-cls)
             action-icon-sel  (icons/set icon)
             action-name-sel  (html/content (str name)))))

(defn- setup-extra-actions
  [actions]
  (ue/content-for extra-action-sel [action actions]
       extra-action-anchor-sel (setup-action action)))

(defn- toggle-super-only-action
  "Enable ':super-only?' actions if user is ':admin?'.
  Hard-coded ':disabled? true' value wins over ':super-only?' setting.
  Hard-coded ':disabled? false' value loses over ':super-only?' setting."
  [{:keys [super?] :as user} {:keys [disabled? super-only?] :as action}]
  (assoc action ::enabled? (if disabled? false (or super? (not super-only?)))))

(defn transform
  [{:keys [secondary-menu-actions user] :as context}]
  (let [actions (map (partial toggle-super-only-action user) secondary-menu-actions)]
    (fn [match]
      (html/at match
               main-action-sel              (setup-action (first actions))
               extra-actions-container-sel  (setup-extra-actions (rest actions))))))
