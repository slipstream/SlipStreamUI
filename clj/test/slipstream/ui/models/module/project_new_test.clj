(ns slipstream.ui.models.module.project-new-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.module :as model]))

(def raw-metadata
  "<projectModule description='A description...' lastModified='2013-05-16 17:04:39.113 CEST' category='Project' deleted='false' resourceUri='module/Public/new' parentUri='module/Public' name='Public/new' version='-1' creation='2013-05-16 17:04:39.113 CEST' shortName='new'>
     <parameters class='org.hibernate.collection.PersistentMap'/>
     <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
	      <groupMembers class='java.util.ArrayList'>
	         <string>meb</string>
	         <string>konstan</string>
	         <string>other</string>
	         <string>xxx</string>
	      </groupMembers>
     </authz>
     <cloudNames length='2'>
        <string>stratuslab</string>
        <string>default</string>
     </cloudNames>
     <user issuper='false' resourceUri='user/super' name='SuperDooper'></user>
  </projectModule>")

(expect
  "parsed-metadata"
  (-> raw-metadata u/clojurify-raw-metadata model/parse))
