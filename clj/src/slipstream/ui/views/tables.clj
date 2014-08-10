(ns slipstream.ui.views.tables
  "Predefined table rows."
  (:require [slipstream.ui.views.table :as table]
            [slipstream.ui.views.util.icons :as icons]))

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