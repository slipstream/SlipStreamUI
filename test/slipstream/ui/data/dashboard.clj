(ns slipstream.ui.data.dashboard
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-dashboard
  (first (html/html-snippet "<dashboard>
	<runs>
     <item username='donald' cloudServiceName='stratuslab' resourceUri='run/638f04c3-44a1-41c7-90db-c81167fc6f19' uuid='638f04c3-44a1-41c7-90db-c81167fc6f19' moduleResourceUri='module/Public/Tutorials/HelloWorld/client_server/11' status='Aborting' startTime='2013-07-05 17:27:12.471 CEST'/>
     <item username='mickey' cloudServiceName='interoute' resourceUri='run/e8d0b957-14a8-4e96-8677-85c7bd9eb64e' uuid='e8d0b957-14a8-4e96-8677-85c7bd9eb64e' moduleResourceUri='module/Mebster/word_press/simple_deployment/410' status='Aborting' startTime='2013-07-04 17:11:56.340 CEST' tags='this is a tag!' />
	</runs>
	<vms>
	   <item runUuid='aaaaaaaa-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='aaaa' cloud='StratusLab' />
	   <item runUuid='bbbbbbbb-8a39-4870-8940-0031f2cffd40' status='Unknown' instanceId='bbbb' cloud='StratusLab' />
	   <item runUuid='cccccccc-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='cccc' cloud='Interoute' />
	   <item runUuid='dddddddd-8a39-4870-8940-0031f2cffd40' status='Running' instanceId='dddd' cloud='EC2' />
	</vms>
  <user issuper='true' resourceUri='user/super' name='super'></user>
</dashboard>")))
