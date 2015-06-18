(ns slipstream.ui.models.run-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.run :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_run_ready_abort.xml"))

(expect
  [:runtime-parameters :summary]
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse keys)))

(expect
  {:last-state-change "2014-09-24 00:20:06.517 CEST"
   :end-time "2014-09-24 00:20:06.517 CEST"
   :status nil
   :mutable? false
   :deleted? false
   :large-run? false
   :counts {:total-orchestrators 1
            :total-nodes 2
            :total-instances 2}
   :state "Aborted"
   :creation "2014-09-24 00:12:43.287 CEST"
   :module-uri "module/examples/tutorials/service-testing/system/72"
   :module-owner "super"
   :start-time "2014-09-24 00:12:43.287 CEST"
   :uri "run/d32f6b31-cd9f-4b1a-aa1d-e8170e51a62d"
   :uuid "d32f6b31-cd9f-4b1a-aa1d-e8170e51a62d"
   :type :deployment-run
   :localized-type "Deployment Run"
   :original-type "orchestration"
   :run-owner "rob"
   :category "Deployment"
   :tags ""
   :abort-msg "Exception <class 'slipstream.exceptions.Exceptions.ExecutionException'> with detail: Failed to connect to 31.171.251.235: <class 'slipstream.utils.ssh.SshAuthFailed'>, Authentication failed."}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :summary)))

(expect
  [:global :orchestrator :node :node]
  (localization/with-lang :en
    (->> raw-metadata-str u/clojurify-raw-metadata-str model/parse :runtime-parameters (mapv :node-type))))

; The metadata mockup contains too many runtime parameters to test,
; so we check only the first and last items.

(expect
  {:node "Global"
   :node-type :global
   :node-instances [{:node-instance-index nil
                     :group "Global",
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
                                           :value "Exception <class 'slipstream.exceptions.Exceptions.ExecutionException'> with detail: Failed to connect to 31.171.251.235: <class 'slipstream.utils.ssh.SshAuthFailed'>, Authentication failed."
                                           :node "Global"}
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
                                           :value "Deployment"
                                           :node "Global"}
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
                                           :value ""
                                           :node "Global"}
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
                                           :value "CloudSigma-zrh:apache,CloudSigma-zrh:testclient"
                                           :node "Global"}
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
                                           :value "true"
                                           :node "Global"}
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
                                           :value "Aborted"
                                           :node "Global"}
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
                                           :value ""
                                           :node "Global"}
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
                                           :value ""
                                           :node "Global"}]}]}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :runtime-parameters first)))

(expect
  {:node-type :node
   :node "testclient"
   :node-instances [{:node-instance-index nil
                     :group "testclient"
                     :runtime-parameters [{:group "testclient"
                                           :deleted? false
                                           :help-hint "IDs of the machines in a mutable deployment."
                                           :creation "2014-09-24 00:12:43.313 CEST"
                                           :name "testclient:ids"
                                           :mapped-param-names ""
                                           :node "testclient"
                                           :type "String"
                                           :set? true
                                           :map-others? false
                                           :order 2147483647
                                           :mapped-value? false
                                           :value "1"}
                                          {:group "testclient"
                                           :deleted? false
                                           :help-hint "Multiplicity number"
                                           :creation "2014-09-24 00:12:43.313 CEST"
                                           :name "testclient:multiplicity"
                                           :mapped-param-names ""
                                           :node "testclient"
                                           :type "String"
                                           :set? true
                                           :map-others? false
                                           :order 2147483647
                                           :mapped-value? false
                                           :value "1"}]}
                      {:node-instance-index 1
                       :group "testclient.1"
                       :runtime-parameters [{:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Number of CPUs (i.e. virtual cores)"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:CloudSigma-zrh.cpu"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "1"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "SSH login password for the image"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:CloudSigma-zrh.login.password"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "Password"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "ubuntupass"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Amount of RAM, in GB"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:CloudSigma-zrh.ram"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "1"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "SMP (number of virtual CPUs)"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:CloudSigma-zrh.smp"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "1"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Machine abort flag, set when aborting"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:abort"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? false
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value ""}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Cloud Service where the node resides"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:cloudservice"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "CloudSigma-zrh"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "'true' when current state is completed"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:complete"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "false"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Volatile extra disk in GB"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:extra.disk.volatile"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? false
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value ""}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "hostname/ip of the image"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:hostname"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "31.171.251.235"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Node instance id"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:id"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "1"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Cloud image id"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:image.id"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "62ee9520-f971-4c11-973c-a54b63066a46"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Cloud instance id"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:instanceid"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "b40d866f-40e4-49c2-8083-7c91d8ff40a3"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "True if it's an orchestrator"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:is.orchestrator"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "false"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Network type"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:network"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "Enum"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value [{:selected? true}]}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Nodename"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:nodename"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "testclient"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Defined scalability state"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:scale.state"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "creating"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Custom state"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:statecustom"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? false
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value ""}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Optional service URL for virtual machine"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:url.service"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? false
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value ""}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "SSH URL to connect to virtual machine"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:url.ssh"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "ssh://root@31.171.251.235"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "State of the VM, according to the cloud layer"
                                             :creation "2014-09-24 00:12:43.306 CEST"
                                             :name "testclient.1:vmstate"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "Unknown"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Server hostname"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:webserver.hostname"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "31.171.245.178"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Port on which the web server listens"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:webserver.port"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? true
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value "8080"}
                                            {:group "testclient.1"
                                             :deleted? false
                                             :help-hint "Server ready to recieve connections"
                                             :creation "2014-09-24 00:12:43.312 CEST"
                                             :name "testclient.1:webserver.ready"
                                             :mapped-param-names ""
                                             :node "testclient"
                                             :type "String"
                                             :set? false
                                             :map-others? false
                                             :order 2147483647
                                             :mapped-value? false
                                             :value ""}]}]}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :runtime-parameters last)))
