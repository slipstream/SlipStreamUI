(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]
            [slipstream.ui.models.common :as common]
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
    (assoc-in m authz-setting-path (u/parse-boolean v))
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
        (assoc :inherited-group-members? (-> attrs :inheritedgroupmembers u/parse-boolean)
               :group-members            (group-members authz))
        (add-rights attrs))))

; (defn- owner
;   [metadata]
;   (let [authz (first (html/select metadata [:authz]))]
;     (-> authz :attrs :owner)))

(defn parse
  [metadata]
  (let [attrs (:attrs metadata)]
    (-> attrs
        (select-keys [:description
                      :category
                      :name
                      :creation])
        (assoc        :version          (-> attrs :version u/parse-pos-int)
                      :short-name       (:shortname attrs)
                      :last-modified    (:lastmodified attrs)
                      :latest-version?  (-> attrs :islatestversion u/parse-boolean)
                      :deleted?         (-> attrs :deleted u/parse-boolean)
                      :uri              (:resourceuri attrs)
                      :parent-uri       (:parenturi attrs)
                      :owner            (-> metadata (html/select [:authz]) first :attrs :owner)
                      :authorization    (authorization metadata)))))


;; Note: The parse function above will generate a map with the following structure:

; {:description "Another description..."
;  :category "Project"
;  :name "Public/OtherProject"
;  :creation "2013-05-16 17:04:39.113 CEST"
;  :version 1
;  :short-name "OtherProject"
;  :last-modified "2013-05-16 17:04:39.113 CEST"
;  :latest-version? nil
;  :deleted? false
;  :uri "module/Public/OtherProject/1"
;  :parent-uri "module/Public"
;  :owner "sixsq"
;  :authorization {:group-members #{"meb" "other" "konstan" "xxx"}
;                  :inherited-group-members? false
;                  :access-rights {:create-children {:public-access? true
;                                                    :group-access? true
;                                                    :owner-access? true}
;                                  :delete {:owner-access? true
;                                           :public-access? false
;                                           :group-access? false}
;                                  :put {:owner-access? true
;                                        :public-access? false
;                                        :group-access? false}
;                                  :post {:group-access? false
;                                         :owner-access? true
;                                         :public-access? false}
;                                  :get {:group-access? true
;                                        :public-access? true
;                                        :owner-access? true}}}}


