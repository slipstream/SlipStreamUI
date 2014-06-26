(ns slipstream.ui.data.user
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-user (first (html/html-snippet "<user deleted='false' resourceUri='user/test' name='test' email='test@example.com' firstName='Test' lastName='Me' organization='Disney' issuper='false' state='ACTIVE' creation='2013-03-06 14:30:59.30 UTC'>
   <parameters class='org.hibernate.collection.PersistentMap'>
      <entry>
         <string><![CDATA[General.Verbosity Level]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='General.Verbosity Level' description='Level of verbosity. 0 - Actions, 1 - Steps, 2 - Details data, 3 - Debugging.' category='General' mandatory='true' type='Enum' readonly='false'>
            <enumValues length='4'>
               <string>0</string>
               <string>1</string>
               <string>2</string>
               <string>3</string>
            </enumValues>
            <value><![CDATA[0]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='interoute.endpoint' description='Cloud endpoint' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[http://something.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.ssh.public.key]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.ssh.public.key' description='SSH Public Key(s) (keys must be separated by new line)' category='atos' mandatory='true' type='RestrictedText' readonly='false'>
            <value><![CDATA[value for atos.ssh.public.key]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.ip.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.ip.type' description='IP type: public, local, private' category='atos' mandatory='true' type='Enum' readonly='false'>
            <enumValues length='3'>
               <string>public</string>
               <string>local</string>
               <string>private</string>
            </enumValues>
            <value><![CDATA[public]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.virtual.datacenter.name]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' defaultvalue='Mars VDC' name='interoute.virtual.datacenter.name' description='Virtual Datacenter name' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloudsigma.username]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='cloudsigma.username' description='CloudSigma account username' category='cloudsigma' mandatory='true' type='String' readonly='false' order='10'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[General.Timeout]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='General.Timeout' description='Minutes - When this timeout is reached, the execution is forcefully terminated.' category='General' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[30]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloudsigma.location]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='cloudsigma.location' description='CloudSigma provider location. ZRH - Zurich, Switzerland. LVS - Las Vegas, US.' category='cloudsigma' mandatory='true' type='Enum' readonly='false'>
            <enumValues length='2'>
               <string>LVS</string>
               <string>ZRH</string>
            </enumValues>
            <value><![CDATA[LVS]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[General.On Error Run Forever]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='General.On Error Run Forever' description='If an error occurs, keep the execution running for investigation.' category='General' mandatory='true' type='Boolean' readonly='false'>
            <value><![CDATA[true]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[General.default.cloud.service]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='General.default.cloud.service' description='Select which cloud you want to use.' category='General' mandatory='true' type='Enum' readonly='false'>
            <enumValues length='3'>
               <string>interoute</string>
               <string>cloudsigma</string>
               <string>atos</string>
            </enumValues>
            <value><![CDATA[cloudsigma]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloudsigma.password]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='cloudsigma.password' description='CloudSigma account password' category='cloudsigma' mandatory='true' type='Password' readonly='false' order='20'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[General.On Success Run Forever]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='General.On Success Run Forever' description='If no errors occur, keep the execution running. Useful for deployment or long tests.' category='General' mandatory='true' type='Boolean' readonly='false'/>
      </entry>
      <entry>
         <string><![CDATA[atos.marketplace.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.marketplace.endpoint' description='Default marketplace endpoint' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[http://marketplace.stratuslab.eu]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.endpoint' description='StratusLab endpoint' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[cloud.lal.stratuslab.eu]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.secret.key]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='interoute.secret.key' description='Secret key (required to submit)' category='interoute' mandatory='true' type='Password' readonly='false'>
            <value><![CDATA[somesecret]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.access.id]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='interoute.access.id' description='Access id (required to submit)' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.password]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.password' description='StratusLab account password' category='atos' mandatory='true' type='Password' readonly='false'  order='20'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.username]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.username' description='StratusLab account username' category='atos' mandatory='true' type='RestrictedString' readonly='false' order='10'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[General.ssh.public.key]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='General.ssh.public.key' description='SSH Public Key(s) (keys must be separated by new line)' category='General' mandatory='true' type='RestrictedText' readonly='false'>
            <value><![CDATA[asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd]]></value>
         </parameter>
      </entry>
   </parameters>
<serviceConfiguration deleted='false' creation='2013-03-06 14:31:01.390 CET'>
   <parameters class='org.hibernate.collection.PersistentMap'>
      <entry>
         <string><![CDATA[cloudsigma.cloud.connector.orchestrator.ssh.username]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloudsigma.cloud.connector.orchestrator.ssh.username' description='Orchestrator SSH username' category='cloudsigma' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[root]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.mail.password]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.mail.password' description='Password for SMTP server.' category='SlipStream_Support' mandatory='true' type='Password' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[b,cysa10]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.cloud.connector.update.clienturl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='interoute.cloud.connector.update.clienturl' description='URL with the cloud client specific connector' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[https://212.159.217.20/downloads/libcloud.tgz]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.base.url]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.base.url' description='Default URL and port for the SlipStream RESTlet' category='SlipStream_Basics' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[https://212.159.217.20]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.mail.debug]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.mail.debug' description='Debug mail sending.' category='SlipStream_Support' mandatory='true' type='Boolean' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[on]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.marketplace.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.marketplace.endpoint' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[http://marketplace.stratuslab.eu]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.orchestrator.instance.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.orchestrator.instance.type' description='Orchestrator instance type' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[t1.micro]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.cloud.connector.orchestrator.instance.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='interoute.cloud.connector.orchestrator.instance.type' description='Orchestrator instance size. (#CPUs,RAM(GB))' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[1,1]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.messaging.queue]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.messaging.queue' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[/571976520084/slipstream_stratuslab_hn/]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.headurl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.headurl' description='' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[$HeadURL: https://code.sixsq.com/svn/SlipStream/trunk/SlipStreamServer/src/main/resources/com/sixsq/slipstream/main/default.config.properties $]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[physicalhost.cloud.connector.orchestrator.host]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='physicalhost.cloud.connector.orchestrator.host' category='physicalhost' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[localhost]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloudsigma.cloud.connector.orchestrator.instance.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloudsigma.cloud.connector.orchestrator.instance.type' description='Orchestrator instance size. (MEM(GB),#SMP,CPU(GHz))' category='cloudsigma' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[1,1,1]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.reports.location]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.reports.location' description='' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[/var/tmp/slipstream/reports]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.cloud.connector.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='interoute.cloud.connector.endpoint' description='Service endpoint for interoute' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[http://vdcbridge.interoute.com/jclouds/api]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.cloud.connector.orchestrator.ssh.password]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='interoute.cloud.connector.orchestrator.ssh.password' description='Orchestrator SSH password' category='interoute' mandatory='true' type='Password' readonly='false'>
            <value><![CDATA[rootpass]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.messaging.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.messaging.type' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[amazonsqs]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.marketplace.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.marketplace.endpoint' description='Marketplace endpoint' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[http://marketplace.stratuslab.eu]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.orchestrator.imageid]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.orchestrator.imageid' description='Image Id of the orchestrator for atos' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[HZTKYZgX7XzSokCHMB60lS0wsiv]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.messaging.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.messaging.type' description='Messaging type: amazaonsqs, rest, dirq' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[amazonsqs]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.mail.ssl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.mail.ssl' description='Use SSL for SMTP server.' category='SlipStream_Support' mandatory='true' type='Boolean' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[on]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.cloud.connector.orchestrator.ssh.username]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='interoute.cloud.connector.orchestrator.ssh.username' description='Orchestrator SSH username' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[root]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloud.connector.class]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloud.connector.class' description='Cloud connector java class name(s) (comma separated for multi-cloud configuration)' category='SlipStream_Basics' mandatory='true' type='Text' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[atos:com.sixsq.slipstream.connector.stratuslab.StratusLabConnector,interoute:com.sixsq.slipstream.connector.abiquo.AbiquoConnector,com.sixsq.slipstream.connector.cloudsigma.CloudSigmaConnector]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.version]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.version' description='Installed SlipStream version' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[1.7]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[openstack.cloud.connector.orchestrator.imageid]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='openstack.cloud.connector.orchestrator.imageid' category='openstack' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[682f990d-7c90-4762-8344-95197ba0f81c]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[ec2.cloud.connector.orchestrator.imageid]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='ec2.cloud.connector.orchestrator.imageid' category='ec2' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[us-east-1:ami-4bf63722]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.support.url]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.support.url' description='URL where support can be obtained.' category='SlipStream_Support' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[http://www.sixsq.com/support]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[openstack.cloud.connector.service.name]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='openstack.cloud.connector.service.name' category='openstack' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[nova]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.update.clienturl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.update.clienturl' description='URL with the cloud client specific connector' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[https://212.159.217.20/downloads/stratuslabclient.tgz]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[openstack.cloud.connector.service.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='openstack.cloud.connector.service.type' category='openstack' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[compute]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.messaging.queue]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.messaging.queue' description='Messaging queue' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[/571976520084/slipstream_stratuslab_hn/]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloudsigma.cloud.connector.location]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloudsigma.cloud.connector.location' description='Location: LVS, ZRH' category='cloudsigma' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[ZRH]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[interoute.cloud.connector.orchestrator.imageid]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='interoute.cloud.connector.orchestrator.imageid' description='Image Id of the orchestrator for interoute' category='interoute' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[SS_ORCH_UBUNTU1204]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.messaging.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.messaging.endpoint' description='Messaging endpoint' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[eu-west-1.queue.amazonaws.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.endpoint' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[cloud.lal.stratuslab.eu]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloudsigma.cloud.connector.orchestrator.imageid]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloudsigma.cloud.connector.orchestrator.imageid' description='Image Id of the orchestrator for cloudsigma' category='cloudsigma' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[5236b9ee-f735-42fd-a236-17558f9e12d3]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[openstack.cloud.connector.service.region]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='openstack.cloud.connector.service.region' category='openstack' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[RegionOne]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.update.clienturl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.update.clienturl' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[https://212.159.217.20/downloads/stratuslabclient.tgz]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[ec2.cloud.connector.update.clienturl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='ec2.cloud.connector.update.clienturl' category='ec2' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[https://212.159.217.20/downloads/awsclient.tgz]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.registration.email]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.registration.email' description='Email address for account approvals, etc.' category='SlipStream_Support' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[<h1>email address</h1> to use for registration]]></instructions>
            <value><![CDATA[register@sixsq.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.messaging.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.messaging.endpoint' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[eu-west-1.queue.amazonaws.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.orchestrator.imageid]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.orchestrator.imageid' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[HZTKYZgX7XzSokCHMB60lS0wsiv]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[openstack.cloud.connector.orchestrator.instance.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='openstack.cloud.connector.orchestrator.instance.type' category='openstack' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[m1.tiny]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.mail.port]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.mail.port' description='Port on SMTP server (defaults to standard ports).' category='SlipStream_Support' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[465]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.support.email]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.support.email' description='Email address for SlipStream support requests' category='SlipStream_Support' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[slipstream-support@sixsq.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.mail.host]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.mail.host' description='Host for SMTP server for email notifications.' category='SlipStream_Support' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[smtp.gmail.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.cloud.connector.endpoint]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='atos.cloud.connector.endpoint' description='Service endpoint for atos' category='atos' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[atos-cloud.sixsq.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.mail.username]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.mail.username' description='Username for SMTP server.' category='SlipStream_Support' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[Username of the mail server account used to send registration emails.]]></instructions>
            <value><![CDATA[mailer@sixsq.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.update.clientbootstrapurl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.update.clientbootstrapurl' description='Endpoint of the SlipStream client bootstrap script' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[https://212.159.217.20/downloads/slipstream.bootstrap]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloud.connector.library.libcloud.url]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloud.connector.library.libcloud.url' description='URL to fetch libcloud library from' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[URL should point to a valid gzipped tarball.]]></instructions>
            <value><![CDATA[http://slipstream.sixsq.com/libcloud.tgz]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloud.connector.security.publicsshkey]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloud.connector.security.publicsshkey' description='SSH public key used for orchestrator' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[/opt/slipstream/server/.ssh/id_rsa.pub]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[ec2.cloud.connector.orchestrator.instance.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='ec2.cloud.connector.orchestrator.instance.type' category='ec2' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[t1.micro]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[cloudsigma.cloud.connector.orchestrator.ssh.password]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='cloudsigma.cloud.connector.orchestrator.ssh.password' description='Orchestrator SSH password' category='cloudsigma' mandatory='true' type='Password' readonly='false'>
            <value><![CDATA[ubuntupass]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[stratuslab.cloud.connector.orchestrator.instance.type]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='stratuslab.cloud.connector.orchestrator.instance.type' category='stratuslab' mandatory='true' type='String' readonly='false'>
            <value><![CDATA[t1.micro]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[slipstream.update.clienturl]]></string>
         <parameter class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' name='slipstream.update.clienturl' description='Endpoint of the SlipStream client tarball' category='SlipStream_Advanced' mandatory='true' type='String' readonly='false'>
            <instructions><![CDATA[]]></instructions>
            <value><![CDATA[https://212.159.217.20/downloads/slipstreamclient.tgz]]></value>
         </parameter>
      </entry>
   </parameters>
   </serviceConfiguration>
   <user issuper='true' resourceUri='user/super' name='SuperDooper' defaultCloud='interoute'></user>
</user>")))
