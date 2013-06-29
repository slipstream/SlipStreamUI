(ns slipstream.ui.data.dashboard
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-dashboard
  (first (html/html-snippet "<dashboard>
	<runs>
	</runs>
	<vms>
	   <item runUuid='aaaaaaaa-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='aaaa' cloud='StratusLab' />
	   <item runUuid='bbbbbbbb-8a39-4870-8940-0031f2cffd40' status='Unknown' instanceId='bbbb' cloud='StratusLab' />
	   <item runUuid='cccccccc-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='cccc' cloud='Interoute' />
	   <item runUuid='dddddddd-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='dddd' cloud='EC2' />
	</vms>
  <user issuper='true' resourceUri='user/super' name='super'></user>
</dashboard>")))
