(ns slipstream.ui.models.module
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.module.image :as image]
            [slipstream.ui.models.module.project :as project]
            [slipstream.ui.models.module.deployment :as deployment]))

 (defn- parse-keyword
   [k]
   (let [[match group-name optional-create-str policy-name]
          (re-matches #"(?i)(owner|group|public)(create)?(\w+)" (name k))]
     (when match
       (let [group-key (keyword (str group-name "-access?"))
             optional-create-prefix (when optional-create-str (str optional-create-str "-"))
             policy-key (->> policy-name (str optional-create-prefix) s/lower-case keyword)]
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
        (assoc :inherited-group-members? (-> attrs :inheritedGroupMembers uc/parse-boolean)
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
        alternative-uri (str (:parentUri attrs) "/" (:shortName attrs))
        publication-date (-> metadata (html/select [html/root :> :published]) first :attrs :publicationDate)]
    {:description       (:description attrs)
     :category          (:category attrs)
     :comment           (-> metadata (html/select [html/root :> :commit :comment html/text]) first)
     :publication       publication-date
     :published?        (boolean publication-date)
     :name              (-> attrs
                            :name
                            (or (u/module-name alternative-uri))
                            u/not-default-new-name)
     :creation          (:creation attrs)
     :version           (-> attrs :version uc/parse-pos-int)
     :short-name        (:shortName attrs)
     :last-modified     (:lastModified attrs)
     :latest-version?   (or
                          (page-type/new?)
                          (-> attrs :isLatestVersion uc/parse-boolean))
     :deleted?          (-> attrs :deleted uc/parse-boolean)
     :uri               (or
                          (-> attrs :resourceUri)
                          alternative-uri)
     :parent-uri        (:parentUri attrs)
     :logo-url          (-> metadata :attrs :logoLink (or ""))
     :owner             (-> metadata (html/select [:authz]) first :attrs :owner)
     :placement-policy  (:placementPolicy attrs)}))

(defn parse
  "See tests for structure of the expected parsed metadata."
  [metadata]
  (-> {:summary           (summary metadata)
       :authorization     (authorization metadata)}
      (assoc-category-sections metadata)))
