(ns slipstream.ui.models.run-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.run :as model]))

(def raw-metadata-str
  "<run category=\"Deployment\" deleted=\"false\" resourceUri=\"run/d32f6b31-cd9f-4b1a-aa1d-e8170e51a62d\" uuid=\"d32f6b31-cd9f-4b1a-aa1d-e8170e51a62d\" type=\"Orchestration\" cloudServiceNames=\"CloudSigma-zrh\" state=\"Aborted\" moduleResourceUri=\"module/examples/tutorials/service-testing/system/72\" startTime=\"2014-09-24 00:12:43.287 CEST\" endTime=\"2014-09-24 00:20:06.517 CEST\" lastStateChangeTime=\"2014-09-24 00:20:06.517 CEST\" nodeNames=\"apache.1,testclient.1,orchestrator-CloudSigma-zrh\" user=\"super\" mutable=\"false\" creation=\"2014-09-24 00:12:43.287 CEST\" groups=\"CloudSigma-zrh:apache,CloudSigma-zrh:testclient\">
    <runtimeParameters class=\"org.hibernate.collection.internal.PersistentMap\">
        <entry>
            <string>
                <![CDATA[ testclient.1:instanceid ]]>
            </string>
            <runtimeParameter description=\"Cloud instance id\" deleted=\"false\" key=\"testclient.1:instanceid\" isSet=\"true\" name=\"instanceid\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ b40d866f-40e4-49c2-8083-7c91d8ff40a3 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:hostname ]]>
            </string>
            <runtimeParameter description=\"hostname/ip of the image\" deleted=\"false\" key=\"apache.1:hostname\" isSet=\"true\" name=\"hostname\" group=\"apache.1\" mapsOthers=\"true\" type=\"String\" mappedRuntimeParameterNames=\"testclient.1:webserver.hostname,\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ 31.171.245.178 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:abort ]]>
            </string>
            <runtimeParameter description=\"Machine abort flag, set when aborting\" deleted=\"false\" key=\"apache.1:abort\" isSet=\"false\" name=\"abort\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:id ]]>
            </string>
            <runtimeParameter description=\"Node instance id\" deleted=\"false\" key=\"testclient.1:id\" isSet=\"true\" name=\"id\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:cloudservice ]]>
            </string>
            <runtimeParameter description=\"Cloud Service where the node resides\" deleted=\"false\" key=\"apache.1:cloudservice\" isSet=\"true\" name=\"cloudservice\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ CloudSigma-zrh ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:nodename ]]>
            </string>
            <runtimeParameter description=\"Nodename\" deleted=\"false\" key=\"testclient.1:nodename\" isSet=\"true\" name=\"nodename\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ testclient ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:category ]]>
            </string>
            <runtimeParameter description=\"Module category\" deleted=\"false\" key=\"ss:category\" isSet=\"true\" name=\"category\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ Deployment ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:instanceid ]]>
            </string>
            <runtimeParameter description=\"Cloud instance id\" deleted=\"false\" key=\"apache.1:instanceid\" isSet=\"true\" name=\"instanceid\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ cdd0e24a-1fc6-4f17-9b45-6149edd2b84e ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:ready ]]>
            </string>
            <runtimeParameter description=\"Server ready to recieve connections\" deleted=\"false\" key=\"apache.1:ready\" isSet=\"false\" name=\"ready\" group=\"apache.1\" mapsOthers=\"true\" type=\"String\" mappedRuntimeParameterNames=\"testclient.1:webserver.ready,\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\"/>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:is.orchestrator ]]>
            </string>
            <runtimeParameter description=\"True if it's an orchestrator\" deleted=\"false\" key=\"testclient.1:is.orchestrator\" isSet=\"true\" name=\"is.orchestrator\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ false ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:url.ssh ]]>
            </string>
            <runtimeParameter description=\"SSH URL to connect to virtual machine\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:url.ssh\" isSet=\"true\" name=\"url.ssh\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ ssh://root@185.12.7.54 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:url.service ]]>
            </string>
            <runtimeParameter description=\"Optional service URL for virtual machine\" deleted=\"false\" key=\"testclient.1:url.service\" isSet=\"false\" name=\"url.service\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:recovery.mode ]]>
            </string>
            <runtimeParameter description=\"Run abort flag, set when aborting\" deleted=\"false\" key=\"ss:recovery.mode\" isSet=\"true\" name=\"recovery.mode\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ true ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:vmstate ]]>
            </string>
            <runtimeParameter description=\"State of the VM, according to the cloud layer\" deleted=\"false\" key=\"testclient.1:vmstate\" isSet=\"true\" name=\"vmstate\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ Unknown ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:extra.disk.volatile ]]>
            </string>
            <runtimeParameter description=\"Volatile extra disk in GB\" deleted=\"false\" key=\"apache.1:extra.disk.volatile\" isSet=\"false\" name=\"extra.disk.volatile\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\"/>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:abort ]]>
            </string>
            <runtimeParameter description=\"Run abort flag, set when aborting\" deleted=\"false\" key=\"ss:abort\" isSet=\"true\" name=\"abort\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[
                Exception <class 'slipstream.exceptions.Exceptions.ExecutionException'> with detail: Failed to connect to 31.171.251.235: <class 'slipstream.utils.ssh.SshAuthFailed'>, Authentication failed.
                ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:cloudservice ]]>
            </string>
            <runtimeParameter description=\"Cloud Service where the node resides\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:cloudservice\" isSet=\"true\" name=\"cloudservice\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ CloudSigma-zrh ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:scale.state ]]>
            </string>
            <runtimeParameter description=\"Defined scalability state\" deleted=\"false\" key=\"apache.1:scale.state\" isSet=\"true\" name=\"scale.state\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ creating ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:url.ssh ]]>
            </string>
            <runtimeParameter description=\"SSH URL to connect to virtual machine\" deleted=\"false\" key=\"testclient.1:url.ssh\" isSet=\"true\" name=\"url.ssh\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ ssh://root@31.171.251.235 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:tags ]]>
            </string>
            <runtimeParameter description=\"Comma separated tag values\" deleted=\"false\" key=\"ss:tags\" isSet=\"false\" name=\"tags\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:CloudSigma-zrh.ram ]]>
            </string>
            <runtimeParameter description=\"Amount of RAM, in GB\" deleted=\"false\" key=\"testclient.1:CloudSigma-zrh.ram\" isSet=\"true\" name=\"CloudSigma-zrh.ram\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:max.iaas.workers ]]>
            </string>
            <runtimeParameter description=\"Max number of concurrently provisioned VMs by orchestrator\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:max.iaas.workers\" isSet=\"true\" name=\"max.iaas.workers\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ 20 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:nodename ]]>
            </string>
            <runtimeParameter description=\"Nodename\" deleted=\"false\" key=\"apache.1:nodename\" isSet=\"true\" name=\"nodename\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ apache ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:CloudSigma-zrh.login.password ]]>
            </string>
            <runtimeParameter description=\"SSH login password for the image\" deleted=\"false\" key=\"apache.1:CloudSigma-zrh.login.password\" isSet=\"true\" name=\"CloudSigma-zrh.login.password\" group=\"apache.1\" mapsOthers=\"false\" type=\"Password\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ ubuntupass ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:groups ]]>
            </string>
            <runtimeParameter description=\"Comma separated node groups\" deleted=\"false\" key=\"ss:groups\" isSet=\"true\" name=\"groups\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ CloudSigma-zrh:apache,CloudSigma-zrh:testclient ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache:multiplicity ]]>
            </string>
            <runtimeParameter description=\"Multiplicity number\" deleted=\"false\" key=\"apache:multiplicity\" isSet=\"true\" name=\"multiplicity\" group=\"apache\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:url.ssh ]]>
            </string>
            <runtimeParameter description=\"SSH URL to connect to virtual machine\" deleted=\"false\" key=\"apache.1:url.ssh\" isSet=\"true\" name=\"url.ssh\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ ssh://root@31.171.245.178 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:is.orchestrator ]]>
            </string>
            <runtimeParameter description=\"True if it's an orchestrator\" deleted=\"false\" key=\"apache.1:is.orchestrator\" isSet=\"true\" name=\"is.orchestrator\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ false ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:webserver.hostname ]]>
            </string>
            <runtimeParameter description=\"Server hostname\" deleted=\"false\" key=\"testclient.1:webserver.hostname\" isSet=\"true\" name=\"webserver.hostname\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ 31.171.245.178 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:complete ]]>
            </string>
            <runtimeParameter description=\"'true' when current state is completed\" deleted=\"false\" key=\"testclient.1:complete\" isSet=\"true\" name=\"complete\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ false ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:cloudservice ]]>
            </string>
            <runtimeParameter description=\"Cloud Service where the node resides\" deleted=\"false\" key=\"testclient.1:cloudservice\" isSet=\"true\" name=\"cloudservice\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ CloudSigma-zrh ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:url.service ]]>
            </string>
            <runtimeParameter description=\"Optional service URL for virtual machine\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:url.service\" isSet=\"false\" name=\"url.service\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:url.service ]]>
            </string>
            <runtimeParameter description=\"Optional service URL for virtual machine\" deleted=\"false\" key=\"apache.1:url.service\" isSet=\"false\" name=\"url.service\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:id ]]>
            </string>
            <runtimeParameter description=\"Node instance id\" deleted=\"false\" key=\"apache.1:id\" isSet=\"true\" name=\"id\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:abort ]]>
            </string>
            <runtimeParameter description=\"Machine abort flag, set when aborting\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:abort\" isSet=\"true\" name=\"abort\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[
                Exception <class 'slipstream.exceptions.Exceptions.ExecutionException'> with detail: Failed to connect to 31.171.251.235: <class 'slipstream.utils.ssh.SshAuthFailed'>, Authentication failed.
                ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:url.service ]]>
            </string>
            <runtimeParameter description=\"Optional service URL for the deployment\" deleted=\"false\" key=\"ss:url.service\" isSet=\"false\" name=\"url.service\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:is.orchestrator ]]>
            </string>
            <runtimeParameter description=\"True if it's an orchestrator\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:is.orchestrator\" isSet=\"true\" name=\"is.orchestrator\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ true ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:instanceid ]]>
            </string>
            <runtimeParameter description=\"Cloud instance id\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:instanceid\" isSet=\"true\" name=\"instanceid\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ ffdd10d3-ca2b-413c-b651-8daeef8c3364 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache:ids ]]>
            </string>
            <runtimeParameter description=\"IDs of the machines in a mutable deployment.\" deleted=\"false\" key=\"apache:ids\" isSet=\"true\" name=\"ids\" group=\"apache\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:statecustom ]]>
            </string>
            <runtimeParameter description=\"Custom state\" deleted=\"false\" key=\"testclient.1:statecustom\" isSet=\"false\" name=\"statecustom\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:state ]]>
            </string>
            <runtimeParameter description=\"Global execution state\" deleted=\"false\" key=\"ss:state\" isSet=\"true\" name=\"state\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ Aborted ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:vmstate ]]>
            </string>
            <runtimeParameter description=\"State of the VM, according to the cloud layer\" deleted=\"false\" key=\"apache.1:vmstate\" isSet=\"true\" name=\"vmstate\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ Unknown ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:webserver.port ]]>
            </string>
            <runtimeParameter description=\"Port on which the web server listens\" deleted=\"false\" key=\"testclient.1:webserver.port\" isSet=\"true\" name=\"webserver.port\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ 8080 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:extra.disk.volatile ]]>
            </string>
            <runtimeParameter description=\"Volatile extra disk in GB\" deleted=\"false\" key=\"testclient.1:extra.disk.volatile\" isSet=\"false\" name=\"extra.disk.volatile\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\"/>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:network ]]>
            </string>
            <runtimeParameter description=\"Network type\" deleted=\"false\" key=\"apache.1:network\" isSet=\"true\" name=\"network\" group=\"apache.1\" mapsOthers=\"false\" type=\"Enum\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ Public ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:complete ]]>
            </string>
            <runtimeParameter description=\"'true' when current state is completed\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:complete\" isSet=\"true\" name=\"complete\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ false ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:image.id ]]>
            </string>
            <runtimeParameter description=\"Cloud image id\" deleted=\"false\" key=\"testclient.1:image.id\" isSet=\"true\" name=\"image.id\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ 62ee9520-f971-4c11-973c-a54b63066a46 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:complete ]]>
            </string>
            <runtimeParameter description=\"'true' when current state is completed\" deleted=\"false\" key=\"apache.1:complete\" isSet=\"true\" name=\"complete\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ false ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:image.id ]]>
            </string>
            <runtimeParameter description=\"Cloud image id\" deleted=\"false\" key=\"apache.1:image.id\" isSet=\"true\" name=\"image.id\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ 62ee9520-f971-4c11-973c-a54b63066a46 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:CloudSigma-zrh.cpu ]]>
            </string>
            <runtimeParameter description=\"Number of CPUs (i.e. virtual cores)\" deleted=\"false\" key=\"apache.1:CloudSigma-zrh.cpu\" isSet=\"true\" name=\"CloudSigma-zrh.cpu\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:CloudSigma-zrh.smp ]]>
            </string>
            <runtimeParameter description=\"SMP (number of virtual CPUs)\" deleted=\"false\" key=\"apache.1:CloudSigma-zrh.smp\" isSet=\"true\" name=\"CloudSigma-zrh.smp\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:CloudSigma-zrh.ram ]]>
            </string>
            <runtimeParameter description=\"Amount of RAM, in GB\" deleted=\"false\" key=\"apache.1:CloudSigma-zrh.ram\" isSet=\"true\" name=\"CloudSigma-zrh.ram\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:abort ]]>
            </string>
            <runtimeParameter description=\"Machine abort flag, set when aborting\" deleted=\"false\" key=\"testclient.1:abort\" isSet=\"false\" name=\"abort\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:webserver.ready ]]>
            </string>
            <runtimeParameter description=\"Server ready to recieve connections\" deleted=\"false\" key=\"testclient.1:webserver.ready\" isSet=\"false\" name=\"webserver.ready\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient:multiplicity ]]>
            </string>
            <runtimeParameter description=\"Multiplicity number\" deleted=\"false\" key=\"testclient:multiplicity\" isSet=\"true\" name=\"multiplicity\" group=\"testclient\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:CloudSigma-zrh.login.password ]]>
            </string>
            <runtimeParameter description=\"SSH login password for the image\" deleted=\"false\" key=\"testclient.1:CloudSigma-zrh.login.password\" isSet=\"true\" name=\"CloudSigma-zrh.login.password\" group=\"testclient.1\" mapsOthers=\"false\" type=\"Password\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ ubuntupass ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:scale.state ]]>
            </string>
            <runtimeParameter description=\"Defined scalability state\" deleted=\"false\" key=\"testclient.1:scale.state\" isSet=\"true\" name=\"scale.state\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ creating ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient:ids ]]>
            </string>
            <runtimeParameter description=\"IDs of the machines in a mutable deployment.\" deleted=\"false\" key=\"testclient:ids\" isSet=\"true\" name=\"ids\" group=\"testclient\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:vmstate ]]>
            </string>
            <runtimeParameter description=\"State of the VM, according to the cloud layer\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:vmstate\" isSet=\"true\" name=\"vmstate\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ Unknown ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:statecustom ]]>
            </string>
            <runtimeParameter description=\"Custom state\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:statecustom\" isSet=\"false\" name=\"statecustom\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:network ]]>
            </string>
            <runtimeParameter description=\"Network type\" deleted=\"false\" key=\"testclient.1:network\" isSet=\"true\" name=\"network\" group=\"testclient.1\" mapsOthers=\"false\" type=\"Enum\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ Public ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:CloudSigma-zrh.cpu ]]>
            </string>
            <runtimeParameter description=\"Number of CPUs (i.e. virtual cores)\" deleted=\"false\" key=\"testclient.1:CloudSigma-zrh.cpu\" isSet=\"true\" name=\"CloudSigma-zrh.cpu\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:statecustom ]]>
            </string>
            <runtimeParameter description=\"Custom state\" deleted=\"false\" key=\"apache.1:statecustom\" isSet=\"false\" name=\"statecustom\" group=\"apache.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ apache.1:port ]]>
            </string>
            <runtimeParameter description=\"Port\" deleted=\"false\" key=\"apache.1:port\" isSet=\"true\" name=\"port\" group=\"apache.1\" mapsOthers=\"true\" type=\"String\" mappedRuntimeParameterNames=\"testclient.1:webserver.port,\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.306 CEST\">
                <![CDATA[ 8080 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:CloudSigma-zrh.smp ]]>
            </string>
            <runtimeParameter description=\"SMP (number of virtual CPUs)\" deleted=\"false\" key=\"testclient.1:CloudSigma-zrh.smp\" isSet=\"true\" name=\"CloudSigma-zrh.smp\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ 1 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ orchestrator-CloudSigma-zrh:hostname ]]>
            </string>
            <runtimeParameter description=\"hostname/ip of the image\" deleted=\"false\" key=\"orchestrator-CloudSigma-zrh:hostname\" isSet=\"true\" name=\"hostname\" group=\"orchestrator-CloudSigma-zrh\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.313 CEST\">
                <![CDATA[ 185.12.7.54 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ testclient.1:hostname ]]>
            </string>
            <runtimeParameter description=\"hostname/ip of the image\" deleted=\"false\" key=\"testclient.1:hostname\" isSet=\"true\" name=\"hostname\" group=\"testclient.1\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.312 CEST\">
                <![CDATA[ 31.171.251.235 ]]>
            </runtimeParameter>
        </entry>
        <entry>
            <string>
                <![CDATA[ ss:complete ]]>
            </string>
            <runtimeParameter description=\"Global complete flag, set when run completed\" deleted=\"false\" key=\"ss:complete\" isSet=\"false\" name=\"complete\" group=\"Global\" mapsOthers=\"false\" type=\"String\" mappedRuntimeParameterNames=\"\" isMappedValue=\"false\" creation=\"2014-09-24 00:12:43.299 CEST\">
                <![CDATA[ ]]>
            </runtimeParameter>
        </entry>
    </runtimeParameters>
    <module class=\"com.sixsq.slipstream.persistence.DeploymentModule\" lastModified=\"2014-07-04 02:21:08.16 CEST\" category=\"Deployment\" description=\"Deployment binding the apache server and the test client node(s).\" deleted=\"false\" resourceUri=\"module/examples/tutorials/service-testing/system/72\" parentUri=\"module/examples/tutorials/service-testing\" name=\"examples/tutorials/service-testing/system\" version=\"72\" isLatestVersion=\"true\" logoLink=\"\" creation=\"2014-05-21 10:49:08.291 CEST\" shortName=\"system\">
        <authz owner=\"super\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
        </authz>
        <commit author=\"super\">
            <comment/>
        </commit>
        <nodes class=\"org.hibernate.collection.internal.PersistentMap\">
            <entry>
                <string>apache</string>
                <node deleted=\"false\" name=\"apache\" multiplicity=\"1\" cloudService=\"hepiaCloud\" imageUri=\"module/examples/tutorials/service-testing/apache\" creation=\"2014-07-04 02:21:07.980 CEST\" network=\"Public\">
                    <parameterMappings class=\"org.hibernate.collection.internal.PersistentMap\"/>
                    <parameters class=\"org.hibernate.collection.internal.PersistentMap\"/>
                    <image lastModified=\"2014-09-29 14:29:34.864 CEST\" category=\"Image\" description=\"Apache web server appliance with custom landing page.\" deleted=\"false\" resourceUri=\"module/examples/tutorials/service-testing/apache/113\" parentUri=\"module/examples/tutorials/service-testing\" name=\"examples/tutorials/service-testing/apache\" version=\"113\" isLatestVersion=\"true\" moduleReferenceUri=\"module/examples/images/ubuntu-12.04\" logoLink=\"http://pegasosdi.uab.es/geoportal/images/Apache_HTTP_server_transparente.png\" isBase=\"false\" creation=\"2014-05-21 10:41:46.451 CEST\" shortName=\"apache\" platform=\"ubuntu\" loginUser=\"root\">
                        <authz owner=\"super\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
                            <groupMembers class=\"java.util.ArrayList\"/>
                        </authz>
                        <commit author=\"super\">
                            <comment>Updated password for CloudSigma-zrh.</comment>
                        </commit>
                        <targets class=\"org.hibernate.collection.internal.PersistentSet\">
                            <target name=\"onvmadd\">
                                <![CDATA[ ]]>
                            </target>
                            <target name=\"onvmremove\">
                                <![CDATA[ ]]>
                            </target>
                            <target name=\"execute\">
                                <![CDATA[
                                #!/bin/sh -xe
                                apt-get update -y
                                apt-get install -y apache2

                                echo 'Hello from Apache deployed by SlipStream!' > /var/www/data.txt

                                service apache2 stop
                                port=$(ss-get port)
                                sed -i -e 's/^Listen.*$/Listen '$port'/' /etc/apache2/ports.conf
                                sed -i -e 's/^NameVirtualHost.*$/NameVirtualHost *:'$port'/' /etc/apache2/ports.conf
                                sed -i -e 's/^<VirtualHost.*$/<VirtualHost *:'$port'>/' /etc/apache2/sites-available/default
                                service apache2 start
                                ss-set ready true
                                url=\"http://$(ss-get hostname):$port\"
                                ss-set url.service $url
                                ss-set ss:url.service $url
                                ]]>
                            </target>
                            <target name=\"report\">
                                <![CDATA[
                                #!/bin/sh -x
                                cp /var/log/apache2/access.log $SLIPSTREAM_REPORT_DIR
                                cp /var/log/apache2/error.log $SLIPSTREAM_REPORT_DIR
                                ]]>
                            </target>
                        </targets>
                        <packages class=\"org.hibernate.collection.internal.PersistentSet\"/>
                        <prerecipe>
                            <![CDATA[ ]]>
                        </prerecipe>
                        <recipe>
                            <![CDATA[ ]]>
                        </recipe>
                        <cloudImageIdentifiers class=\"org.hibernate.collection.internal.PersistentSet\"/>
                        <parameters class=\"org.hibernate.collection.internal.PersistentMap\">
                            <entry>
                                <string>instanceid</string>
                                <parameter name=\"instanceid\" description=\"Cloud instance id\" category=\"Output\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>T-Systems-embl.cpu</string>
                                <parameter name=\"T-Systems-embl.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"T-Systems-embl\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.login.password</string>
                                <parameter name=\"CloudSigma-zrh.login.password\" description=\"SSH login password for the image\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"Password\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 76C13D86-A326-4696-AED0-58ADAAD68AE5 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 76C13D86-A326-4696-AED0-58ADAAD68AE5 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>StratusLab.instance.type</string>
                                <parameter name=\"StratusLab.instance.type\" description=\"Cloud instance type\" category=\"StratusLab\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"7\">
                                        <string>m1.small</string>
                                        <string>c1.medium</string>
                                        <string>m1.large</string>
                                        <string>m1.xlarge</string>
                                        <string>c1.xlarge</string>
                                        <string>t1.micro</string>
                                        <string>standard.xsmall</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ m1.small ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ m1.small ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.disks.bus.type</string>
                                <parameter name=\"nuvlabox.disks.bus.type\" description=\"VM disks bus type\" category=\"nuvlabox\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"2\">
                                        <string>virtio</string>
                                        <string>scsi</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ virtio ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ virtio ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>port</string>
                                <parameter name=\"port\" description=\"Port\" category=\"Output\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 8080 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 8080 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>extra.disk.volatile</string>
                                <parameter name=\"extra.disk.volatile\" description=\"Volatile extra disk in GB\" category=\"Cloud\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>StratusLab.disks.bus.type</string>
                                <parameter name=\"StratusLab.disks.bus.type\" description=\"VM disks bus type\" category=\"StratusLab\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"2\">
                                        <string>virtio</string>
                                        <string>scsi</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ virtio ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ virtio ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>hepiaCloud.instance.type</string>
                                <parameter name=\"hepiaCloud.instance.type\" description=\"Instance type (flavor)\" category=\"hepiaCloud\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ m1.tiny ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ m1.tiny ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>InterouteV2.networks</string>
                                <parameter name=\"InterouteV2.networks\" description=\"List of networks (comma separated)\" category=\"InterouteV2\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ HNEB0_IPACP ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ HNEB0_IPACP ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>StratusLab.cpu</string>
                                <parameter name=\"StratusLab.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"StratusLab\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.ram</string>
                                <parameter name=\"CloudSigma-zrh.ram\" description=\"Amount of RAM, in GB\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.cpu</string>
                                <parameter name=\"CloudSigma-zrh.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>Exoscale.instance.type</string>
                                <parameter name=\"Exoscale.instance.type\" description=\"Instance type (flavor)\" category=\"Exoscale\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ Micro ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ Micro ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>T-Systems-embl.catalog</string>
                                <parameter name=\"T-Systems-embl.catalog\" description=\"Catalog where to put the image when you build it\" category=\"T-Systems-embl\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ SS ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ SS ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.cpu</string>
                                <parameter name=\"nuvlabox.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"nuvlabox\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>network</string>
                                <parameter name=\"network\" description=\"Network type\" category=\"Cloud\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"2\">
                                        <string>Public</string>
                                        <string>Private</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ Public ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ Public ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.smp</string>
                                <parameter name=\"CloudSigma-zrh.smp\" description=\"SMP (number of virtual CPUs)\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>T-Systems-embl.ram</string>
                                <parameter name=\"T-Systems-embl.ram\" description=\"Amount of RAM, in GB\" category=\"T-Systems-embl\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>InterouteV2.instance.type</string>
                                <parameter name=\"InterouteV2.instance.type\" description=\"Instance type (flavor)\" category=\"InterouteV2\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 512-1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 512-1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.instance.type</string>
                                <parameter name=\"nuvlabox.instance.type\" description=\"Cloud instance type\" category=\"nuvlabox\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"7\">
                                        <string>m1.small</string>
                                        <string>c1.medium</string>
                                        <string>m1.large</string>
                                        <string>m1.xlarge</string>
                                        <string>c1.xlarge</string>
                                        <string>t1.micro</string>
                                        <string>standard.xsmall</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ m1.small ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ m1.small ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>Exoscale.security.groups</string>
                                <parameter name=\"Exoscale.security.groups\" description=\"Security Groups (comma separated list)\" category=\"Exoscale\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ default ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ default ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>InterouteV2.security.groups</string>
                                <parameter name=\"InterouteV2.security.groups\" description=\"Security Groups (comma separated list)\" category=\"InterouteV2\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>hostname</string>
                                <parameter name=\"hostname\" description=\"hostname/ip of the image\" category=\"Output\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>hepiaCloud.security.groups</string>
                                <parameter name=\"hepiaCloud.security.groups\" description=\"Security Groups (comma separated list)\" category=\"hepiaCloud\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ default ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ default ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>StratusLab.ram</string>
                                <parameter name=\"StratusLab.ram\" description=\"Amount of RAM, in GB\" category=\"StratusLab\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>AmazonEC2-eu.instance.type</string>
                                <parameter name=\"AmazonEC2-eu.instance.type\" description=\"Cloud instance type\" category=\"AmazonEC2-eu\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
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
                                    <value>
                                        <![CDATA[ t1.micro ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ t1.micro ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.ram</string>
                                <parameter name=\"nuvlabox.ram\" description=\"Amount of RAM, in GB\" category=\"nuvlabox\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>ready</string>
                                <parameter name=\"ready\" description=\"Server ready to recieve connections\" category=\"Output\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>AmazonEC2-eu.security.groups</string>
                                <parameter name=\"AmazonEC2-eu.security.groups\" description=\"Security groups (comma separated list)\" category=\"AmazonEC2-eu\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ default ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ default ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.is.firewall</string>
                                <parameter name=\"nuvlabox.is.firewall\" description=\"If this flag is set, the instance will be started on the internal network and on the natted network so it can act as a firewall for instances on the internal network.\" category=\"nuvlabox\" mandatory=\"true\" type=\"Boolean\" readonly=\"false\" order_=\"10\" isSet=\"true\" order=\"10\">
                                    <value>
                                        <![CDATA[ false ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ false ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                        </parameters>
                    </image>
                </node>
            </entry>
            <entry>
                <string>testclient</string>
                <node deleted=\"false\" name=\"testclient\" multiplicity=\"1\" cloudService=\"hepiaCloud\" imageUri=\"module/examples/tutorials/service-testing/client\" creation=\"2014-07-04 02:21:07.980 CEST\" network=\"Public\">
                    <parameterMappings class=\"org.hibernate.collection.internal.PersistentMap\">
                        <entry>
                            <string>webserver.ready</string>
                            <parameter name=\"webserver.ready\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isMappedValue=\"true\" order=\"0\">
                                <value>
                                    <![CDATA[ apache:ready ]]>
                                </value>
                            </parameter>
                        </entry>
                        <entry>
                            <string>webserver.port</string>
                            <parameter name=\"webserver.port\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isMappedValue=\"true\" order=\"0\">
                                <value>
                                    <![CDATA[ apache:port ]]>
                                </value>
                            </parameter>
                        </entry>
                        <entry>
                            <string>webserver.hostname</string>
                            <parameter name=\"webserver.hostname\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isMappedValue=\"true\" order=\"0\">
                                <value>
                                    <![CDATA[ apache:hostname ]]>
                                </value>
                            </parameter>
                        </entry>
                    </parameterMappings>
                    <parameters class=\"org.hibernate.collection.internal.PersistentMap\">
                        <entry>
                            <string>webserver.ready</string>
                            <parameter name=\"webserver.ready\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isMappedValue=\"true\" order=\"0\">
                                <value>
                                    <![CDATA[ apache:ready ]]>
                                </value>
                            </parameter>
                        </entry>
                        <entry>
                            <string>webserver.port</string>
                            <parameter name=\"webserver.port\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isMappedValue=\"true\" order=\"0\">
                                <value>
                                    <![CDATA[ apache:port ]]>
                                </value>
                            </parameter>
                        </entry>
                        <entry>
                            <string>webserver.hostname</string>
                            <parameter name=\"webserver.hostname\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isMappedValue=\"true\" order=\"0\">
                                <value>
                                    <![CDATA[ apache:hostname ]]>
                                </value>
                            </parameter>
                        </entry>
                    </parameters>
                    <image lastModified=\"2014-09-29 14:30:22.768 CEST\" category=\"Image\" description=\"Web client tests server connectivity and verifies content.\" deleted=\"false\" resourceUri=\"module/examples/tutorials/service-testing/client/114\" parentUri=\"module/examples/tutorials/service-testing\" name=\"examples/tutorials/service-testing/client\" version=\"114\" isLatestVersion=\"true\" moduleReferenceUri=\"module/examples/images/ubuntu-12.04\" logoLink=\"\" isBase=\"false\" creation=\"2014-05-21 10:44:21.971 CEST\" shortName=\"client\" platform=\"ubuntu\" loginUser=\"root\">
                        <authz owner=\"super\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
                            <groupMembers class=\"java.util.ArrayList\"/>
                        </authz>
                        <commit author=\"super\">
                            <comment>Updated password for CloudSigma-zrh.</comment>
                        </commit>
                        <targets class=\"org.hibernate.collection.internal.PersistentSet\">
                            <target name=\"onvmremove\">
                                <![CDATA[ ]]>
                            </target>
                            <target name=\"onvmadd\">
                                <![CDATA[ ]]>
                            </target>
                            <target name=\"execute\">
                                <![CDATA[
                                #!/bin/sh -xe
                                # Wait for the metadata to be resolved
                                web_server_ip=$(ss-get --timeout 360 webserver.hostname)
                                web_server_port=$(ss-get --timeout 360 webserver.port)
                                ss-get --timeout 360 webserver.ready

                                # Execute the test
                                ENDPOINT=http://${web_server_ip}:${web_server_port}/data.txt
                                wget -t 2 -O /tmp/data.txt ${ENDPOINT}
                                [ \"$?\" = \"0\" ] & ss-set statecustom \"OK: $(cat /tmp/data.txt)\" || ss-abort \"Could not get the test file: ${ENDPOINT}\"
                                ]]>
                            </target>
                            <target name=\"report\">
                                <![CDATA[
                                #!/bin/sh -x
                                cp /tmp/data.txt $SLIPSTREAM_REPORT_DIR
                                ]]>
                            </target>
                        </targets>
                        <packages class=\"org.hibernate.collection.internal.PersistentSet\"/>
                        <prerecipe>
                            <![CDATA[ ]]>
                        </prerecipe>
                        <recipe>
                            <![CDATA[ ]]>
                        </recipe>
                        <cloudImageIdentifiers class=\"org.hibernate.collection.internal.PersistentSet\"/>
                        <parameters class=\"org.hibernate.collection.internal.PersistentMap\">
                            <entry>
                                <string>instanceid</string>
                                <parameter name=\"instanceid\" description=\"Cloud instance id\" category=\"Output\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>T-Systems-embl.cpu</string>
                                <parameter name=\"T-Systems-embl.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"T-Systems-embl\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.login.password</string>
                                <parameter name=\"CloudSigma-zrh.login.password\" description=\"SSH login password for the image\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"Password\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 76C13D86-A326-4696-AED0-58ADAAD68AE5 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 76C13D86-A326-4696-AED0-58ADAAD68AE5 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>StratusLab.instance.type</string>
                                <parameter name=\"StratusLab.instance.type\" description=\"Cloud instance type\" category=\"StratusLab\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"7\">
                                        <string>m1.small</string>
                                        <string>c1.medium</string>
                                        <string>m1.large</string>
                                        <string>m1.xlarge</string>
                                        <string>c1.xlarge</string>
                                        <string>t1.micro</string>
                                        <string>standard.xsmall</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ m1.small ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ m1.small ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.disks.bus.type</string>
                                <parameter name=\"nuvlabox.disks.bus.type\" description=\"VM disks bus type\" category=\"nuvlabox\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"2\">
                                        <string>virtio</string>
                                        <string>scsi</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ virtio ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ virtio ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>extra.disk.volatile</string>
                                <parameter name=\"extra.disk.volatile\" description=\"Volatile extra disk in GB\" category=\"Cloud\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>StratusLab.disks.bus.type</string>
                                <parameter name=\"StratusLab.disks.bus.type\" description=\"VM disks bus type\" category=\"StratusLab\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"2\">
                                        <string>virtio</string>
                                        <string>scsi</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ virtio ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ virtio ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>hepiaCloud.instance.type</string>
                                <parameter name=\"hepiaCloud.instance.type\" description=\"Instance type (flavor)\" category=\"hepiaCloud\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ m1.tiny ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ m1.tiny ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>InterouteV2.networks</string>
                                <parameter name=\"InterouteV2.networks\" description=\"List of networks (comma separated)\" category=\"InterouteV2\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ HnxJgPrvWithGwServiceParis ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ HnxJgPrvWithGwServiceParis ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>StratusLab.cpu</string>
                                <parameter name=\"StratusLab.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"StratusLab\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.ram</string>
                                <parameter name=\"CloudSigma-zrh.ram\" description=\"Amount of RAM, in GB\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.cpu</string>
                                <parameter name=\"CloudSigma-zrh.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>Exoscale.instance.type</string>
                                <parameter name=\"Exoscale.instance.type\" description=\"Instance type (flavor)\" category=\"Exoscale\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ Micro ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ Micro ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>T-Systems-embl.catalog</string>
                                <parameter name=\"T-Systems-embl.catalog\" description=\"Catalog where to put the image when you build it\" category=\"T-Systems-embl\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ SS ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ SS ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>webserver.ready</string>
                                <parameter name=\"webserver.ready\" description=\"Server ready to recieve connections\" category=\"Input\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>nuvlabox.cpu</string>
                                <parameter name=\"nuvlabox.cpu\" description=\"Number of CPUs (i.e. virtual cores)\" category=\"nuvlabox\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>network</string>
                                <parameter name=\"network\" description=\"Network type\" category=\"Cloud\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"2\">
                                        <string>Public</string>
                                        <string>Private</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ Public ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ Public ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>CloudSigma-zrh.smp</string>
                                <parameter name=\"CloudSigma-zrh.smp\" description=\"SMP (number of virtual CPUs)\" category=\"CloudSigma-zrh\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>webserver.port</string>
                                <parameter name=\"webserver.port\" description=\"Port on which the web server listens\" category=\"Input\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>T-Systems-embl.ram</string>
                                <parameter name=\"T-Systems-embl.ram\" description=\"Amount of RAM, in GB\" category=\"T-Systems-embl\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>InterouteV2.instance.type</string>
                                <parameter name=\"InterouteV2.instance.type\" description=\"Instance type (flavor)\" category=\"InterouteV2\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 512-1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 512-1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.instance.type</string>
                                <parameter name=\"nuvlabox.instance.type\" description=\"Cloud instance type\" category=\"nuvlabox\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <enumValues length=\"7\">
                                        <string>m1.small</string>
                                        <string>c1.medium</string>
                                        <string>m1.large</string>
                                        <string>m1.xlarge</string>
                                        <string>c1.xlarge</string>
                                        <string>t1.micro</string>
                                        <string>standard.xsmall</string>
                                    </enumValues>
                                    <value>
                                        <![CDATA[ m1.small ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ m1.small ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>Exoscale.security.groups</string>
                                <parameter name=\"Exoscale.security.groups\" description=\"Security Groups (comma separated list)\" category=\"Exoscale\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ default ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ default ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>InterouteV2.security.groups</string>
                                <parameter name=\"InterouteV2.security.groups\" description=\"Security Groups (comma separated list)\" category=\"InterouteV2\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>hostname</string>
                                <parameter name=\"hostname\" description=\"hostname/ip of the image\" category=\"Output\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>hepiaCloud.security.groups</string>
                                <parameter name=\"hepiaCloud.security.groups\" description=\"Security Groups (comma separated list)\" category=\"hepiaCloud\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ default ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ default ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>StratusLab.ram</string>
                                <parameter name=\"StratusLab.ram\" description=\"Amount of RAM, in GB\" category=\"StratusLab\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                            <entry>
                                <string>AmazonEC2-eu.instance.type</string>
                                <parameter name=\"AmazonEC2-eu.instance.type\" description=\"Cloud instance type\" category=\"AmazonEC2-eu\" mandatory=\"true\" type=\"Enum\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
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
                                    <value>
                                        <![CDATA[ t1.micro ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ t1.micro ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.ram</string>
                                <parameter name=\"nuvlabox.ram\" description=\"Amount of RAM, in GB\" category=\"nuvlabox\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ 1 ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ 1 ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>AmazonEC2-eu.security.groups</string>
                                <parameter name=\"AmazonEC2-eu.security.groups\" description=\"Security groups (comma separated list)\" category=\"AmazonEC2-eu\" mandatory=\"true\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"true\" order=\"0\">
                                    <value>
                                        <![CDATA[ default ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ default ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>nuvlabox.is.firewall</string>
                                <parameter name=\"nuvlabox.is.firewall\" description=\"If this flag is set, the instance will be started on the internal network and on the natted network so it can act as a firewall for instances on the internal network.\" category=\"nuvlabox\" mandatory=\"true\" type=\"Boolean\" readonly=\"false\" order_=\"10\" isSet=\"true\" order=\"10\">
                                    <value>
                                        <![CDATA[ false ]]>
                                    </value>
                                    <defaultValue>
                                        <![CDATA[ false ]]>
                                    </defaultValue>
                                </parameter>
                            </entry>
                            <entry>
                                <string>webserver.hostname</string>
                                <parameter name=\"webserver.hostname\" description=\"Server hostname\" category=\"Input\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" isSet=\"false\" order=\"0\"/>
                            </entry>
                        </parameters>
                    </image>
                </node>
            </entry>
        </nodes>
        <parameters class=\"org.hibernate.collection.internal.PersistentMap\"/>
    </module>
    <parameters class=\"org.hibernate.collection.internal.PersistentMap\">
        <entry>
            <string>testclient:node.increment</string>
            <parameter name=\"testclient:node.increment\" description=\"Current increment value for node instances ids\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ 2 ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>General.On Error Run Forever</string>
            <parameter name=\"General.On Error Run Forever\" description=\"If an error occurs, keep the execution running for investigation.\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ false ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>garbage_collected</string>
            <parameter name=\"garbage_collected\" description=\"true if the Run was already garbage collected\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ false ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>apache:cloudservice</string>
            <parameter name=\"apache:cloudservice\" description=\"Cloud Service where the node resides\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ CloudSigma-zrh ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>apache:multiplicity</string>
            <parameter name=\"apache:multiplicity\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ 1 ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>apache:node.increment</string>
            <parameter name=\"apache:node.increment\" description=\"Current increment value for node instances ids\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ 2 ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>testclient:cloudservice</string>
            <parameter name=\"testclient:cloudservice\" description=\"Cloud Service where the node resides\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ CloudSigma-zrh ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>General.On Success Run Forever</string>
            <parameter name=\"General.On Success Run Forever\" description=\"If no errors occur, keep the execution running. Useful for deployment or long tests.\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ false ]]>
                </value>
            </parameter>
        </entry>
        <entry>
            <string>testclient:multiplicity</string>
            <parameter name=\"testclient:multiplicity\" description=\"\" category=\"General\" mandatory=\"false\" type=\"String\" readonly=\"false\" order_=\"0\" order=\"0\">
                <value>
                    <![CDATA[ 1 ]]>
                </value>
            </parameter>
        </entry>
    </parameters>
    <cloudServiceNamesList length=\"1\">
        <string>CloudSigma-zrh</string>
    </cloudServiceNamesList>
   <user issuper='true' resourceUri='user/super' name='SuperDooper'></user>
</run>")

(expect
  [:runtime-parameters :summary]
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse keys)))

(expect
  {:end-time "2014-09-24 00:20:06.517 CEST"
   :status nil
   :mutable? false
   :deleted? false
   :state "Aborted"
   :creation "2014-09-24 00:12:43.287 CEST"
   :module-uri "module/examples/tutorials/service-testing/system/72"
   :owner "super"
   :start-time "2014-09-24 00:12:43.287 CEST"
   :uri "run/d32f6b31-cd9f-4b1a-aa1d-e8170e51a62d"
   :uuid "d32f6b31-cd9f-4b1a-aa1d-e8170e51a62d"
   :type "Deployment Run"
   :original-type "orchestration"
   :user "super"
   :category "Deployment"
   :tags ""}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :summary)))


; The metadata mockup contains too many runtime parameters to test,
; so we check only the first category.

(expect
  {:group "Global"
   :runtime-parameters [{:group "Global"
                         :deleted? false
                         :help-hint "Run abort flag, set when aborting"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:abort"
                         :mapped-param-names ""
                         :type "String"
                         :set? true
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value "Exception <class 'slipstream.exceptions.Exceptions.ExecutionException'> with detail: Failed to connect to 31.171.251.235: <class 'slipstream.utils.ssh.SshAuthFailed'>, Authentication failed."}
                        {:group "Global"
                         :deleted? false
                         :help-hint "Module category"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:category"
                         :mapped-param-names ""
                         :type "String"
                         :set? true
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value "Deployment"}
                        {:group "Global"
                         :deleted? false
                         :help-hint "Global complete flag, set when run completed"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:complete"
                         :mapped-param-names ""
                         :type "String"
                         :set? false
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value ""}
                        {:group "Global"
                         :deleted? false
                         :help-hint "Comma separated node groups"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:groups"
                         :mapped-param-names ""
                         :type "String"
                         :set? true
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value "CloudSigma-zrh:apache,CloudSigma-zrh:testclient"}
                        {:group "Global"
                         :deleted? false
                         :help-hint "Run abort flag, set when aborting"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:recovery.mode"
                         :mapped-param-names ""
                         :type "String"
                         :set? true
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value "true"}
                        {:group "Global"
                         :deleted? false
                         :help-hint "Global execution state"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:state"
                         :mapped-param-names ""
                         :type "String"
                         :set? true
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value "Aborted"}
                        {:group "Global"
                         :deleted? false
                         :help-hint "Comma separated tag values"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:tags"
                         :mapped-param-names ""
                         :type "String"
                         :set? false
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value ""}
                        {:group "Global"
                         :deleted? false
                         :help-hint "Optional service URL for the deployment"
                         :creation "2014-09-24 00:12:43.299 CEST"
                         :name "ss:url.service"
                         :mapped-param-names ""
                         :type "String"
                         :set? false
                         :map-others? false
                         :order 2147483647
                         :mapped-value? false
                         :value ""}]}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :runtime-parameters first)))

