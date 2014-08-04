(ns slipstream.ui.data.welcome
  (:require [net.cgrand.enlive-html :as html]))

(def xml-welcome-legacy
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

(def xml-welcome
  (first (html/html-snippet "<welcome>
   <modules>
      <item resourceUri='module/EBU_TTF/53' category='Project' description='Global module for TTF relates deployments and tasks' version='53' name='EBU_TTF'>
         <authz owner='rob' ownerGet='true' ownerPut='true' ownerPost='false' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item resourceUri='module/SlipStream/280' category='Project' description='SlipStream dog fooding :-)' version='280' name='SlipStream'>
         <authz owner='sixsq_dev' ownerGet='true' ownerPut='true' ownerPost='false' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='true' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'>
               <string>lionel</string>
               <string>konstan</string>
               <string>loomis</string>
               <string>meb</string>
               <string>rob</string>
               <string>sebastien.fievet</string>
            </groupMembers>
         </authz>
      </item>
      <item resourceUri='module/apps/528' category='Project' description=' version='528' name='apps'>
         <authz owner='super' ownerGet='true' ownerPut='true' ownerPost='false' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='true' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'>
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item resourceUri='module/examples/56' category='Project' description='Examples highlighting SlipStream features.  See User Guide on the documentation page.' version='56' name='examples'>
         <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='false' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item class='com.sixsq.slipstream.module.ModuleViewPublished' resourceUri='module/examples/tutorials/wordpress/wordpress/478' category='Deployment' description='simple, single node deployment of WordPress' published='true' logoLink='http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png' version='478' name='wordpress'>
         <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='true' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='true' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item class='com.sixsq.slipstream.module.ModuleViewPublished' resourceUri='module/apps/minecraft-app/481' category='Deployment' description='A Minecraft Server' published='true' logoLink='http://www.esourceengine.com/downloads/minecraft/14085_minecraft.jpg' version='481' name='minecraft-app'>
         <authz owner='sebastien.fievet' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='true' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='true' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
            <groupMembers class='java.util.ArrayList'>
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item class='com.sixsq.slipstream.module.ModuleViewPublished' resourceUri='module/apps/openerp/336' category='Deployment' description='An OpenERP instance backed with PostgresSQL' published='true' logoLink='http://www.techreceptives.com/wp-content/uploads/2013/06/openerp-logo2.png' version='336' name='openerp'>
         <authz owner='sebastien.fievet' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='true' groupPost='true' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='true' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
            <groupMembers class='java.util.ArrayList'>
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item class='com.sixsq.slipstream.module.ModuleViewPublished' resourceUri='module/examples/images/windows-server-2012/391' category='Image' description='Standard installation of the Windows Server 2012 R2 operating system (For Exoscale: Please ensure that port 5985 is open in your security group named &quot;default&quot;)' published='true' logoLink='http://blogs.technet.com/resized-image.ashx/__size/375x0/__key/communityserver-blogs-components-weblogfiles/00-00-00-57-46/6457.ws2012img.jpg' version='391' name='windows-server-2012'>
         <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='true' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='true' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item class='com.sixsq.slipstream.module.ModuleViewPublished' resourceUri='module/examples/images/ubuntu-12.04/517' category='Image' description='Minimal installation of the Ubuntu 12.04 (LTS) operating system.' published='true' logoLink='http://design.ubuntu.com/wp-content/uploads/ubuntu-logo14.png' version='517' name='ubuntu-12.04'>
         <authz owner='super' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='true' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='true' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
      <item class='com.sixsq.slipstream.module.ModuleViewPublished' resourceUri='module/examples/images/centos-6/479' category='Image' description='Minimal installation of the CentOS 6 operating system.' published='true' logoLink='http://blog.quadranet.com/wp-content/uploads/2014/01/centos.png' version='479' name='centos-6'>
         <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='true' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='true' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
            <groupMembers class='java.util.ArrayList'/>
         </authz>
      </item>
   </modules>
   <user issuper='true' resourceUri='user/meb' name='meb'></user>
</welcome>")))