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
   <serviceCatalogs>
   <serviceCatalog cloud=\"Exoscale\" creation=\"2014-12-07 21:40:30.30 CET\" deleted=\"false\" resourceUri=\"service_catalog/Exoscale\">
      <parameters class=\"org.hibernate.collection.internal.PersistentMap\">
         <entry>
            <string>Exoscale.single.vm.min.cpu</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the minimum number of CPU cores with which this supplier’s VMs can be configured; Value: an integer number, e.g. 1; Explanation: to give an indication of the minimum configurable environment\" mandatory=\"true\" name=\"Exoscale.single.vm.min.cpu\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.price.charging.unit</string>
            <parameter category=\"Price\" description=\"Nature: the unit used for charging; Value: the pricing unit, e.g. GHz, portion of CPU chip, etc.; Explanation: this could vary per supplier, as there is no standard unit. Work from the ODCA or Deutsche Boerse could be used to derive such a standard, at least for comparative purposes, in the future\" mandatory=\"true\" name=\"Exoscale.price.charging.unit\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.single.vm.storage.volatile</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the amount of volatile storage available locally to that VM; Value: expressed in relevant terms, e.g. 500 GB; Explanation: the amount of “scratch” space, which could be used, e.g. to extend the random access memory of a VM. Local disk space is typically slower than ram but faster than persistent storage space\" mandatory=\"true\" name=\"Exoscale.single.vm.storage.volatile\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.overall.capacity.ram</string>
            <parameter category=\"Overall capacity\" description=\"Nature: the amount of random-access memory in total; Value: expressed in relevant terms, e.g. 10 TB; Explanation: the amount of memory available across the installation as a whole. See below for what is available on any one system\" mandatory=\"true\" name=\"Exoscale.overall.capacity.ram\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.overall.capacity.cpu</string>
            <parameter category=\"Overall capacity\" description=\"The number of CPU cores (currently) available within (the relevant part of) the supplier’s IaaS environment\" mandatory=\"true\" name=\"Exoscale.overall.capacity.cpu\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions><![CDATA[Value: an integer, and possibly approximate, number, e.g. 1,000; Explanation: to give an indication of the scale of the environment available for use]]></instructions>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.suppliers.catalog</string>
            <parameter category=\"Suppliers catalogue\" description=\"Nature: URL of web site with further details; Value: e.g. http://example.com\" mandatory=\"true\" name=\"Exoscale.suppliers.catalog\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.single.vm.max.ram</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the maximum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment\" mandatory=\"true\" name=\"Exoscale.single.vm.max.ram\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.location</string>
            <parameter category=\"Locations\" description=\"Nature: geographical location of relevant data centre(s); Value: ISO-standard country code and name for cloud location and operational company location, e.g. NL The Netherlands; Explanation: currently, data protection legislation differs per country\" mandatory=\"true\" name=\"Exoscale.location\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value><![CDATA[asdf]]></value>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.single.vm.max.cpu</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the maximum number of CPU cores with which this supplier’s  VMs can be configured; Value: an integer number, e.g. 8; Explanation: to give an indication of the maximum configurable environment\" mandatory=\"true\" name=\"Exoscale.single.vm.max.cpu\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.single.vm.storage.persistent</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the amount of persistent storage (e.g. SSD, disk, tape) available to that VM; Storage access method (e.g. local or network); Storage type: block device or network mount; Value: expressed in relevant terms, e.g. 10 TB per drive/block device; Resilience level or equivalent eg. RAID6, RAID5 etc. Explanation: possibly multiple values, e.g. per technology type. This presumes that storage is associated with a particular VM, i.e. it is locally attached or via a restricted network. Otherwise, it could be up to the total figure, as above\" mandatory=\"true\" name=\"Exoscale.single.vm.storage.persistent\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.overall.capacity.storage</string>
            <parameter category=\"Overall capacity\" description=\"Nature: the amount of persistent storage (e.g. SSD, disk, tape) available within that supplier’s environment; Value: expressed in relevant terms, e.g. 10 PB; Explanation: possibly multiple values, e.g. per technology type\" mandatory=\"true\" name=\"Exoscale.overall.capacity.storage\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.price.ram.per.hour</string>
            <parameter category=\"Price\" description=\"Nature: the price for use of a unit (e.g. 1 GB) of memory per hour; Value: the price in euros, e.g. €0.05\" mandatory=\"true\" name=\"Exoscale.price.ram.per.hour\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.price.charging.period</string>
            <parameter category=\"Price\" description=\"Nature: the period used for charging; Value: the pricing period, e.g. hour, month; Explanation: this could vary per resource, e.g. CPU per hour, storage per month\" mandatory=\"true\" name=\"Exoscale.price.charging.period\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.price.cpu.per.hour</string>
            <parameter category=\"Price\" description=\"Nature: the price for use of a unit of processing per period, e.g. hour; Value: the price in euros, e.g. €0.05\" mandatory=\"true\" name=\"Exoscale.price.cpu.per.hour\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.price.storage.per.hour</string>
            <parameter category=\"Price\" description=\"Nature: the price for use of a unit (e.g. 1 GB) of storage per hour; Value: the price in euros, e.g. €0.0005; Explanation: note that is possible that storage is either associated with a particular VM or as a generally-available resource\" mandatory=\"true\" name=\"Exoscale.price.storage.per.hour\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.single.vm.min.ram</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the minimum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment\" mandatory=\"true\" name=\"Exoscale.single.vm.min.ram\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>Exoscale.price.io</string>
            <parameter category=\"Price\" description=\"Nature: the price for transmitting a unit (e.g. 1 GB) in or out of the environment; Value: the price in euros, e.g. €0.30\" mandatory=\"true\" name=\"Exoscale.price.io\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
      </parameters>
   </serviceCatalog>
   <serviceCatalog cloud=\"StratusLab\" creation=\"2014-12-07 21:40:30.34 CET\" deleted=\"false\" resourceUri=\"service_catalog/StratusLab\">
      <parameters class=\"org.hibernate.collection.internal.PersistentMap\">
         <entry>
            <string>StratusLab.single.vm.min.cpu</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the minimum number of CPU cores with which this supplier’s VMs can be configured; Value: an integer number, e.g. 1; Explanation: to give an indication of the minimum configurable environment\" mandatory=\"true\" name=\"StratusLab.single.vm.min.cpu\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.price.io</string>
            <parameter category=\"Price\" description=\"Nature: the price for transmitting a unit (e.g. 1 GB) in or out of the environment; Value: the price in euros, e.g. €0.30\" mandatory=\"true\" name=\"StratusLab.price.io\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.price.storage.per.hour</string>
            <parameter category=\"Price\" description=\"Nature: the price for use of a unit (e.g. 1 GB) of storage per hour; Value: the price in euros, e.g. €0.0005; Explanation: note that is possible that storage is either associated with a particular VM or as a generally-available resource\" mandatory=\"true\" name=\"StratusLab.price.storage.per.hour\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.overall.capacity.storage</string>
            <parameter category=\"Overall capacity\" description=\"Nature: the amount of persistent storage (e.g. SSD, disk, tape) available within that supplier’s environment; Value: expressed in relevant terms, e.g. 10 PB; Explanation: possibly multiple values, e.g. per technology type\" mandatory=\"true\" name=\"StratusLab.overall.capacity.storage\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.overall.capacity.ram</string>
            <parameter category=\"Overall capacity\" description=\"Nature: the amount of random-access memory in total; Value: expressed in relevant terms, e.g. 10 TB; Explanation: the amount of memory available across the installation as a whole. See below for what is available on any one system\" mandatory=\"true\" name=\"StratusLab.overall.capacity.ram\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.single.vm.max.ram</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the maximum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment\" mandatory=\"true\" name=\"StratusLab.single.vm.max.ram\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.suppliers.catalog</string>
            <parameter category=\"Suppliers catalogue\" description=\"Nature: URL of web site with further details; Value: e.g. http://example.com\" mandatory=\"true\" name=\"StratusLab.suppliers.catalog\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.overall.capacity.cpu</string>
            <parameter category=\"Overall capacity\" description=\"The number of CPU cores (currently) available within (the relevant part of) the supplier’s IaaS environment\" mandatory=\"true\" name=\"StratusLab.overall.capacity.cpu\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions><![CDATA[Value: an integer, and possibly approximate, number, e.g. 1,000; Explanation: to give an indication of the scale of the environment available for use]]></instructions>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.single.vm.storage.volatile</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the amount of volatile storage available locally to that VM; Value: expressed in relevant terms, e.g. 500 GB; Explanation: the amount of “scratch” space, which could be used, e.g. to extend the random access memory of a VM. Local disk space is typically slower than ram but faster than persistent storage space\" mandatory=\"true\" name=\"StratusLab.single.vm.storage.volatile\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.price.cpu.per.hour</string>
            <parameter category=\"Price\" description=\"Nature: the price for use of a unit of processing per period, e.g. hour; Value: the price in euros, e.g. €0.05\" mandatory=\"true\" name=\"StratusLab.price.cpu.per.hour\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.price.charging.unit</string>
            <parameter category=\"Price\" description=\"Nature: the unit used for charging; Value: the pricing unit, e.g. GHz, portion of CPU chip, etc.; Explanation: this could vary per supplier, as there is no standard unit. Work from the ODCA or Deutsche Boerse could be used to derive such a standard, at least for comparative purposes, in the future\" mandatory=\"true\" name=\"StratusLab.price.charging.unit\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.single.vm.max.cpu</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the maximum number of CPU cores with which this supplier’s  VMs can be configured; Value: an integer number, e.g. 8; Explanation: to give an indication of the maximum configurable environment\" mandatory=\"true\" name=\"StratusLab.single.vm.max.cpu\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.price.ram.per.hour</string>
            <parameter category=\"Price\" description=\"Nature: the price for use of a unit (e.g. 1 GB) of memory per hour; Value: the price in euros, e.g. €0.05\" mandatory=\"true\" name=\"StratusLab.price.ram.per.hour\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.single.vm.min.ram</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the minimum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment\" mandatory=\"true\" name=\"StratusLab.single.vm.min.ram\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.location</string>
            <parameter category=\"Locations\" description=\"Nature: geographical location of relevant data centre(s); Value: ISO-standard country code and name for cloud location and operational company location, e.g. NL The Netherlands; Explanation: currently, data protection legislation differs per country\" mandatory=\"true\" name=\"StratusLab.location\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.price.charging.period</string>
            <parameter category=\"Price\" description=\"Nature: the period used for charging; Value: the pricing period, e.g. hour, month; Explanation: this could vary per resource, e.g. CPU per hour, storage per month\" mandatory=\"true\" name=\"StratusLab.price.charging.period\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
         <entry>
            <string>StratusLab.single.vm.storage.persistent</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the amount of persistent storage (e.g. SSD, disk, tape) available to that VM; Storage access method (e.g. local or network); Storage type: block device or network mount; Value: expressed in relevant terms, e.g. 10 TB per drive/block device; Resilience level or equivalent eg. RAID6, RAID5 etc. Explanation: possibly multiple values, e.g. per technology type. This presumes that storage is associated with a particular VM, i.e. it is locally attached or via a restricted network. Otherwise, it could be up to the total figure, as above\" mandatory=\"true\" name=\"StratusLab.single.vm.storage.persistent\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
               <instructions/>
               <value/>
            </parameter>
         </entry>
      </parameters>
   </serviceCatalog>
   </serviceCatalogs>
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
   :service-catalog {:items [{:cloud "Exoscale"
                              :creation "2014-12-07 21:40:30.30 CET"
                              :uri "service_catalog/Exoscale"
                              :parameters [{:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value "asdf", :category "Locations", :description "Nature: geographical location of relevant data centre(s); Value: ISO-standard country code and name for cloud location and operational company location, e.g. NL The Netherlands; Explanation: currently, data protection legislation differs per country", :type "String", :name "Exoscale.location"}
                                           {:help-hint "Value: an integer, and possibly approximate, number, e.g. 1,000; Explanation: to give an indication of the scale of the environment available for use", :read-only? true, :mandatory? true, :order 0, :value nil, :category "Overall capacity", :description "The number of CPU cores (currently) available within (the relevant part of) the supplier’s IaaS environment", :type "String", :name "Exoscale.overall.capacity.cpu"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Overall capacity", :description "Nature: the amount of random-access memory in total; Value: expressed in relevant terms, e.g. 10 TB; Explanation: the amount of memory available across the installation as a whole. See below for what is available on any one system", :type "String", :name "Exoscale.overall.capacity.ram"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Overall capacity", :description "Nature: the amount of persistent storage (e.g. SSD, disk, tape) available within that supplier’s environment; Value: expressed in relevant terms, e.g. 10 PB; Explanation: possibly multiple values, e.g. per technology type", :type "String", :name "Exoscale.overall.capacity.storage"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the period used for charging; Value: the pricing period, e.g. hour, month; Explanation: this could vary per resource, e.g. CPU per hour, storage per month", :type "String", :name "Exoscale.price.charging.period"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the unit used for charging; Value: the pricing unit, e.g. GHz, portion of CPU chip, etc.; Explanation: this could vary per supplier, as there is no standard unit. Work from the ODCA or Deutsche Boerse could be used to derive such a standard, at least for comparative purposes, in the future", :type "String", :name "Exoscale.price.charging.unit"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for use of a unit of processing per period, e.g. hour; Value: the price in euros, e.g. €0.05", :type "String", :name "Exoscale.price.cpu.per.hour"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for transmitting a unit (e.g. 1 GB) in or out of the environment; Value: the price in euros, e.g. €0.30", :type "String", :name "Exoscale.price.io"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for use of a unit (e.g. 1 GB) of memory per hour; Value: the price in euros, e.g. €0.05", :type "String", :name "Exoscale.price.ram.per.hour"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for use of a unit (e.g. 1 GB) of storage per hour; Value: the price in euros, e.g. €0.0005; Explanation: note that is possible that storage is either associated with a particular VM or as a generally-available resource", :type "String", :name "Exoscale.price.storage.per.hour"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the maximum number of CPU cores with which this supplier’s  VMs can be configured; Value: an integer number, e.g. 8; Explanation: to give an indication of the maximum configurable environment", :type "String", :name "Exoscale.single.vm.max.cpu"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the maximum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment", :type "String", :name "Exoscale.single.vm.max.ram"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the minimum number of CPU cores with which this supplier’s VMs can be configured; Value: an integer number, e.g. 1; Explanation: to give an indication of the minimum configurable environment", :type "String", :name "Exoscale.single.vm.min.cpu"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the minimum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment", :type "String", :name "Exoscale.single.vm.min.ram"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the amount of persistent storage (e.g. SSD, disk, tape) available to that VM; Storage access method (e.g. local or network); Storage type: block device or network mount; Value: expressed in relevant terms, e.g. 10 TB per drive/block device; Resilience level or equivalent eg. RAID6, RAID5 etc. Explanation: possibly multiple values, e.g. per technology type. This presumes that storage is associated with a particular VM, i.e. it is locally attached or via a restricted network. Otherwise, it could be up to the total figure, as above", :type "String", :name "Exoscale.single.vm.storage.persistent"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the amount of volatile storage available locally to that VM; Value: expressed in relevant terms, e.g. 500 GB; Explanation: the amount of “scratch” space, which could be used, e.g. to extend the random access memory of a VM. Local disk space is typically slower than ram but faster than persistent storage space", :type "String", :name "Exoscale.single.vm.storage.volatile"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Suppliers catalogue", :description "Nature: URL of web site with further details; Value: e.g. http://example.com", :type "String", :name "Exoscale.suppliers.catalog"}]}
                             {:cloud "StratusLab"
                              :creation "2014-12-07 21:40:30.34 CET"
                              :uri "service_catalog/StratusLab"
                              :parameters [{:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Locations", :description "Nature: geographical location of relevant data centre(s); Value: ISO-standard country code and name for cloud location and operational company location, e.g. NL The Netherlands; Explanation: currently, data protection legislation differs per country", :type "String", :name "StratusLab.location"}
                                           {:help-hint "Value: an integer, and possibly approximate, number, e.g. 1,000; Explanation: to give an indication of the scale of the environment available for use", :read-only? true, :mandatory? true, :order 0, :value nil, :category "Overall capacity", :description "The number of CPU cores (currently) available within (the relevant part of) the supplier’s IaaS environment", :type "String", :name "StratusLab.overall.capacity.cpu"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Overall capacity", :description "Nature: the amount of random-access memory in total; Value: expressed in relevant terms, e.g. 10 TB; Explanation: the amount of memory available across the installation as a whole. See below for what is available on any one system", :type "String", :name "StratusLab.overall.capacity.ram"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Overall capacity", :description "Nature: the amount of persistent storage (e.g. SSD, disk, tape) available within that supplier’s environment; Value: expressed in relevant terms, e.g. 10 PB; Explanation: possibly multiple values, e.g. per technology type", :type "String", :name "StratusLab.overall.capacity.storage"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the period used for charging; Value: the pricing period, e.g. hour, month; Explanation: this could vary per resource, e.g. CPU per hour, storage per month", :type "String", :name "StratusLab.price.charging.period"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the unit used for charging; Value: the pricing unit, e.g. GHz, portion of CPU chip, etc.; Explanation: this could vary per supplier, as there is no standard unit. Work from the ODCA or Deutsche Boerse could be used to derive such a standard, at least for comparative purposes, in the future", :type "String", :name "StratusLab.price.charging.unit"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for use of a unit of processing per period, e.g. hour; Value: the price in euros, e.g. €0.05", :type "String", :name "StratusLab.price.cpu.per.hour"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for transmitting a unit (e.g. 1 GB) in or out of the environment; Value: the price in euros, e.g. €0.30", :type "String", :name "StratusLab.price.io"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for use of a unit (e.g. 1 GB) of memory per hour; Value: the price in euros, e.g. €0.05", :type "String", :name "StratusLab.price.ram.per.hour"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Price", :description "Nature: the price for use of a unit (e.g. 1 GB) of storage per hour; Value: the price in euros, e.g. €0.0005; Explanation: note that is possible that storage is either associated with a particular VM or as a generally-available resource", :type "String", :name "StratusLab.price.storage.per.hour"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the maximum number of CPU cores with which this supplier’s  VMs can be configured; Value: an integer number, e.g. 8; Explanation: to give an indication of the maximum configurable environment", :type "String", :name "StratusLab.single.vm.max.cpu"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the maximum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment", :type "String", :name "StratusLab.single.vm.max.ram"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the minimum number of CPU cores with which this supplier’s VMs can be configured; Value: an integer number, e.g. 1; Explanation: to give an indication of the minimum configurable environment", :type "String", :name "StratusLab.single.vm.min.cpu"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the minimum amount of random-access memory (currently) available within VMs; Value: expressed in relevant terms, e.g. 128 GB; Explanation: the amount of memory available to any one VM within the supplier’s IaaS environment", :type "String", :name "StratusLab.single.vm.min.ram"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the amount of persistent storage (e.g. SSD, disk, tape) available to that VM; Storage access method (e.g. local or network); Storage type: block device or network mount; Value: expressed in relevant terms, e.g. 10 TB per drive/block device; Resilience level or equivalent eg. RAID6, RAID5 etc. Explanation: possibly multiple values, e.g. per technology type. This presumes that storage is associated with a particular VM, i.e. it is locally attached or via a restricted network. Otherwise, it could be up to the total figure, as above", :type "String", :name "StratusLab.single.vm.storage.persistent"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Single VM capacity", :description "Nature: the amount of volatile storage available locally to that VM; Value: expressed in relevant terms, e.g. 500 GB; Explanation: the amount of “scratch” space, which could be used, e.g. to extend the random access memory of a VM. Local disk space is typically slower than ram but faster than persistent storage space", :type "String", :name "StratusLab.single.vm.storage.volatile"}
                                           {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Suppliers catalogue", :description "Nature: URL of web site with further details; Value: e.g. http://example.com", :type "String", :name "StratusLab.suppliers.catalog"}]}]}})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))