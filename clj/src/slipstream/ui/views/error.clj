(ns slipstream.ui.views.error
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]))

(def error-template-filename (common/get-template "error.html"))

(def status-code-sel [:#status-code])
(def error-title-sel [:#error-title])
(def error-msg-sel [:#error-msg])

(html/defsnippet error-header-snip error-template-filename base/header-sel
  [message code user]
  status-code-sel (html/content (str code))
  error-msg-sel (html/html-content (str message)))

(defn page [message code user]
  (base/generate
    {:title "Error"
     :template error-template-filename
     :header (error-header-snip message code user)
     :content nil}))
