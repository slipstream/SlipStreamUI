(ns slipstream.ui.models.module.project-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.module :as model]))

(def raw-metadata-str
  "<projectModule description='Another description...' lastModified='2013-05-16 17:04:39.113 CEST' category='Project' deleted='false' resourceUri='module/Public/OtherProject/1' parentUri='module/Public' name='Public/OtherProject' version='1' creation='2013-05-16 17:04:39.113 CEST' shortName='OtherProject' isLatestVersion='true' >
     <parameters class='org.hibernate.collection.PersistentMap'/>
     <authz owner='sixsq' ownerGet='true' ownerPut='false' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='true' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='false'>
        <groupMembers class='java.util.ArrayList'>
           <string>meb</string>
           <string>konstan</string>
           <string>other</string>
           <string>another</string>
           <string>xxx</string>
        </groupMembers>
     </authz>
     <comment>ccc</comment>
     <cloudNames length='2'>
        <string>stratuslab</string>
        <string>default</string>
     </cloudNames>
     <children>
        <item category='Project' description='Deployment of tools like Jenkins slave.' name='Tooling' version='54'>
            <authz groupCreateChildren='false' groupDelete='false' groupGet='false' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='rob' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='false' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='false' publicPost='false' publicPut='false'>
                <groupMembers/>
            </authz>
        </item>
        <item category='Deployment' name='deploy-slave' version='78'>
          <authz groupCreateChildren='false' groupDelete='false' groupGet='false' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='test' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='false' publicPost='false' publicPut='false'>
            <groupMembers/>
          </authz>
        </item>
        <item category='Image' description='CentOS 6.4 Jenkins Slave' name='slave-centos' version='80'>
          <authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='rob' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='false' publicPost='false' publicPut='false'>
            <groupMembers/>
          </authz>
        </item>
        <item category='Project' description='Deployment of tools like Jenkins slave.' name='a-Tooling' version='123'>
            <authz groupCreateChildren='false' groupDelete='false' groupGet='false' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='rob' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='false' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='false' publicPost='false' publicPut='false'>
                <groupMembers/>
            </authz>
        </item>
        <item category='Image' description='CentOS 6.4 Jenkins Slave' name='a-slave-centos' version='345'>
          <authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='thomas' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='false' publicPost='false' publicPut='false'>
            <groupMembers/>
          </authz>
        </item>
        <item category='Deployment' name='a-deploy-slave' version='234'>
          <authz groupCreateChildren='false' groupDelete='false' groupGet='false' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='rob' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='false' publicPost='false' publicPut='false'>
            <groupMembers/>
          </authz>
        </item>
        </children>
     <user issuper='true' resourceUri='user/super' name='SuperDooper'></user>
  </projectModule>")

(def parsed-metadata
  {:authorization {:access-rights {:create-children {:public-access? false
                                                   :group-access? true
                                                   :owner-access? true}
                                   :delete {:owner-access? true
                                            :public-access? false
                                            :group-access? false}
                                   :put {:owner-access? false
                                         :public-access? false
                                         :group-access? true}
                                   :post {:group-access? false
                                          :owner-access? true
                                          :public-access? false}
                                   :get {:group-access? true
                                         :public-access? true
                                         :owner-access? true}}
                   :group-members #{"meb" "other" "konstan" "xxx" "another"}
                   :inherited-group-members? false}
   :children [{:category "Project"
               :name "Tooling"
               :description "Deployment of tools like Jenkins slave."
               :owner "rob"
               :version 54
               :uri "module/Public/OtherProject/Tooling/54"}
              {:category "Project"
               :name "a-Tooling"
               :description "Deployment of tools like Jenkins slave."
               :owner "rob"
               :version 123
               :uri "module/Public/OtherProject/a-Tooling/123"}
              {:category "Deployment"
               :name "a-deploy-slave"
               :description nil
               :owner "rob"
               :version 234
               :uri "module/Public/OtherProject/a-deploy-slave/234"}
              {:category "Image"
               :name "a-slave-centos"
               :description "CentOS 6.4 Jenkins Slave"
               :owner "thomas"
               :version 345
               :uri "module/Public/OtherProject/a-slave-centos/345"}
              {:category "Deployment"
               :name "deploy-slave"
               :description nil
               :owner "test"
               :version 78
               :uri "module/Public/OtherProject/deploy-slave/78"}
              {:category "Image"
               :name "slave-centos"
               :description "CentOS 6.4 Jenkins Slave"
               :owner "rob"
               :version 80
               :uri "module/Public/OtherProject/slave-centos/80"}]
   :summary {:deleted? false
             :creation "2013-05-16 17:04:39.113 CEST"
             :name "Public/OtherProject"
             :short-name "OtherProject"
             :owner "sixsq"
             :version 1
             :uri "module/Public/OtherProject/1"
             :latest-version? true
             :last-modified "2013-05-16 17:04:39.113 CEST"
             :parent-uri "module/Public"
             :description "Another description..."
             :category "Project"}})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
