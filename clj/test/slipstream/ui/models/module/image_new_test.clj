(ns slipstream.ui.models.module.image-new-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.module :as model]))

(def raw-metadata-str
  "<imageModule category='Image' creation='2013-03-07 21:03:09.124 CET' deleted='false' description='Nice Ubuntu distro' imageId='HZTKYZgX7XzSokCHMB60lS0wsiv' isBase='true' lastModified='2013-03-07 21:03:09.337 CET' loginUser='donald' name='Public/BaseImages/Ubuntu/new' parentUri='module/Public/BaseImages/Ubuntu' platform='debian' resourceUri='module/Public/BaseImages/Ubuntu/new' shortName='new' version='4'>
      <parameters class='org.hibernate.collection.PersistentMap'>
          <entry>
              <string>extra.disk.volatile</string>
              <parameter category='Cloud' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Volatile extra disk in GB' isSet='false' mandatory='true' name='extra.disk.volatile' readonly='false' type='String'>
                  <value>12345</value>
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
              </parameter>
          </entry>
          <entry>
              <string>stratuslab.cpu</string>
              <parameter category='stratuslab' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Requested CPUs' isSet='false' mandatory='true' name='stratuslab.cpu' readonly='false' type='String'/>
          </entry>
          <entry>
              <string>stratuslab.ram</string>
              <parameter category='stratuslab' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Requested RAM (in GB)' isSet='false' mandatory='true' name='stratuslab.ram' readonly='false' type='String'/>
          </entry>
          <entry>
              <string>instanceid</string>
              <parameter category='Output' class='com.sixsq.slipstream.persistence.ModuleParameter' description='Cloud instance id' isSet='false' mandatory='true' name='instanceid' readonly='false' type='String'/>
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
          <groupMembers class='java.util.ArrayList'/>
      </authz>
      <cloudNames length='2'>
          <string>stratuslab</string>
          <string>default</string>
      </cloudNames>
      <targets class='org.hibernate.collection.PersistentBag'>
          <target name='execute' runInBackground='false'>execute target</target>
          <target name='report' runInBackground='false'>report target</target>
      </targets>
      <packages class='org.hibernate.collection.PersistentBag'>
          <package key='key' name='apache2' repository='repo'/>
      </packages>
      <prerecipe>some pre-recipe</prerecipe>
      <recipe>some recipe</recipe>
      <cloudImageIdentifiers class='org.hibernate.collection.PersistentBag'>
          <cloudImageIdentifier cloudImageIdentifier='HZTKYZgX7XzSokCHMB60lS0wsiv' cloudServiceName='stratuslab' resourceUri='module/Public/BaseImages/Ubuntu/12.04/4/stratuslab'/>
          <cloudImageIdentifier cloudImageIdentifier='abc' cloudServiceName='my-cloud' resourceUri='module/Public/BaseImages/Ubuntu/12.04/4/stratuslab'/>
      </cloudImageIdentifiers>
      <extraDisks class='org.hibernate.collection.PersistentBag'/>
      <user issuper='false' name='toto' resourceUri='user/super'/>
  </imageModule>")


(def parsed-metadata
  {:runs []
   :deployment {:targets {:on-vm-remove {:code nil
                                         :run-in-background nil}
                          :on-vm-add {:code nil
                                      :run-in-background nil}
                          :report {:code "report target"
                                   :run-in-background false}
                          :execute {:code "execute target"
                                    :run-in-background false}}
                :parameters [{:name "instanceid"
                              :category "Output"
                              :type "String"
                              :description "Cloud instance id"
                              :value nil
                              :order 1
                              :placeholder "Provided at runtime by SlipStream"
                              :read-only? false
                              :help-hint "The instanceid is a default deployment parameter popupaled by SlipStream on deployment. You can access the live value in the deployment recipe with 'ss-get instanceid'."
                              :disabled? true}
                             {:disabled? true
                              :help-hint "The hostname is a default deployment parameter popupaled by SlipStream on deployment. You can access the live value in the deployment recipe with 'ss-get hostname'."
                              :read-only? false
                              :order 2
                              :placeholder "Provided at runtime by SlipStream"
                              :value "123.234.345"
                              :category "Output"
                              :description "Hostname or ip address of the image"
                              :type "String"
                              :name "hostname"}]}
   :image-creation {:recipe {:code "some recipe"}
                    :packages [{:repository "repo"
                                :name "apache2"
                                :key "key"}]
                    :pre-recipe {:code "some pre-recipe"}}
   :cloud-configuration [{:category-type :global
                          :category "Cloud"
                          :parameters [{:help-hint nil
                                        :read-only? false
                                        :order 2147483647
                                        :value "12345"
                                        :category "Cloud"
                                        :description "Volatile extra disk in GB"
                                        :type "String"
                                        :name "extra.disk.volatile"}
                                       {:help-hint nil
                                        :name "network"
                                        :read-only? false
                                        :type "Enum"
                                        :order 2147483647
                                        :value [{:selected? true, :value "Public", :text "Public"}
                                                {:value "Private", :text "Private"}]
                                        :description "Network type"
                                        :category "Cloud"}]}
                         {:category-type :global
                          :category "stratuslab"
                          :parameters [{:help-hint nil
                                        :read-only? false
                                        :order 2147483647
                                        :value nil
                                        :category "stratuslab"
                                        :description "Requested CPUs"
                                        :type "String"
                                        :name "stratuslab.cpu"}
                                       {:help-hint nil
                                        :name "stratuslab.disks.bus.type"
                                        :read-only? false
                                        :type "Enum"
                                        :order 2147483647
                                        :value [{:selected? true, :value "virtio", :text "virtio"}
                                                {:value "scsi", :text "scsi"}]
                                        :description "VM disks bus type"
                                        :category "stratuslab"}
                                       {:help-hint nil
                                        :name "stratuslab.instance.type"
                                        :read-only? false
                                        :type "Enum"
                                        :order 2147483647
                                        :value [{:selected? true, :value "m1.small", :text "m1.small"}
                                                {:value "c1.medium", :text "c1.medium"}
                                                {:value "m1.large", :text "m1.large"}
                                                {:value "m1.xlarge", :text "m1.xlarge"}
                                                {:value "c1.xlarge", :text "c1.xlarge"}
                                                {:value "t1.micro", :text "t1.micro"}
                                                {:value "standard.xsmall", :text "standard.xsmall"}]
                                        :description "Cloud instance type"
                                        :category "stratuslab"}
                                       {:help-hint nil
                                        :read-only? false
                                        :order 2147483647
                                        :value nil
                                        :category "stratuslab"
                                        :description "Requested RAM (in GB)"
                                        :type "String"
                                        :name "stratuslab.ram"}]}]
   :os-details {:platform [{:value  "centos",   :text   "CentOS"}
                           {:value  "debian",   :text   "Debian", :selected? true}
                           {:value  "fedora",   :text   "Fedora"}
                           {:value  "opensuse", :text   "OpenSuse"}
                           {:value  "redhat",   :text   "RedHat"}
                           {:value  "sles",     :text   "Sles"}
                           {:value  "ubuntu",   :text   "Ubuntu"}
                           {:value  "windows",  :text   "Windows"}
                           {:value  "other",    :text   "Other"}]
                :login-username "donald"}
   :cloud-image-details {:native-image? true
                         :cloud-identifiers {"stratuslab" "HZTKYZgX7XzSokCHMB60lS0wsiv"
                                             "my-cloud" "abc"}
                         :reference-image nil}
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
                                   :get {:group-access? true
                                         :public-access? true
                                         :owner-access? true}}
                   :group-members #{}
                   :inherited-group-members? true}
   :available-clouds [{:selected? true, :value "stratuslab", :text "stratuslab"}
                      {:value "default", :text "default"}]
   :summary {:deleted? false
             :comment nil
             :creation "2013-03-07 21:03:09.124 CET"
             :name "Public/BaseImages/Ubuntu/new"
             :short-name "new"
             :published? false
             :publication nil
             :image nil
             :owner "sixsq"
             :version 4
             :uri "module/Public/BaseImages/Ubuntu/new"
             :latest-version? nil
             :last-modified "2013-03-07 21:03:09.337 CET"
             :parent-uri "module/Public/BaseImages/Ubuntu"
             :description "Nice Ubuntu distro"
             :category "Image"}})

(expect
  parsed-metadata
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))



