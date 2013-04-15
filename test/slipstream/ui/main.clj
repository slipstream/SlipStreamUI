(ns slipstream.ui.main
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.root-projects :as root-projects]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.utils :as utils]
            [slipstream.ui.models.version :as version]
            [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:use [net.cgrand.moustache :only [app]]))

;; =============================================================================
;; Dummy Data
;; =============================================================================

;(defn zip-str [s]
;  (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(def xml-projects (first (html/html-snippet "<list>
<item category='Project' description='a desc' name='Public' resourceUri='module/Public/1' version='1'>
<authz groupCreateChildren='true' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='false' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='true' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<version>111.222.333</version>
<groupMembers class='java.util.ArrayList'></groupMembers>
</authz>
</item>
<item category='Project' name='Other' resourceUri='module/Other/2' version='2'>
<authz groupCreateChildren='true' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='false' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='true' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<version>aaa.222.333</version>
<groupMembers class='java.util.ArrayList'></groupMembers>
</authz>
</item>
<user issuper='true' resourceUri='user/super' username='super'></user>
<breadcrumbs path=''>
<crumb name='module' path='module'></crumb>
</breadcrumbs>
</list>")))

(def xml-project (first (html/html-snippet "<projectModule category='Project' creation='2013-03-08 22:37:25.341 CET' deleted='false' lastModified='2013-03-08 22:37:25.342 CET' name='Public' parentUri='module/' resourceUri='module/Public/1' shortName='Public' version='1'>
   
<parameters class='org.hibernate.collection.PersistentMap'></parameters>
   
<authz groupCreateChildren='true' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='false' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='true' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
      
<groupMembers class='java.util.ArrayList'></groupMembers>
   
</authz>
   
<cloudNames length='2'>
      
<string>stratuslab</string>
      
<string>default</string>
   
</cloudNames>
   
<children class='java.util.ArrayList'>
      
<item category='Project' name='BaseImages' resourceUri='module/Public/BaseImages/2' version='2'>
         
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
            
<groupMembers class='java.util.ArrayList'></groupMembers>
         
</authz>
      
</item>
      
<item category='Project' name='Tutorials' resourceUri='module/Public/Tutorials/7' version='7'>
         
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
            
<groupMembers class='java.util.ArrayList'></groupMembers>
         
</authz>
      
</item>
   
</children>

<user issuper='true' resourceUri='user/super' username='super'></user>
<breadcrumbs path=''>
<crumb name='module' path='module'></crumb>
<crumb name='Public' path='module/Public'></crumb>
<crumb name='1' path='module/Public/1'></crumb>
</breadcrumbs>
</projectModule>")))

(def xml-image (first (html/html-snippet "<imageModule category='Image' creation='2013-03-07 21:03:09.124 CET' deleted='false' imageId='HZTKYZgX7XzSokCHMB60lS0wsiv' isBase='true' lastModified='2013-03-07 21:03:09.337 CET' loginUser='root' name='Public/BaseImages/Ubuntu/12.04' parentUri='module/Public/BaseImages/Ubuntu' platform='debian' resourceUri='module/Public/BaseImages/Ubuntu/12.04/4' shortName='12.04' version='4'>
<parameters class='org.hibernate.collection.PersistentMap'>
<entry>
<string>extra.disk.volatile</string>
<parameter category='Cloud' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Volatile extra disk in GB' isSet='false' mandatory='true' name='extra.disk.volatile' readonly='false' type='String'></parameter>
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
<parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='hostname/ip of the image' isSet='false' mandatory='true' name='hostname' readonly='false' type='String'></parameter>
</entry>
<entry>
<string>dummy</string>
<parameter category='Input' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Placeholder for display' isSet='false' mandatory='true' name='dummy' readonly='false' type='Dummy'></parameter>
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
   
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
      
<groupMembers class='java.util.ArrayList'></groupMembers>
   
</authz>
   
<cloudNames length='2'>
      
<string>stratuslab</string>
      
<string>default</string>
   
</cloudNames>
   
<targets class='org.hibernate.collection.PersistentBag'></targets>
   
<packages class='org.hibernate.collection.PersistentBag'></packages>
   
<prerecipe></prerecipe>
   
<recipe></recipe>
   
<cloudImageIdentifiers class='org.hibernate.collection.PersistentBag'>
      
<cloudImageIdentifier cloudImageIdentifier='HZTKYZgX7XzSokCHMB60lS0wsiv' cloudServiceName='stratuslab' resourceUri='module/Public/BaseImages/Ubuntu/12.04/4/stratuslab'></cloudImageIdentifier>
   
</cloudImageIdentifiers>
   
<extraDisks class='org.hibernate.collection.PersistentBag'></extraDisks>

<user issuper='true' resourceUri='user/super' username='super'></user>
<breadcrumbs path=''>
<crumb name='module' path='module'></crumb>
<crumb name='Public' path='module/Public'></crumb>
<crumb name='BaseImages' path='module/Public/BaseImages'></crumb>
<crumb name='Ubuntu' path='module/Public/BaseImages/Ubuntu'></crumb>
<crumb name='12.04' path='module/Public/BaseImages/Ubuntu/12.04'></crumb>
<crumb name='4' path='module/Public/BaseImages/Ubuntu/12.04/4'></crumb>
</breadcrumbs>
</imageModule>")))

(def xml-deployment (first (html/html-snippet "<deploymentModule category='Deployment' creation='2013-03-08 22:37:40.773 CET' deleted='false' lastModified='2013-03-08 22:37:40.774 CET' name='Public/Tutorials/HelloWorld/client_server' parentUri='module/Public/Tutorials/HelloWorld' resourceUri='module/Public/Tutorials/HelloWorld/client_server/11' shortName='client_server' version='11'>
   
<parameters class='org.hibernate.collection.PersistentMap'></parameters>
   
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='true' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='true' publicPut='false'>
      
<groupMembers class='java.util.ArrayList'></groupMembers>
   
</authz>
   
<cloudNames length='2'>
      
<string>stratuslab</string>
      
<string>default</string>
   
</cloudNames>
   
<nodes class='org.hibernate.collection.PersistentMap'>
      
<entry>
         
<string>testclient1</string>
         
<node cloudService='default' creation='2013-03-08 22:37:40.773 CET' deleted='false' imageUri='module/Public/Tutorials/HelloWorld/testclient' multiplicity='1' name='testclient1' network='Public'>
            
<parameters class='org.hibernate.collection.PersistentMap'>
               
<entry>
                  
<string>webserver.port</string>
                  
<parameter category='General' class='com.sixsq.slipstream.persistence.NodeParameter' description='' isMappedValue='true' mandatory='false' name='webserver.port' readonly='false' type='String'>
                     
<value>apache1:port</value>
                  
</parameter>
               
</entry>
               
<entry>
                  
<string>webserver.ready</string>
                  
<parameter category='General' class='com.sixsq.slipstream.persistence.NodeParameter' description='' isMappedValue='true' mandatory='false' name='webserver.ready' readonly='false' type='String'>
                     
<value>apache1:ready</value>
                  
</parameter>
               
</entry>
               
<entry>
                  
<string>webserver.hostname</string>
                  
<parameter category='General' class='com.sixsq.slipstream.persistence.NodeParameter' description='' isMappedValue='true' mandatory='false' name='webserver.hostname' readonly='false' type='String'>
                     
<value>apache1:hostname</value>
                  
</parameter>
               
</entry>
            
</parameters>
            
<parameterMappings class='org.hibernate.collection.PersistentMap'>
               
<entry>
                  
<string>webserver.port</string>
                  
<nodeParameter category='General' description='' isMappedValue='true' mandatory='false' name='webserver.port' readonly='false' type='String'>
                     
<value>apache1:port</value>
                  
</nodeParameter>
               
</entry>
               
<entry>
                  
<string>webserver.ready</string>
                  
<nodeParameter category='General' description='' isMappedValue='true' mandatory='false' name='webserver.ready' readonly='false' type='String'>
                     
<value>apache1:ready</value>
                  
</nodeParameter>
               
</entry>
               
<entry>
                  
<string>webserver.hostname</string>
                  
<nodeParameter category='General' description='' isMappedValue='true' mandatory='false' name='webserver.hostname' readonly='false' type='String'>
                     
<value>apache1:hostname</value>
                  
</nodeParameter>
               
</entry>
            
</parameterMappings>
            
<image category='Image' creation='2013-03-08 22:37:40.730 CET' deleted='false' imageId='HZTKYZgX7XzSokCHMB60lS0wsiv' isBase='false' lastModified='2013-03-08 22:37:40.734 CET' loginUser='root' moduleReferenceUri='module/Public/BaseImages/Ubuntu/12.04' name='Public/Tutorials/HelloWorld/testclient' parentUri='module/Public/Tutorials/HelloWorld' platform='debian' resourceUri='module/Public/Tutorials/HelloWorld/testclient/10' shortName='testclient' version='10'>
               
<parameters class='org.hibernate.collection.PersistentMap'>
                  
<entry>
                     
<string>extra.disk.volatile</string>
                     
<parameter category='Cloud' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Volatile extra disk in GB' isSet='false' mandatory='true' name='extra.disk.volatile' readonly='false' type='String'></parameter>
                  
</entry>
                  
<entry>
                     
<string>webserver.port</string>
                     
<parameter category='Input' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Port on which the web server listens' isSet='false' mandatory='false' name='webserver.port' readonly='false' type='String'></parameter>
                  
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
                     
<string>webserver.ready</string>
                     
<parameter category='Input' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Server ready to recieve connections' isSet='false' mandatory='false' name='webserver.ready' readonly='false' type='String'></parameter>
                  
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
                     
<parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='hostname/ip of the image' isSet='false' mandatory='true' name='hostname' readonly='false' type='String'></parameter>
                  
</entry>
                  
<entry>
                     
<string>dummy</string>
                     
<parameter category='Input' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Placeholder for display' isSet='false' mandatory='true' name='dummy' readonly='false' type='Dummy'></parameter>
                  
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
                  
<entry>
                     
<string>webserver.hostname</string>
                     
<parameter category='Input' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Server hostname' isSet='false' mandatory='false' name='webserver.hostname' readonly='false' type='String'></parameter>
                  
</entry>
               
</parameters>
               
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
                  
<groupMembers class='java.util.ArrayList'></groupMembers>
               
</authz>
               
<targets class='org.hibernate.collection.PersistentBag'>
                  
<target name='execute' runInBackground='false'>#!/bin/sh -xe
# Wait for the metadata to be resolved
web_server_ip=$(ss-get --timeout 360 webserver.hostname)
web_server_port=$(ss-get --timeout 360 webserver.port)
ss-get --timeout 360 webserver.ready

# Execute the test
ENDPOINT=http://${web_server_ip}:${web_server_port}/data.txt
wget -t 2 -O /tmp/data.txt ${ENDPOINT}
[ '$?' = '0' ] &amp; ss-set statecustom 'OK: $(cat /tmp/data.txt)' || ss-abort 'Could not get the test file: ${ENDPOINT}'
</target>
                  
<target name='report' runInBackground='false'>#!/bin/sh -x
cp /tmp/data.txt $SLIPSTREAM_REPORT_DIR</target>
               
</targets>
               
<packages class='org.hibernate.collection.PersistentBag'></packages>
               
<prerecipe></prerecipe>
               
<recipe></recipe>
               
<cloudImageIdentifiers class='org.hibernate.collection.PersistentBag'></cloudImageIdentifiers>
               
<extraDisks class='org.hibernate.collection.PersistentBag'></extraDisks>
            
</image>
         
</node>
      
</entry>
      
<entry>
         
<string>apache1</string>
         
<node cloudService='default' creation='2013-03-08 22:37:40.773 CET' deleted='false' imageUri='module/Public/Tutorials/HelloWorld/apache' multiplicity='1' name='apache1' network='Public'>
            
<parameters class='org.hibernate.collection.PersistentMap'></parameters>
            
<parameterMappings class='org.hibernate.collection.PersistentMap'></parameterMappings>
            
<image category='Image' creation='2013-03-08 22:37:40.671 CET' deleted='false' imageId='HZTKYZgX7XzSokCHMB60lS0wsiv' isBase='false' lastModified='2013-03-08 22:37:40.674 CET' loginUser='root' moduleReferenceUri='module/Public/BaseImages/Ubuntu/12.04' name='Public/Tutorials/HelloWorld/apache' parentUri='module/Public/Tutorials/HelloWorld' platform='debian' resourceUri='module/Public/Tutorials/HelloWorld/apache/9' shortName='apache' version='9'>
               
<parameters class='org.hibernate.collection.PersistentMap'>
                  
<entry>
                     
<string>port</string>
                     
<parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Port' isSet='true' mandatory='false' name='port' readonly='false' type='String'>
                        
<value>8080</value>
                        
<defaultValue>8080</defaultValue>
                     
</parameter>
                  
</entry>
                  
<entry>
                     
<string>extra.disk.volatile</string>
                     
<parameter category='Cloud' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Volatile extra disk in GB' isSet='false' mandatory='true' name='extra.disk.volatile' readonly='false' type='String'></parameter>
                  
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
                     
<parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='hostname/ip of the image' isSet='false' mandatory='true' name='hostname' readonly='false' type='String'></parameter>
                  
</entry>
                  
<entry>
                     
<string>dummy</string>
                     
<parameter category='Input' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Placeholder for display' isSet='false' mandatory='true' name='dummy' readonly='false' type='Dummy'></parameter>
                  
</entry>
                  
<entry>
                     
<string>stratuslab.cpu</string>
                     
<parameter category='stratuslab' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Requested CPUs' isSet='false' mandatory='true' name='stratuslab.cpu' readonly='false' type='String'></parameter>
                  
</entry>
                  
<entry>
                     
<string>ready</string>
                     
<parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Server ready to recieve connections' isSet='false' mandatory='false' name='ready' readonly='false' type='String'></parameter>
                  
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
               
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
                  
<groupMembers class='java.util.ArrayList'></groupMembers>
               
</authz>
               
<targets class='org.hibernate.collection.PersistentBag'>
                  
<target name='execute' runInBackground='false'>#!/bin/sh -xe
apt-get update -y
apt-get install -y apache2

echo 'Hello from Apache deployed by SlipStream!' &gt; /var/www/data.txt

service apache2 stop
port=$(ss-get port)
sed -i -e 's/^Listen.*$/Listen '$port'/' /etc/apache2/ports.conf
sed -i -e 's/^NameVirtualHost.*$/NameVirtualHost *:'$port'/' /etc/apache2/ports.conf
sed -i -e 's/^&lt;VirtualHost.*$/&lt;VirtualHost *:'$port'&gt;/' /etc/apache2/sites-available/default
service apache2 start
ss-set ready true</target>
                  
<target name='report' runInBackground='false'>#!/bin/sh -x
cp /var/log/apache2/access.log $SLIPSTREAM_REPORT_DIR
cp /var/log/apache2/error.log $SLIPSTREAM_REPORT_DIR</target>
               
</targets>
               
<packages class='org.hibernate.collection.PersistentBag'></packages>
               
<prerecipe></prerecipe>
               
<recipe></recipe>
               
<cloudImageIdentifiers class='org.hibernate.collection.PersistentBag'></cloudImageIdentifiers>
               
<extraDisks class='org.hibernate.collection.PersistentBag'></extraDisks>
            
</image>
         
</node>
      
</entry>
   
</nodes>

<user issuper='true' resourceUri='user/super' username='super'></user>
<breadcrumbs path=''>
<crumb name='module' path='module'></crumb>
<crumb name='Public' path='module/Public'></crumb>
<crumb name='Tutorials' path='module/Public/Tutorials'></crumb>
<crumb name='HelloWorld' path='module/Public/Tutorials/HelloWorld'></crumb>
<crumb name='client_server' path='module/Public/Tutorials/HelloWorld/client_server'></crumb>
<crumb name='11' path='module/Public/Tutorials/HelloWorld/client_server/11'></crumb>
</breadcrumbs>
</deploymentModule>")))

;; =============================================================================
;; Pages
;; =============================================================================

;(defn index
;  ([] (base/base {}))
;  ([ctxt] (base/base ctxt)))

(defn module-page [module edit?]
  (base/base 
    {:title "Project"
     :header (header/header module edit?)
     :content (project/pr xml-projects)
     :footer (footer/footer module edit? @version/slipstream-release-version)}))

(defn root-projects-page []
  (base/base 
    {:title "Project"
     :header (header/header xml-projects false)
     :content (root-projects/root-projects xml-projects)
     :footer (footer/footer xml-projects false @version/slipstream-release-version)}))

(defn error-page [message code & user]
  (base/base 
    {:title "Error"
     :header (header/header-top-only) 
     :content (common/error-snip message code)
     :footer (footer/footer nil nil @version/slipstream-release-version)}))

(defn module-view [module]
  (module-page module false))

(defn module-edit [module]
  (module-page module true))

(defn module-projects-view []
  (module-view xml-projects))

(defn module-projects-edit []
  (module-edit xml-projects))

(defn module-project-view []
  (module-view xml-project))

(defn module-project-edit []
  (module-edit xml-project))

(defn module-image-view []
  (module-view xml-image))

(defn module-image-edit []
  (module-edit xml-image))

(defn module-deployment-view []
  (module-view xml-deployment))

(defn module-deployment-edit []
  (module-edit xml-deployment))

;; =============================================================================
;; Routes
;; =============================================================================

(def routes
  (app
;    [""] (utils/render-request index)
    ["module"] (-> (root-projects-page) ring.util.response/response constantly)
    ["Public-view"] (-> (module-project-view) ring.util.response/response constantly)
    ["Public-edit"] (-> (module-project-edit) ring.util.response/response constantly)
    ["Image-view"] (-> (module-image-view) ring.util.response/response constantly)
    ["Image-edit"] (-> (module-image-edit) ring.util.response/response constantly)
    ["Deployment-view"] (-> (module-deployment-view) ring.util.response/response constantly)
    ["Deployment-edit"] (-> (module-deployment-edit) ring.util.response/response constantly)
    [&] (-> (error-page 
          "The server has not found anything matching the request URI"
          "404"
          (user/user xml-image)) ring.util.response/response constantly)))

;; =============================================================================
;; The App
;; =============================================================================

(defonce ^:dynamic *server* (utils/run-server routes))
