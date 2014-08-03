(ns slipstream.ui.views.secondary-menu
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]))

(def main-action-sel [:.ss-secondary-menu-main-action])
(def extra-action-sel [:.ss-secondary-menu-extra-action])
(def action-icon-sel [[:span (html/nth-of-type 1)]])
(def action-name-sel [[:span html/last-of-type]])
(def extra-action-anchor-sel [:a])

(defn- setup-action
  [{:keys [icon name uri enabled?] :or {enabled? true}}]
  (fn [action-node]
    (html/at action-node
             u/this           (u/when-set-onclick enabled? "window.location = '" uri "';")
             u/this           (u/when-add-class (not enabled?) "disabled")
             action-icon-sel  (u/set-icon icon)
             action-name-sel  (html/content (str name)))))

(defn setup-extra-actions
  [actions]
  (html/clone-for [action actions]
    extra-action-anchor-sel   (setup-action action)))

(defn transform
  [actions]
  (fn [match]
    (html/at match
             main-action-sel  (setup-action (first actions))
             extra-action-sel (setup-extra-actions (rest actions)))))
