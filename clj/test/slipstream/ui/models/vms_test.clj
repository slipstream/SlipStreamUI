(ns slipstream.ui.models.vms-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.vms :as model]))

(def raw-metadata-str
  "<vms>
     <vm runUuid='aaaaaaaa-8a39-4870-8940-0031f2cffd40' user='test' state='Running' instanceId='aaaa' cloud='StratusLab' measurement='2014-02-04 16:18:08.670 CET' />
     <vm runUuid='bbbbbbbb-8a39-4870-8940-0031f2cffd40' user='test' state='Unknown' instanceId='bbbb' cloud='StratusLab' measurement='2014-02-04 16:18:08.670 CET' />
     <vm runUuid='cccccccc-8a39-4870-8940-0031f2cffd40' user='test' state='Running' instanceId='cccc' cloud='Interoute' measurement='2014-02-04 16:18:08.670 CET' />
     <vm runUuid='dddddddd-8a39-4870-8940-0031f2cffd40' user='test' state='Running' instanceId='dddd' cloud='EC2' measurement='2014-02-04 16:18:08.670 CET' />
     <vm runUuid='Unknown' user='test' status='Running' instanceId='dddd' cloud='EC2' measurement='2014-02-04 16:18:08.670 CET' />
  <user issuper='true' resourceUri='user/super' name='super'></user>
</vms>")

(expect
  [{:cloud-name "EC2"
    :vms [{:cloud-name "EC2"
           :run-uuid "dddddddd-8a39-4870-8940-0031f2cffd40"
           :cloud-instance-id "dddd"
           :username "test"
           :measurement "2014-02-04 16:18:08.670 CET"
           :state "Running"}
          {:cloud-name "EC2"
           :run-uuid "Unknown"
           :cloud-instance-id "dddd"
           :username "test"
           :measurement "2014-02-04 16:18:08.670 CET"}]}
   {:cloud-name "Interoute"
    :vms [{:cloud-name "Interoute"
           :run-uuid "cccccccc-8a39-4870-8940-0031f2cffd40"
           :cloud-instance-id "cccc"
           :username "test"
           :measurement "2014-02-04 16:18:08.670 CET"
           :state "Running"}]}
   {:cloud-name "StratusLab"
    :vms [{:cloud-name "StratusLab"
           :run-uuid "aaaaaaaa-8a39-4870-8940-0031f2cffd40"
           :cloud-instance-id "aaaa"
           :username "test"
           :measurement "2014-02-04 16:18:08.670 CET"
           :state "Running"}
          {:cloud-name "StratusLab"
           :run-uuid "bbbbbbbb-8a39-4870-8940-0031f2cffd40"
           :cloud-instance-id "bbbb"
           :username "test"
           :measurement "2014-02-04 16:18:08.670 CET"
           :state "Unknown"}]}]
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
