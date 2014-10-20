(ns slipstream.ui.models.vms
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]))

(defn- parse-vm
  [vm-metadata]
  (-> vm-metadata
      (select-keys [:state
                    :measurement])
      (assoc        :username           (-> vm-metadata :user))
      (assoc        :cloud-instance-id  (-> vm-metadata :instanceid))
      (assoc        :run-uuid           (-> vm-metadata :runuuid))
      (assoc        :cloud-name         (-> vm-metadata :cloud))))

(defn- group-vms
  [vms]
  (uc/coll-grouped-by :cloud-name vms
                      :items-keyword :vms))

(defn parse
  [metadata]
  (->> (html/select metadata [:vms :vm])
       identity
       (map :attrs)
       (map parse-vm)
       group-vms))
