(ns slipstream.ui.views.project
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.views.authz-view :as authz-view]
            [slipstream.ui.views.authz-edit :as authz-edit]
            [slipstream.ui.views.module-base :as module-base]))

(def children-header-sel [:#children-header])
(def children-sel [:#children])

(html/defsnippet children-snip module-base/project-view-template-html [children-sel :> :table]
  [parent]
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr (html/nth-of-type 1)]] 
                  (html/clone-for 
                    [child (modules-model/children parent)
                     :let [attrs (module-model/attrs child)]]
                    [[:td (html/nth-of-type 1)]] (html/do->
                                                   (html/remove-class "project_category")
                                                   (html/add-class (module-base/to-css-class (:category attrs))))
                    [[:a]] (html/do->
                             (html/set-attr :href (str "/" (:resourceuri attrs)))
                             (html/content (:name attrs)))
                    [[:td (html/nth-of-type 2)]] (html/content (:description attrs))
                    [[:td (html/nth-of-type 3)]] (html/content (:version attrs))))

(html/defsnippet view-snip module-base/project-view-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb module)

  children-sel (html/content (children-snip module))
  #{[children-header-sel] [children-sel]} (if (empty? (modules-model/children module))
                                            nil
                                            identity)

  module-base/module-summary-sel (html/substitute 
                                   (module-base/module-summary-view-snip module))

  authz/authorization-sel (html/substitute (authz-view/authz-snip module)))

(html/defsnippet edit-snip module-base/project-edit-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb module)

  children-sel (html/content (children-snip module))
  module-base/module-summary-sel (html/substitute 
                                   (module-base/module-summary-edit-snip module))

  authz/authorization-sel (html/substitute (authz-edit/authz-snip module)))

(html/defsnippet new-snip module-base/project-new-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb module)

  children-sel (html/content (children-snip module))
  module-base/module-summary-sel (html/substitute 
                                   (module-base/module-summary-new-snip module))

  authz/authorization-sel (html/substitute (authz-edit/authz-snip module)))

