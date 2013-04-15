(ns slipstream.ui.views.root-projects
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.views.common :as common]))

(def project-template-html "slipstream/ui/views/project.html")

(html/defsnippet project project-template-html common/content-sel
  [project]
  [:tbody :> :tr] 
  (html/clone-for [module (modules/modules projects)]
                  [[:a]] (html/do->
                           (html/set-attr :href (:resourceuri (module/attrs module)))
                           (html/content (:name (module/attrs module))))
                  [[:td (html/nth-of-type 2)]] (html/content (:description (module/attrs module)))
                  [[:td (html/nth-of-type 3)]] (html/content (:owner (authz/attrs module)))
                  [[:td (html/nth-of-type 4)]] (html/content (:version (module/attrs module)))))



