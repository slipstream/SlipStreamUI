(ns slipstream.ui.models.module.deployment-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.module :as model]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]))

(def raw-metadata-str
  "<deploymentModule category=\"Deployment\" creation=\"2014-11-21 15:59:48.282 UTC\" deleted=\"false\" description=\"Testing how to create a deployment\" isLatestVersion=\"true\" lastModified=\"2014-11-24 15:57:48.882 UTC\" logoLink=\"\" parentUri=\"module/EBU_TTF/test-ui\" shortName=\"deployment-test\" version=\"612\">
    <authz groupCreateChildren=\"false\" groupDelete=\"false\" groupGet=\"false\" groupPost=\"false\" groupPut=\"false\" inheritedGroupMembers=\"true\" owner=\"rob\" ownerCreateChildren=\"true\" ownerDelete=\"true\" ownerGet=\"true\" ownerPost=\"true\" ownerPut=\"true\" publicCreateChildren=\"false\" publicDelete=\"false\" publicGet=\"false\" publicPost=\"false\" publicPut=\"false\">
        <groupMembers class='java.util.ArrayList'>
            <string>meb</string>
            <string>konstan</string>
            <string>other</string>
            <string>xxx</string>
        </groupMembers>
    </authz>
    <commit author=\"rob\">
        <comment/>
    </commit>
    <cloudNames length=\"3\">
        <string>exoscale-ch-gva</string>
        <string>ec2-eu-west</string>
        <string>default</string>
    </cloudNames>
    <runs count='2' limit='20' offset='0' totalCount='2'>
        <item abort=\"Unknown key ubuntu2:blah\" cloudServiceNames=\"exoscale-ch-gva\" moduleResourceUri=\"module/mynewproject1/ubuntu-dpl/659\" startTime=\"2015-01-09 11:39:56.813 CET\" status=\"Aborted\" tags=\"\" type=\"Orchestration\" username=\"konstantest\" uuid=\"43a560db-7948-4b67-abb2-3c3af32d10e6\"/>
        <item abort=\"Unknown key ss:blah\" cloudServiceNames=\"exoscale-ch-gva\" moduleResourceUri=\"module/mynewproject1/ubuntu-dpl/659\" startTime=\"2015-01-09 11:33:58.583 CET\" status=\"Aborted\" tags=\"\" type=\"Orchestration\" username=\"konstantest\" uuid=\"c6a7157b-acbb-4e69-b3f8-ad085e75bbc6\"/>
    </runs>
    <nodes>
        <entry>
            <string>node1</string>
            <node cloudService=\"exoscale-ch-gva\" creation=\"2014-11-24 15:57:48.865 UTC\" deleted=\"false\" imageUri=\"module/EBU_TTF/test-ui/vm1\" multiplicity=\"1\" name=\"node1\" network=\"Public\">
                <parameterMappings>
                    <entry>
                        <string>my-input-param-2</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"true\" mandatory=\"false\" name=\"my-input-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>node2:my-vm2-output-param-2</value>
                        </parameter>
                    </entry>
                    <entry>
                        <string>my-input-param-1</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"false\" mandatory=\"false\" name=\"my-input-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>'some-value-for-input-1'</value>
                        </parameter>
                    </entry>
                </parameterMappings>
                <parameters>
                    <entry>
                        <string>my-input-param-2</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"true\" mandatory=\"false\" name=\"my-input-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>node2:my-vm2-output-param-2</value>
                        </parameter>
                    </entry>
                    <entry>
                        <string>my-input-param-1</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"false\" mandatory=\"false\" name=\"my-input-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>'some-value-for-input-1'</value>
                        </parameter>
                    </entry>
                </parameters>
                <image category=\"Image\" creation=\"2014-11-21 15:53:18.452 UTC\" deleted=\"false\" description=\"\" isBase=\"false\" isLatestVersion=\"true\" lastModified=\"2014-11-21 15:53:18.486 UTC\" loginUser=\"root\" logoLink=\"\" moduleReferenceUri=\"module/examples/images/centos-6/544\" name=\"EBU_TTF/test-ui/vm1\" parentUri=\"module/EBU_TTF/test-ui\" platform=\"centos\" shortName=\"vm1\" version=\"608\">
                    <authz groupCreateChildren=\"false\" groupDelete=\"false\" groupGet=\"false\" groupPost=\"false\" groupPut=\"false\" inheritedGroupMembers=\"true\" owner=\"rob\" ownerCreateChildren=\"true\" ownerDelete=\"true\" ownerGet=\"true\" ownerPost=\"true\" ownerPut=\"true\" publicCreateChildren=\"false\" publicDelete=\"false\" publicGet=\"false\" publicPost=\"false\" publicPut=\"false\">
                        <groupMembers/>
                    </authz>
                    <commit author=\"rob\">
                        <comment/>
                    </commit>
                    <targets>
                        <target name=\"report\"/>
                        <target name=\"onvmremove\"/>
                        <target name=\"onvmadd\"/>
                        <target name=\"execute\"/>
                    </targets>
                    <packages/>
                    <prerecipe/>
                    <recipe/>
                    <cloudImageIdentifiers/>
                    <parameters>
                        <entry>
                            <string>instanceid</string>
                            <parameter category=\"Output\" description=\"Cloud instance id\" isSet=\"false\" mandatory=\"true\" name=\"instanceid\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\"/>
                        </entry>
                        <entry>
                            <string>my-output-param-1</string>
                            <parameter category=\"Output\" description=\"This is my first output param\" isSet=\"true\" mandatory=\"false\" name=\"my-output-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-output-1</value>
                                <defaultValue>some-value-for-output-1</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>ec2-eu-west.security.groups</string>
                            <parameter category=\"ec2-eu-west\" description=\"Security groups (comma separated list)\" isSet=\"true\" mandatory=\"true\" name=\"ec2-eu-west.security.groups\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>default</value>
                                <defaultValue>default</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>extra.disk.volatile</string>
                            <parameter category=\"Cloud\" description=\"Volatile extra disk in GB\" isSet=\"false\" mandatory=\"true\" name=\"extra.disk.volatile\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\"/>
                        </entry>
                        <entry>
                            <string>ec2-eu-west.instance.type</string>
                            <parameter category=\"ec2-eu-west\" description=\"Cloud instance type\" isSet=\"true\" mandatory=\"true\" name=\"ec2-eu-west.instance.type\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Enum\">
                                <enumValues length=\"10\">
                                    <string>m1.small</string>
                                    <string>m1.large</string>
                                    <string>m1.xlarge</string>
                                    <string>c1.medium</string>
                                    <string>c1.xlarge</string>
                                    <string>m2.xlarge</string>
                                    <string>m2.2xlarge</string>
                                    <string>m2.4xlarge</string>
                                    <string>cc1.4xlarge</string>
                                    <string>t1.micro</string>
                                </enumValues>
                                <value>m1.small</value>
                                <defaultValue>m1.small</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>my-input-param-2</string>
                            <parameter category=\"Input\" description=\"This is my second input param\" isSet=\"true\" mandatory=\"false\" name=\"my-input-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-input-2</value>
                                <defaultValue>some-value-for-input-2</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>exoscale-ch-gva.security.groups</string>
                            <parameter category=\"exoscale-ch-gva\" description=\"Security Groups (comma separated list)\" isSet=\"true\" mandatory=\"true\" name=\"exoscale-ch-gva.security.groups\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>default</value>
                                <defaultValue>default</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>network</string>
                            <parameter category=\"Cloud\" description=\"Network type\" isSet=\"true\" mandatory=\"true\" name=\"network\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Enum\">
                                <enumValues length=\"2\">
                                    <string>Public</string>
                                    <string>Private</string>
                                </enumValues>
                                <value>Public</value>
                                <defaultValue>Public</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>exoscale-ch-gva.instance.type</string>
                            <parameter category=\"exoscale-ch-gva\" description=\"Instance type (flavor)\" isSet=\"true\" mandatory=\"true\" name=\"exoscale-ch-gva.instance.type\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>Small</value>
                                <defaultValue>Small</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>hostname</string>
                            <parameter category=\"Output\" description=\"hostname/ip of the image\" isSet=\"false\" mandatory=\"true\" name=\"hostname\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\"/>
                        </entry>
                        <entry>
                            <string>my-input-param-1</string>
                            <parameter category=\"Input\" description=\"This is my first input param\" isSet=\"true\" mandatory=\"false\" name=\"my-input-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-input-1</value>
                                <defaultValue>some-value-for-input-1</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>my-output-param-2</string>
                            <parameter category=\"Output\" description=\"This is my second output param\" isSet=\"true\" mandatory=\"false\" name=\"my-output-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-output-2</value>
                                <defaultValue>some-value-for-output-2</defaultValue>
                            </parameter>
                        </entry>
                    </parameters>
                </image>
            </node>
        </entry>
        <entry>
            <string>node2</string>
            <node cloudService=\"exoscale-ch-gva\" creation=\"2014-11-24 15:57:48.865 UTC\" deleted=\"false\" imageUri=\"module/EBU_TTF/test-ui/vm2/610\" multiplicity=\"1\" name=\"node2\" network=\"Public\">
                <parameterMappings>
                    <entry>
                        <string>my-vm2-input-param-2</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"false\" mandatory=\"false\" name=\"my-vm2-input-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>'some-value-for-vm2-input-2'</value>
                        </parameter>
                    </entry>
                    <entry>
                        <string>my-vm2-input-param-1</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"true\" mandatory=\"false\" name=\"my-vm2-input-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>node1:my-output-param-1</value>
                        </parameter>
                    </entry>
                </parameterMappings>
                <parameters>
                    <entry>
                        <string>my-vm2-input-param-2</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"false\" mandatory=\"false\" name=\"my-vm2-input-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>'some-value-for-vm2-input-2'</value>
                        </parameter>
                    </entry>
                    <entry>
                        <string>my-vm2-input-param-1</string>
                        <parameter category=\"General\" description=\"\" isMappedValue=\"true\" mandatory=\"false\" name=\"my-vm2-input-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                            <value>node1:my-output-param-1</value>
                        </parameter>
                    </entry>
                </parameters>
                <image category=\"Image\" creation=\"2014-11-21 15:53:18.452 UTC\" deleted=\"false\" description=\"\" isBase=\"false\" isLatestVersion=\"true\" lastModified=\"2014-11-21 15:56:06.887 UTC\" loginUser=\"root\" logoLink=\"\" moduleReferenceUri=\"module/examples/images/centos-6/544\" name=\"EBU_TTF/test-ui/vm2\" parentUri=\"module/EBU_TTF/test-ui\" platform=\"centos\" shortName=\"vm2\" version=\"610\">
                    <authz groupCreateChildren=\"false\" groupDelete=\"false\" groupGet=\"false\" groupPost=\"false\" groupPut=\"false\" inheritedGroupMembers=\"true\" owner=\"rob\" ownerCreateChildren=\"true\" ownerDelete=\"true\" ownerGet=\"true\" ownerPost=\"true\" ownerPut=\"true\" publicCreateChildren=\"false\" publicDelete=\"false\" publicGet=\"false\" publicPost=\"false\" publicPut=\"false\">
                        <groupMembers/>
                    </authz>
                    <commit author=\"rob\">
                        <comment/>
                    </commit>
                    <targets>
                        <target name=\"onvmremove\"/>
                        <target name=\"execute\"/>
                        <target name=\"onvmadd\"/>
                        <target name=\"report\"/>
                    </targets>
                    <packages/>
                    <prerecipe/>
                    <recipe/>
                    <cloudImageIdentifiers/>
                    <parameters>
                        <entry>
                            <string>instanceid</string>
                            <parameter category=\"Output\" description=\"Cloud instance id\" isSet=\"false\" mandatory=\"true\" name=\"instanceid\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\"/>
                        </entry>
                        <entry>
                            <string>ec2-eu-west.security.groups</string>
                            <parameter category=\"ec2-eu-west\" description=\"Security groups (comma separated list)\" isSet=\"true\" mandatory=\"true\" name=\"ec2-eu-west.security.groups\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>default</value>
                                <defaultValue>default</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>extra.disk.volatile</string>
                            <parameter category=\"Cloud\" description=\"Volatile extra disk in GB\" isSet=\"false\" mandatory=\"true\" name=\"extra.disk.volatile\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\"/>
                        </entry>
                        <entry>
                            <string>ec2-eu-west.instance.type</string>
                            <parameter category=\"ec2-eu-west\" description=\"Cloud instance type\" isSet=\"true\" mandatory=\"true\" name=\"ec2-eu-west.instance.type\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Enum\">
                                <enumValues length=\"10\">
                                    <string>m1.small</string>
                                    <string>m1.large</string>
                                    <string>m1.xlarge</string>
                                    <string>c1.medium</string>
                                    <string>c1.xlarge</string>
                                    <string>m2.xlarge</string>
                                    <string>m2.2xlarge</string>
                                    <string>m2.4xlarge</string>
                                    <string>cc1.4xlarge</string>
                                    <string>t1.micro</string>
                                </enumValues>
                                <value>m1.small</value>
                                <defaultValue>m1.small</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>my-vm2-input-param-2</string>
                            <parameter category=\"Input\" description=\"This is my second input-vm2 param\" isSet=\"true\" mandatory=\"false\" name=\"my-vm2-input-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-vm2-input-2</value>
                                <defaultValue>some-value-for-vm2-input-2</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>exoscale-ch-gva.security.groups</string>
                            <parameter category=\"exoscale-ch-gva\" description=\"Security Groups (comma separated list)\" isSet=\"true\" mandatory=\"true\" name=\"exoscale-ch-gva.security.groups\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>default</value>
                                <defaultValue>default</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>my-vm2-output-param-2</string>
                            <parameter category=\"Output\" description=\"This is my second output-vm2 param\" isSet=\"true\" mandatory=\"false\" name=\"my-vm2-output-param-2\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-vm2-output-2</value>
                                <defaultValue>some-value-for-vm2-output-2</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>my-vm2-input-param-1</string>
                            <parameter category=\"Input\" description=\"This is my first input-vm2 param\" isSet=\"true\" mandatory=\"false\" name=\"my-vm2-input-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-vm2-input-1</value>
                                <defaultValue>some-value-for-vm2-input-1</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>network</string>
                            <parameter category=\"Cloud\" description=\"Network type\" isSet=\"true\" mandatory=\"true\" name=\"network\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Enum\">
                                <enumValues length=\"2\">
                                    <string>Public</string>
                                    <string>Private</string>
                                </enumValues>
                                <value>Public</value>
                                <defaultValue>Public</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>exoscale-ch-gva.instance.type</string>
                            <parameter category=\"exoscale-ch-gva\" description=\"Instance type (flavor)\" isSet=\"true\" mandatory=\"true\" name=\"exoscale-ch-gva.instance.type\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>Small</value>
                                <defaultValue>Small</defaultValue>
                            </parameter>
                        </entry>
                        <entry>
                            <string>hostname</string>
                            <parameter category=\"Output\" description=\"hostname/ip of the image\" isSet=\"false\" mandatory=\"true\" name=\"hostname\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\"/>
                        </entry>
                        <entry>
                            <string>my-vm2-output-param-1</string>
                            <parameter category=\"Output\" description=\"This is my first output-vm2 param\" isSet=\"true\" mandatory=\"false\" name=\"my-vm2-output-param-1\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
                                <value>some-value-for-vm2-output-1</value>
                                <defaultValue>some-value-for-vm2-output-1</defaultValue>
                            </parameter>
                        </entry>
                    </parameters>
                </image>
            </node>
        </entry>
    </nodes>
    <parameters/>
    <user issuper='false' resourceUri='user/regularuser' name='regularuser' defaultCloud='sky'></user>
</deploymentModule>")

(def parsed-metadata
   {:nodes [{:name "node1"
             :template-node? nil
             :reference-image "EBU_TTF/test-ui/vm1"
             :default-multiplicity 1
             :default-cloud [{:value "exoscale-ch-gva"
                              :text "exoscale-ch-gva"}
                             {:value "ec2-eu-west"
                              :text "ec2-eu-west"}
                             {:selected? true
                              :value "default"
                              :text "default"}]
             :output-parameters ["hostname" "instanceid" "my-output-param-1" "my-output-param-2"]
             :mappings [{:name "my-input-param-1"
                         :mapped-value? false
                         :value "'some-value-for-input-1'"}
                        {:name "my-input-param-2"
                         :mapped-value? true
                         :value "node2:my-vm2-output-param-2"}]}
            {:name "node2"
             :template-node? nil
             :reference-image "EBU_TTF/test-ui/vm2/610"
             :default-multiplicity 1
             :default-cloud [{:value "exoscale-ch-gva"
                              :text "exoscale-ch-gva"}
                             {:value "ec2-eu-west"
                              :text "ec2-eu-west"}
                             {:selected? true
                              :value "default"
                              :text "default"}]
             :output-parameters ["hostname" "instanceid" "my-vm2-output-param-1" "my-vm2-output-param-2"]
             :mappings [{:name "my-vm2-input-param-1"
                         :mapped-value? true
                         :value "node1:my-output-param-1"}
                        {:name "my-vm2-input-param-2"
                         :mapped-value? false
                         :value "'some-value-for-vm2-input-2'"}]}]
    :runs  {:pagination {:offset 0
                         :limit 20
                         :count 2
                         :total-count 2
                         :cloud-name nil}
            :runs [{:cloud-names "exoscale-ch-gva"
                    :abort-msg "Unknown key ubuntu2:blah"
                    :uri nil
                    :module-uri "module/mynewproject1/ubuntu-dpl/659"
                    :type :deployment-run
                    :start-time "2015-01-09 11:39:56.813 CET"
                    :username "konstantest"
                    :uuid "43a560db-7948-4b67-abb2-3c3af32d10e6"
                    :status "Aborted"
                    :tags ""}
                   {:cloud-names "exoscale-ch-gva"
                    :abort-msg "Unknown key ss:blah"
                    :uri nil
                    :module-uri "module/mynewproject1/ubuntu-dpl/659"
                    :type :deployment-run
                    :start-time "2015-01-09 11:33:58.583 CET"
                    :username "konstantest"
                    :uuid "c6a7157b-acbb-4e69-b3f8-ad085e75bbc6"
                    :status "Aborted"
                    :tags ""}]}
    :summary {:publication nil
              :deleted? false
              :published? false
              :comment nil
              :creation "2014-11-21 15:59:48.282 UTC"
              :name "EBU_TTF/test-ui/deployment-test"
              :logo-url ""
              :short-name "deployment-test"
              :owner "rob"
              :version 612
              :uri "module/EBU_TTF/test-ui/deployment-test"
              :latest-version? true
              :last-modified "2014-11-24 15:57:48.882 UTC"
              :parent-uri "module/EBU_TTF/test-ui"
              :description "Testing how to create a deployment"
              :category "Deployment"}
    :available-clouds [{:value "exoscale-ch-gva", :text "exoscale-ch-gva", :selected? true}
                       {:value "ec2-eu-west", :text "ec2-eu-west"}
                       {:value "default", :text "default"}]
    :authorization {:access-rights {:create-children {:public-access? false
                                                      :group-access? false
                                                      :owner-access? true}
                                    :delete {:owner-access? true
                                             :public-access? false
                                             :group-access? false}
                                    :put {:owner-access? true
                                          :public-access? false
                                          :group-access? false}
                                    :post {:group-access? false
                                           :owner-access? true
                                           :public-access? false}
                                    :get {:group-access? false
                                          :public-access? false
                                          :owner-access? true}}
                    :group-members #{"meb" "other" "konstan" "xxx"}
                    :inherited-group-members? true}})

(expect
  parsed-metadata
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))

(expect
  parsed-metadata
  (localization/with-lang :en
    (page-type/with-page-type "view"
      (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))))

(def ^:private blank-node-template
  {:name nil
   :template-node? true
   :reference-image "new"
   :default-multiplicity 1
   :default-cloud [{:value "exoscale-ch-gva"
                    :text "exoscale-ch-gva"}
                   {:value "ec2-eu-west"
                    :text "ec2-eu-west"}
                   {:selected? true
                    :value "default"
                    :text "default"}]
   :output-parameters ()
   :mappings [{:name nil
               :mapped-value? nil
               :value nil}]})

(expect
  (update-in parsed-metadata [:nodes] conj blank-node-template)
  (localization/with-lang :en
    (page-type/with-page-type "edit"
      (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))))
