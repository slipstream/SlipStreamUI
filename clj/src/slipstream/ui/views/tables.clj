(ns slipstream.ui.views.tables
  "Predefined table rows."
  (:require [slipstream.ui.views.table :as table]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.parameters :as p]))

(localization/def-scoped-t)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- shared-project-headers
  "All headers must be computed on runtime to take into account the localization
  language."
  []
  [nil
   (t :header.name)
   (t :header.description)
   (t :header.owner)
   (t :header.version)])

(defn- shared-project-row
  [{:keys [name uri description owner version] :as shared-project}]
  {:style nil
   :cells [{:type :cell/icon, :content icons/project}
           {:type :cell/link, :content {:text name, :href uri}}
           {:type :cell/text, :content description}
           {:type :cell/text, :content owner}
           {:type :cell/text, :content version}]})

(defn shared-projects-table
  [shared-projects]
  (let [headers (shared-project-headers)
        rows (map shared-project-row shared-projects)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- user-headers
  []
  [nil
   (t :header.username)
   (t :header.first-name)
   (t :header.last-name)
   (t :header.organization)
   (t :header.state)
   (t :header.last-online)])

(defn- user-row
  [{:keys [username uri first-name last-name organization state last-online online?] :as user}]
  {:style (when online? :success)
   :cells [{:type :cell/icon, :content icons/user}
           {:type :cell/link, :content {:text username, :href uri}}
           {:type :cell/text, :content first-name}
           {:type :cell/text, :content last-name}
           {:type :cell/text, :content organization}
           {:type :cell/text, :content state}
           {:type :cell/text, :content (or (not-empty last-online) "Unknown")}]})

(defn users-table
  [users]
  (let [headers (user-headers)
        rows (map user-row users)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- doc-headers
  []
  [nil
   (t :header.name)
   (t :header.html)
   (t :header.pdf)
   (t :header.epub)])

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
  (let [headers (doc-headers)
        rows (map doc-row docs)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- version-headers
  []
  [nil
   (t :header.version)
   (t :header.comment)
   (t :header.author)
   (t :header.date)])

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
  (let [headers (version-headers)
        rows (map (partial version-row icon) versions)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- parameter-headers
  []
  [(t :header.description)
   (t :header.value)
   nil])

(defn- cell-type?
  [x]
  (and
    (keyword? x)
    (-> x namespace (= "cell"))))

(defn- type->cell-type
  [type]
  (if (cell-type? type)
    type
    (case type
      "Enum"              :cell/text
      "String"            :cell/text
      "Boolean"           :cell/boolean
      "RestrictedText"    :cell/text
      "Password"          :cell/password
      "RestrictedString"  :cell/text)))

(defn- parameter-row
  [{:keys [type description value help-hint name] :as parameter}]
  {:style nil
   :cells [{:type :cell/text, :content description}
           {:type (type->cell-type type), :content value}
           {:type :cell/help-hint, :content help-hint}]})

(defn parameters-table
  [parameters]
  (let [headers (parameter-headers)
        rows (map parameter-row parameters)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn user-summary-table
  [user-summary-map]
  (parameters-table
    (p/map->parameter-list user-summary-map
      :username     {:type :cell/text}
      :first-name   {:type :cell/text}
      :last-name    {:type :cell/text}
      :organization {:type :cell/text}
      :email        {:type :cell/email}
      :super?       {:type :cell/boolean}
      :creation     {:type :cell/timestamp}
      :state        {:type :cell/text})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn module-summary-table
  [module]
  (parameters-table
    (p/map->parameter-list module
      :name          {:type :cell/text}
      :uri           {:as-parameter :module-version :type :cell/module-version}
      :description   {:type :cell/text}
      :comment       {:type :cell/text}
      :category      {:type :cell/text}
      :creation      {:type :cell/timestamp}
      :last-modified {:type :cell/timestamp}
      :owner         {:type :cell/text}
      )))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn- access-right-headers
  []
  [(t :header.access-right)
   (t :header.owner)
   (t :header.group)
   (t :header.public)])

(defn- access-right-row-style
  [{:keys [owner-access? group-access? public-access?]}]
  (if-not owner-access?
    :danger
    (case [group-access? public-access?]
          [false         false]          :warning
          [true          true]           :success
          nil)))

(defn- access-right-row
  [[access-right-name {:keys [owner-access? group-access? public-access?] :as access-rights}]]
  {:style (access-right-row-style access-rights)
   :cells [{:type :cell/text,     :content access-right-name}
           {:type :cell/boolean,  :content owner-access?}
           {:type :cell/boolean,  :content group-access?}
           {:type :cell/boolean,  :content public-access?}]})

(defn- map->sorted-vector
  [access-rights]
  (vector
    [(t :access.get)             (:get access-rights)]
    [(t :access.put)             (:put access-rights)]
    [(t :access.delete)          (:delete access-rights)]
    [(t :access.create-children) (:create-children access-rights)]))

(defn access-rights-table
  [access-rights]
  (let [headers (access-right-headers)
        rows (->> access-rights
                  map->sorted-vector
                  (map access-right-row))]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn group-members-table
  [group-members]
  (parameters-table
    (p/map->parameter-list group-members
      :inherited-group-members? {:type :cell/boolean}
      :group-members            {:type :cell/set})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn cloud-image-details-table
  [cloud-image-details]
  (parameters-table
    (p/map->parameter-list cloud-image-details
      :native-image?      {:type :cell/boolean}
      :cloud-identifiers  {:type :cell/map}
      :reference-image    {:type :cell/url})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn os-details-table
  [os-details]
  (parameters-table
    (p/map->parameter-list os-details
      :platform         {:type :cell/text}
      :login-username   {:type :cell/text})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- run-headers
  []
  [nil
   (t :run.id)
   (t :run.module)
   (t :run.status)
   (t :run.start-time)
   (t :run.user)
   (t :run.tags)])

(defn- run-row
  [{:keys [cloud-name uri module-uri start-time username uuid status tags] :as run}]
  {:style nil
   :cells [{:type :cell/icon,      :content icons/run}
           {:type :cell/link,      :content {:text (uc/trim-from uuid \-), :href uri}}
           {:type :cell/link,      :content {:text module-uri, :href module-uri}}
           {:type :cell/text,      :content status}
           {:type :cell/timestamp, :content start-time}
           {:type :cell/link,      :content {:text username, :href (str "/user/" username)}}
           {:type :cell/text,      :content tags}]})

(defn runs-table
  [docs]
  (let [headers (run-headers)
        rows (map run-row docs)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
