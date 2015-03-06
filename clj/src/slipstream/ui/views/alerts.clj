(ns slipstream.ui.views.alerts
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "alerts.html"))

(def alert-class-sel [:.alert])
(def dismiss-button-sel [:button])
(def title-sel [:.alert-title])
(def msg-sel [:.alert-msg])

(def alert-sel
  {:error   [:#alert-error]
   :warning [:#alert-warning]
   :success [:#alert-success]
   :info    [:#alert-info]})

(defn is-fixed?
  [alert]
  (= (:container alert) :fixed))

(def is-floating?
  (complement is-fixed?))

(def alerts
  "Static (computed) snippet with all alerts HTML templates.
  Since the templates don't change, we don't need to compute them at runtime."
  (let [snip (html/snippet template-filename [:body] [] identity)]
    (snip)))

(defmulti alert
  #(cond
     (keyword? %) :pre-defined
     (map? %) :custom))

(defmethod alert nil
  [_]
  nil)

(defmethod alert :pre-defined
  [pre-built-alert-id]
  (html/at (html/select alerts [pre-built-alert-id])
           [:> :div] (html/remove-attr :id)))

(defmethod alert :custom
  [{:keys [type title msg dismissible data-context] :or {dismissible (is-floating? alert) type :info} :as alert}]
  (html/at (html/select alerts (alert-sel type))
           [:> :div] (html/remove-attr :id)
           dismiss-button-sel (when dismissible identity)
           ue/this   (ue/when-set-data :context data-context)
           title-sel (ue/if-content (not-empty title)
                                    title
                                    (-> type name (str ".title") keyword t))
           msg-sel (html/html-content msg)))

(defn hidden-templates
  []
  (html/at (html/select alerts alert-class-sel)
           alert-class-sel (html/add-class "hidden")))
