(ns slipstream.ui.models.module.project-root-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]))

(def raw-metadata-str
  "<projectModule description='A description...' lastModified='2013-05-16 17:04:39.113 CEST' category='Project' deleted='false' resourceUri='module/Public/1' parentUri='module/' name='Public' version='1' creation='2013-05-16 17:04:39.113 CEST' shortName='Public'>
     <parameters class='org.hibernate.collection.PersistentMap'/>
     <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='true' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='true' inheritedGroupMembers='false'>
	      <groupMembers class='java.util.ArrayList'>
	         <string>meb</string>
	         <string>konstan</string>
	         <string>other</string>
	         <string>xxx</string>
	      </groupMembers>
     </authz>
     <comment>ccc</comment>
     <cloudNames length='2'>
        <string>stratuslab</string>
        <string>default</string>
     </cloudNames>
     <children class='java.util.ArrayList'>
        <item resourceUri='module/Public/BaseImages/2' category='Project' name='BaseImages' version='2'>
           <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
              <groupMembers class='java.util.ArrayList'/>
           </authz>
        </item>
        <item resourceUri='module/Public/Tutorials/7' category='Project' name='Tutorials' version='7'>
           <authz owner='sixsq' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='true' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='true' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
              <groupMembers class='java.util.ArrayList'/>
           </authz>
        </item>
     </children>
     <user issuper='false' resourceUri='user/super' name='SuperDooper'></user>
  </projectModule>")
