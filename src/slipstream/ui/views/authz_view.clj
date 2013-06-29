(ns slipstream.ui.views.authz-view
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.views.authz :as authz]))

(html/defsnippet authz-view-snip authz/authorization-view-template-html authz/authorization-view-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (authz/assign-checked authz-model/ownerget? authz)
  [[:td (html/nth-of-type 3)] :input]
  (authz/assign-checked authz-model/groupget? authz)
  [[:td (html/nth-of-type 4)] :input]
  (authz/assign-checked authz-model/publicget? authz))

(html/defsnippet authz-edit-snip authz/authorization-view-template-html authz/authorization-edit-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (authz/assign-checked authz-model/ownerput? authz)
  [[:td (html/nth-of-type 3)] :input]
  (authz/assign-checked authz-model/groupput? authz)
  [[:td (html/nth-of-type 4)] :input]
  (authz/assign-checked authz-model/publicput? authz))

(html/defsnippet authz-delete-snip authz/authorization-view-template-html authz/authorization-delete-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (authz/assign-checked authz-model/ownerdelete? authz)
  [[:td (html/nth-of-type 3)] :input]
  (authz/assign-checked authz-model/groupdelete? authz)
  [[:td (html/nth-of-type 4)] :input]
  (authz/assign-checked authz-model/publicdelete? authz))

(html/defsnippet authz-post-snip authz/authorization-view-template-html authz/authorization-execute-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (authz/assign-checked authz-model/ownerpost? authz)
  [[:td (html/nth-of-type 3)] :input]
  (authz/assign-checked authz-model/grouppost? authz)
  [[:td (html/nth-of-type 4)] :input]
  (authz/assign-checked authz-model/publicpost? authz))

(html/defsnippet authz-children-snip authz/authorization-view-template-html authz/authorization-create-children-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (authz/assign-checked authz-model/ownercreatechildren? authz)
  [[:td (html/nth-of-type 3)] :input]
  (authz/assign-checked authz-model/groupcreatechildren? authz)
  [[:td (html/nth-of-type 4)] :input]
  (authz/assign-checked authz-model/publiccreatechildren? authz))

(html/defsnippet authz-group authz/authorization-view-template-html authz/authorization-inheritance-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (authz/assign-checked authz-model/ownercreatechildren? authz)
  [[:td (html/nth-of-type 3)] :input]
  (authz/assign-checked authz-model/groupcreatechildren? authz)
  [[:td (html/nth-of-type 4)] :input]
  (authz/assign-checked authz-model/publiccreatechildren? authz))

(html/defsnippet authz-project-snip authz/authorization-view-template-html authz/authorization-acl-sel
  [authz]
  authz/authorization-view-sel (html/substitute (authz-view-snip authz))
  authz/authorization-edit-sel (html/substitute (authz-edit-snip authz))
  authz/authorization-delete-sel (html/substitute (authz-delete-snip authz))
  authz/authorization-create-children-sel (html/substitute (authz-children-snip authz))
  authz/authorization-execute-sel nil)

(html/defsnippet authz-image-snip authz/authorization-view-template-html authz/authorization-acl-sel
  [authz]
  authz/authorization-view-sel (html/substitute (authz-view-snip authz))
  authz/authorization-edit-sel (html/substitute (authz-edit-snip authz))
  authz/authorization-delete-sel (html/substitute (authz-delete-snip authz))
  authz/authorization-execute-sel (html/substitute (authz-post-snip authz))
  [authz/authorization-execute-sel :> [:td html/first-of-type]] (html/content "Build or Run Single Image")
  authz/authorization-create-children-sel nil)

(html/defsnippet authz-deployment-snip authz/authorization-view-template-html authz/authorization-acl-sel
  [authz]
  authz/authorization-view-sel (html/substitute (authz-view-snip authz))
  authz/authorization-edit-sel (html/substitute (authz-edit-snip authz))
  authz/authorization-delete-sel (html/substitute (authz-delete-snip authz))
  [authz/authorization-execute-sel :> [:td html/first-of-type]] (html/content "Run Deployment"))

(html/defsnippet authz-inhertance-snip authz/authorization-view-template-html authz/authorization-inheritance-sel
  [authz]
  [:#authz-inherited :td :input]
  (authz/assign-checked authz-model/inherited? authz)
  [:#authz-group :td]
  (html/content (authz-model/group authz)))

(defmulti authz-by-category
  (fn [authz category]
    category))

(defmethod authz-by-category "Project"
  [authz category]
    (authz-project-snip authz))

(defmethod authz-by-category "Image"
  [authz category]
    (authz-image-snip authz))

(defmethod authz-by-category "Deployment"
  [authz category]
    (authz-deployment-snip authz))

(html/defsnippet authz-snip authz/authorization-view-template-html authz/authorization-sel
  [module]
  authz/authorization-acl-sel
  (let [category (:category (module-model/attrs module))]
    (html/substitute
      (authz-by-category
        (authz-model/authz module)
        category)))
  authz/authorization-inheritance-sel 
  (html/substitute (authz-inhertance-snip (authz-model/authz module))))
