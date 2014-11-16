(ns slipstream.ui.views.secondary-menu
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.current-user :as current-user]))

(def ^:private main-action-sel [:.ss-secondary-menu-main-action])
(def ^:private extra-actions-container-sel [:#ss-secondary-menu :> :div :> :ul])
(def ^:private extra-actions-toggle-sel [:.ss-secondary-extra-actions-toggle])
(def ^:private extra-action-sel [[:.ss-secondary-menu-extra-action (ue/first-of-class "ss-secondary-menu-extra-action")]])
(def ^:private action-icon-sel [[:span (html/nth-of-type 1)]])
(def ^:private action-name-sel [[:span html/last-of-type]])
(def ^:private extra-action-anchor-sel [:a])

(defn- setup-action
  [{:keys [icon name id hidden? super-only?] enabled? ::enabled? :or {enabled? true}}]
  (fn [action-node]
    (html/at action-node
             ue/this           (ue/set-id id)
             ue/this           (ue/enable-class hidden? "hidden")
             ue/this           (ue/enable-class (not enabled?) "disabled")
             ue/this           (ue/when-set-disabled-reason (and (not enabled?) super-only?) "ss-super-only-action")
             action-icon-sel   (icons/set icon)
             action-name-sel   (html/content (str name)))))

(defn- setup-extra-actions
  [extra-actions]
  (when extra-actions
    (ue/content-for extra-action-sel [extra-action extra-actions]
         extra-action-anchor-sel (setup-action extra-action))))

(defn- toggle-super-only-action
  "Enable ':super-only?' actions if user is ':super?'.
  Hard-coded ':disabled? true' value wins over ':super-only?' setting.
  Hard-coded ':disabled? false' value loses over ':super-only?' setting."
  [{:keys [disabled? super-only?] :as action}]
  (assoc action ::enabled? (if disabled?
                             false
                             (or (current-user/super?) (not super-only?)))))

(defn- call-action-fn
  "Actions defined in slipstream.ui.views.secondary-menu-actions are functions
  (in order to take into account the localization language). To make the code
  more readable, they can be included in the secondary-menu-actions vector in both
  called [(action/new) (action/import)] or uncalled forms [action/new action/import]."
  [map-or-fn]
  (if (map? map-or-fn)
    map-or-fn
    (map-or-fn)))

(defn transform
  [secondary-menu-actions]
  (let [actions (->> secondary-menu-actions
                     (map call-action-fn)
                     (map toggle-super-only-action)
                     not-empty)]
    (when actions
      (fn [match]
          (html/at match
                   main-action-sel              (setup-action (first actions))
                   extra-actions-toggle-sel     (ue/remove-if-not (next actions))
                   extra-actions-container-sel  (setup-extra-actions (next actions)))))))
