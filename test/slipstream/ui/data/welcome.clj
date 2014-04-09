(ns slipstream.ui.data.welcome
  (:require [net.cgrand.enlive-html :as html]))

(def xml-welcome
  (first (html/html-snippet "<welcome>
   <modules>
      <item logoLink='http://castlefranksystems.com/wp-content/uploads/2013/11/openerp-logo.png' resourceUri='module/Public/Something_with_a_really_super_long_name/1' category='Deployment' name='Something_with_a_really_super_long_name' version='4321' published='true' description='a description'>
         <authz owner='sixsq inc' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item resourceUri='module/examples/21' category='Project' name='examples' version='21'>
         <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item logoLink='http://blog.twmg.com.au/wp-content/uploads/2013/09/wordpress-logo.jpg' resourceUri='module/Public/wordpress/122' category='Image' name='Wordpress' version='1111' published='true' description='wordpress description'>
         <authz owner='wordpress' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item resourceUri='module/Public/nuvlabox/122' category='Image' name='nuvlabox' version='2222' published='true' description='nuvlabox description'>
         <authz owner='nuvlabox inc' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
   </modules>
   <serviceCatalogues>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/cloudstack' cloud='cloudstack' creation='2013-11-12 20:21:20.192 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[cloudstack.support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.support.email' description='Support email adress for this cloud service' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' category='Single VM Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.overall.ram' description='Overall available RAM' category='Overall Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.overall.cpu' description='Overall available CPUs (cores)' category='Overall Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' category='Single VM Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.general.description' description='General description of the cloud service (including optional further links)' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/stratuslab' cloud='stratuslab' creation='2013-11-12 20:21:20.98 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[stratuslab.support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.support.email' description='Support email adress for this cloud service' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[mickey@disney.com]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[You won't be able to provision more for a single VM]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.overall.ram' description='Overall available RAM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.overall.cpu' description='Overall available CPUs (cores)' category='General' mandatory='true' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' category='General' mandatory='true' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.general.description' description='General description of the cloud service (including optional further links)' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
   </serviceCatalogues>
   <serviceConfiguration deleted='false' creation='2013-03-06 14:31:01.390 CET'>
     <parameters class='org.hibernate.collection.PersistentMap'><entry>
       <entry>
	       <string>
	         <![CDATA[ slipstream.service.catalog.enable ]]>
	       </string>
	       <parameter name='slipstream.service.catalog.enable' description='Metering enabled.' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
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
   <user issuper='true' resourceUri='user/meb' name='meb'></user>
</welcome>")))