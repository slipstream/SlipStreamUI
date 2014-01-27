(ns slipstream.ui.data.message
  (:require [net.cgrand.enlive-html :as html]))

(def xml-message (first (html/html-snippet "<string>
This is a message :-)
<serviceConfiguration creation='2014-01-23 15:03:05.811 CET' deleted='false'>
   <parameters class='java.util.HashMap'>
      <entry>
         <string><![CDATA[slipstream.mail.password]]></string>
         <parameter category='SlipStream_Support' class='com.sixsq.slipstream.persistence.ServiceConfigurationParameter' description='Password for SMTP server.' mandatory='true' name='slipstream.mail.password' readonly='false' type='Password'>
            <instructions/>
            <value><![CDATA[b,cysa10]]></value>
         </parameter>
      </entry>
   </parameters>
</serviceConfiguration>
</string>")))
