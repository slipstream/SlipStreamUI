(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.module.image :as image]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.authz :as authz]))

(def module-root-uri "module/")
(def module-root-uri-length (count module-root-uri))
(def default-cloud-name "default")

(defn attrs
  [module]
  (common/attrs module))

(defn module-name
  [module]
  (common/elem-name module))

(defn module-category
  [module]
  (:category (attrs module)))

(defn module-description
  [module]
  (:description (attrs module)))

(defn module-version
  [module]
  (:version (attrs module)))

(defn module-logo-link
  [module]
  (:logolink (attrs module)))

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

(defn module-commit
  [module]
  (first (html/select module [:commit])))

(defn module-commit-comment
  [module]
  (common/content (first (html/select (module-commit module) [:comment]))))

(defn module-commit-author
  [module]
  (:author (common/attrs (module-commit module))))

(defn module-latestversion?
  [module]
  (common/true-value? (:islatestversion (attrs module))))

(defn title-extra
  [module]
  (if (not (module-latestversion? module))
    (str "<i class='icon-warning-sign'></i>You are not on the latest version of this module!")
    (str "")))

(defn titles
  [module]
  (let
    [attrs (attrs module)
     {:keys [name description category]} attrs
     comment (module-commit-comment module)]
    [name description comment category]))

(defn titles-with-version
  [module]
  (let
    [attrs (attrs module)
     {:keys [name description category version]} attrs
     comment (module-commit-comment module)]
    [name (str "Version: " version " - " description) comment category (title-extra module)]))

(defn nodes
  [deployment]
  (html/select deployment [:node]))

(defn content
  [elem]
  (common/content elem))

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

(defn image
  [metadata]
  (first (html/select metadata [:image])))

(defn runs
  [module]
  (first (html/select module [:runs])))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 (defn- parse-keyword
   [k]
   (let [[match group-name optional-create-str policy-name]
          (re-matches #"(owner|group|public)(create)?(\w+)" (name k))]
     (when match
       (let [group-key (keyword (str group-name "-access?"))
             optional-create-prefix (when optional-create-str (str optional-create-str "-"))
             policy-key (keyword (str optional-create-prefix policy-name))]
        [:access-rights policy-key group-key]))))

(defn- assoc-authz-setting
  "Assoc into the map 'm' the access rights transforming the original
  keyword into a path. E.g. will merge ':grouppost true' into {:post {:group-access? true}}"
  [m [k v]]
  (if-let [authz-setting-path (parse-keyword k)]
    (assoc-in m authz-setting-path (uc/parse-boolean v))
    m))

(defn- add-rights
  [m attrs]
  (reduce assoc-authz-setting m attrs))

(defn- group-members
  [authz]
  (-> authz
      (html/select [:groupMembers :string html/text-node])
      set))

(defn- authorization
  [metadata]
  (let [authz (first (html/select metadata [:authz]))
        attrs (:attrs authz)]
    (-> {}
        (assoc :inherited-group-members? (-> attrs :inheritedgroupmembers uc/parse-boolean)
               :group-members            (group-members authz))
        (add-rights attrs))))

(defmulti category-sections (comp uc/keywordize :category :attrs))

(defmethod category-sections :project
  [metadata])

(defmethod category-sections :image
  [metadata]
  (image/sections metadata))

(defn assoc-category-sections
  [module-map metadata]
  (merge-with merge module-map (category-sections metadata)))

(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  (let [attrs (:attrs metadata)]
    (-> {}
        (assoc-in [:summary :description]     (-> attrs :description))
        (assoc-in [:summary :category]        (-> attrs :category))
        (assoc-in [:summary :name]            (-> attrs :name))
        (assoc-in [:summary :creation]        (-> attrs :creation))
        (assoc-in [:summary :version]         (-> attrs :version uc/parse-pos-int))
        (assoc-in [:summary :short-name]      (-> attrs :shortname))
        (assoc-in [:summary :last-modified]   (-> attrs :lastmodified))
        (assoc-in [:summary :latest-version?] (-> attrs :islatestversion uc/parse-boolean))
        (assoc-in [:summary :deleted?]        (-> attrs :deleted uc/parse-boolean))
        (assoc-in [:summary :uri]             (-> attrs :resourceuri))
        (assoc-in [:summary :parent-uri]      (-> attrs :parenturi))
        (assoc-in [:summary :owner]           (-> metadata (html/select [:authz]) first :attrs :owner))
        (assoc     :authorization             (authorization metadata))
        (assoc-category-sections metadata))))
