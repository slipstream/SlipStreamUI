(ns slipstream.ui.data.project
  (:require [net.cgrand.enlive-html :as html]))

(def xml-project (first (html/html-snippet "<projectModule category='Project' creation='2013-03-08 22:37:25.341 CET' deleted='false' lastModified='2013-03-08 22:37:25.342 CET' name='Public' parentUri='module/' resourceUri='module/Public/1' shortName='Public' version='1'>
<parameters class='org.hibernate.collection.PersistentMap'></parameters>
<authz groupCreateChildren='true' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='false' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='true' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<groupMembers class='java.util.ArrayList'></groupMembers>
</authz>
<cloudNames length='2'>
<string>stratuslab</string>
<string>default</string>
</cloudNames>
<children class='java.util.ArrayList'>
<item category='Project' name='BaseImages' resourceUri='module/Public/BaseImages/2' version='2'>
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<groupMembers class='java.util.ArrayList'></groupMembers>
</authz>
</item>
<item category='Project' name='Tutorials' resourceUri='module/Public/Tutorials/7' version='7'>
<authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<groupMembers class='java.util.ArrayList'></groupMembers> 
</authz>
</item>
</children>
<user issuper='true' resourceUri='user/super' username='super'></user>
<breadcrumbs path=''>
<crumb name='module' path='module'></crumb>
<crumb name='Public' path='module/Public'></crumb>
<crumb name='1' path='module/Public/1'></crumb>
</breadcrumbs>
</projectModule>")))
