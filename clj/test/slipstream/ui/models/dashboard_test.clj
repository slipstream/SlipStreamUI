(ns slipstream.ui.models.dashboard-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.dashboard :as model]))

(def raw-metadata-str
  "<dashboard>
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
    <parameters class='org.hibernate.collection.PersistentMap'>
      <entry>
        <string>
          <![CDATA[ slipstream.metering.enable ]]>
        </string>
        <parameter name='slipstream.metering.enable' description='Metering enabled.' category='SlipStream_Advanced' mandatory='true' type='Boolean' readonly='false'>
          <instructions>
            <![CDATA[ ]]>
          </instructions>
          <value>
            <![CDATA[ true ]]>
          </value>
        </parameter>
      </entry>
      <entry>
	      <string>
	        <![CDATA[ slipstream.quota.enable ]]>
	      </string>
	      <parameter name='slipstream.quota.enable' description='Quota enabled.' category='SlipStream_Advanced' mandatory='true' type='Boolean' readonly='false'>
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
</dashboard>")

(expect
  {:runs [{:cloud-name "interoute"
           :runs [{:cloud-name "interoute"
                   :uri "run/e8d0b957-14a8-4e96-8677-85c7bd9eb64e"
                   :module-uri "module/Mebster/word_press/simple_deployment/410"
                   :type nil
                   :start-time "2013-07-04 17:11:56.340 CEST"
                   :username "mickey"
                   :uuid "e8d0b957-14a8-4e96-8677-85c7bd9eb64e"
                   :status "Aborting"
                   :tags "this is a tag!"}]}
           {:cloud-name "stratuslab"
           :runs [{:cloud-name "stratuslab"
                   :uri "run/638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :module-uri "module/Public/Tutorials/HelloWorld/client_server/11"
                   :type :deployment-run
                   :start-time "2013-07-05 17:27:12.471 CEST"
                   :username "donald"
                   :uuid "638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :status "Aborting"}
                  {:cloud-name "stratuslab"
                   :uri "run/638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :module-uri "module/Public/Tutorials/HelloWorld/client_server/11"
                   :type :image-run
                   :start-time "2013-07-05 17:27:12.471 CEST"
                   :username "donald"
                   :uuid "638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :status "Aborting"}
                  {:cloud-name "stratuslab"
                   :uri "run/638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :module-uri "module/Public/Tutorials/HelloWorld/client_server/11"
                   :type :image-build
                   :start-time "2013-07-05 17:27:12.471 CEST"
                   :username "donald"
                   :uuid "638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :status "Aborting"}]}]
   :vms   []
   :quota {:enabled? true
           :usage [{:cloud "CloudA"
                    :current-usage 0
                    :quota 5}
                   {:cloud "CloudB"
                    :current-usage 13
                    :quota 15}]}
   :metering {:enabled? true}}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))
