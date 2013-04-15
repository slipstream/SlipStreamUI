(ns slipstream.ui.views.common
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]))

(def content-sel [:#content])

(def interaction-sel [:.interaction])

(def header-template-html "slipstream/ui/views/header.html")

(defn buttons [root edit?]
  (let
    [type (:tag root)
     super? (user/super? root)
     edit ["Save" "Cancel" "Delete"]]
    (case type
      :list ["New Project" "Import..."]
      :imageModule (if edit?
                     edit
                     ["Build" "Run" "Edit" "Copy..." (when super? "Publish")])
      :deploymentModule (if edit?
                          edit
                          ["Run" "Run..." "Edit" "Copy..." (when super? "Publish")])
      :projectModule (if edit?
                       edit
                       ["Edit" "New Project" "New Machine Image" "New Deployment"]))))

(html/defsnippet header-buttons header-template-html interaction-sel
  [{buttons :buttons}]
  [[:li (html/nth-of-type 1)]] (html/clone-for 
                                 [button buttons] 
                                 [:button] 
                                 (html/content button))
  [[:li html/last-of-type]] nil)

(def error-template-html "slipstream/ui/views/error.html")

(html/defsnippet error-snip error-template-html [:#messages]
  [message code]
  [:#error] (html/content (str "Error (" code "): " message)))
