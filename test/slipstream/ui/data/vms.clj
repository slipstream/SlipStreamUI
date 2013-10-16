(ns slipstream.ui.data.vms
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-vms
  (first (html/html-snippet "<vms>
	   <vm runUuid='aaaaaaaa-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='aaaa' cloudServiceName='StratusLab' />
	   <vm runUuid='bbbbbbbb-8a39-4870-8940-0031f2cffd40' status='Unknown' instanceId='bbbb' cloudServiceName='StratusLab' />
	   <vm runUuid='cccccccc-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='cccc' cloudServiceName='Interoute' />
	   <vm runUuid='dddddddd-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='dddd' cloudServiceName='EC2' />
  <user issuper='true' resourceUri='user/super' name='super'></user>
</vms>")))
