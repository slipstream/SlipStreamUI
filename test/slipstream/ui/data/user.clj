(ns slipstream.ui.data.user
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-user (first (html/html-snippet "<user deleted='false' resourceUri='user/super' name='super' email='super@sixsq.com' firstName='Super' lastName='User' organization='SixSq' issuper='true' state='ACTIVE' creation='2013-03-06 14:30:59.30 UTC'>
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
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='cloudsigma.username' description='CloudSigma account username' category='cloudsigma' mandatory='true' type='String' readonly='false'>
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
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='cloudsigma.password' description='CloudSigma account password' category='cloudsigma' mandatory='true' type='Password' readonly='false'>
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
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.password' description='StratusLab account password' category='atos' mandatory='true' type='Password' readonly='false'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[atos.username]]></string>
         <parameter class='com.sixsq.slipstream.persistence.UserParameter' name='atos.username' description='StratusLab account username' category='atos' mandatory='true' type='RestrictedString' readonly='false'>
            <value><![CDATA[]]></value>
         </parameter>
      </entry>
   </parameters>
</user>")))
