(ns slipstream.ui.models.authz
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.user :as user-model]))

(defn authz [module]
  "Extract authz map from module"
  (first (html/select module [html/root :> :authz])))

(defn attrs [authz]
  (:attrs authz))

(defn owner [authz]
  (-> authz attrs :owner))

(defn inherited? [authz]
  (= "true" (:inheritedgroupmembers (attrs authz))))

(defn group-seq [authz]
  "Extract group members as seq"
  (flatten (map :content (html/select authz [:groupMembers :> :string]))))

(defn group [authz]
  "Extract group members"
  (string/join ", " (group-seq authz)))

(defn in-group? [authz user]
  (let
    [username (user-model/username user)]
    (not-empty 
      (filter #(= % (user-model/username user)) (group-seq authz)))))

(defn ownerget? [authz]
  (= "true" (:ownerget (attrs authz))))

(defn ownerput? [authz]
  (= "true" (:ownerput (attrs authz))))

(defn ownerpost? [authz]
  (= "true" (:ownerpost (attrs authz))))

(defn ownerdelete? [authz]
  (= "true" (:ownerdelete (attrs authz))))

(defn ownercreatechildren? [authz]
  (= "true" (:ownercreatechildren (attrs authz))))

(defn groupget? [authz]
  (= "true" (:groupget (attrs authz))))

(defn groupput? [authz]
  (= "true" (:groupput (attrs authz))))

(defn grouppost? [authz]
  (= "true" (:grouppost (attrs authz))))

(defn groupdelete? [authz]
  (= "true" (:groupdelete (attrs authz))))

(defn groupcreatechildren? [authz]
  (= "true" (:groupcreatechildren (attrs authz))))

(defn publicget? [authz]
  (= "true" (:publicget (attrs authz))))

(defn publicput? [authz]
  (= "true" (:publicput (attrs authz))))

(defn publicpost? [authz]
  (= "true" (:publicpost (attrs authz))))

(defn publicdelete? [authz]
  (= "true" (:publicdelete (attrs authz))))

(defn publiccreatechildren? [authz]
  (= "true" (:publiccreatechildren (attrs authz))))

(defn is-owner?
  [authz user]
  (= 
    (user-model/username user)
    (owner authz)))

(defn can-get?
  [authz user]
  (or
    (user-model/super? user)
    (and 
      (is-owner? authz user)
      (ownerget? authz))
    (and
      (in-group? authz user)
      (groupget? authz))
    (publicget? authz)))

(defn can-put?
  [authz user]
  (or
    (user-model/super? user)
    (and 
      (is-owner? authz user)
      (ownerput? authz))
    (and
      (in-group? authz user)
      (groupput? authz))
    (publicput? authz)))

(defn can-post?
  [authz user]
  (or
    (user-model/super? user)
    (and 
      (is-owner? authz user)
      (ownerpost? authz))
    (and
      (in-group? authz user)
      (grouppost? authz))
    (publicpost? authz)))

(defn can-delete?
  [authz user]
  (or
    (user-model/super? user)
    (and 
      (is-owner? authz user)
      (ownerdelete? authz))
    (and
      (in-group? authz user)
      (groupdelete? authz))
    (publicdelete? authz)))

(defn can-createchildren?
  [authz user]
  (or
    (user-model/super? user)
    (and 
      (is-owner? authz user)
      (ownercreatechildren? authz))
    (and
      (in-group? authz user)
      (groupcreatechildren? authz))
    (publiccreatechildren? authz)))
