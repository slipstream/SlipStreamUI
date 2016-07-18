(ns slipstream.ui.models.module.project-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.module :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_project.xml"))

(def parsed-metadata
  {:children [{:category "Image"
             :name "apache_web_server"
             :description "Apache Web Server on CentOS 6"
             :owner "bob"
             :version 132
             :uri "module/neutral_projects_for_mockup_metadata/apache_web_server/132"}
            {:category "Image"
             :name "centos_6_base_image"
             :description "Base image for CentOS 6"
             :owner "bob"
             :version 131
             :uri "module/neutral_projects_for_mockup_metadata/centos_6_base_image/131"}
            {:category "Deployment"
             :name "deployment_01"
             :description "Multinode deployment with input and output params."
             :owner "alice"
             :version 136
             :uri "module/neutral_projects_for_mockup_metadata/deployment_01/136"}]
 :summary {:publication nil
           :deleted? false
           :published? false
           :comment "Add SixSq logo."
           :creation "2015-03-13 19:09:38.116 CET"
           :name "neutral_projects_for_mockup_metadata"
           :short-name "neutral_projects_for_mockup_metadata"
           :owner "bob"
           :version 133
           :uri "module/neutral_projects_for_mockup_metadata/133"
           :latest-version? true
           :logo-url "http://ww1.prweb.com/prfiles/2014/11/04/12300280/sixsq-high.jpg"
           :last-modified "2015-03-13 19:36:31.339 CET"
           :parent-uri "module/"
           :placement-policy nil
           :description "The projects here shouldn't contain any personal or private information, since they are intended to be used as mockup metadata in the SlipStreamUI project."
           :category "Project"}
 :authorization {:access-rights {:delete {:public-access? true
                                          :owner-access? true
                                          :group-access? true}
                                 :put {:public-access? true
                                       :group-access? true
                                       :owner-access? true}
                                 :post {:owner-access? false
                                        :group-access? false
                                        :public-access? false}
                                 :create-children {:public-access? true
                                                   :group-access? true
                                                   :owner-access? true}
                                 :get {:owner-access? true
                                       :group-access? true
                                       :public-access? true}}
                 :group-members #{}
                 :inherited-group-members? true}})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
