(ns slipstream.ui.views.action
  (:require [net.cgrand.enlive-html :as html]
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

(defn page [metadata]
  (base/base 
    {:title (common/title "Action Confirmation")
     :header (header-snip (action-model/message metadata))
     :content nil}))
