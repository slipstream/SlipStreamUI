(ns slipstream.ui.models.action-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.action :as model]))

(def raw-metadata-str
  "<?xml version='1.0' encoding='UTF-8' standalone='no'?><string>
    Your email address has been confirmed.
    You should now be receiving another email with your account details.
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
  </string>")

(def parsed-metadata
  "
    Your email address has been confirmed.
    You should now be receiving another email with your account details.
    ")

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
