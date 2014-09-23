(ns slipstream.ui.models.module.project-test
  (:use [expectations])
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.module :as module]))

(def raw-metadata (first (html/html-snippet "<projectModule description='Another description...' lastModified='2013-05-16 17:04:39.113 CEST' category='Project' deleted='false' resourceUri='module/Public/OtherProject/1' parentUri='module/Public' name='Public/OtherProject' version='1' creation='2013-05-16 17:04:39.113 CEST' shortName='OtherProject' isLatestVersion='true' >
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
     <user issuper='true' resourceUri='user/super' name='SuperDooper'></user>
  </projectModule>")))

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
  (module/parse raw-metadata))