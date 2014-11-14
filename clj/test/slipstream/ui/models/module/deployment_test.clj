(ns slipstream.ui.models.module.deployment-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.module :as model]))

(def raw-metadata-str
  "<deploymentModule category='Deployment' creation='2013-03-08 22:37:40.773 CET' deleted='false' lastModified='2013-03-08 22:37:40.774 CET' name='Public/Tutorials/HelloWorld/client_server' parentUri='module/Public/Tutorials/HelloWorld' resourceUri='module/Public/Tutorials/HelloWorld/client_server/11' shortName='client_server' version='11'>
  <parameters class='org.hibernate.collection.PersistentMap'></parameters>
  <authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='true' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
    <groupMembers class='java.util.ArrayList'></groupMembers>
  </authz>
  <cloudNames length='1'>
    <string>stratuslab</string>
  </cloudNames>
  <nodes class='org.hibernate.collection.PersistentMap'>
    <entry>
      <string>testclient1</string>
      <node cloudService='default' creation='2013-03-08 22:37:40.773 CET' deleted='false' imageUri='module/Public/Tutorials/HelloWorld/testclient' multiplicity='5' name='testclient1' network='Public'>
        <parameters class='org.hibernate.collection.PersistentMap'>
          <entry>
            <string>webserver.port</string>
            <parameter category='General' class='com.sixsq.slipstream.persistence.NodeParameter' description='' isMappedValue='true' mandatory='false' name='webserver.port' readonly='false' type='String'>
              <value>apache1:port_of_something</value>
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
              <value>'something'</value>
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
        <node cloudService='stratuslab' creation='2013-03-08 22:37:40.773 CET' deleted='false' imageUri='module/Public/Tutorials/HelloWorld/apache' multiplicity='1' name='apache1' network='Public'>
          <parameters class='org.hibernate.collection.PersistentMap'></parameters>
          <parameterMappings class='org.hibernate.collection.PersistentMap'></parameterMappings>
        </node>
      </entry>
    </nodes>
    <user issuper='false' resourceUri='user/toto' name='Toto'></user>
  </deploymentModule>")

(def parsed-metadata
   {:authorization {:access-rights {:create-children {:public-access? false
                                                      :group-access? false
                                                      :owner-access? true}
                                    :delete {:owner-access? true
                                             :public-access? false
                                             :group-access? false}
                                    :put {:owner-access? true
                                          :public-access? false
                                          :group-access? false}
                                    :post {:group-access? true
                                           :owner-access? true
                                           :public-access? false}
                                    :get {:group-access? true
                                          :public-access? true
                                          :owner-access? true}}
                    :group-members #{}
                    :inherited-group-members? true}
    :nodes [{:name "testclient1"
             :reference-image "module/Public/Tutorials/HelloWorld/testclient"
             :default-multiplicity 5
             :default-cloud "default"}
            {:name "apache1"
             :reference-image "module/Public/Tutorials/HelloWorld/apache"
             :default-multiplicity 1
             :default-cloud "stratuslab"}]
    :summary {:deleted? false
              :comment nil
              :creation "2013-03-08 22:37:40.773 CET"
              :name "Public/Tutorials/HelloWorld/client_server"
              :short-name "client_server"
              :owner "sixsq"
              :version 11
              :published? false
              :publication nil
              :uri "module/Public/Tutorials/HelloWorld/client_server/11"
              :latest-version? nil
              :last-modified "2013-03-08 22:37:40.774 CET"
              :parent-uri "module/Public/Tutorials/HelloWorld"
              :description nil
              :category "Deployment"}})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))