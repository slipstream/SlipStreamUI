(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.authz :as authz]))

(defn attrs
  [module]
  (common/attrs module))

(defn module-name
  [module]
  (common/elem-name module))

(defn module-category
  [module]
  (:category (attrs module)))

(defn username
  [module]
  (-> module user/attrs :name))

(defn user
  [module]
  (user/attrs module))

(defn user-attrs
  [module]
  (-> module user attrs))

(defn owner
  [module]
  (-> (authz/authz module) authz/attrs :owner))

(defn module-comment
  [module]
  (first (common/content (first (html/select module [:comment])))))

(defn titles
  [module]
  (let 
    [attrs (attrs module)
     {:keys [name description category]} attrs 
     comment (module-comment module)]
    [name description comment category]))

(defn titles-with-version
  [module]
  (let 
    [attrs (attrs module)
     {:keys [name description category version]} attrs 
     comment (module-comment module)]
    [name (str "Version: " version " - " description) comment category]))

(defn children
  [project]
  (modules/children project))

(defn content
  [elem]
  (common/content elem))

(defn item-comment
  [item]
  (first (:content (first (html/select item [:comment])))))
