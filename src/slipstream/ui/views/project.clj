(ns slipstream.ui.views.project
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.modules :as modules-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.authz :as authz]
            [slipstream.ui.views.module-base :as module-base]))

;; Selectors

(def children-header-sel [:#children-header])
(def children-sel [:#children])

;; Templates

(def project-new-template-html "slipstream/ui/views/project-new.html")

(defn authz-buttons
  [module]
  (let
    [authz (authz-model/authz module)
     user (user-model/user module)
     can-get? (authz-model/can-get? authz user)
     can-put? (authz-model/can-put? authz user)
     can-delete? (authz-model/can-delete? authz user)
     can-post? (authz-model/can-post? authz user)
     can-createchildren? (authz-model/can-createchildren? authz user)]
    (html/transformation
      #{[:#build-button-top] [:#build-button-bottom]} 
      (if can-post?
        (html/remove-attr :disabled) 
        (html/set-attr :disabled "disabled"))
      #{[:#edit-button-top] [:#edit-button-bottom]} 
      (if can-put?
        (html/remove-attr :disabled) 
        (html/set-attr :disabled "disabled"))
      #{[:#publish-button-top] [:#publish-button-bottom]} 
      (if (user-model/super? user)
        (html/remove-attr :disabled) 
        (html/set-attr :disabled "disabled")))))

(html/defsnippet children-snip module-base/project-view-template-html [children-sel :> :table]
  [children]
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr (html/nth-of-type 1)]] 
                  (html/clone-for 
                    [child children
                     :let [attrs (module-model/attrs child)]]
                    [[:td (html/nth-of-type 1)]] (html/do->
                                                   (html/remove-class "project_category")
                                                   (html/add-class (module-base/to-css-class (:category attrs))))
                    [[:a]] (html/do->
                             (html/set-attr :href (str "/" (:resourceuri attrs)))
                             (html/content (:name attrs)))
                    [[:td (html/nth-of-type 2)]] (html/content (:description attrs))
                    [[:td (html/nth-of-type 3)]] (html/content (module-model/owner child))
                    [[:td (html/nth-of-type 4)]] (html/content (:version attrs))))

(html/defsnippet view-interaction-snip module-base/project-view-template-html module-base/module-interaction-top-sel
  [module]
  (authz-buttons module))

(html/defsnippet view-snip module-base/project-view-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))

  children-sel (html/content (children-snip (modules-model/children module)))
  #{[children-header-sel] [children-sel]} (if (empty? (modules-model/children module))
                                            nil
                                            identity)

  module-base/module-summary-sel (html/substitute 
                                   (module-base/module-summary-view-snip module))

  module-base/module-interaction-top-sel
    (html/substitute
      (view-interaction-snip module))

  module-base/module-interaction-bottom-sel
    (html/substitute
      (view-interaction-snip module))

  authz/authorization-sel (html/substitute (authz/authz-view-snip module)))

(html/defsnippet edit-interaction-snip module-base/project-edit-template-html module-base/module-interaction-top-sel
  [module]
  (authz-buttons module))

(html/defsnippet edit-snip module-base/project-edit-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))

  children-sel (html/content (children-snip module))
  module-base/module-summary-sel (html/substitute 
                                   (module-base/module-summary-edit-snip module))

  authz/authorization-sel (html/substitute (authz/authz-edit-snip module)))

(html/defsnippet summary-new-snip project-new-template-html module-base/module-summary-sel
  [module]
  (module-base/module-summary-new-trans module))

(html/defsnippet new-snip project-new-template-html common/content-sel
  [module]
  common/breadcrumb-sel (module-base/breadcrumb (module-model/module-name module))
  
  module-base/module-summary-sel (html/substitute 
                                   (summary-new-snip module))

  authz/authorization-sel (html/substitute (authz/authz-edit-snip module)))

