(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.authz :as authz]))

(defn attrs [module]
  (:attrs module))

(defn module-name [module]
  (:name (attrs module)))

(defn module-category [module]
  (:category (attrs module)))

(defn user [module]
  (-> (user/attrs module) :username))

(defn owner [module]
  (-> (authz/authz module) authz/attrs :owner))

(defn module-comment [module]
  (first (:content (first (html/select module [:comment])))))

(defn titles [module]
  (let 
    [attrs (attrs module)
     {:keys [name description category]} attrs 
     comment (module-comment module)]
    [name description comment category]))

(defn titles-with-version [module]
  (let 
    [attrs (attrs module)
     {:keys [name description category version]} attrs 
     comment (module-comment module)]
    [name (str "Version: " version " - " description) comment category]))

(defn children [project]
  (modules/modules project))