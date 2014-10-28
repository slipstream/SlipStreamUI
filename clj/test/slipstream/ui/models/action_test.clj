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
  {:title   "Your email address has been confirmed."
   :message "You should now be receiving another email with your account details."})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Further message formats

(def raw-metadata-str-one-line
  "<?xml version='1.0' encoding='UTF-8' standalone='no'?><string>
    Your email address has been confirmed. You should now be receiving another email with your account details.
  </string>")

(def parsed-metadata-one-line
  {:title   nil
   :message "Your email address has been confirmed. You should now be receiving another email with your account details."})

(expect
  parsed-metadata-one-line
  (-> raw-metadata-str-one-line u/clojurify-raw-metadata-str model/parse))

(def raw-metadata-str-more-than-two-lines
  "<?xml version='1.0' encoding='UTF-8' standalone='no'?><string>
    Your email address has been confirmed.
    You should now be receiving another email with your account details.
    And this should be the 2nd line of the message.
  </string>")

(def parsed-metadata-more-than-two-lines
  {:title   "Your email address has been confirmed."
   :message (str "You should now be receiving another email with your account details."
                 "\n"
                 "    And this should be the 2nd line of the message.")})

(expect
  parsed-metadata-more-than-two-lines
  (-> raw-metadata-str-more-than-two-lines u/clojurify-raw-metadata-str model/parse))
