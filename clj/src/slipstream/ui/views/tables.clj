(ns slipstream.ui.views.tables
  "Predefined table rows."
  (:require [slipstream.ui.views.table :as table]))

(def ^:private shared-project-headers
  [nil
   "Name"
   "Description"
   "Owner"
   "Version"])

(defn- shared-project-row
  [{:keys [name uri description owner version] :as shared-project}]
  [{:type :cell/icon, :content :folder-open}
   {:type :cell/link, :content {:text name, :href uri}}
   {:type :cell/text, :content description}
   {:type :cell/text, :content owner}
   {:type :cell/text, :content version}
   ])

(defn shared-projects-table
  [shared-projects]
  (let [headers shared-project-headers
        rows (map shared-project-row shared-projects)]
    (table/build {:headers headers
                  :rows rows})))