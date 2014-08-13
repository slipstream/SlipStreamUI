(ns slipstream.ui.views.tables
  "Predefined table rows."
  (:require [slipstream.ui.views.table :as table]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.models.parameters :as p]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:private shared-project-headers
  [nil
   "Name"
   "Description"
   "Owner"
   "Version"])

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
  (let [headers shared-project-headers
        rows (map shared-project-row shared-projects)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:private user-headers
  [nil
   "Username"
   "First Name"
   "Last Name"
   "State"
   "Last online"])

(defn- user-row
  [{:keys [username uri first-name last-name state last-online online?] :as user}]
  {:style (when online? :success)
   :cells [{:type :cell/icon, :content icons/user}
           {:type :cell/link, :content {:text username, :href uri}}
           {:type :cell/text, :content first-name}
           {:type :cell/text, :content last-name}
           {:type :cell/text, :content state}
           {:type :cell/text, :content (or (not-empty last-online) "Unknown")}]})

(defn users-table
  [users]
  (let [headers user-headers
        rows (map user-row users)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:private doc-headers
  [nil
   "Name"
   "HTML"
   "PDF"
   "ePub"])

(defn- doc-row
  [{:keys [title basename] :as doc}]
  {:style nil
   :cells [{:type :cell/icon, :content icons/documentation}
           {:type :cell/text, :content title}
           {:type :cell/external-link, :content {:text "Open in new page"
                                                 :href (str "/html/" basename ".html")}}
           {:type :cell/external-link, :content {:text "Open PDF in new page"
                                                 :href (str "/pdf/" basename ".pdf")}}
           {:type :cell/external-link, :content {:text "Download"
                                                 :href (str "/epub/" basename ".epub")}}]})

(defn docs-table
  [docs]
  (let [headers doc-headers
        rows (map doc-row docs)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:private version-headers
  [nil
   "Version"
   "Comment"
   "Author"
   "Date"])

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
  (let [headers version-headers
        rows (map (partial version-row icon) versions)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:private parameter-headers
  ["Description"
   "Value"])

(defn- parameter-type->cell-type
  [parameter-type]
  (case parameter-type
    "Enum"              :cell/text
    "String"            :cell/text
    "Boolean"           :cell/boolean
    "RestrictedText"    :cell/text
    "Password"          :cell/password
    "RestrictedString"  :cell/text))

(defn- parameter-row
  [{:keys [type description value] :as parameter}]
  {:style nil
   :cells [{:type :cell/text, :content description}
           {:type (parameter-type->cell-type type), :content value}]})

(defn parameters-table
  [parameters]
  (let [headers parameter-headers
        rows (map parameter-row parameters)]
    (table/build {:headers headers
                  :rows rows})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn user-summary-table
  [user-summary-map]
  (parameters-table
    (p/map->parameter-list user-summary-map
      :username     {:description "Username"          :type "String"}
      :first-name   {:description "First name"        :type "String"}
      :last-name    {:description "Last name"         :type "String"}
      :organization {:description "Organization"      :type "String"}
      :email        {:description "Email"             :type "String"}
      :super?       {:description "Is administrator?" :type "Boolean"}
      :creation     {:description "Date of creation"  :type "String"}
      :state        {:description "Status"            :type "String"})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
