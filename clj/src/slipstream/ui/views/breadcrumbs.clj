(ns slipstream.ui.views.breadcrumbs
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.breadcrumbs :as breadcrumbs]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]))

(def ^:private disabled-cls "disabled")

(def ^:private item-sel [[:.ss-breadcrumb-item (ue/first-of-class "ss-breadcrumb-item")]])
(def ^:private anchor-sel [:a])
(def ^:private icon-sel (concat anchor-sel [:> :.ss-breadcrumb-item-icon]))
(def ^:private text-sel (concat anchor-sel [:> :.ss-breadcrumb-item-text]))

(def ^:private initial-breadcrumb
  {:icon icons/home :uri "/"})

(defn transform
  [{:keys [resource-uri] :as context}]
  (when resource-uri
    (let [breadcrumbs (cons initial-breadcrumb (breadcrumbs/parse resource-uri))]
      (ue/content-for item-sel [{:keys [text uri icon]} breadcrumbs]
                     ue/this     (ue/enable-class (empty? uri) disabled-cls)
                     anchor-sel (ue/set-href uri)
                     icon-sel   (icons/set icon)
                     text-sel   (html/content (str text))))))
