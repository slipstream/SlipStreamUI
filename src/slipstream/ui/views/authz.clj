(ns slipstream.ui.views.authz
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.module :as module-model]))

(def authorization-view-template-html "slipstream/ui/views/authorization-view.html")
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
