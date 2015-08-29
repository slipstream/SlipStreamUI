(ns slipstream.ui.models.module.image-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.module :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_image.xml"))

(def parsed-metadata
  {:targets [{:target-type  :script
              :target-name  "prerecipe"
              :context      #{:image-creation}
              :script       nil}
             {:target-type  :packages
              :target-name  "packages"
              :context      #{:image-creation}
              :packages   [{:repository "repo_1"
                            :name       "package_1"
                            :key        "key_1"}
                           {:repository "repo_2"
                            :name       "package_2"
                            :key        "key_2"}]}
             {:target-type  :script
              :target-name  "recipe"
              :context      #{:image-creation}
              :script       nil}
             {:target-type  :script
              :target-name  "execute"
              :context      #{:deployment :ss-client-access}
              :script       "deployement recipe"}
             {:target-type  :script
              :target-name  "report"
              :context      #{:deployment :ss-client-access}
              :script       nil}
             {:target-type  :script
              :context      #{:scaling :ss-client-access}
              :target-name  "onvmadd"
              :script       nil}
             {:target-type  :script
              :context      #{:scaling :ss-client-access}
              :target-name  "onvmremove"
              :script       nil}]
   :deployment-parameters   [{:help-hint nil
                              :mandatory? false
                              :read-only? false
                              :order 0
                              :value "default_value_for_input_param_1"
                              :category "Input"
                              :description "The input param 1"
                              :type "String"
                              :name "input_param_1"}
                             {:help-hint nil
                              :mandatory? false
                              :read-only? false
                              :order 0
                              :value "default_value_for_input_param_2"
                              :category "Input"
                              :description "The input param 2"
                              :type "String"
                              :name "input_param_2"}
                             {:help-hint nil
                              :mandatory? false
                              :read-only? false
                              :order 0
                              :value "default_value_for_output_param_1"
                              :category "Output"
                              :description "The output param 1"
                              :type "String"
                              :name "output_param_1"}
                             {:help-hint nil
                              :mandatory? false
                              :read-only? false
                              :order 0
                              :value "default_value_for_output_param_2"
                              :category "Output"
                              :description "The output param 2"
                              :type "String"
                              :name "output_param_2"}
                             {:disabled? true
                              :help-hint "The instanceid is a default deployment parameter popupaled by SlipStream on deployment. You can access the live value in the deployment recipe with 'ss-get instanceid'."
                              :name "instanceid"
                              :read-only? false
                              :mandatory? true
                              :type "String"
                              :order 1
                              :value nil
                              :description "Cloud instance ID"
                              :placeholder "Provided at runtime by SlipStream"
                              :category "Output"}
                             {:disabled? true
                              :help-hint "The hostname is a default deployment parameter popupaled by SlipStream on deployment. You can access the live value in the deployment recipe with 'ss-get hostname'."
                              :name "hostname"
                              :read-only? false
                              :mandatory? true
                              :type "String"
                              :order 2
                              :value nil
                              :description "Hostname or IP address of the image"
                              :placeholder "Provided at runtime by SlipStream"
                              :category "Output"}]
  :cloud-configuration [{:category-type :global
                         :category "Cloud"
                         :parameters [{:help-hint nil
                                       :mandatory? true
                                       :read-only? false
                                       :order 0
                                       :value nil
                                       :category "Cloud"
                                       :description "Volatile extra disk in GB"
                                       :type "String"
                                       :name "extra.disk.volatile"}
                                      {:help-hint nil
                                       :mandatory? true
                                       :read-only? false
                                       :order 0
                                       :value [{:selected? true
                                                :value "Public"
                                                :text "Public"}
                                               {:value "Private"
                                                :text "Private"}]
                                       :category "Cloud"
                                       :description "Network type"
                                       :type "Enum"
                                       :name "network"}]}
                        {:category-type :global
                         :category "Cloud1"
                         :parameters [{:help-hint nil
                                       :mandatory? true
                                       :read-only? false
                                       :order 0
                                       :value nil
                                       :category "Cloud1"
                                       :description "Number of CPUs (i.e. virtual cores)"
                                       :type "String"
                                       :name "Cloud1.cpu"}
                                      {:help-hint nil
                                       :mandatory? true
                                       :read-only? false
                                       :order 0
                                       :value [{:selected? true
                                                :value "virtio"
                                                :text "virtio"}
                                               {:value "scsi"
                                                :text "scsi"}]
                                       :category "Cloud1"
                                       :description "VM disks bus type"
                                       :type "Enum"
                                       :name "Cloud1.disks.bus.type"}
                                      {:help-hint nil
                                       :mandatory? true
                                       :read-only? false
                                       :order 0
                                       :value [{:selected? true
                                                :value  "m1.small"
                                                :text   "m1.small"}
                                               {:value  "c1.medium"
                                                :text   "c1.medium"}
                                               {:value  "m1.large"
                                                :text   "m1.large"}
                                               {:value  "m1.xlarge"
                                                :text   "m1.xlarge"}
                                               {:value  "c1.xlarge"
                                                :text   "c1.xlarge"}
                                               {:value  "t1.micro"
                                                :text   "t1.micro"}
                                               {:value  "standard.xsmall"
                                                :text   "standard.xsmall"}]
                                       :category "Cloud1"
                                       :description "Cloud instance type"
                                       :type "Enum"
                                       :name "Cloud1.instance.type"}
                                      {:help-hint nil
                                       :mandatory? true
                                       :read-only? false
                                       :order 0
                                       :value nil
                                       :category "Cloud1"
                                       :description "Amount of RAM, in GB"
                                                    :type "String"
                                                    :name "Cloud1.ram"}]}
                         {:category-type :global
                          :category "Cloud2"
                          :parameters [{:help-hint nil
                                        :mandatory? true
                                        :read-only? false
                                        :order 0
                                        :value nil
                                        :category "Cloud2"
                                        :description "Instance type (flavor)"
                                        :type "String"
                                        :name "Cloud2.instance.type"}
                                       {:help-hint nil
                                        :mandatory? true
                                        :read-only? false
                                        :order 0
                                        :value "default"
                                        :category "Cloud2"
                                        :description "Security Groups (comma separated list)"
                                        :type "String"
                                        :name "Cloud2.security.groups"}]}
                         {:category-type :global
                          :category "Cloud3"
                          :parameters [{:help-hint nil
                                        :mandatory? true
                                        :read-only? false
                                        :order 0
                                        :value nil
                                        :category "Cloud3"
                                        :description "Instance type (flavor)"
                                        :type "String"
                                        :name "Cloud3.instance.type"}
                                       {:help-hint nil
                                        :mandatory? true
                                        :read-only? false
                                        :order 0
                                        :value "default"
                                        :category "Cloud3"
                                        :description "Security Groups (comma separated list)"
                                        :type "String"
                                        :name "Cloud3.security.groups"}]}
                          {:category-type :global
                           :category "Cloud4"
                           :parameters [{:help-hint nil
                                         :mandatory? true
                                         :read-only? false
                                         :order 0
                                         :value [{:selected? true
                                                  :value  "m1.small"
                                                  :text   "m1.small"}
                                                 {:value  "m1.large"
                                                  :text   "m1.large"}
                                                 {:value  "m1.xlarge"
                                                  :text   "m1.xlarge"}
                                                 {:value  "c1.medium"
                                                  :text   "c1.medium"}
                                                 {:value  "c1.xlarge"
                                                  :text   "c1.xlarge"}
                                                 {:value  "m2.xlarge"
                                                  :text   "m2.xlarge"}
                                                 {:value  "m2.2xlarge"
                                                  :text   "m2.2xlarge"}
                                                 {:value  "m2.4xlarge"
                                                  :text   "m2.4xlarge"}
                                                 {:value  "cc1.4xlarge"
                                                  :text   "cc1.4xlarge"}
                                                 {:value  "t1.micro"
                                                  :text   "t1.micro"}]
                                         :category "Cloud4"
                                         :description "Cloud instance type"
                                         :type "Enum"
                                         :name "Cloud4.instance.type"}
                                        {:help-hint nil
                                         :mandatory? true
                                         :read-only? false
                                         :order 0
                                         :value "default"
                                         :category "Cloud4"
                                         :description "Security groups (comma separated list)"
                                         :type "String"
                                         :name "Cloud4.security.groups"}]}]
       :os-details {:platform [{:selected? true
                              :value "centos"
                              :text "CentOS"}
                             {:value "debian"
                              :text "Debian"}
                             {:value "fedora"
                              :text "Fedora"}
                             {:value "opensuse"
                              :text "OpenSuse"}
                             {:value "redhat"
                              :text "RedHat"}
                             {:value "sles"
                              :text "Sles"}
                             {:value "ubuntu"
                              :text "Ubuntu"}
                             {:value "windows"
                              :text "Windows"}
                             {:value "other"
                              :text "Other"}]
                    :login-username "centos_username"}
      :cloud-image-details {:native-image? false
                            :cloud-identifiers {"Cloud2-with-veeeery-long-name" nil
                                                "Cloud1" nil
                                                "Cloud4" nil
                                                "Cloud3" nil}
                            :reference-image "neutral_projects_for_mockup_metadata/centos_6_base_image/131"}
      :runs {:pagination {:offset 0
                          :limit 20
                          :count-shown 0
                          :count-total 0
                          :cloud-name nil}
             :runs []}
      :summary {:publication nil
                :deleted? false
                :published? false
                :comment "Initial version of this image."
                :creation "2015-03-13 19:35:03.629 CET"
                :name "neutral_projects_for_mockup_metadata/apache_web_server"
                :short-name "apache_web_server"
                :owner "bob"
                :version 132
                :uri "module/neutral_projects_for_mockup_metadata/apache_web_server/132"
                :latest-version? false
                :logo-url "http://cubnetwork.com/wp-content/uploads/2014/09/Apache-web-server.jpg"
                :last-modified "2015-03-13 19:35:03.652 CET"
                :parent-uri "module/neutral_projects_for_mockup_metadata"
                :description "Apache Web Server on CentOS 6"
                :category "Image"}
      :authorization {:access-rights {:delete {:public-access? true
                                               :owner-access? true
                                               :group-access? true}
                                      :put {:public-access? true
                                            :group-access? true
                                            :owner-access? true}
                                      :post {:owner-access? true
                                             :group-access? true
                                             :public-access? true}
                                      :create-children {:public-access? false
                                                        :group-access? false
                                                        :owner-access? true}
                                      :get {:owner-access? true
                                            :group-access? true
                                            :public-access? true}}
                      :group-members #{}
                      :inherited-group-members? true}})

(expect
  parsed-metadata
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))
