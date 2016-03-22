(ns slipstream.ui.models.vms
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.time :as time]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.models.pagination :as pagination]))

(defn- parse-vm
  [vm-metadata]
  (let [run-uuid   (some-> vm-metadata :runUuid (s/replace "Unknown" "") not-empty)
        run-owner  (-> vm-metadata :runOwner not-empty)
        recent-vm? (-> vm-metadata :measurement time/parse time/recent?)]
    (-> vm-metadata
        (select-keys [:state
                      :measurement])
        (assoc :username          (-> vm-metadata :user not-empty))
        (assoc :run-link-state (cond
                                 recent-vm?                   :pending
                                 (not run-uuid)               :unknown
                                 (current-user/is? run-owner) :accessible
                                 (current-user/super?)        :accessible
                                 :else                        :not-accessible))
        (assoc :cloud-instance-id (-> vm-metadata :instanceId   not-empty))
        (assoc :run-uuid          run-uuid)
        (assoc :cloud-name        (-> vm-metadata :cloud        not-empty))
        (assoc :ip-address        (-> vm-metadata :ip           not-empty))
        (assoc :run-owner         run-owner)
        (assoc :name              (-> vm-metadata :name         not-empty))
        (assoc :cpu               (-> vm-metadata :cpu          not-empty))
        (assoc :ram               (-> vm-metadata :ram          not-empty))
        (assoc :disk              (-> vm-metadata :disk         not-empty))
        (assoc :instance-type     (-> vm-metadata :instanceType not-empty)))))

(defn parse
  [metadata]
  {:pagination  (pagination/parse metadata)
   :vms (doall
          ; NOTE: Forcing seq evaluation to ensure the value of the dynamic var *current-user*
          (->> (html/select metadata [:vms :vm])
             identity
             (map :attrs)
             (map parse-vm)))})
