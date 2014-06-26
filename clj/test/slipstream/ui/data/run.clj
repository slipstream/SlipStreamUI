(ns slipstream.ui.data.run
  (:require [net.cgrand.enlive-html :as html]))

(def xml-run (first (html/html-snippet "<run category='Deployment' deleted='false' resourceUri='run/06f207d4-d25b-4597-a23a-f79a07b2f791' uuid='06f207d4-d25b-4597-a23a-f79a07b2f791' type='Orchestration' cloudServiceName='interoute' moduleResourceUri='module/Public/Tutorials/HelloWorld/client_server/11' startTime='2013-06-12 15:39:55.575 CEST' nodeNames='orchestrator-interoute, testclient1.1, apache1.1, ' user='meb' creation='2013-06-12 15:39:55.575 CEST' state='Terminal' status='Done' groups='interoute:testclient1, interoute:apache1, '>
   <parameters class='org.hibernate.collection.PersistentMap'>
      <entry>
         <string><![CDATA[apache1--cloudservice]]></string>
         <parameter class='com.sixsq.slipstream.persistence.RunParameter' name='apache1--cloudservice' description='Cloud Service where the node resides' category='General' mandatory='false' type='String' readonly='false'>
            <value><![CDATA[interoute]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1--multiplicity]]></string>
         <parameter class='com.sixsq.slipstream.persistence.RunParameter' name='testclient1--multiplicity' description='Multiplicity number' category='General' mandatory='false' type='String' readonly='false'>
            <value><![CDATA[1]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1--cloudservice]]></string>
         <parameter class='com.sixsq.slipstream.persistence.RunParameter' name='testclient1--cloudservice' description='Cloud Service where the node resides' category='General' mandatory='false' type='String' readonly='false'>
            <value><![CDATA[interoute]]></value>
         </parameter>
      </entry>
      <entry>
         <string><![CDATA[apache1--multiplicity]]></string>
         <parameter class='com.sixsq.slipstream.persistence.RunParameter' name='apache1--multiplicity' description='Multiplicity number' category='General' mandatory='false' type='String' readonly='false'>
            <value><![CDATA[1]]></value>
         </parameter>
      </entry>
   </parameters>
   <runtimeParameters class='org.hibernate.collection.PersistentMap'>
      <entry>
         <string><![CDATA[testclient1.1:statecustom]]></string>
         <runtimeParameter description='Custom state' deleted='false' key='testclient1.1:statecustom' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[OK: Hello from Apache deployed by SlipStream!]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:tags]]></string>
         <runtimeParameter description='Comma separated tag values' deleted='false' key='orchestrator-interoute:tags' isSet='false' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:interoute.cpu]]></string>
         <runtimeParameter description='Number of CPUs' deleted='false' key='apache1.1:interoute.cpu' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:url.service]]></string>
         <runtimeParameter description='URL for machine deployment' deleted='false' key='apache1.1:url.service' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[http://localhost:8080]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:url.ssh]]></string>
         <runtimeParameter description='URL for ssh on machine' deleted='false' key='apache1.1:url.ssh' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[http://localhost:8080]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:url.something.else]]></string>
         <runtimeParameter description='another URL for machine' deleted='false' key='apache1.1:url.something.else' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[http://localhost:8080]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:vmstate]]></string>
         <runtimeParameter description='State of the VM, according to the cloud layer' deleted='false' key='testclient1.1:vmstate' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[Unknown]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:vmstate]]></string>
         <runtimeParameter description='State of the VM, according to the cloud layer' deleted='false' key='testclient1.1:vmstate' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[Unknown]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:vmstate]]></string>
         <runtimeParameter description='State of the VM, according to the cloud layer' deleted='false' key='orchestrator-interoute:vmstate' isSet='true' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Unknown]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:cloudsigma.smp]]></string>
         <runtimeParameter description='SMP (number of virtual CPUs)' deleted='false' key='apache1.1:cloudsigma.smp' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:nodename]]></string>
         <runtimeParameter description='Nodename' deleted='false' key='apache1.1:nodename' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[apache1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[ss:groups]]></string>
         <runtimeParameter description='Comma separated node groups' deleted='false' key='ss:groups' isSet='true' group='Global' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[interoute:testclient1, interoute:apache1, ]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:index]]></string>
         <runtimeParameter description='Node index' deleted='false' key='testclient1.1:index' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:multiplicity]]></string>
         <runtimeParameter description='Multiplicity value for this node' deleted='false' key='testclient1.1:multiplicity' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:cloudsigma.ram]]></string>
         <runtimeParameter description='RAM in GB' deleted='false' key='apache1.1:cloudsigma.ram' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[ss:tags]]></string>
         <runtimeParameter description='Comma separated tag values' deleted='false' key='ss:tags' isSet='false' group='Global' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[These are my tags]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:interoute.login.password]]></string>
         <runtimeParameter description='SSH login password for the image' deleted='false' key='apache1.1:interoute.login.password' isSet='true' group='apache1.1' mapsOthers='false' type='Password' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[rootpass]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:abort]]></string>
         <runtimeParameter description='Machine abort flag, set when aborting' deleted='false' key='orchestrator-interoute:abort' isSet='false' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:extra.disk.volatile]]></string>
         <runtimeParameter description='Volatile extra disk in GB' deleted='false' key='testclient1.1:extra.disk.volatile' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[ss:state]]></string>
         <runtimeParameter description='Global execution state' deleted='false' key='ss:state' isSet='true' group='Global' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Terminal]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:statemessage]]></string>
         <runtimeParameter description='Machine state message' deleted='false' key='testclient1.1:statemessage' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Shutdown]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:atos.ram]]></string>
         <runtimeParameter description='Requested RAM (in GB)' deleted='false' key='testclient1.1:atos.ram' isSet='false' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'/>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:atos.disks.bus.type]]></string>
         <runtimeParameter description='VM disks bus type' deleted='false' key='apache1.1:atos.disks.bus.type' isSet='true' group='apache1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[virtio]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:atos.ram]]></string>
         <runtimeParameter description='Requested RAM (in GB)' deleted='false' key='apache1.1:atos.ram' isSet='false' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'/>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:cloudservice]]></string>
         <runtimeParameter description='Cloud Service where the node resides' deleted='false' key='apache1.1:cloudservice' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[interoute]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:complete]]></string>
         <runtimeParameter description='&apos;true&apos; when current state is completed' deleted='false' key='apache1.1:complete' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[true]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:abort]]></string>
         <runtimeParameter description='Machine abort flag, set when aborting' deleted='false' key='apache1.1:abort' isSet='false' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:statecustom]]></string>
         <runtimeParameter description='Custom state' deleted='false' key='apache1.1:statecustom' isSet='false' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:complete]]></string>
         <runtimeParameter description='&apos;true&apos; when current state is completed' deleted='false' key='orchestrator-interoute:complete' isSet='true' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[false]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:interoute.login.password]]></string>
         <runtimeParameter description='SSH login password for the image' deleted='false' key='testclient1.1:interoute.login.password' isSet='true' group='testclient1.1' mapsOthers='false' type='Password' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'><![CDATA[rootpass]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:tags]]></string>
         <runtimeParameter description='Comma separated tag values' deleted='false' key='apache1.1:tags' isSet='false' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:atos.instance.type]]></string>
         <runtimeParameter description='Cloud instance type' deleted='false' key='apache1.1:atos.instance.type' isSet='true' group='apache1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[m1.small]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:statemessage]]></string>
         <runtimeParameter description='Machine state message' deleted='false' key='orchestrator-interoute:statemessage' isSet='true' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Inactive]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:cloudsigma.login.password]]></string>
         <runtimeParameter description='SSH login password for the image' deleted='false' key='testclient1.1:cloudsigma.login.password' isSet='true' group='testclient1.1' mapsOthers='false' type='Password' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'><![CDATA[ubuntupass]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:interoute.network]]></string>
         <runtimeParameter description='Network types to start the image on' deleted='false' key='apache1.1:interoute.network' isSet='true' group='apache1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[ExternalAndPrivate]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:hostname]]></string>
         <runtimeParameter description='hostname/ip of the image' deleted='false' key='orchestrator-interoute:hostname' isSet='true' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[195.143.226.153]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:hostname]]></string>
         <runtimeParameter description='hostname/ip of the image' deleted='false' key='apache1.1:hostname' isSet='true' group='apache1.1' mapsOthers='true' type='String' mappedRuntimeParameterNames='testclient1.1:webserver.hostname,' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[195.143.226.154]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:network]]></string>
         <runtimeParameter description='Network type' deleted='false' key='apache1.1:network' isSet='true' group='apache1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[Public]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:statemessage]]></string>
         <runtimeParameter description='Machine state message' deleted='false' key='apache1.1:statemessage' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[Shutdown]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:state]]></string>
         <runtimeParameter description='Machine state' deleted='false' key='orchestrator-interoute:state' isSet='true' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Terminal]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:webserver.port]]></string>
         <runtimeParameter description='Port on which the web server listens' deleted='false' key='testclient1.1:webserver.port' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'><![CDATA[8080]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:extra.disk.volatile]]></string>
         <runtimeParameter description='Volatile extra disk in GB' deleted='false' key='apache1.1:extra.disk.volatile' isSet='false' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'/>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:vmstate]]></string>
         <runtimeParameter description='State of the VM, according to the cloud layer' deleted='false' key='apache1.1:vmstate' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[Unknown]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:state]]></string>
         <runtimeParameter description='Machine state' deleted='false' key='testclient1.1:state' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Terminal]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:index]]></string>
         <runtimeParameter description='Node index' deleted='false' key='apache1.1:index' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:interoute.cpu]]></string>
         <runtimeParameter description='Number of CPUs' deleted='false' key='testclient1.1:interoute.cpu' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:atos.cpu]]></string>
         <runtimeParameter description='Requested CPUs' deleted='false' key='testclient1.1:atos.cpu' isSet='false' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'/>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:atos.cpu]]></string>
         <runtimeParameter description='Requested CPUs' deleted='false' key='apache1.1:atos.cpu' isSet='false' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'/>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:state]]></string>
         <runtimeParameter description='Machine state' deleted='false' key='apache1.1:state' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[Terminal]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:cloudservice]]></string>
         <runtimeParameter description='Cloud Service where the node resides' deleted='false' key='orchestrator-interoute:cloudservice' isSet='true' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[interoute]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:cloudsigma.cpu]]></string>
         <runtimeParameter description='CPU in GHz' deleted='false' key='testclient1.1:cloudsigma.cpu' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:webserver.ready]]></string>
         <runtimeParameter description='Server ready to recieve connections' deleted='false' key='testclient1.1:webserver.ready' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'><![CDATA[true]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[ss:category]]></string>
         <runtimeParameter description='Module category' deleted='false' key='ss:category' isSet='true' group='Global' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Deployment]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[ss:abort]]></string>
         <runtimeParameter description='Run abort flag, set when aborting' deleted='false' key='ss:abort' isSet='false' group='Global' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:nodename]]></string>
         <runtimeParameter description='Nodename' deleted='false' key='testclient1.1:nodename' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[testclient1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:cloudsigma.smp]]></string>
         <runtimeParameter description='SMP (number of virtual CPUs)' deleted='false' key='testclient1.1:cloudsigma.smp' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:hostname]]></string>
         <runtimeParameter description='hostname/ip of the image' deleted='false' key='testclient1.1:hostname' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'><![CDATA[192.168.0.2]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:instanceid]]></string>
         <runtimeParameter description='Cloud instance id' deleted='false' key='testclient1.1:instanceid' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[22dc1bdf-a764-4b92-83d0-cb2d746a7ed7]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:interoute.ram]]></string>
         <runtimeParameter description='RAM in GB' deleted='false' key='apache1.1:interoute.ram' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:multiplicity]]></string>
         <runtimeParameter description='Multiplicity value for this node' deleted='false' key='apache1.1:multiplicity' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:ready]]></string>
         <runtimeParameter description='Server ready to recieve connections' deleted='false' key='apache1.1:ready' isSet='true' group='apache1.1' mapsOthers='true' type='String' mappedRuntimeParameterNames='testclient1.1:webserver.ready,' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[true]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:webserver.hostname]]></string>
         <runtimeParameter description='Server hostname' deleted='false' key='testclient1.1:webserver.hostname' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[195.143.226.154]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:cloudservice]]></string>
         <runtimeParameter description='Cloud Service where the node resides' deleted='false' key='testclient1.1:cloudservice' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[interoute]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:interoute.ram]]></string>
         <runtimeParameter description='RAM in GB' deleted='false' key='testclient1.1:interoute.ram' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[ss:statemessage]]></string>
         <runtimeParameter description='Global execution state message' deleted='false' key='ss:statemessage' isSet='true' group='Global' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[Inactive]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:tags]]></string>
         <runtimeParameter description='Comma separated tag values' deleted='false' key='testclient1.1:tags' isSet='false' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:instanceid]]></string>
         <runtimeParameter description='Cloud instance id' deleted='false' key='orchestrator-interoute:instanceid' isSet='true' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[09a1b54a-08dd-4eb0-8712-5320a744c483]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:cloudsigma.ram]]></string>
         <runtimeParameter description='RAM in GB' deleted='false' key='testclient1.1:cloudsigma.ram' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:abort]]></string>
         <runtimeParameter description='Machine abort flag, set when aborting' deleted='false' key='testclient1.1:abort' isSet='false' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[orchestrator-interoute:statecustom]]></string>
         <runtimeParameter description='Custom state' deleted='false' key='orchestrator-interoute:statecustom' isSet='false' group='orchestrator-interoute' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.575 CEST'><![CDATA[]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:complete]]></string>
         <runtimeParameter description='&apos;true&apos; when current state is completed' deleted='false' key='testclient1.1:complete' isSet='true' group='testclient1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.576 CEST'><![CDATA[true]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:cloudsigma.cpu]]></string>
         <runtimeParameter description='CPU in GHz' deleted='false' key='apache1.1:cloudsigma.cpu' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[1]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:atos.instance.type]]></string>
         <runtimeParameter description='Cloud instance type' deleted='false' key='testclient1.1:atos.instance.type' isSet='true' group='testclient1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[m1.small]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:interoute.network]]></string>
         <runtimeParameter description='Network types to start the image on' deleted='false' key='testclient1.1:interoute.network' isSet='true' group='testclient1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[Private]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:port]]></string>
         <runtimeParameter description='Port' deleted='false' key='apache1.1:port' isSet='true' group='apache1.1' mapsOthers='true' type='String' mappedRuntimeParameterNames='testclient1.1:webserver.port,' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[8080]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:instanceid]]></string>
         <runtimeParameter description='Cloud instance id' deleted='false' key='apache1.1:instanceid' isSet='true' group='apache1.1' mapsOthers='false' type='String' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[5ce32b77-ee11-4c9b-86c8-a8a9c6484d02]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:atos.disks.bus.type]]></string>
         <runtimeParameter description='VM disks bus type' deleted='false' key='testclient1.1:atos.disks.bus.type' isSet='true' group='testclient1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.587 CEST'><![CDATA[virtio]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[apache1.1:cloudsigma.login.password]]></string>
         <runtimeParameter description='SSH login password for the image' deleted='false' key='apache1.1:cloudsigma.login.password' isSet='true' group='apache1.1' mapsOthers='false' type='Password' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.598 CEST'><![CDATA[ubuntupass]]></runtimeParameter>
      </entry>
      <entry>
         <string><![CDATA[testclient1.1:network]]></string>
         <runtimeParameter description='Network type' deleted='false' key='testclient1.1:network' isSet='true' group='testclient1.1' mapsOthers='false' type='Enum' mappedRuntimeParameterNames='' isMappedValue='false' creation='2013-06-12 15:39:55.586 CEST'><![CDATA[Public]]></runtimeParameter>
      </entry>
   </runtimeParameters>
   <module class='com.sixsq.slipstream.persistence.DeploymentModule' lastModified='2013-03-06 14:31:01.861 CET' category='Deployment' deleted='false' resourceUri='module/Public/Tutorials/HelloWorld/client_server/11' parentUri='module/Public/Tutorials/HelloWorld' name='Public/Tutorials/HelloWorld/client_server' version='11' creation='2013-03-06 14:31:01.860 CET' shortName='client_server'>
      <parameters class='org.hibernate.collection.PersistentMap'/>
      <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='true' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='true' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
         <groupMembers class='java.util.ArrayList'/>
      </authz>
      <nodes class='org.hibernate.collection.PersistentMap'>
         <entry>
            <string>testclient1</string>
            <node deleted='false' name='testclient1' multiplicity='1' cloudService='interoute' imageUri='module/Public/Tutorials/HelloWorld/testclient' creation='2013-03-06 14:31:01.861 CET' network='Public'>
               <parameters class='org.hibernate.collection.PersistentMap'>
                  <entry>
                     <string><![CDATA[webserver.port]]></string>
                     <parameter class='com.sixsq.slipstream.persistence.NodeParameter' name='webserver.port' description='' category='General' mandatory='false' type='String' readonly='false' isMappedValue='true'>
                        <value><![CDATA[apache1:port]]></value>
                     </parameter>
                  </entry>
                  <entry>
                     <string><![CDATA[webserver.ready]]></string>
                     <parameter class='com.sixsq.slipstream.persistence.NodeParameter' name='webserver.ready' description='' category='General' mandatory='false' type='String' readonly='false' isMappedValue='true'>
                        <value><![CDATA[apache1:ready]]></value>
                     </parameter>
                  </entry>
                  <entry>
                     <string><![CDATA[webserver.hostname]]></string>
                     <parameter class='com.sixsq.slipstream.persistence.NodeParameter' name='webserver.hostname' description='' category='General' mandatory='false' type='String' readonly='false' isMappedValue='true'>
                        <value><![CDATA[apache1:hostname]]></value>
                     </parameter>
                  </entry>
               </parameters>
               <parameterMappings class='org.hibernate.collection.PersistentMap'>
                  <entry>
                     <string>webserver.port</string>
                     <nodeParameter name='webserver.port' description='' category='General' mandatory='false' type='String' readonly='false' isMappedValue='true'>
                        <value><![CDATA[apache1:port]]></value>
                     </nodeParameter>
                  </entry>
                  <entry>
                     <string>webserver.ready</string>
                     <nodeParameter name='webserver.ready' description='' category='General' mandatory='false' type='String' readonly='false' isMappedValue='true'>
                        <value><![CDATA[apache1:ready]]></value>
                     </nodeParameter>
                  </entry>
                  <entry>
                     <string>webserver.hostname</string>
                     <nodeParameter name='webserver.hostname' description='' category='General' mandatory='false' type='String' readonly='false' isMappedValue='true'>
                        <value><![CDATA[apache1:hostname]]></value>
                     </nodeParameter>
                  </entry>
               </parameterMappings>
               <image lastModified='2013-03-13 13:58:44.877 CET' category='Image' description='' deleted='false' resourceUri='module/Public/Tutorials/HelloWorld/testclient/26' parentUri='module/Public/Tutorials/HelloWorld' name='Public/Tutorials/HelloWorld/testclient' version='26' moduleReferenceUri='module/Public/BaseImages/Ubuntu/12.04' isBase='false' imageId='SS_ORCH_UBUNTU1204' creation='2013-03-06 14:31:01.797 CET' shortName='testclient' loginUser='root' platform='ubuntu'>
                  <parameters class='org.hibernate.collection.PersistentMap'>
                     <entry>
                        <string><![CDATA[extra.disk.volatile]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='extra.disk.volatile' description='Volatile extra disk in GB' category='Cloud' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[webserver.port]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='webserver.port' description='Port on which the web server listens' category='Input' mandatory='false' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[webserver.ready]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='webserver.ready' description='Server ready to recieve connections' category='Input' mandatory='false' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.ram]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.ram' description='RAM in GB' category='cloudsigma' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[hostname]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='hostname' description='hostname/ip of the image' category='Output' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.login.password]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.login.password' description='SSH login password for the image' category='cloudsigma' mandatory='true' type='Password' readonly='false' isSet='true'>
                           <value><![CDATA[ubuntupass]]></value>
                           <defaultValue><![CDATA[ubuntupass]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.login.password]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.login.password' description='SSH login password for the image' category='interoute' mandatory='true' type='Password' readonly='false' isSet='true'>
                           <value><![CDATA[rootpass]]></value>
                           <defaultValue><![CDATA[rootpass]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.ram]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.ram' description='Requested RAM (in GB)' category='atos' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[network]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='network' description='Network type' category='Cloud' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='2'>
                              <string>Public</string>
                              <string>Private</string>
                           </enumValues>
                           <value><![CDATA[Public]]></value>
                           <defaultValue><![CDATA[Public]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.cpu]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.cpu' description='Requested CPUs' category='atos' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.cpu]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.cpu' description='Number of CPUs' category='interoute' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.disks.bus.type]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.disks.bus.type' description='VM disks bus type' category='atos' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='2'>
                              <string>virtio</string>
                              <string>scsi</string>
                           </enumValues>
                           <value><![CDATA[virtio]]></value>
                           <defaultValue><![CDATA[virtio]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.ram]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.ram' description='RAM in GB' category='interoute' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.smp]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.smp' description='SMP (number of virtual CPUs)' category='cloudsigma' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.cpu]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.cpu' description='CPU in GHz' category='cloudsigma' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.instance.type]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.instance.type' description='Cloud instance type' category='atos' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='7'>
                              <string>m1.small</string>
                              <string>c1.medium</string>
                              <string>m1.large</string>
                              <string>m1.xlarge</string>
                              <string>c1.xlarge</string>
                              <string>t1.micro</string>
                              <string>standard.xsmall</string>
                           </enumValues>
                           <value><![CDATA[m1.small]]></value>
                           <defaultValue><![CDATA[m1.small]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.network]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.network' description='Network types to start the image on' category='interoute' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='3'>
                              <string>External</string>
                              <string>Private</string>
                              <string>ExternalAndPrivate</string>
                           </enumValues>
                           <value><![CDATA[Private]]></value>
                           <defaultValue><![CDATA[Private]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[instanceid]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='instanceid' description='Cloud instance id' category='Output' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[webserver.hostname]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='webserver.hostname' description='Server hostname' category='Input' mandatory='false' type='String' readonly='false' isSet='false'/>
                     </entry>
                  </parameters>
                  <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
                     <groupMembers class='java.util.ArrayList'/>
                  </authz>
                  <comment></comment>
                  <targets class='org.hibernate.collection.PersistentBag'>
                     <target runInBackground='false' name='report'><![CDATA[#!/bin/sh -x
cp /tmp/data.txt $SLIPSTREAM_REPORT_DIR]]></target>
                     <target runInBackground='false' name='execute'><![CDATA[#!/bin/sh -xe
# Wait for the metadata to be resolved
web_server_ip=$(ss-get --timeout 360 webserver.hostname)
web_server_port=$(ss-get --timeout 360 webserver.port)
ss-get --timeout 360 webserver.ready

# Execute the test
ENDPOINT=http://${web_server_ip}:${web_server_port}/data.txt
wget -t 2 -O /tmp/data.txt ${ENDPOINT}
[ '$?' = '0' ] & ss-set statecustom 'OK: $(cat /tmp/data.txt)' || ss-abort 'Could not get the test file: ${ENDPOINT}'

sfdisk -s
]]></target>
                  </targets>
                  <packages class='org.hibernate.collection.PersistentBag'/>
                  <prerecipe><![CDATA[]]></prerecipe>
                  <recipe><![CDATA[]]></recipe>
                  <cloudImageIdentifiers class='org.hibernate.collection.PersistentBag'/>
                  <extraDisks class='org.hibernate.collection.PersistentBag'/>
               </image>
            </node>
         </entry>
         <entry>
            <string>apache1</string>
            <node deleted='false' name='apache1' multiplicity='1' cloudService='interoute' imageUri='module/Public/Tutorials/HelloWorld/apache' creation='2013-03-06 14:31:01.861 CET' network='Public'>
               <parameters class='org.hibernate.collection.PersistentMap'/>
               <parameterMappings class='org.hibernate.collection.PersistentMap'/>
               <image lastModified='2013-03-08 15:14:34.417 CET' category='Image' description='' deleted='false' resourceUri='module/Public/Tutorials/HelloWorld/apache/19' parentUri='module/Public/Tutorials/HelloWorld' name='Public/Tutorials/HelloWorld/apache' version='19' moduleReferenceUri='module/Public/BaseImages/Ubuntu/12.04' isBase='false' imageId='SS_ORCH_UBUNTU1204' creation='2013-03-06 14:31:01.732 CET' shortName='apache' loginUser='root' platform='ubuntu'>
                  <parameters class='org.hibernate.collection.PersistentMap'>
                     <entry>
                        <string><![CDATA[port]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='port' description='Port' category='Output' mandatory='false' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[8080]]></value>
                           <defaultValue><![CDATA[8080]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[extra.disk.volatile]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='extra.disk.volatile' description='Volatile extra disk in GB' category='Cloud' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.ram]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.ram' description='RAM in GB' category='cloudsigma' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[hostname]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='hostname' description='hostname/ip of the image' category='Output' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.login.password]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.login.password' description='SSH login password for the image' category='cloudsigma' mandatory='true' type='Password' readonly='false' isSet='true'>
                           <value><![CDATA[ubuntupass]]></value>
                           <defaultValue><![CDATA[ubuntupass]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.login.password]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.login.password' description='SSH login password for the image' category='interoute' mandatory='true' type='Password' readonly='false' isSet='true'>
                           <value><![CDATA[rootpass]]></value>
                           <defaultValue><![CDATA[rootpass]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.ram]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.ram' description='Requested RAM (in GB)' category='atos' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[ready]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='ready' description='Server ready to recieve connections' category='Output' mandatory='false' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[network]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='network' description='Network type' category='Cloud' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='2'>
                              <string>Public</string>
                              <string>Private</string>
                           </enumValues>
                           <value><![CDATA[Public]]></value>
                           <defaultValue><![CDATA[Public]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.cpu]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.cpu' description='Requested CPUs' category='atos' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.disks.bus.type]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.disks.bus.type' description='VM disks bus type' category='atos' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='2'>
                              <string>virtio</string>
                              <string>scsi</string>
                           </enumValues>
                           <value><![CDATA[virtio]]></value>
                           <defaultValue><![CDATA[virtio]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.cpu]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.cpu' description='Number of CPUs' category='interoute' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.ram]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.ram' description='RAM in GB' category='interoute' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.smp]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.smp' description='SMP (number of virtual CPUs)' category='cloudsigma' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[atos.instance.type]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='atos.instance.type' description='Cloud instance type' category='atos' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='7'>
                              <string>m1.small</string>
                              <string>c1.medium</string>
                              <string>m1.large</string>
                              <string>m1.xlarge</string>
                              <string>c1.xlarge</string>
                              <string>t1.micro</string>
                              <string>standard.xsmall</string>
                           </enumValues>
                           <value><![CDATA[m1.small]]></value>
                           <defaultValue><![CDATA[m1.small]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[cloudsigma.cpu]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='cloudsigma.cpu' description='CPU in GHz' category='cloudsigma' mandatory='true' type='String' readonly='false' isSet='true'>
                           <value><![CDATA[1]]></value>
                           <defaultValue><![CDATA[1]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[interoute.network]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='interoute.network' description='Network types to start the image on' category='interoute' mandatory='true' type='Enum' readonly='false' isSet='true'>
                           <enumValues length='3'>
                              <string>External</string>
                              <string>Private</string>
                              <string>ExternalAndPrivate</string>
                           </enumValues>
                           <value><![CDATA[ExternalAndPrivate]]></value>
                           <defaultValue><![CDATA[ExternalAndPrivate]]></defaultValue>
                        </parameter>
                     </entry>
                     <entry>
                        <string><![CDATA[instanceid]]></string>
                        <parameter class='com.sixsq.slipstream.persistence.ModuleParameter' name='instanceid' description='Cloud instance id' category='Output' mandatory='true' type='String' readonly='false' isSet='false'/>
                     </entry>
                  </parameters>
                  <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
                     <groupMembers class='java.util.ArrayList'/>
                  </authz>
                  <comment>Updated cloudsigma image password</comment>
                  <targets class='org.hibernate.collection.PersistentBag'>
                     <target runInBackground='false' name='execute'><![CDATA[#!/bin/sh -xe
apt-get update -y
apt-get install -y apache2

echo 'Hello from Apache deployed by SlipStream!' > /var/www/data.txt

service apache2 stop
port=$(ss-get port)
sed -i -e 's/^Listen.*$/Listen '$port'/' /etc/apache2/ports.conf
sed -i -e 's/^NameVirtualHost.*$/NameVirtualHost *:'$port'/' /etc/apache2/ports.conf
sed -i -e 's/^<VirtualHost.*$/<VirtualHost *:'$port'>/' /etc/apache2/sites-available/default
service apache2 start
ss-set ready true]]></target>
                     <target runInBackground='false' name='report'><![CDATA[#!/bin/sh -x
cp /var/log/apache2/access.log $SLIPSTREAM_REPORT_DIR
cp /var/log/apache2/error.log $SLIPSTREAM_REPORT_DIR]]></target>
                  </targets>
                  <packages class='org.hibernate.collection.PersistentBag'/>
                  <prerecipe><![CDATA[]]></prerecipe>
                  <recipe><![CDATA[]]></recipe>
                  <cloudImageIdentifiers class='org.hibernate.collection.PersistentBag'/>
                  <extraDisks class='org.hibernate.collection.PersistentBag'/>
               </image>
            </node>
         </entry>
      </nodes>
   </module>
   <user issuper='true' resourceUri='user/super' name='SuperDooper'></user>
</run>")))
