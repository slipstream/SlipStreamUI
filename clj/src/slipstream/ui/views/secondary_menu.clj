(ns slipstream.ui.views.secondary-menu
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]))

(def main-action-sel [:.ss-secondary-menu-main-action])
(def extra-action-sel [:.ss-secondary-menu-extra-action])
(def action-icon-sel [[:span (html/nth-of-type 1)]])
(def action-name-sel [[:span html/last-of-type]])
(def extra-action-anchor-sel [:a])

(defn- setup-action
  [{:keys [icon name uri] enabled? ::enabled? :or {enabled? true}}]
  (fn [action-node]
    (html/at action-node
             u/this           (u/when-set-onclick enabled? "window.location = '" uri "';")
             u/this           (u/when-add-class (not enabled?) "disabled")
             action-icon-sel  (u/set-icon icon)
             action-name-sel  (html/content (str name)))))

(defn- setup-extra-actions
  [actions]
  (html/clone-for [action actions]
    extra-action-anchor-sel   (setup-action action)))

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
               main-action-sel  (setup-action (first actions))
               extra-action-sel (setup-extra-actions (rest actions))))))
