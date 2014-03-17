(ns slipstream.ui.data.dashboard
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-dashboard
  (first (html/html-snippet "<dashboard>
	<runs>
     <item type='Orchestration' username='donald' cloudServiceName='stratuslab' resourceUri='run/638f04c3-44a1-41c7-90db-c81167fc6f19' uuid='638f04c3-44a1-41c7-90db-c81167fc6f19' moduleResourceUri='module/Public/Tutorials/HelloWorld/client_server/11' status='Aborting' startTime='2013-07-05 17:27:12.471 CEST'/>
     <item type='Run' username='donald' cloudServiceName='stratuslab' resourceUri='run/638f04c3-44a1-41c7-90db-c81167fc6f19' uuid='638f04c3-44a1-41c7-90db-c81167fc6f19' moduleResourceUri='module/Public/Tutorials/HelloWorld/client_server/11' status='Aborting' startTime='2013-07-05 17:27:12.471 CEST'/>
     <item type='Machine' username='donald' cloudServiceName='stratuslab' resourceUri='run/638f04c3-44a1-41c7-90db-c81167fc6f19' uuid='638f04c3-44a1-41c7-90db-c81167fc6f19' moduleResourceUri='module/Public/Tutorials/HelloWorld/client_server/11' status='Aborting' startTime='2013-07-05 17:27:12.471 CEST'/>
     <item username='mickey' cloudServiceName='interoute' resourceUri='run/e8d0b957-14a8-4e96-8677-85c7bd9eb64e' uuid='e8d0b957-14a8-4e96-8677-85c7bd9eb64e' moduleResourceUri='module/Mebster/word_press/simple_deployment/410' status='Aborting' startTime='2013-07-04 17:11:56.340 CEST' tags='this is a tag!' />
	</runs>
  <usage class='java.util.ArrayList'>
     <usageElement cloud='CloudA' quota='5' currentUsage='0' />
     <usageElement cloud='CloudB' quota='15' currentUsage='13' />
  </usage>
  <user issuper='true' resourceUri='user/super' name='super'></user>
  <serviceConfiguration deleted='false' creation='2013-03-06 14:31:01.390 CET'>
    <parameters class='org.hibernate.collection.PersistentMap'><entry>
      <entry>
	      <string>
	        <![CDATA[ slipstream.metering.enable ]]>
	      </string>
	      <parameter name='slipstream.metering.enable' description='Metering enabled.' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
	        <instructions>
	          <![CDATA[ ]]>
	        </instructions>
	        <value>
	          <![CDATA[ true ]]>
	        </value>
	      </parameter>
      </entry>
    </parameters>
  </serviceConfiguration'>
</dashboard>")))
