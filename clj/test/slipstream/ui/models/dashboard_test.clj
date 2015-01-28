(ns slipstream.ui.models.dashboard-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.dashboard :as model]))

(def raw-metadata-str
  "<dashboard>
    <runs offset='0' limit='20' count='12'>
        <item resourceUri='run/8ded8e5b-46ad-4bec-bcff-d761155f0e2e' uuid='8ded8e5b-46ad-4bec-bcff-d761155f0e2e' moduleResourceUri='module/first-project/newdeployment/66' status='Initializing' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2015-01-20 11:06:13.675 CET' cloudServiceNames='CloudA' username='bob' type='Orchestration' tags=''/>
        <item resourceUri='run/36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae' uuid='36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae' moduleResourceUri='module/first-project/newdeployment/85' status='Initializing' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2015-01-14 17:23:18.262 CET' cloudServiceNames='CloudB' username='bob' type='Orchestration' tags=''/>
        <item resourceUri='run/b36dfcc0-b77b-4746-b7fd-0acd7038a02d' uuid='b36dfcc0-b77b-4746-b7fd-0acd7038a02d' moduleResourceUri='module/first-project/a-non-native-image/5' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2015-01-07 13:36:32.26 CET' cloudServiceNames='CloudA' username='bob' type='Machine' tags=''/>
        <item resourceUri='run/0aa79e3f-878a-40b1-8e88-bb6808a183cc' uuid='0aa79e3f-878a-40b1-8e88-bb6808a183cc' moduleResourceUri='module/calsproject/deployment/54' status='Cancelled' abort='Marketplace endpoint should be set for CloudB' startTime='2014-12-16 12:40:30.118 CET' cloudServiceNames='CloudB' username='cal' type='Orchestration' tags='123, 123, asdf, asdf, asdf, asdf, bbb, d, ssd, zzz'/>
        <item resourceUri='run/079dd5c1-85af-4387-95a7-b24fc0b2be53' uuid='079dd5c1-85af-4387-95a7-b24fc0b2be53' moduleResourceUri='module/first-project/a-non-native-image/5' status='Initializing' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-15 01:03:45.240 CET' cloudServiceNames='CloudA' username='bob' type='Machine' tags='a----asdf, as, asd, asdf, df, fdfdsa, foij, patata'/>
        <item resourceUri='run/672c2946-578e-4093-8d7a-af3e0f432bac' uuid='672c2946-578e-4093-8d7a-af3e0f432bac' moduleResourceUri='module/first-project/newdeployment/4' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-11 14:41:15.310 CET' cloudServiceNames='CloudA' username='bob' type='Orchestration' tags='19-vms'/>
        <item resourceUri='run/2351f969-3a82-4ffe-a002-a2f76984f02a' uuid='2351f969-3a82-4ffe-a002-a2f76984f02a' moduleResourceUri='module/first-project/a-non-native-image/5' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 19:36:08.763 CET' cloudServiceNames='CloudA' username='bob' type='Machine' tags='myDeployment, patata, tata, tata, toto'/>
        <item resourceUri='run/0afb0b57-036e-4d2c-833d-699842cfeaab' uuid='0afb0b57-036e-4d2c-833d-699842cfeaab' moduleResourceUri='module/first-project/a-non-native-image/5' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 18:00:03.8 CET' cloudServiceNames='CloudA' username='bob' type='Machine' tags=''/>
        <item resourceUri='run/03dd6e4d-e265-43dd-bdd6-80ddc03120bd' uuid='03dd6e4d-e265-43dd-bdd6-80ddc03120bd' moduleResourceUri='module/first-project/a-non-native-image/5' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 16:34:34.940 CET' cloudServiceNames='CloudA' username='bob' type='Machine' tags=''/>
        <item resourceUri='run/985d9025-d78f-449f-801b-3be7158effe9' uuid='985d9025-d78f-449f-801b-3be7158effe9' moduleResourceUri='module/first-project/a-non-native-image/5' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 11:03:35.805 CET' cloudServiceNames='CloudA' username='bob' type='Machine' tags='1234, asdf, asdfasd, that, this'/>
        <item resourceUri='run/058cf171-9930-4999-9066-66b890433b4e' uuid='058cf171-9930-4999-9066-66b890433b4e' moduleResourceUri='module/first-project/newdeployment/4' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 08:11:20.114 CET' cloudServiceNames='CloudA' username='bob' type='Orchestration' tags='enter, myfirstdeployment'/>
        <item resourceUri='run/68f39a0a-74ae-4195-a414-753d99135909' uuid='68f39a0a-74ae-4195-a414-753d99135909' moduleResourceUri='module/first-project/a-native-machine/3' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/bob\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 07:53:55.271 CET' cloudServiceNames='CloudA' username='bob' type='Run' tags=''/>
    </runs>
    <clouds class='java.util.ArrayList'>
        <string>CloudA</string>
        <string>CloudB</string>
    </clouds>
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
  </serviceConfiguration>
</dashboard>")

(expect
  {:clouds ["CloudA" "CloudB"]
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
