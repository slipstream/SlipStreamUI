(ns slipstream.ui.data.image
  (:require [net.cgrand.enlive-html :as html]))

(def xml-image (first (html/html-snippet "<imageModule logoLink='http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png' category='Image' creation='2013-03-07 21:03:09.124 CET' deleted='false' imageId='HZTKYZgX7XzSokCHMB60lS0wsiv' isBase='false' lastModified='2013-03-07 21:03:09.337 CET' loginUser='donald' name='Public/BaseImages/with-a-very-long-name/Ubuntu/12.04' parentUri='module/Public/BaseImages/Ubuntu/toto' platform='debian' resourceUri='module/Public/BaseImages/Ubuntu/12.04' shortName='12.04' version='4' description='Nice Ubuntu distro'>
<commit author='an-author'><comment>this is a comment</comment></commit>
<parameters class='org.hibernate.collection.PersistentMap'>
<entry>
<string>password</string>
<parameter category='Cloud' description='A password' isSet='false' mandatory='true' name='password' readonly='false' type='Password'>
   <value>youshouldnotseethis</value>
   <instructions><![CDATA[Password...]]></instructions>
</parameter>
</entry>
<string>extra.disk.volatile</string>
<parameter category='Cloud' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Volatile extra disk in GB' isSet='false' mandatory='true' name='extra.disk.volatile' readonly='false' type='String'>
   <value>12345XXX</value>
   <instructions><![CDATA[Some help :-)]]></instructions>
</parameter>
</entry>
<entry>
<string>stratuslab.disks.bus.type</string>
<parameter category='stratuslab' class='com.sixsq.slipstream.persistence.ModuleParameter' description='VM disks bus type' isSet='true' mandatory='true' name='stratuslab.disks.bus.type' readonly='false' type='Enum'>
<enumValues length='2'>
<string>virtio</string>
<string>scsi</string>
</enumValues>
<value>VIRTIO</value>
<defaultValue>VIRTIO</defaultValue>
</parameter>
</entry>
<entry>
<string>stratuslab.instance.type</string>
<parameter category='stratuslab' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Cloud instance type' isSet='true' mandatory='true' name='stratuslab.instance.type' readonly='false' type='Enum'>
<enumValues length='7'>
<string>m1.small</string>
<string>c1.medium</string>
<string>m1.large</string>
<string>m1.xlarge</string>
<string>c1.xlarge</string>
<string>t1.micro</string>
<string>standard.xsmall</string>
</enumValues>
<value>M1_SMALL</value>
<defaultValue>M1_SMALL</defaultValue>
</parameter>
</entry>
<entry>
<string>hostname</string>
<parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='hostname/ip of the image' isSet='false' mandatory='true' name='hostname' readonly='false' type='String'>
   <value>123.234.345</value>
   <instructions><![CDATA[Some help :-)]]></instructions>
</parameter>
</entry>
<entry>
<string>stratuslab.cpu</string>
<parameter category='stratuslab' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Requested CPUs' isSet='false' mandatory='true' name='stratuslab.cpu' readonly='false' type='String'></parameter>
</entry>
<entry>
<string>stratuslab.ram</string>
<parameter category='stratuslab' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Requested RAM (in GB)' isSet='false' mandatory='true' name='stratuslab.ram' readonly='false' type='String'></parameter>
</entry>
<entry>
<string>instanceid</string>
<parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Cloud instance id' isSet='false' mandatory='true' name='instanceid' readonly='false' type='String'></parameter>
</entry>
<entry>
<string>network</string>
<parameter category='Cloud' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Network type' isSet='true' mandatory='true' name='network' readonly='false' type='Enum'>
<enumValues length='2'>
<string>Public</string>
<string>Private</string>
</enumValues>
<value>Public</value>
<defaultValue>Public</defaultValue>
</parameter>
</entry>
</parameters>
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='true' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<groupMembers class='java.util.ArrayList'></groupMembers>
</authz>
<cloudNames length='3'>
<string>stratuslab</string>
<string>atos</string>
<string>sky</string>
</cloudNames>
<targets class='org.hibernate.collection.PersistentBag'>
<target name='execute' runInBackground='false'>execute target</target>
<target name='report' runInBackground='false'>report target</target>
<target name='onvmadd' runInBackground='false'>onvmadd target</target>
<target name='onvmremove' runInBackground='false'>onvmremove target</target>
</targets>
<packages class='org.hibernate.collection.PersistentBag'>
<package key='httpd_key' name='httpd' repository='httpd_repo'/>
<package key='vim_key' name='vim' repository='vim_repo'/>
<package key='mlocate_key' name='mlocate' repository='mlocate_repo'/>
</packages>
<prerecipe>some pre-recipe</prerecipe>
<recipe>some recipe</recipe>
<cloudImageIdentifiers class='org.hibernate.collection.PersistentBag'>
<cloudImageIdentifier cloudImageIdentifier='HZTKYZgX7XzSokCHMB60lS0wsiv' cloudServiceName='stratuslab' resourceUri='module/Public/BaseImages/Ubuntu/12.04/4/stratuslab'></cloudImageIdentifier>
<cloudImageIdentifier cloudImageIdentifier='abc' cloudServiceName='my-cloud' resourceUri='module/Public/BaseImages/Ubuntu/12.04/4/stratuslab'></cloudImageIdentifier>
</cloudImageIdentifiers>
<extraDisks class='org.hibernate.collection.PersistentBag'></extraDisks>
<runs>
  <item username='donald' cloudServiceName='stratuslab' resourceUri='run/638f04c3-44a1-41c7-90db-c81167fc6f19' uuid='638f04c3-44a1-41c7-90db-c81167fc6f19' moduleResourceUri='module/Public/Tutorials/HelloWorld/client_server/11' status='Aborting' startTime='2013-07-05 17:27:12.471 CEST'/>
  <item username='mickey' cloudServiceName='interoute' resourceUri='run/e8d0b957-14a8-4e96-8677-85c7bd9eb64e' uuid='e8d0b957-14a8-4e96-8677-85c7bd9eb64e' moduleResourceUri='module/Mebster/word_press/simple_deployment/410' status='Aborting' startTime='2013-07-04 17:11:56.340 CEST' tags='this is a tag!' />
</runs>
<user issuper='true' resourceUri='user/super' name='super' defaultCloud='sky'></user>
</imageModule>")))
