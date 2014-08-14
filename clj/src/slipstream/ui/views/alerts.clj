(ns slipstream.ui.views.alerts
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.utils :as u]))

(def template-filename (common/get-template "alerts.html"))

(def alert-class-sel [:.alert])
(def dismiss-button-sel [:button])
(def title-sel [:.alert-title])
(def msg-sel [:.alert-msg])

(def alert-sel
  {:error   [:#alert-error]
   :warning [:#alert-warning]
   :success [:#alert-success]
   :info    [:#alert-info]})

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
  (prn pre-built-alert-id)
  (html/at (html/select alerts [pre-built-alert-id])
           [:> :div] (html/remove-attr :id)))

(defmethod alert :custom
  [{:keys [type title msg dismissible] :or {dismissible true type :info}}]
  (html/at (html/select alerts (alert-sel type))
           [:> :div] (html/remove-attr :id)
           dismiss-button-sel (when dismissible identity)
           title-sel (u/when-content title)
           msg-sel (html/content msg)))

(defn hidden-templates
  []
  (html/at (html/select alerts alert-class-sel)
           alert-class-sel (html/add-class "hidden")))
