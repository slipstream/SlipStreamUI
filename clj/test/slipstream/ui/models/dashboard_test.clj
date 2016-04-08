(ns slipstream.ui.models.dashboard-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.dashboard :as model]))

(def raw-metadata-str
  "<dashboard>
   <clouds class='java.util.ArrayList'>
      <string>Cloud-01</string>
      <string>Cloud-02</string>
      <string>Cloud-03</string>
      <string>Cloud-04</string>
      <string>Cloud-05</string>
      <string>Cloud-06</string>
      <string>Cloud-07</string>
      <string>Cloud-08</string>
   </clouds>
   <cloudUsages class='java.util.ArrayList'>
      <cloudUsage cloud='Cloud-01'   vmQuota='20'  userRunUsage='1' userVmUsage='1' userInactiveVmUsage='0' othersVmUsage='0' pendingVmUsage='0' unknownVmUsage='0'/>
      <cloudUsage cloud='Cloud-02'   vmQuota='20'  userRunUsage='0' userVmUsage='0' userInactiveVmUsage='0' othersVmUsage='3' pendingVmUsage='1' unknownVmUsage='0'/>
      <cloudUsage cloud='Cloud-03'   vmQuota='8'   userRunUsage='0' userVmUsage='0' userInactiveVmUsage='2' othersVmUsage='0' pendingVmUsage='0' unknownVmUsage='0'/>
      <cloudUsage cloud='Cloud-04'   vmQuota='20'  userRunUsage='3' userVmUsage='5' userInactiveVmUsage='0' othersVmUsage='0' pendingVmUsage='0' unknownVmUsage='0'/>
      <cloudUsage cloud='Cloud-05'   vmQuota='20'  userRunUsage='0' userVmUsage='0' userInactiveVmUsage='0' othersVmUsage='0' pendingVmUsage='0' unknownVmUsage='0'/>
      <cloudUsage cloud='Cloud-06'   vmQuota='8'   userRunUsage='0' userVmUsage='0' userInactiveVmUsage='0' othersVmUsage='0' pendingVmUsage='0' unknownVmUsage='0'/>
      <cloudUsage cloud='Cloud-07'   vmQuota='8'   userRunUsage='0' userVmUsage='0' userInactiveVmUsage='0' othersVmUsage='0' pendingVmUsage='0' unknownVmUsage='2'/>
      <cloudUsage cloud='Cloud-08'   vmQuota='20'  userRunUsage='2' userVmUsage='0' userInactiveVmUsage='0' othersVmUsage='1' pendingVmUsage='0' unknownVmUsage='2'/>
      <cloudUsage cloud='All Clouds' vmQuota='124' userRunUsage='6' userVmUsage='6' userInactiveVmUsage='2' othersVmUsage='4' pendingVmUsage='2' unknownVmUsage='5'/>
   </cloudUsages>
   <user issuper='false' resourceUri='user/super' name='super'></user>
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
  {:clouds ["Cloud-01"
            "Cloud-02"
            "Cloud-03"
            "Cloud-04"
            "Cloud-05"
            "Cloud-06"
            "Cloud-07"
            "Cloud-08"]
   :quota {:enabled? true
           :usage [{:cloud "All Clouds" :vm-quota 124 :user-run-usage 6 :user-vm-usage 6 :user-inactive-vm-usage 2 :others-vm-usage 4 :pending-vm-usage 2 :unknown-vm-usage 5 }
                   {:cloud "Cloud-01"   :vm-quota 20  :user-run-usage 1 :user-vm-usage 1 :user-inactive-vm-usage 0 :others-vm-usage 0 :pending-vm-usage 0 :unknown-vm-usage 0 }
                   {:cloud "Cloud-02"   :vm-quota 20  :user-run-usage 0 :user-vm-usage 0 :user-inactive-vm-usage 0 :others-vm-usage 3 :pending-vm-usage 1 :unknown-vm-usage 0 }
                   {:cloud "Cloud-03"   :vm-quota 8   :user-run-usage 0 :user-vm-usage 0 :user-inactive-vm-usage 2 :others-vm-usage 0 :pending-vm-usage 0 :unknown-vm-usage 0 }
                   {:cloud "Cloud-04"   :vm-quota 20  :user-run-usage 3 :user-vm-usage 5 :user-inactive-vm-usage 0 :others-vm-usage 0 :pending-vm-usage 0 :unknown-vm-usage 0 }
                   {:cloud "Cloud-07"   :vm-quota 8   :user-run-usage 0 :user-vm-usage 0 :user-inactive-vm-usage 0 :others-vm-usage 0 :pending-vm-usage 0 :unknown-vm-usage 2 }
                   {:cloud "Cloud-08"   :vm-quota 20  :user-run-usage 2 :user-vm-usage 0 :user-inactive-vm-usage 0 :others-vm-usage 1 :pending-vm-usage 0 :unknown-vm-usage 2 }]}
   :metering {:enabled? true}}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))
