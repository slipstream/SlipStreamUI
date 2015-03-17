(ns slipstream.ui.models.module.deployment-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.module :as model]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_deployment.xml"))

(def parsed-metadata
  {:runs {:pagination {:offset 0
                     :limit 20
                     :count-shown 0
                     :count-total 0
                     :cloud-name nil}
        :runs []}
 :nodes [{:name "node1"
          :template-node? nil
          :reference-image "neutral_projects_for_mockup_metadata/apache_web_server"
          :default-multiplicity 1
          :default-cloud [{:value "Cloud3"
                           :text "Cloud3"}
                          {:value "Cloud4"
                           :text "Cloud4"}
                          {:value "Cloud1"
                           :text "Cloud1"}
                          {:value "Cloud2"
                           :text "Cloud2"}
                          {:selected? true
                           :value "default"
                           :text "default"}]
          :output-parameters ["hostname" "instanceid" "output_param_1" "output_param_2"]
          :mappings [{:name "input_param_1"
                      :mapped-value? true
                      :value "node2:output_param_1"}
                    {:name "input_param_2"
                     :mapped-value? false
                     :value "'default_value_for_input_param_2'"}]}
          {:name "node2"
             :template-node? nil
             :reference-image "neutral_projects_for_mockup_metadata/apache_web_server"
             :default-multiplicity 1
             :default-cloud [{:value "Cloud3"
                              :text "Cloud3"}
                             {:value "Cloud4"
                              :text "Cloud4"}
                             {:value "Cloud1"
                              :text "Cloud1"}
                             {:value "Cloud2"
                              :text "Cloud2"}
                             {:selected? true
                              :value "default"
                              :text "default"}]
             :output-parameters ["hostname" "instanceid" "output_param_1" "output_param_2"]
             :mappings [{:name "input_param_1"
                         :mapped-value? false
                         :value "'default_value_for_input_param_1'"}
                       {:name "input_param_2"
                        :mapped-value? true
                        :value "node1:output_param_2"}]}]
 :summary {:publication nil
           :deleted? false
           :published? false
           :comment "Initial version of this deployment."
           :creation "2015-03-16 17:23:00.876 CET"
           :name "neutral_projects_for_mockup_metadata/deployment_01"
           :short-name "deployment_01"
           :owner "alice"
           :version 136
           :uri "module/neutral_projects_for_mockup_metadata/deployment_01/136"
           :latest-version? true
           :logo-url "https://newevolutiondesigns.com/images/freebies/vibrant-wallpaper-preview-16.jpg"
           :last-modified "2015-03-16 17:23:00.901 CET"
           :parent-uri "module/neutral_projects_for_mockup_metadata"
           :description "Multinode deployment with input and output params."
           :category "Deployment"}
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

(expect
  parsed-metadata
  (localization/with-lang :en
    (page-type/with-page-type "view"
      (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))))

(def ^:private blank-node-template
  {:name nil
   :template-node? true
   :reference-image "new"
   :default-multiplicity 1
   :default-cloud [{:value "Cloud3"
                    :text "Cloud3"}
                   {:value "Cloud4"
                    :text "Cloud4"}
                   {:value "Cloud1"
                    :text "Cloud1"}
                   {:value "Cloud2"
                    :text "Cloud2"}
                   {:selected? true
                    :value "default"
                    :text "default"}]
   :output-parameters ()
   :mappings [{:name nil
               :mapped-value? nil
               :value nil}]})

(expect
  (update-in parsed-metadata [:nodes] conj blank-node-template)
  (localization/with-lang :en
    (page-type/with-page-type "edit"
      (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))))
