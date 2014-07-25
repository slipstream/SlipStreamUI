(ns slipstream.ui.views.singin
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]))

(def singin-template-filename (common/get-template "singin.html"))

(html/defsnippet header singin-template-filename base/header-sel
  []
  identity)

(html/defsnippet content singin-template-filename base/content-sel
  []
  identity)

(defn page [metadata type]
  (prn metadata)
  (base/base
    {:template singin-template-filename
     :title "Sign up"
     ; :alerts [:#alert-wrong-credentials 
     ;          {:type :info, :msg "This could be an interesting note."}]
     :header (header)
     :content (content)
     :type type
     }))
