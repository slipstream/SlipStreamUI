(ns slipstream.ui.views.authz
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.module :as module-model]))

(def authorization-edit-template-html "slipstream/ui/views/authorization-edit.html")

(def authorization-sel [:#authorization])
(def authorization-acl-sel [:#authorization-acl-table])
(def authorization-inheritance-sel [:#authorization-inheritance-table])
(def authorization-view-sel [:#authz-view])
(def authorization-edit-sel [:#authz-edit])
(def authorization-delete-sel [:#authz-delete])
(def authorization-execute-sel [:#authz-execute])
(def authorization-create-children-sel [:#authz-children])

(defn assign-checked
  [acl? authz]
  (if (true? (acl? authz))
    (html/set-attr :checked "true")
    (html/remove-attr :checked)))

(html/defsnippet authz-view-part-snip authorization-edit-template-html authorization-view-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (assign-checked authz-model/ownerget? authz)
  [[:td (html/nth-of-type 3)] :input]
  (assign-checked authz-model/groupget? authz)
  [[:td (html/nth-of-type 4)] :input]
  (assign-checked authz-model/publicget? authz))

(html/defsnippet authz-edit-part-snip authorization-edit-template-html authorization-edit-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (assign-checked authz-model/ownerput? authz)
  [[:td (html/nth-of-type 3)] :input]
  (assign-checked authz-model/groupput? authz)
  [[:td (html/nth-of-type 4)] :input]
  (assign-checked authz-model/publicput? authz))

(html/defsnippet authz-delete-part-snip authorization-edit-template-html authorization-delete-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (assign-checked authz-model/ownerdelete? authz)
  [[:td (html/nth-of-type 3)] :input]
  (assign-checked authz-model/groupdelete? authz)
  [[:td (html/nth-of-type 4)] :input]
  (assign-checked authz-model/publicdelete? authz))

(html/defsnippet authz-post-part-snip authorization-edit-template-html authorization-execute-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (assign-checked authz-model/ownerpost? authz)
  [[:td (html/nth-of-type 3)] :input]
  (assign-checked authz-model/grouppost? authz)
  [[:td (html/nth-of-type 4)] :input]
  (assign-checked authz-model/publicpost? authz))

(html/defsnippet authz-children-part-snip authorization-edit-template-html authorization-create-children-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (assign-checked authz-model/ownercreatechildren? authz)
  [[:td (html/nth-of-type 3)] :input]
  (assign-checked authz-model/groupcreatechildren? authz)
  [[:td (html/nth-of-type 4)] :input]
  (assign-checked authz-model/publiccreatechildren? authz))

(html/defsnippet authz-group-part-snip authorization-edit-template-html authorization-inheritance-sel
  [authz]
  [[:td (html/nth-of-type 2)] :input]
  (assign-checked authz-model/ownercreatechildren? authz)
  [[:td (html/nth-of-type 3)] :input]
  (assign-checked authz-model/groupcreatechildren? authz)
  [[:td (html/nth-of-type 4)] :input]
  (assign-checked authz-model/publiccreatechildren? authz))

(html/defsnippet authz-project-snip authorization-edit-template-html authorization-acl-sel
  [authz]
  authorization-view-sel (html/substitute (authz-view-part-snip authz))
  authorization-edit-sel (html/substitute (authz-edit-part-snip authz))
  authorization-delete-sel (html/substitute (authz-delete-part-snip authz))
  authorization-create-children-sel (html/substitute (authz-children-part-snip authz))
  authorization-execute-sel nil)

(html/defsnippet authz-module-snip authorization-edit-template-html authorization-acl-sel
  [authz execute-msg]
  authorization-view-sel (html/substitute (authz-view-part-snip authz))
  authorization-edit-sel (html/substitute (authz-edit-part-snip authz))
  authorization-delete-sel (html/substitute (authz-delete-part-snip authz))
  authorization-execute-sel (html/substitute (authz-post-part-snip authz))
  [authorization-execute-sel :> [:td html/first-of-type]] (html/content execute-msg)
  authorization-create-children-sel nil)

(defn authz-image
  [authz]
  (authz-module-snip authz "Build or Run Single Image"))

(defn authz-deployment
  [authz]
  (authz-module-snip authz "Run Deployment"))

(html/defsnippet authz-inhertance-snip authorization-edit-template-html authorization-inheritance-sel
  [authz]
  [:#authz-inherited]
  (assign-checked authz-model/inherited? authz)
  [:#authz-group]
  (html/set-attr :value (authz-model/group authz)))

(defmulti authz-by-category
  (fn [authz category]
    category))

(defmethod authz-by-category "Project"
  [authz category]
    (authz-project-snip authz))

(defmethod authz-by-category "Image"
  [authz category]
    (authz-image authz))

(defmethod authz-by-category "Deployment"
  [authz category]
    (authz-deployment authz))

(html/defsnippet authz-edit-snip authorization-edit-template-html authorization-sel
  [module]
  authorization-acl-sel
  (let [category (:category (module-model/attrs module))]
    (html/substitute
      (authz-by-category
        (authz-model/authz module)
        category)))
  
  authorization-inheritance-sel 
  (html/substitute
    (authz-inhertance-snip
      (authz-model/authz module))))

(html/defsnippet authz-view-snip authorization-edit-template-html authorization-sel
  [module]
  authorization-sel (html/substitute (authz-edit-snip module))
  ; Only difference for view compared with edit is that we disable all input elements
  [authorization-sel :input] (html/set-attr :disabled "disabled"))
