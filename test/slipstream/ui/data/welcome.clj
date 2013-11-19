(ns slipstream.ui.data.welcome
  (:require [net.cgrand.enlive-html :as html]))

(def xml-welcome
  (first (html/html-snippet "<welcome>
   <modules>
      <item resourceUri='module/Public/1' category='Project' name='Public' version='1'>
         <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item resourceUri='module/examples/21' category='Project' name='examples' version='21'>
         <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
   </modules>
   <serviceCatalogues>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/loco' cloud='loco' creation='2013-11-12 20:21:20.192 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='support.email' description='Support email adress for this cloud service' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='overall.ram' description='Overall available RAM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='overall.cpu' description='Overall available CPUs (cores)' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='general.description' description='General description of the cloud service (including optional further links)' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/toto' cloud='toto' creation='2013-11-12 20:21:20.98 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='support.email' description='Support email adress for this cloud service' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[mickey@disney.com]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[You won't be able to provision more for a single VM]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='overall.ram' description='Overall available RAM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='overall.cpu' description='Overall available CPUs (cores)' category='General' mandatory='true' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' category='General' mandatory='true' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='general.description' description='General description of the cloud service (including optional further links)' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
   </serviceCatalogues>
   <user issuper='true' resourceUri='user/super' name='super'></user>
</welcome>")))