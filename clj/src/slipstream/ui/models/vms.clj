(ns slipstream.ui.models.vms
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.pagination :as pagination]))

(defn- parse-vm
  [vm-metadata]
  (-> vm-metadata
      (select-keys [:state
                    :measurement])
      (assoc        :username           (-> vm-metadata :user       not-empty))
      (assoc        :cloud-instance-id  (-> vm-metadata :instanceId not-empty))
      (assoc        :run-uuid           (some-> vm-metadata :runUuid (s/replace "Unknown" "") not-empty))
      (assoc        :cloud-name         (-> vm-metadata :cloud      not-empty))
      (assoc        :ip-address         (-> vm-metadata :ip         not-empty))
      (assoc        :run-owner          (-> vm-metadata :runOwner   not-empty))
      (assoc        :name               (-> vm-metadata :name       not-empty))))

(defn parse
  [metadata]
  {:pagination  (pagination/parse metadata)
   :vms (->> (html/select metadata [:vms :vm])
             identity
             (map :attrs)
             (map parse-vm))})
