(ns slipstream.ui.views.error
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.user :as user-models]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.common :as common]))

(def error-template-html "slipstream/ui/views/error.html")

(html/defsnippet error-titles-snip header/header-template-html header/titles-sel
  [message code]
  header/titles-sel (html/substitute
                      (header/header-titles-snip
                        "<i style='color:red' class='icon-warning-sign'></i>Error"
                        message 
                        (str "Code: " code)
                        "Error")))

(html/defsnippet error-header-snip error-template-html header/header-sel
  [message code user]
  header/header-sel identity
  header/header-top-bar-sel (html/substitute (header/header-top-bar-snip (user-models/attrs user)))
  header/titles-sel (html/substitute (error-titles-snip message code)))

(defn page [message code user]
  (base/base 
    {:title (common/title "Error")
     :header (error-header-snip 
               message 
               code
               user)
     :content nil
     :footer (footer/footer-snip)}))
