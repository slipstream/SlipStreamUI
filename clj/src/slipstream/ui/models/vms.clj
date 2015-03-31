(ns slipstream.ui.models.vms
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.pagination :as pagination]))

(defn- parse-vm
  [vm-metadata]
  (-> vm-metadata
      (select-keys [:state
                    :measurement])
      (assoc        :username           (-> vm-metadata :user))
      (assoc        :cloud-instance-id  (-> vm-metadata :instanceId))
      (assoc        :run-uuid           (-> vm-metadata :runUuid))
      (assoc        :cloud-name         (-> vm-metadata :cloud))
      (assoc        :ip-address         (-> vm-metadata :ip))
      (assoc        :run-owner          (-> vm-metadata :runOwner))
      (assoc        :name               (-> vm-metadata :name))))

(defn parse
  [metadata]
  {:pagination  (pagination/parse metadata)
   :vms (->> (html/select metadata [:vms :vm])
             identity
             (map :attrs)
             (map parse-vm))})
