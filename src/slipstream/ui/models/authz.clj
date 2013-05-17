(ns slipstream.ui.models.authz
  (:require [net.cgrand.enlive-html :as html]))

(defn authz [module]
  "Extract authz map from module"
  (first (html/select module [html/root :> :authz])))

(defn attrs [authz]
  (:attrs authz))

(defn inherited? [authz]
  (= "true" (:inheritedgroupmembers (attrs authz))))

(defn group [authz]
  "Extract group members"
  (first (:content (first (html/select authz [html/root :> :groupMembers])))))

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

