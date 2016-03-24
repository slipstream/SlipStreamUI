(ns slipstream.ui.models.module.image-new-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.module :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_new_image.xml"))

(def parsed-metadata
  {:targets [{:target-type  :script
              :target-name  "prerecipe"
              :target-machine-type :application-component
              :context      #{:image-creation}
              :script       "some pre-recipe"}
             {:target-type  :packages
              :target-name  "packages"
              :target-machine-type :application-component
              :context      #{:image-creation}
              :packages   [{:repository "repo"
                            :name       "apache2"
                            :key        "key"}]}
             {:target-type  :script
              :target-name  "recipe"
              :target-machine-type :application-component
              :context      #{:image-creation}
              :script       "some recipe"}
             {:target-type  :script
              :target-name  "execute"
              :target-machine-type :application-component
              :context      #{:deployment :ss-client-access}
              :script       "execute target"}
             {:target-type  :script
              :target-name  "report"
              :target-machine-type :application-component
              :context      #{:deployment :ss-client-access}
              :script       "report target"}
             {:target-type  :script
              :target-name  "onvmadd"
              :target-machine-type :application-component
              :context      #{:scaling :ss-client-access}
              :script       nil}
             {:target-type  :script
              :target-name  "onvmremove"
              :target-machine-type :application-component
              :context      #{:scaling :ss-client-access}
              :script       nil}]
   :deployment-parameters [{:name "instanceid"
                            :category "Output"
                            :type "String"
                            :description "Cloud instance ID"
                            :value nil
                            :order 1
                            :placeholder "Provided at runtime by SlipStream"
                            :read-only? false
                            :mandatory? true
                            :help-hint "The instanceid is a default deployment parameter popupaled by SlipStream on deployment. You can access the live value in the deployment recipe with 'ss-get instanceid'."
                            :disabled? true}
                           {:disabled? true
                            :help-hint "The hostname is a default deployment parameter popupaled by SlipStream on deployment. You can access the live value in the deployment recipe with 'ss-get hostname'."
                            :read-only? false
                            :mandatory? true
                            :order 2
                            :placeholder "Provided at runtime by SlipStream"
                            :value "123.234.345"
                            :category "Output"
                            :description "Hostname or IP address of the image"
                            :type "String"
                            :name "hostname"}]
   :input-parameters []
   :cloud-configuration [{:category-type :global
                          :category "Cloud"
                          :parameters [{:help-hint nil
                                        :read-only? false
                                        :mandatory? true
                                        :order 2147483647
                                        :value "12345"
                                        :category "Cloud"
                                        :description "Volatile extra disk in GB"
                                        :type "String"
                                        :name "extra.disk.volatile"}
                                       {:help-hint nil
                                        :name "network"
                                        :read-only? false
                                        :mandatory? true
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
                                        :mandatory? true
                                        :order 2147483647
                                        :value nil
                                        :category "stratuslab"
                                        :description "Requested CPUs"
                                        :type "String"
                                        :name "stratuslab.cpu"}
                                       {:help-hint nil
                                        :name "stratuslab.disks.bus.type"
                                        :read-only? false
                                        :mandatory? true
                                        :type "Enum"
                                        :order 2147483647
                                        :value [{:selected? true, :value "virtio", :text "virtio", :original-selection "VIRTIO"}
                                                {:value "scsi", :text "scsi"}]
                                        :description "VM disks bus type"
                                        :category "stratuslab"}
                                       {:help-hint nil
                                        :name "stratuslab.instance.type"
                                        :read-only? false
                                        :mandatory? true
                                        :type "Enum"
                                        :order 2147483647
                                        :value [{:selected? true, :value "m1.small", :text "m1.small", :original-selection "M1_SMALL"}
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
                                        :mandatory? true
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
   :summary {:deleted? false
             :comment nil
             :creation "2013-03-07 21:03:09.124 CET"
             :name nil
             :logo-url ""
             :short-name "new"
             :published? false
             :publication nil
             :owner "sixsq"
             :version 4
             :uri "module/Public/BaseImages/Ubuntu/new"
             :latest-version? true
             :last-modified "2013-03-07 21:03:09.337 CET"
             :parent-uri "module/Public/BaseImages/Ubuntu"
             :description "Nice Ubuntu distro"
             :category "Image"}})

(expect
  parsed-metadata
  (page-type/with-page-type "new"
    (localization/with-lang :en
      (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))))



