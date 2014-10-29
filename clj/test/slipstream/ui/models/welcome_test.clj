(ns slipstream.ui.models.welcome-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.welcome :as model]))

(def raw-metadata-str
  "<welcome>
   <modules>
      <item resourceUri=\"module/EBU_TTF/53\" category=\"Project\" description=\"Global module for TTF relates deployments and tasks\" version=\"53\" name=\"EBU_TTF\">
         <authz owner=\"rob\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"false\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item resourceUri=\"module/SlipStream/280\" category=\"Project\" description=\"SlipStream dog fooding :-)\" version=\"280\" name=\"SlipStream\">
         <authz owner=\"sixsq_dev\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"false\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"false\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>lionel</string>
               <string>konstan</string>
               <string>loomis</string>
               <string>meb</string>
               <string>rob</string>
               <string>seb</string>
            </groupMembers>
         </authz>
      </item>
      <item resourceUri=\"module/apps/528\" category=\"Project\" description=\"\" version=\"528\" name=\"apps\">
         <authz owner=\"super\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"true\" publicGet=\"true\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"false\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item resourceUri=\"module/examples/56\" category=\"Project\" description=\"Examples highlighting SlipStream features.  See User Guide on the documentation page.\" version=\"56\" name=\"examples\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"true\" publicGet=\"true\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"true\" inheritedGroupMembers=\"false\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/tutorials/wordpress/wordpress/478\" category=\"Deployment\" description=\"simple, single node deployment of WordPress\" published=\"true\" logoLink=\"http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png\" version=\"478\" name=\"wordpress\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/apps/minecraft-app/481\" category=\"Deployment\" description=\"A Minecraft Server\" published=\"true\" logoLink=\"http://www.esourceengine.com/downloads/minecraft/14085_minecraft.jpg\" version=\"481\" name=\"minecraft-app\">
         <authz owner=\"seb\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/apps/openerp/336\" category=\"Deployment\" description=\"An OpenERP instance backed with PostgresSQL\" published=\"true\" logoLink=\"http://www.techreceptives.com/wp-content/uploads/2013/06/openerp-logo2.png\" version=\"336\" name=\"openerp\">
         <authz owner=\"seb\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/images/windows-server-2012/391\" category=\"Image\" description=\"Standard installation of the Windows Server 2012 R2 operating system (For Exoscale: Please ensure that port 5985 is open in your security group named &quot;default&quot;)\" published=\"true\" logoLink=\"http://blogs.technet.com/resized-image.ashx/__size/375x0/__key/communityserver-blogs-components-weblogfiles/00-00-00-57-46/6457.ws2012img.jpg\" version=\"391\" name=\"windows-server-2012\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/images/ubuntu-12.04/517\" category=\"Image\" description=\"Minimal installation of the Ubuntu 12.04 (LTS) operating system.\" published=\"true\" logoLink=\"http://design.ubuntu.com/wp-content/uploads/ubuntu-logo14.png\" version=\"517\" name=\"ubuntu-12.04\">
         <authz owner=\"super\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/images/centos-6/479\" category=\"Image\" description=\"Minimal installation of the CentOS 6 operating system.\" published=\"true\" logoLink=\"http://blog.quadranet.com/wp-content/uploads/2014/01/centos.png\" version=\"479\" name=\"centos-6\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
   </modules>
   <serviceCatalogues>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/cloudstack' cloud='cloudstack' creation='2013-11-12 20:21:20.192 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[cloudstack.support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.support.email' description='Support email adress for this cloud service' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' category='Single VM Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.overall.ram' description='Overall available RAM' category='Overall Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.overall.cpu' description='Overall available CPUs (cores)' category='Overall Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' category='Single VM Capacity' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[cloudstack.general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='cloudstack.general.description' description='General description of the cloud service (including optional further links)' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/stratuslab' cloud='stratuslab' creation='2013-11-12 20:21:20.98 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[stratuslab.support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.support.email' description='Support email adress for this cloud service' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[mickey@disney.com]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[You won't be able to provision more for a single VM]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.overall.ram' description='Overall available RAM' category='General' mandatory='false' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.overall.cpu' description='Overall available CPUs (cores)' category='General' mandatory='true' type='String' readonly='false' catalogCategory='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' category='General' mandatory='true' type='String' readonly='false' catalogCategory='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[stratuslab.general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='stratuslab.general.description' description='General description of the cloud service (including optional further links)' category='General' mandatory='false' type='String' readonly='false' catalogCategory='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
   </serviceCatalogues>
   <user issuper='true' resourceUri='user/meb' name='meb'></user>
</welcome>")

(def parsed-metadata
  {:published-apps [{:publisher "sixsq"
                     :image "http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png"
                     :uri "module/examples/tutorials/wordpress/wordpress/478"
                     :version "478"
                     :name "wordpress"
                     :description "simple, single node deployment of WordPress"}
                    {:publisher "seb"
                     :image "http://www.esourceengine.com/downloads/minecraft/14085_minecraft.jpg"
                     :uri "module/apps/minecraft-app/481"
                     :version "481"
                     :name "minecraft-app"
                     :description "A Minecraft Server"}
                    {:publisher "seb"
                     :image "http://www.techreceptives.com/wp-content/uploads/2013/06/openerp-logo2.png"
                     :uri "module/apps/openerp/336"
                     :version "336"
                     :name "openerp"
                     :description "An OpenERP instance backed with PostgresSQL"}
                    {:publisher "sixsq"
                     :image "http://blogs.technet.com/resized-image.ashx/__size/375x0/__key/communityserver-blogs-components-weblogfiles/00-00-00-57-46/6457.ws2012img.jpg"
                     :uri "module/examples/images/windows-server-2012/391"
                     :version "391"
                     :name "windows-server-2012"
                     :description "Standard installation of the Windows Server 2012 R2 operating system (For Exoscale: Please ensure that port 5985 is open in your security group named \"default\")"}
                    {:publisher "super"
                     :image "http://design.ubuntu.com/wp-content/uploads/ubuntu-logo14.png"
                     :uri "module/examples/images/ubuntu-12.04/517"
                     :version "517"
                     :name "ubuntu-12.04"
                     :description "Minimal installation of the Ubuntu 12.04 (LTS) operating system."}
                    {:publisher "sixsq"
                     :image "http://blog.quadranet.com/wp-content/uploads/2014/01/centos.png"
                     :uri "module/examples/images/centos-6/479"
                     :version "479"
                     :name "centos-6"
                     :description "Minimal installation of the CentOS 6 operating system."}]
   :projects [{:owner "rob"
               :uri "module/EBU_TTF/53"
               :name "EBU_TTF"
               :version "53"
               :description "Global module for TTF relates deployments and tasks"
               :category "Project"}
              {:owner "sixsq_dev"
               :uri "module/SlipStream/280"
               :name "SlipStream"
               :version "280"
               :description "SlipStream dog fooding :-)"
               :category "Project"}
              {:owner "super"
               :uri "module/apps/528"
               :name "apps"
               :version "528"
               :description ""
               :category "Project"}
              {:owner "sixsq"
               :uri "module/examples/56"
               :name "examples"
               :version "56"
               :description "Examples highlighting SlipStream features.  See User Guide on the documentation page."
               :category "Project"}]
   :service-catalog {:items [{:cloud "cloudstack"
                              :creation "2013-11-12 20:21:20.192 CET"
                              :uri "servicecatalog/cloudstack"
                              :parameters [{:help-hint nil
                                            :read-only? false
                                            :order 2147483647
                                            :value nil
                                            :category "General"
                                            :description "General description of the cloud service (including optional further links)"
                                            :type "String"
                                            :name "cloudstack.general.description"}
                                           {:help-hint nil
                                            :read-only? false
                                            :order 2147483647
                                            :value nil
                                            :category "General"
                                            :description "Support email adress for this cloud service"
                                            :type "String"
                                            :name "cloudstack.support.email"}
                                           {:help-hint nil
                                            :read-only? false
                                            :order 2147483647
                                            :value nil
                                            :category "Overall Capacity"
                                            :description "Overall available CPUs (cores)"
                                            :type "String"
                                            :name "cloudstack.overall.cpu"}
                                           {:help-hint nil
                                            :read-only? false
                                            :order 2147483647
                                            :value nil
                                            :category "Overall Capacity"
                                            :description "Overall available RAM"
                                            :type "String"
                                            :name "cloudstack.overall.ram"}
                                           {:help-hint nil
                                            :read-only? false
                                            :order 2147483647
                                            :value nil
                                            :category "Single VM Capacity"
                                            :description "Maximum number of CPUs (cores) for a single VM"
                                            :type "String"
                                            :name "cloudstack.single.vm.max.cpu"}
                                           {:help-hint nil
                                            :read-only? false
                                            :order 2147483647
                                            :value nil
                                            :category "Single VM Capacity"
                                            :description "Maximum number of RAM (GB) for a single VM"
                                            :type "String"
                                            :name "cloudstack.single.vm.max.ram"}]}
                           {:cloud "stratuslab"
                            :creation "2013-11-12 20:21:20.98 CET"
                            :uri "servicecatalog/stratuslab"
                            :parameters [{:help-hint nil
                                          :read-only? false
                                          :order 2147483647
                                          :value nil
                                          :category "General"
                                          :description "General description of the cloud service (including optional further links)"
                                          :type "String"
                                          :name "stratuslab.general.description"}
                                         {:help-hint nil
                                          :read-only? false
                                          :order 2147483647
                                          :value nil
                                          :category "General"
                                          :description "Overall available CPUs (cores)"
                                          :type "String"
                                          :name "stratuslab.overall.cpu"}
                                         {:help-hint nil
                                          :read-only? false
                                          :order 2147483647
                                          :value nil
                                          :category "General"
                                          :description "Overall available RAM"
                                          :type "String"
                                          :name "stratuslab.overall.ram"}
                                         {:help-hint "You won't be able to provision more for a single VM"
                                          :read-only? false
                                          :order 2147483647
                                          :value nil
                                          :category "General"
                                          :description "Maximum number of CPUs (cores) for a single VM"
                                          :type "String"
                                          :name "stratuslab.single.vm.max.cpu"}
                                         {:help-hint nil
                                          :read-only? false
                                          :order 2147483647
                                          :value nil
                                          :category "General"
                                          :description "Maximum number of RAM (GB) for a single VM"
                                          :type "String"
                                          :name "stratuslab.single.vm.max.ram"}
                                         {:help-hint nil
                                          :read-only? false
                                          :order 2147483647
                                          :value "mickey@disney.com"
                                          :category "General"
                                          :description "Support email adress for this cloud service"
                                          :type "String"
                                          :name "stratuslab.support.email"}]}]}})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))