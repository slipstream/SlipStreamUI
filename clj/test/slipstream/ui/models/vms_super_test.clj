(ns slipstream.ui.models.vms-super-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.vms :as model]))

;; NOTE: For request /vms?media=xml&offset=10&limit=5&cloud=CloudA
(def raw-metadata-str
  "<vms offset='10' limit='5' count='5' totalCount='19' cloud='CloudA'>
      <vm cloud='CloudA' user='bob' state='Running' instanceId='4f2708e1-6aa6-4469-8f5e-b18ffd42cb50_machine' measurement='2014-12-08 13:09:24.82 CET' runUuid='a1b345f0-c434-490e-849f-c3894af55588' ip='127.0.0.1' name='machine' runOwner='super'/>
      <vm cloud='CloudA' user='bob' state='Terminated' instanceId='157701e9-9c52-45b1-a506-13241446aad3_machine' measurement='2014-12-08 13:09:24.98 CET' runUuid='a1b345f0-c434-490e-849f-c3894af55588' name='machine' runOwner='bob'/>
      <vm cloud='CloudA' user='bob' state='Running' instanceId='vm_gateway' measurement='2014-12-08 13:09:24.112 CET' runUuid='35c3789f-5da1-4504-8294-d41489d035ae' ip='127.0.0.1' name='vm_gateway' runOwner='super'/>
      <vm cloud='CloudA' user='bob' state='Running' instanceId='vApp_6_centos' measurement='2014-12-08 13:09:24.126 CET'/>
      <vm cloud='CloudA' user='bob' state='Running' instanceId='1b0afd4f-340d-4bef-89e2-d6f4a776f2ea' measurement='2014-12-08 13:09:33.215 CET' runUuid='Unknown'/>
      <user issuper='true' resourceUri='user/super' name='super'></user>
  </vms>")

(expect
  {:pagination {:offset 10
                :limit 5
                :count-shown 5
                :count-total 19
                :cloud-name "CloudA"}
   :vms [{:cloud-name "CloudA"
          :run-uuid "a1b345f0-c434-490e-849f-c3894af55588"
          :cloud-instance-id "4f2708e1-6aa6-4469-8f5e-b18ffd42cb50_machine"
          :username "bob"
          :measurement "2014-12-08 13:09:24.82 CET"
          :state "Running",
          :ip-address "127.0.0.1",
          :name "machine",
          :run-owner "super"}
         {:cloud-name "CloudA"
          :run-uuid "a1b345f0-c434-490e-849f-c3894af55588"
          :cloud-instance-id "157701e9-9c52-45b1-a506-13241446aad3_machine"
          :username "bob"
          :measurement "2014-12-08 13:09:24.98 CET"
          :state "Terminated",
          :ip-address nil,
          :name "machine",
          :run-owner "bob"}
         {:cloud-name "CloudA"
          :run-uuid "35c3789f-5da1-4504-8294-d41489d035ae"
          :cloud-instance-id "vm_gateway"
          :username "bob"
          :measurement "2014-12-08 13:09:24.112 CET"
          :state "Running",
          :ip-address "127.0.0.1",
          :name "vm_gateway",
          :run-owner "super"}
         {:cloud-name "CloudA"
          :run-uuid nil
          :cloud-instance-id "vApp_6_centos"
          :username "bob"
          :measurement "2014-12-08 13:09:24.126 CET"
          :state "Running",
          :ip-address nil,
          :name nil,
          :run-owner nil}
         {:cloud-name "CloudA"
          :run-uuid nil
          :cloud-instance-id "1b0afd4f-340d-4bef-89e2-d6f4a776f2ea"
          :username "bob"
          :measurement "2014-12-08 13:09:33.215 CET"
          :state "Running",
          :ip-address nil,
          :name nil,
          :run-owner nil}]}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
