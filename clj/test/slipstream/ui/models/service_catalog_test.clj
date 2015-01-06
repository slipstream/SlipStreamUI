(ns slipstream.ui.models.service-catalog-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.service-catalog :as model]))

(def raw-metadata-str
  "<serviceCatalogs>
   <serviceCatalog cloud=\"Exoscale\" creation=\"2014-12-16 16:50:36.852 CET\" deleted=\"false\" resourceUri=\"service_catalog/Exoscale\">
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
               <value>asdf</value>
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
            <string>Exoscale.single.vm.max.cpu</string>
            <parameter category=\"Single VM capacity\" description=\"Nature: the maximum number of CPU cores with which this supplier’s  VMs can be configured; Value: an integer number, e.g. 8; Explanation: to give an indication of the maximum configurable environment\" mandatory=\"true\" name=\"Exoscale.single.vm.max.cpu\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
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
            <string>Exoscale.price.io</string>
            <parameter category=\"Price\" description=\"Nature: the price for transmitting a unit (e.g. 1 GB) in or out of the environment; Value: the price in euros, e.g. €0.30\" mandatory=\"true\" name=\"Exoscale.price.io\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
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
      </parameters>
   </serviceCatalog>
   <serviceCatalog cloud=\"StratusLab\" creation=\"2014-12-16 16:50:36.879 CET\" deleted=\"false\" resourceUri=\"service_catalog/StratusLab\">
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
            <string>StratusLab.price.ram.per.hour</string>
            <parameter category=\"Price\" description=\"Nature: the price for use of a unit (e.g. 1 GB) of memory per hour; Value: the price in euros, e.g. €0.05\" mandatory=\"true\" name=\"StratusLab.price.ram.per.hour\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
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
<user issuper=\"true\" name=\"rob\" resourceUri=\"user/rob\"/><serviceConfiguration creation=\"2014-12-16 16:50:36.796 CET\" deleted=\"false\">
   <parameters class=\"java.util.concurrent.ConcurrentHashMap\">
      <entry>
         <string>slipstream.service.catalog.enable</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Enable service catalog feature.\" mandatory=\"true\" name=\"slipstream.service.catalog.enable\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Boolean\">
            <instructions/>
            <value><![CDATA[true]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.support.email</string>
         <parameter category=\"SlipStream_Support\" description=\"Email address for SlipStream support requests\" mandatory=\"true\" name=\"slipstream.support.email\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[support@example.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string>Exoscale.endpoint</string>
         <parameter category=\"Exoscale\" description=\"Service endpoint for Exoscale (e.g. http://example.com:5000)\" mandatory=\"true\" name=\"Exoscale.endpoint\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value><![CDATA[fdf]]></value>
         </parameter>
      </entry>
      <entry>
         <string>Exoscale.orchestrator.imageid</string>
         <parameter category=\"Exoscale\" description=\"Image Id of the orchestrator for Exoscale\" mandatory=\"true\" name=\"Exoscale.orchestrator.imageid\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.reports.location</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Location where the deployments and build reports are saved\" mandatory=\"true\" name=\"slipstream.reports.location\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[/Users/sixsq_rob/Dropbox/dev/projects/2014/SlipStream/tmp/slipstream/reports]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.version</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Installed SlipStream version\" mandatory=\"true\" name=\"slipstream.version\" order=\"0\" order_=\"0\" readonly=\"true\" type=\"String\">
            <instructions/>
            <value><![CDATA[2.3.8-SNAPSHOT]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.mail.username</string>
         <parameter category=\"SlipStream_Support\" description=\"Username for SMTP server.\" mandatory=\"true\" name=\"slipstream.mail.username\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions><![CDATA[Username of the mail server account you wan to use to send registration emails.]]></instructions>
            <value><![CDATA[sender]]></value>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.max.iaas.workers</string>
         <parameter category=\"StratusLab\" description=\"Max number of concurrently provisioned VMs by orchestrator\" mandatory=\"true\" name=\"StratusLab.max.iaas.workers\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value><![CDATA[20]]></value>
         </parameter>
      </entry>
      <entry>
         <string>Exoscale.max.iaas.workers</string>
         <parameter category=\"Exoscale\" description=\"Max number of concurrently provisioned VMs by orchestrator\" mandatory=\"true\" name=\"Exoscale.max.iaas.workers\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value><![CDATA[200]]></value>
         </parameter>
      </entry>
      <entry>
         <string>Exoscale.quota.vm</string>
         <parameter category=\"Exoscale\" description=\"VM quota for Exoscale (i.e. maximum number number of VM allowed)\" mandatory=\"true\" name=\"Exoscale.quota.vm\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>cloud.connector.class</string>
         <parameter category=\"SlipStream_Basics\" description=\"Cloud connector java class name(s) (comma separated for multi-cloud configuration)\" mandatory=\"true\" name=\"cloud.connector.class\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Text\">
            <instructions/>
            <value><![CDATA[StratusLab:stratuslab,
Exoscale:cloudstack]]></value>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.endpoint</string>
         <parameter category=\"StratusLab\" description=\"Service endpoint for StratusLab (e.g. http://example.com:5000)\" mandatory=\"true\" name=\"StratusLab.endpoint\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.metering.hostname</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Metering server full hostname, including protocol, hostname/ip and port (e.g. http://localhost:2005).\" mandatory=\"true\" name=\"slipstream.metering.hostname\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[http://localhost:2005]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.update.clienturl</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Endpoint of the SlipStream client tarball\" mandatory=\"true\" name=\"slipstream.update.clienturl\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[https://example.com/downloads/slipstreamclient.tgz]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.update.clientbootstrapurl</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Endpoint of the SlipStream client bootstrap script\" mandatory=\"true\" name=\"slipstream.update.clientbootstrapurl\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[https://example.com/downloads/slipstream.bootstrap]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.registration.email</string>
         <parameter category=\"SlipStream_Support\" description=\"Email address for account approvals, etc.\" mandatory=\"true\" name=\"slipstream.registration.email\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions><![CDATA[<h1>email address</h1> to use for registration]]></instructions>
            <value><![CDATA[register@example.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.mail.password</string>
         <parameter category=\"SlipStream_Support\" description=\"Password for SMTP server.\" mandatory=\"true\" name=\"slipstream.mail.password\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Password\">
            <instructions/>
            <value><![CDATA[1234]]></value>
         </parameter>
      </entry>
      <entry>
         <string>Exoscale.zone</string>
         <parameter category=\"Exoscale\" description=\"Zone\" mandatory=\"true\" name=\"Exoscale.zone\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.mail.ssl</string>
         <parameter category=\"SlipStream_Support\" description=\"Use SSL for SMTP server.\" mandatory=\"true\" name=\"slipstream.mail.ssl\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Boolean\">
            <instructions/>
            <value><![CDATA[true]]></value>
         </parameter>
      </entry>
      <entry>
         <string>cloud.connector.orchestrator.publicsshkey</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Path to the SSH public key to put in the orchestrator\" mandatory=\"true\" name=\"cloud.connector.orchestrator.publicsshkey\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[/Users/sixsq_rob/.ssh/id_rsa_slipstream_test.pub]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.mail.port</string>
         <parameter category=\"SlipStream_Support\" description=\"Port on SMTP server (defaults to standard ports).\" mandatory=\"true\" name=\"slipstream.mail.port\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[465]]></value>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.pdisk.endpoint</string>
         <parameter category=\"StratusLab\" description=\"PDisk endpoint\" mandatory=\"true\" name=\"StratusLab.pdisk.endpoint\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>Exoscale.orchestrator.instance.type</string>
         <parameter category=\"Exoscale\" description=\"Orchestrator instance type\" mandatory=\"true\" name=\"Exoscale.orchestrator.instance.type\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.update.clienturl</string>
         <parameter category=\"StratusLab\" description=\"URL with the cloud client specific connector\" mandatory=\"true\" name=\"StratusLab.update.clienturl\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.registration.enable</string>
         <parameter category=\"SlipStream_Basics\" description=\"Allow user self registration. If checked, user will be able to create accounts themselves.\" mandatory=\"true\" name=\"slipstream.registration.enable\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Boolean\">
            <instructions/>
            <value><![CDATA[true]]></value>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.orchestrator.instance.type</string>
         <parameter category=\"StratusLab\" description=\"Orchestrator instance type\" mandatory=\"true\" name=\"StratusLab.orchestrator.instance.type\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.quota.enable</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Quota enforcement enabled\" mandatory=\"true\" name=\"slipstream.quota.enable\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Boolean\">
            <instructions/>
            <value><![CDATA[true]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.metering.enable</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Metering enabled\" mandatory=\"true\" name=\"slipstream.metering.enable\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Boolean\">
            <instructions/>
            <value><![CDATA[true]]></value>
         </parameter>
      </entry>
      <entry>
         <string>cloud.connector.orchestrator.privatesshkey</string>
         <parameter category=\"SlipStream_Advanced\" description=\"Path to the SSH private key used to connect to the orchestrator (used only for some Clouds)\" mandatory=\"true\" name=\"cloud.connector.orchestrator.privatesshkey\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>cloud.connector.library.libcloud.url</string>
         <parameter category=\"SlipStream_Advanced\" description=\"URL to fetch libcloud library from\" mandatory=\"true\" name=\"cloud.connector.library.libcloud.url\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions><![CDATA[URL should point to a valid gzipped tarball.]]></instructions>
            <value><![CDATA[https://example.com/downloads/libcloud.tgz]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.mail.debug</string>
         <parameter category=\"SlipStream_Support\" description=\"Debug mail sending.\" mandatory=\"true\" name=\"slipstream.mail.debug\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"Boolean\">
            <instructions/>
            <value><![CDATA[true]]></value>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.quota.vm</string>
         <parameter category=\"StratusLab\" description=\"VM quota for StratusLab (i.e. maximum number number of VM allowed)\" mandatory=\"true\" name=\"StratusLab.quota.vm\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.marketplace.endpoint</string>
         <parameter category=\"StratusLab\" description=\"Marketplace endpoint\" mandatory=\"true\" name=\"StratusLab.marketplace.endpoint\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.mail.host</string>
         <parameter category=\"SlipStream_Support\" description=\"Host for SMTP server for email notifications.\" mandatory=\"true\" name=\"slipstream.mail.host\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[smtp.gmail.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string>slipstream.base.url</string>
         <parameter category=\"SlipStream_Basics\" description=\"Default URL and port for the SlipStream RESTlet\" mandatory=\"true\" name=\"slipstream.base.url\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <instructions/>
            <value><![CDATA[https://example.com]]></value>
         </parameter>
      </entry>
      <entry>
         <string>StratusLab.orchestrator.imageid</string>
         <parameter category=\"StratusLab\" description=\"Image Id of the orchestrator for StratusLab\" mandatory=\"true\" name=\"StratusLab.orchestrator.imageid\" order=\"0\" order_=\"0\" readonly=\"false\" type=\"String\">
            <value/>
         </parameter>
      </entry>
   </parameters>
</serviceConfiguration></serviceCatalogs>")

(def parsed-metadata
  {:items [{:cloud "Exoscale"
            :creation "2014-12-16 16:50:36.852 CET"
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
            :creation "2014-12-16 16:50:36.879 CET"
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
                         {:help-hint nil, :read-only? true, :mandatory? true, :order 0, :value nil, :category "Suppliers catalogue", :description "Nature: URL of web site with further details; Value: e.g. http://example.com", :type "String", :name "StratusLab.suppliers.catalog"}]}]})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
