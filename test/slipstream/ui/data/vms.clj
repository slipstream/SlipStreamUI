(ns slipstream.ui.data.vms
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-vms
  (first (html/html-snippet "<vms>
	   <vm runUuid='aaaaaaaa-8a39-4870-8940-0031f2cffd40' user='test' state='Running' instanceId='aaaa' cloud='StratusLab' measurement='2014-02-04 16:18:08.670 CET' />
	   <vm runUuid='bbbbbbbb-8a39-4870-8940-0031f2cffd40' user='test' state='Unknown' instanceId='bbbb' cloud='StratusLab' measurement='2014-02-04 16:18:08.670 CET' />
	   <vm runUuid='cccccccc-8a39-4870-8940-0031f2cffd40' user='test' state='Running' instanceId='cccc' cloud='Interoute' measurement='2014-02-04 16:18:08.670 CET' />
	   <vm runUuid='dddddddd-8a39-4870-8940-0031f2cffd40' user='test' state='Running' instanceId='dddd' cloud='EC2' measurement='2014-02-04 16:18:08.670 CET' />
	   <vm runUuid='Unknown' user='test' status='Running' instanceId='dddd' cloud='EC2' measurement='2014-02-04 16:18:08.670 CET' />
  <user issuper='true' resourceUri='user/super' name='super'></user>
</vms>")))
