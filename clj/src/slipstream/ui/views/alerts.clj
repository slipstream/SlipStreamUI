(ns slipstream.ui.views.alerts
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]))

(def alerts-template-filename (common/get-template "alerts.html"))

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
  (let [snip (html/snippet alerts-template-filename [:body] [] identity)]
    (snip)))

(defn when-content
  [content]
  (fn [match]
    (if (seq content)
      ((html/content content) match)
      (identity match))))

(defmulti alert type)

(defmethod alert clojure.lang.Keyword
  [pre-built-alert-id]
  (html/select alerts [pre-built-alert-id]))

(defmethod alert clojure.lang.PersistentArrayMap
  [{:keys [type title msg dismissible] :or {dismissible true}}]
  (html/at (html/select alerts (alert-sel type))
           dismiss-button-sel (when dismissible identity)
           title-sel (when-content title)
           msg-sel (html/content msg)))


