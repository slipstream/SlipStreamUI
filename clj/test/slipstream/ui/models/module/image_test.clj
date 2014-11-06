(ns slipstream.ui.models.module.image-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.module :as model]))

(def raw-metadata-str "<imageModule logoLink='http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png' category='Image' creation='2013-03-07 21:03:09.124 CET' deleted='false' imageId='HZTKYZgX7XzSokCHMB60lS0wsiv' isBase='false' lastModified='2013-03-07 21:03:09.337 CET' loginUser='donald' name='Public/BaseImages/with-a-very-long-name/Ubuntu/12.04' parentUri='module/Public/BaseImages/Ubuntu/toto' platform='debian' resourceUri='module/Public/BaseImages/Ubuntu/12.04' shortName='12.04' version='4' description='Nice Ubuntu distro'>
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
</imageModule>")

(def parsed-metadata
  {:cloud-image-details {:reference-image nil
                       :cloud-identifiers {"stratuslab" "HZTKYZgX7XzSokCHMB60lS0wsiv"
                                           "my-cloud" "abc"}
                       :native-image? false}
   :os-details {:login-username "donald"
                :platform [{:value  "centos",   :text   "CentOS"}
                           {:value  "debian",   :text   "Debian", :selected? true}
                           {:value  "fedora",   :text   "Fedora"}
                           {:value  "opensuse", :text   "OpenSuse"}
                           {:value  "redhat",   :text   "RedHat"}
                           {:value  "sles",     :text   "Sles"}
                           {:value  "ubuntu",   :text   "Ubuntu"}
                           {:value  "windows",  :text   "Windows"}
                           {:value  "other",    :text   "Other"}]}
   :cloud-configuration [{:category-type :global
                          :category "Cloud"
                          :parameters [{:help-hint nil
                                        :name "network"
                                        :read-only? false
                                        :type "Enum"
                                        :order 2147483647
                                        :value [{:value  "Public",  :text   "Public", :selected? true}
                                                {:value  "Private", :text   "Private"}]
                                        :description "Network type"
                                        :category "Cloud"}
                                       {:help-hint "Password..."
                                        :read-only? false
                                        :order 2147483647
                                        :value "youshouldnotseethis"
                                        :category "Cloud"
                                        :description "A password"
                                        :type "Password"
                                        :name "password"}]}
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
                                        :value [{:value "virtio", :text "virtio", :selected? true}
                                                {:value "scsi",   :text "scsi"}]
                                        :description "VM disks bus type"
                                        :category "stratuslab"}
                                       {:help-hint nil
                                        :name "stratuslab.instance.type"
                                        :read-only? false
                                        :type "Enum"
                                        :order 2147483647
                                        :value [{:value "m1.small", :text "m1.small", :selected? true}
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
   :image-creation {:recipe {:code "some recipe"}
                    :packages [{:repository "httpd_repo"
                                :name "httpd"
                                :key "httpd_key"}
                               {:repository "mlocate_repo"
                                :name "mlocate"
                                :key "mlocate_key"}
                               {:repository "vim_repo"
                                :name "vim"
                                :key "vim_key"}]
                    :pre-recipe {:code "some pre-recipe"}}
   :deployment {:targets {:on-vm-remove {:code "onvmremove target"
                                         :run-in-background false}
                          :on-vm-add {:code "onvmadd target"
                                      :run-in-background false}
                          :report {:code "report target"
                                   :run-in-background false}
                          :execute {:code "execute target"
                                    :run-in-background false}}
                :parameters [{:category-type :deployment
                              :category "Output"
                              :parameters [{:help-hint "Some help :-)"
                                            :read-only? false
                                            :order 2147483647
                                            :value "123.234.345"
                                            :category "Output"
                                            :description "hostname/ip of the image"
                                            :type "String"
                                            :name "hostname"}
                                           {:help-hint nil
                                            :read-only? false
                                            :order 2147483647
                                            :value nil
                                            :category "Output"
                                            :description "Cloud instance id"
                                            :type "String"
                                            :name "instanceid"}]}]}
   :runs [{:cloud-name "interoute"
           :runs [{:cloud-name "interoute"
                   :uri "run/e8d0b957-14a8-4e96-8677-85c7bd9eb64e"
                   :module-uri "module/Mebster/word_press/simple_deployment/410"
                   :start-time "2013-07-04 17:11:56.340 CEST"
                   :username "mickey"
                   :uuid "e8d0b957-14a8-4e96-8677-85c7bd9eb64e"
                   :status "Aborting"
                   :tags "this is a tag!"
                   :type nil}]}
          {:cloud-name "stratuslab"
           :runs [{:cloud-name "stratuslab"
                   :uri "run/638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :module-uri "module/Public/Tutorials/HelloWorld/client_server/11"
                   :start-time "2013-07-05 17:27:12.471 CEST"
                   :username "donald"
                   :uuid "638f04c3-44a1-41c7-90db-c81167fc6f19"
                   :status "Aborting"
                   :type nil}]}]
   :authorization {:access-rights {:create-children {:public-access? false
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
   :summary {:deleted? false
             :creation "2013-03-07 21:03:09.124 CET"
             :name "Public/BaseImages/with-a-very-long-name/Ubuntu/12.04"
             :short-name "12.04"
             :image "http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png"
             :owner "sixsq"
             :version 4
             :uri "module/Public/BaseImages/Ubuntu/12.04"
             :latest-version? nil
             :last-modified "2013-03-07 21:03:09.337 CET"
             :parent-uri "module/Public/BaseImages/Ubuntu/toto"
             :description "Nice Ubuntu distro"
             :category "Image"}})

(expect
  parsed-metadata
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))