(ns slipstream.ui.data.versions
  (:require [net.cgrand.enlive-html :as html]))

(def xml-versions (first (html/html-snippet "<versionList>
   <item category='Image' resourceUri='module/Mebster/mycentos63/226' lastModified='2013-05-24 13:09:36.340 CEST' version='226' name='mycentos63'>
      <authz owner='meb' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='false' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
         <groupMembers class='java.util.ArrayList'/>
      </authz>
      <comment>Added yum and mlocate packages</comment>
   </item>
   <item category='Image' resourceUri='module/Mebster/mycentos63/244' lastModified='2013-06-02 20:56:10.679 CEST' version='244' name='mycentos63'>
      <authz owner='meb' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='false' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
         <groupMembers class='java.util.ArrayList'/>
      </authz>
   </item>
   <item category='Image' resourceUri='module/Mebster/mycentos63/245' lastModified='2013-06-06 10:56:34.23 CEST' version='245' name='mycentos63'>
      <authz owner='meb' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='false' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
         <groupMembers class='java.util.ArrayList'/>
      </authz>
   </item>
   <user issuper='true' resourceUri='user/super' name='super'></user>
</versionList>")))