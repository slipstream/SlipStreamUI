(ns slipstream.ui.views.tables
  "Predefined table rows."
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.table :as table]
            [slipstream.ui.models.parameters :as p]))

(localization/def-scoped-t)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- welcome-project-row
  [{:keys [name uri description owner version] :as welcome-project}]
  {:style nil
   :cells [{:type :cell/icon, :content icons/project}
           {:type :cell/link, :content {:text name, :href uri}}
           {:type :cell/text, :content description}
           {:type :cell/text, :content owner}
           {:type :cell/text, :content version}]})

(defn welcome-projects-table
  [welcome-projects]
  (table/build
    {:headers [nil :name :description :owner :version]
     :rows (map welcome-project-row welcome-projects)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- user-row
  [{:keys [username first-name last-name organization state last-online online?] :as user}]
  {:style (when online? :success)
   :cells [{:type :cell/icon,       :content icons/user}
           {:type :cell/username,   :content username}
           {:type :cell/text,       :content first-name}
           {:type :cell/text,       :content last-name}
           {:type :cell/text,       :content organization}
           {:type :cell/text,       :content state}
           {:type :cell/timestamp,  :content last-online}]})

(defn users-table
  [users]
  (table/build
    {:headers [nil :username :first-name :last-name :organization :state :last-online]
     :rows (map user-row users)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- doc-row
  [{:keys [title basename] :as doc}]
  {:style nil
   :cells [{:type :cell/icon, :content icons/documentation}
           {:type :cell/text, :content title}
           {:type :cell/external-link, :content {:text (t :documentation.open-html)
                                                 :href (str "/html/" basename ".html")}}
           {:type :cell/external-link, :content {:text (t :documentation.open-pdf)
                                                 :href (str "/pdf/" basename ".pdf")}}
           {:type :cell/external-link, :content {:text (t :documentation.open-epub)
                                                 :href (str "/epub/" basename ".epub")}}]})

(defn docs-table
  [docs]
  (table/build
    {:headers [nil :name :html :pdf :epub]
     :rows (map doc-row docs)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- version-row
  [icon {:keys [version uri commit] :as version}]
  {:style nil
   :cells [{:type :cell/icon, :content icon}
           {:type :cell/link, :content {:text version :href uri}}
           {:type :cell/text, :content (:comment commit)}
           {:type :cell/text, :content (:author commit)}
           {:type :cell/text, :content (:date commit)}]})

(defn versions-table
  [icon versions]
  (table/build
    {:headers [nil :version :comment :author :date]
     :rows (map (partial version-row icon) versions)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- cell-type?
  [x]
  (and
    (keyword? x)
    (-> x namespace (= "cell"))))

(defn- cell-type-for
  [{:keys [type name] :as parameter}]
  (cond
    (cell-type? type) type
    (re-matches #".*:url\..*" name) :cell/url
    :else (case type
            "String"            :cell/text
            "RestrictedString"  :cell/text
            "Text"              :cell/textarea
            "RestrictedText"    :cell/textarea
            "Password"          :cell/password
            "Enum"              :cell/enum
            "Boolean"           :cell/boolean)))

(defn- value-of
  [{:keys [name value id-format-fn built-from-map? read-only?] :as parameter} cell-type row-index]
  (let [formated-name (if (fn? id-format-fn)
                        (id-format-fn name)
                        (format "parameter-%s--%s--value" name row-index))
        value-base (cond-> {:id formated-name, :row-index row-index, :read-only? read-only?}
                           (not built-from-map?) (assoc :parameter parameter))]
    (case cell-type
      :cell/textarea  (assoc value-base :text      value)
      :cell/text      (assoc value-base :text      value)
      :cell/email     (assoc value-base :email     value)
      :cell/enum      (assoc value-base :enum      value)
      :cell/url       (assoc value-base :url       value)
      :cell/boolean   (assoc value-base :value     value)
      :cell/password  (assoc value-base :password  value)
      value)))

(defn- parameter-row
  [first-cols-keywords
   row-index
   {:keys [type editable? hidden? help-hint]
    :or {editable? (page-type/edit-or-new?)}
    :as parameter}]
  (let [cell-type (cell-type-for parameter)
        first-cells (mapv #(do {:type :cell/text, :content (get parameter %)}) first-cols-keywords)]
    {:style nil
     :hidden? hidden?
     :cells (conj first-cells
                  {:type cell-type, :content (value-of parameter cell-type row-index), :editable? editable?}
                  {:type :cell/help-hint, :content help-hint})}))

(defn build-parameters-table
  "The last columns are always value and hint. The first ones might change."
  [first-cols-keywords parameters]
  (table/build
    {:headers (concat first-cols-keywords [:value nil])
     :rows (map-indexed (partial parameter-row first-cols-keywords) parameters)}))

(defn parameters-table
  [parameters]
  (build-parameters-table [:description] parameters))

(defn runtime-parameters-table
  [parameters]
  (build-parameters-table [:name] parameters))

(defn service-catalog-parameters-table
  [parameters]
  (build-parameters-table [:name :description :category] parameters))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn user-summary-table
  [user-summary-map]
  (parameters-table
    (p/map->parameter-list user-summary-map
      :username     {:type :cell/text, :editable? (page-type/new?), :id-format-fn (constantly "name")}
      ; :username     {:type :cell/text, :editable? (page-type/new?), :hidden? (page-type/new?)}
      ; :username     {:type :cell/text, :editable? true,  :hidden? (page-type/not-new?), :id-format-fn (constantly "name")}
      :first-name   {:type :cell/text}
      :last-name    {:type :cell/text}
      :organization {:type :cell/text}
      :email        {:type :cell/email}
      :super?       {:type :cell/boolean,   :editable? (and (page-type/edit-or-new?) (current-user/super?)), :id-format-fn (constantly "issuper")}
      :creation     {:type :cell/timestamp, :editable? false}
      :password-new {:type :cell/password,  :editable? true,  :hidden? (page-type/view?), :id-format-fn (constantly "password1")}
      :password-old {:type :cell/password,  :editable? true,  :hidden? (page-type/not-edit?), :id-format-fn (constantly "password2")}
      :state        {:type :cell/text,      :editable? false, :hidden? (page-type/edit-or-new?)})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn module-summary-table
  [module]
  (parameters-table
    (p/map->parameter-list module
      :name          {:type :cell/module-name,    :editable? (page-type/new?)}
      :uri           {:type :cell/module-version, :as-parameter :module-version, :editable? false, :hidden? (page-type/new?)}
      :description   {:type :cell/text}
      :comment       {:type :cell/text,       :hidden?  (page-type/edit-or-new?)}
      :category      {:type :cell/text,       :editable? false, :hidden? (page-type/new?)}
      :creation      {:type :cell/timestamp,  :editable? false, :hidden? (page-type/new?)}
      :last-modified {:type :cell/timestamp,  :editable? false, :hidden? (page-type/new?)}
      :owner         {:type :cell/username,   :editable? false, :hidden? (page-type/new?)}
      :image         {:type :cell/text,       :hidden? (page-type/view?)})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- access-right-row-style
  [{:keys [owner-access? group-access? public-access?]}]
  (if-not owner-access?
    :danger
    (case [group-access? public-access?]
          [false         false]          :warning
          [true          true]           :success
          nil)))

(def ^:private access-rights-id-suffix
  {:get               "Get"
   :put               "Put"
   :delete            "Delete"
   :create-children   "CreateChildren"
   :post              "Post"})

(defn- access-right-row
  [access-rights [access-right-name access-rights-key]]
  (let [{:keys [owner-access? group-access? public-access?] :as access-rights} (get access-rights access-rights-key)
        id-suffix (access-rights-id-suffix access-rights-key)]
    {:style (when (page-type/view-or-chooser?) (access-right-row-style access-rights))
     :cells [{:type :cell/text,     :content access-right-name}
             {:type :cell/boolean,  :content {:value owner-access?,   :id (str "owner"  id-suffix)},  :editable? (page-type/edit-or-new?)}
             {:type :cell/boolean,  :content {:value group-access?,   :id (str "group"  id-suffix)},  :editable? (page-type/edit-or-new?)}
             {:type :cell/boolean,  :content {:value public-access?,  :id (str "public" id-suffix)},  :editable? (page-type/edit-or-new?)}]}))

(defn- access-rights-rows
  [module-type]
  (vector
    [(t :access.view)   :get]
    [(t :access.edit)   :put]
    [(t :access.delete) :delete]
    (case module-type
      :project    [(t :access.create-children)    :create-children]
      :image      [(t :access.build-or-run-image) :post]
      :deployment [(t :access.run-deployment)     :post])))

(defn access-rights-table
  [module]
  (let [module-type   (-> module :summary :category uc/keywordize)
        access-rights (-> module :authorization :access-rights)]
    (table/build
      {:headers [:access-right :owner :group :public]
       :rows (->> module-type
                  access-rights-rows
                  (map (partial access-right-row access-rights)))})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn group-members-table
  [group-members]
  (parameters-table
    (p/map->parameter-list group-members
      :inherited-group-members? {:type :cell/boolean, :id-format-fn (constantly "inheritedGroupMembers")}
      :group-members            {:type :cell/set,     :id-format-fn (constantly "groupmembers")})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn cloud-image-details-table
  [cloud-image-details]
  (parameters-table
    (p/map->parameter-list cloud-image-details
      :native-image?      {:type :cell/boolean}
      :cloud-identifiers  {:type :cell/map}
      :reference-image    {:type :cell/reference-module})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn os-details-table
  [os-details]
  (parameters-table
    (p/map->parameter-list os-details
      :platform         {:type :cell/enum}
      :login-username   {:type :cell/text})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- run-row
  [{:keys [cloud-name uri module-uri start-time username uuid status tags type] :as run}]
  {:style nil
   :cells [{:type :cell/icon,      :content (icons/icon-for (or type :run))}
           {:type :cell/link,      :content {:text (uc/trim-from uuid \-), :href uri}}
           {:type :cell/url,       :content module-uri}
           {:type :cell/text,      :content status}
           {:type :cell/timestamp, :content start-time}
           {:type :cell/username,  :content username}
           {:type :cell/text,      :content tags}]})

(defn runs-table
  [docs]
  (table/build
    {:headers [nil :id :module :status :start-time :user :tags]
     :rows (map run-row docs)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- deployment-parameter-value-cell
  [{:keys [read-only value type]}]
  (case type
     "String" {:type :cell/text
               :editable? (page-type/edit-or-new?)
               :content {:text value
                         :tooltip (when read-only
                           (t :deployment-parameter.is-read-only))}}))

(defn- deployment-parameter-row
  [{:keys [help-hint read-only order value category description type name]
    :as deployment-parameter}]
  {:style  (case category
             "Output" :info
             "Input"  :warning)
   :cells [{:type :cell/text,      :content name,         :editable? (page-type/edit-or-new?)}
           {:type :cell/text,      :content description,  :editable? (page-type/edit-or-new?)}
           {:type :cell/enum,      :content (u/enum ["Output" "Input"] :deployment-parameter-category category), :editable? (page-type/edit-or-new?)}
           (deployment-parameter-value-cell deployment-parameter)
           {:type :cell/help-hint, :content help-hint}]})

(defn deployment-parameters-table
  [deployment-parameters]
  (table/build
    {:headers [:name :description :category :value nil]
     :rows (map deployment-parameter-row deployment-parameters)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- image-creation-package-row
  [{:keys [repository name key] :as image-creation-package}]
  {:style  nil
   :cells [{:type :cell/text, :content name,        :editable? (page-type/edit-or-new?)}
           {:type :cell/text, :content repository,  :editable? (page-type/edit-or-new?)}
           {:type :cell/text, :content key,         :editable? (page-type/edit-or-new?)}]})

(defn image-creation-packages-table
  [image-creation-packages]
  (table/build
    {:headers [:name :repository :key]
     :rows (map image-creation-package-row image-creation-packages)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- project-child-row
  [{:keys [category name description owner version uri] :as project-child}]
  {:style  nil
   :cells [{:type :cell/icon,     :content (icons/icon-for category)}
           {:type :cell/link,     :content {:text name, :href uri}}
           {:type :cell/text,     :content description}
           {:type :cell/username, :content owner}
           {:type :cell/text,     :content version}]})

(defn project-children-table
  [project-children]
  (table/build
    {:headers [nil :name :description :owner :version]
     :rows (map project-child-row project-children)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- deployment-node-row
  [{:keys [name reference-image default-multiplicity default-cloud] :as deployment-node}]
  {:style  nil
   :cells [{:type :cell/text, :content name}
           {:type :cell/map,  :content (dissoc deployment-node :name)}]})

(defn deployment-nodes-table
  [deployment-nodes]
  (table/build
    {:headers [:name :image-link]
     :rows (map deployment-node-row deployment-nodes)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn run-summary-table
  [run]
  (parameters-table
    (p/map->parameter-list run
      :module-uri         {:type :cell/url,       :editable? false}
      :category           {:type :cell/text,      :editable? false}
      :user               {:type :cell/username,  :editable? false}
      :start-time         {:type :cell/timestamp, :editable? false}
      :end-time           {:type :cell/timestamp, :editable? false}
      :last-state-change  {:type :cell/timestamp, :editable? false}
      :state              {:type :cell/text,      :editable? false}
      :type               {:type :cell/text,      :editable? false, :as-parameter :run-type}
      :uuid               {:type :cell/text,      :editable? false, :as-parameter :run-id}
      :tags               {:type :cell/text,      :editable? true})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- vm-row
  [{:keys [cloud-name run-uuid cloud-instance-id username state] :as vm}]
  {:style  nil
   :cells [{:type :cell/link,     :content {:text (uc/trim-from run-uuid \-), :href (str "/run/" run-uuid)}}
           {:type :cell/text,     :content (localization/with-prefixed-t :run.state
                                             (-> (or state :unknown) uc/keywordize t))}
           {:type :cell/username, :content username}
           {:type :cell/text,     :content cloud-instance-id}]})

(defn vms-table
  [vms]
  (table/build
    {:headers [:slipstream-id :state :user :cloud-instance-id]
     :rows (map vm-row vms)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
