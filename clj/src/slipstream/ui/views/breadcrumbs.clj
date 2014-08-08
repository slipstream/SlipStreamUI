(ns slipstream.ui.views.breadcrumbs
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u :refer [defn-memo]]
            [slipstream.ui.views.util.icons :as icons]))

(def ^:private disabled-cls "disabled")

(def ^:private item-sel [[:.ss-breadcrumb-item (u/first-of-class "ss-breadcrumb-item")]])
(def ^:private anchor-sel [:a])
(def ^:private icon-sel (concat anchor-sel [:> :.ss-breadcrumb-item-icon]))
(def ^:private text-sel (concat anchor-sel [:> :.ss-breadcrumb-item-text]))

(def ^:private initial-breadcrumb
  {:icon icons/home :uri "/"})

(defn- make-last-inactive
  [breadcrumbs]
  (when (not-empty breadcrumbs)
    (conj (vec (butlast breadcrumbs))
          (-> breadcrumbs last (dissoc :uri)))))

(defn transform
  [{:keys [breadcrumbs] :as context}]
  (when breadcrumbs
    (let [breadcrumbs (cons initial-breadcrumb (make-last-inactive breadcrumbs))]
      (u/content-for item-sel [{:keys [text uri icon]} breadcrumbs]
                     u/this     (u/enable-class (empty? uri) disabled-cls)
                     anchor-sel (u/set-href uri)
                     icon-sel   (icons/set icon)
                     text-sel   (html/content (str text))))))
