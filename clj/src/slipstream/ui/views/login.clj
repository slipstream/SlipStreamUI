(ns slipstream.ui.views.login
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]))

(def template-filename (common/get-template "login.html"))

(html/defsnippet header-snip template-filename base/header-sel
  []
  identity)

(html/defsnippet content-snip template-filename base/content-sel
  []
  identity)

(defn page [metadata type]
  (base/generate
    {:template-filename template-filename
     :page-title "Sign up"
     ; :alerts [:#alert-wrong-credentials 
     ;          {:type :info, :msg "This could be an interesting note."}]
     :header (header-snip)
     :content (content-snip)
     :type type
     :metadata metadata
     }))
