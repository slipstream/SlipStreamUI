;(ns slipstream.ui.views.project
;  (:require [net.cgrand.enlive-html :as html]
;            [slipstream.ui.models.authz :as authz]
;            [slipstream.ui.models.modules :as modules]
;            [slipstream.ui.models.module :as module]
;            [slipstream.ui.views.module :as views-module]
;            [slipstream.ui.views.header :as header]
;            [slipstream.ui.views.footer :as footer]
;            [slipstream.ui.views.base :as base]
;            [slipstream.ui.models.version :as version]
;            [slipstream.ui.views.common :as common]))
;
;(def project-view-template-html "slipstream/ui/views/project-view.html")
;(def project-edit-template-html "slipstream/ui/views/project-edit.html")
;
;(html/defsnippet project-view project-view-template-html common/content-sel
;  [project]
;  [:tbody :> :tr]
;  (html/clone-for [module (modules/modules project)]
;                  [[:a]] (html/do->
;                           (html/set-attr :href (:resourceuri (module/attrs module)))
;                           (html/content (:name (module/attrs module))))
;                  [[:td (html/nth-of-type 2)]] (html/content (:description (module/attrs module)))
;                  [[:td (html/nth-of-type 3)]] (html/content (:owner (authz/attrs module)))
;                  [[:td (html/nth-of-type 4)]] (html/content (:version (module/attrs module)))))
;
;(html/defsnippet project-edit project-edit-template-html common/content-sel
;  [project]
;  [:tbody :> :tr]
;  (html/clone-for [module (modules/modules project)]
;                  [[:a]] (html/do->
;                           (html/set-attr :href (:resourceuri (module/attrs module)))
;                           (html/content (:name (module/attrs module))))
;                  [[:td (html/nth-of-type 2)]] (html/content (:description (module/attrs module)))
;                  [[:td (html/nth-of-type 3)]] (html/content (:owner (authz/attrs module)))
;                  [[:td (html/nth-of-type 4)]] (html/content (:version (module/attrs module)))))
;
;(defn page [module edit?]
;  (let [title (str "SlipStream™ | " (module/module-name module))
;        header (header/header-snip module)
;        footer (footer/footer-snip @version/slipstream-release-version)]
;    (if (true? edit?)
;      (base/base 
;        {:title title
;         :header header
;         :content (project-view module)
;         :footer footer})
;      (base/base 
;        {:title title
;         :header header
;         :content (project-edit module)
;         :footer footer}))))
