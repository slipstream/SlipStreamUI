(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.module.image :as image]
            [slipstream.ui.models.module.project :as project]
            [slipstream.ui.models.module.deployment :as deployment]))

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
  keyword into a path. E.g. will merge ':grouppost true' into {:post {:group-access? true}}.
  See tests for expectations."
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
  [metadata]
  (project/sections metadata))

(defmethod category-sections :image
  [metadata]
  (image/sections metadata))

(defmethod category-sections :deployment
  [metadata]
  (deployment/sections metadata))

(defn- assoc-category-sections
  [module-map metadata]
  (merge-with merge module-map (category-sections metadata)))

(defn- summary
  [metadata]
  (let [attrs (:attrs metadata)
        alternative-uri (str (:parenturi attrs) "/" (:shortname attrs))
        publication-date (-> metadata (html/select [:published]) first :attrs :publicationdate)]
    {:description       (-> attrs :description)
     :category          (-> attrs :category)
     :comment           (-> metadata (html/select [:comment html/text]) first)
     :publication       publication-date
     :published?        (boolean publication-date)
     :name              (-> attrs
                            :name
                            (or (u/module-name alternative-uri))
                            u/not-default-new-name)
     :creation          (-> attrs :creation)
     :version           (-> attrs :version uc/parse-pos-int)
     :short-name        (-> attrs :shortname)
     :last-modified     (-> attrs :lastmodified)
     :latest-version?   (or
                          (page-type/new?)
                          (-> attrs :islatestversion uc/parse-boolean))
     :deleted?          (-> attrs :deleted uc/parse-boolean)
     :uri               (or
                          (-> attrs :resourceuri)
                          alternative-uri)
     :parent-uri        (-> attrs :parenturi)
     :owner             (-> metadata (html/select [:authz]) first :attrs :owner)}))

(defn- available-clouds
  [metadata]
  (let [cloud-default (-> metadata (html/select [:user]) first :attrs :defaultcloud)]
    (-> metadata
        (html/select [:cloudNames :string html/text-node])
        (u/enum :available-clouds)
        (u/enum-select cloud-default))))

(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  (-> {:summary           (summary metadata)
       :available-clouds  (available-clouds metadata)
       :authorization     (authorization metadata)}
      (assoc-category-sections metadata)))
