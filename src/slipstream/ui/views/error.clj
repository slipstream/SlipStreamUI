(ns slipstream.ui.views.error
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.user :as user-models]
            [slipstream.ui.views.header :as header]))

(def error-template-html "slipstream/ui/views/error.html")

(html/defsnippet error-titles-snip header/header-template-html header/titles-sel
  [message code]
  header/titles-sel (html/substitute
                      (apply header/header-titles-snip
                        {:title "<i style='color:red' class='icon-warning-sign'></i>Error"
                         :title-sub message 
                         :title-desc (str "Code: " code)
                         :category "Error"})))

(html/defsnippet error-header-snip error-template-html header/header-sel
  [message code & user]
  header/header-sel identity
  header/header-top-bar-sel (html/substitute (header/header-top-bar-snip (user-models/attrs user)))
  header/titles-sel (html/substitute (error-titles-snip message code)))

(defn page [message code & user]
  (base/base 
    {:title "Error"
     :header (error-header-snip 
               "Oops... there was an error :-(" 
               123 
               user)
     :content nil
     :footer (footer/footer-snip @version/slipstream-release-version)}))
