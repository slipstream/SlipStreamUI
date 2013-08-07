(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.authz :as authz]))

(def module-root-uri "module/")
(def module-root-uri-length (count module-root-uri))

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
  (common/content (first (html/select module [:comment]))))

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

(defn nodes
  [deployment]
  (html/select deployment [:node]))

(defn content
  [elem]
  (common/content elem))

(defn item-comment
  [item]
  (first (:content (first (html/select item [:comment])))))

(defn available-clouds
  [module]
  (flatten 
    (map :content (html/select module [:cloudNames :string]))))

(defn published?
  [module]
  (not (empty? (html/select module [:published]))))

(defn parent-uri
  [module]
  (:parenturi (attrs module)))
  
(defn parent-name
  [module]
  (str
    (apply str
           (drop 
             module-root-uri-length
             (parent-uri module)))
    "/"))

(defn new?
  [module]
  (= "new" (:shortname (attrs module))))

(defn base?
  [image]
  (= "true" (:isbase (attrs image))))
