(ns slipstream.ui.views.action
  (:require [slipstream.ui.util.icons :as icons]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.action :as action-model]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.common :as common]))

(def action-template-html (common/get-template "action.html"))

(html/defsnippet header-snip action-template-html header/header-sel
  [message]
  header/header-titles-sel
    (html/substitute
      (header/header-titles-snip "Action Confirmation" message "" "Action")))

(defn page-legacy [metadata]
  (base/base
    {:title (common/title "Action Confirmation")
     :header (header-snip (action-model/parse metadata))
     :content nil}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn page
  [metadata]
  (base/generate
    {:metadata metadata
      :placeholder-page? true
      :header {:icon icons/action-ok
              :title "Action Confirmation"
              :subtitle "Your email address has been confirmed"}
      ; :resource-uri "/run/91aa79a"
      ; :secondary-menu-actions [action/terminate]
      :content nil}))
