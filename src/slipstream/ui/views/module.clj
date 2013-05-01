(ns slipstream.ui.views.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.views.header :as header]))

(def edit ["Save" "Cancel" "Delete"])

(defmulti interaction 
  (fn [type super? edit?]
    type))

(defmethod interaction :list
  [type super? edit?]
    ["New Project" "Import..."])

(defmethod interaction :imageModule
  [type super? edit?]
  (if edit?
    edit
    ["Build" "Run" "Edit" "Copy..." (when super? "Publish")]))

(defmethod interaction :deploymentModule
  [type super? edit?]
  (if edit?
    edit
    ["Run" "Run..." "Edit" "Copy..." (when super? "Publish")]))

(defmethod interaction :projectModule
  [type super? edit?]
  (if edit?
    edit
    ["Run" "Run..." "Edit" "Copy..." (when super? "Publish")]))

(defn interaction-buttons [root edit?]
  (let
    [type (:tag root)
     super? (user/super? root)]
    (interaction type super? edit?)))

;(html/defsnippet interaction-snip common/interations-template-html common/interaction-sel
;  [root edit?]
;  common/interaction-sel (html/substitute
;                           (common/header-buttons
;                             {:buttons (interaction-buttons root edit?)})))
;
(html/defsnippet header-summary-snip header/header-template-html header/header-summary-sel
  [title sub-title comment]
  [:h2] (html/content title)
  [:h3] (html/content sub-title)
  [:p] (html/content comment))

(html/defsnippet header header/header-template-html header/header-sel
  [module]
  header/header-summary-sel (header-summary-snip (merge {:keys [:name :description]} module {:comment (module-model/module-comment module)})))


(def project-view-template-html "slipstream/ui/views/project-view.html")
(def project-edit-template-html "slipstream/ui/views/project-edit.html")

(html/defsnippet content-view project-view-template-html common/content-sel
  [module]
  identity)

(defn page [module edit?]
  (base/base 
    {:title (str "SlipStream™ | " (module-model/module-name module))
     :header (header module)
     :content (content-view module)
     :footer (footer/footer-snip @version/slipstream-release-version)}))

                 