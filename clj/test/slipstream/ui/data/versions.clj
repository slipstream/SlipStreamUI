(ns slipstream.ui.data.versions
  (:require [net.cgrand.enlive-html :as html]))

(def xml-versions-legacy (first (html/html-snippet "<versionList>
   <item category='Image' resourceUri='module/Mebster/mycentos63/244' lastModified='2013-06-02 20:56:10.679 CEST' version='244' name='mycentos63'>
      <authz owner='donald' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='false' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
         <groupMembers class='java.util.ArrayList'/>
      </authz>
      <commit author='some-user' />
   </item>
   <item category='Image' resourceUri='module/Mebster/mycentos63/226' lastModified='2013-05-24 13:09:36.340 CEST' version='99' name='mycentos63'>
      <authz owner='donald' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='false' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
         <groupMembers class='java.util.ArrayList'/>
      </authz>
      <commit author='super'><comment>Added yum and mlocate packages</comment></commit>
   </item>
   <item category='Image' resourceUri='module/Mebster/mycentos63/245' lastModified='2013-06-06 10:56:34.23 CEST' version='245' name='mycentos63'>
      <authz owner='donald' ownerGet='true' ownerPut='true' ownerPost='true' ownerDelete='true' ownerCreateChildren='true' groupGet='false' groupPut='false' groupPost='false' groupDelete='false' groupCreateChildren='false' publicGet='false' publicPut='false' publicPost='false' publicDelete='false' publicCreateChildren='false' inheritedGroupMembers='true'>
         <groupMembers class='java.util.ArrayList'/>
      </authz>
      <commit author='some-user' />
   </item>
   <user issuper='true' resourceUri='user/super' name='super'></user>
</versionList>")))


(def xml-versions (first (html/html-snippet "<versionList>
    <item category='Deployment' lastModified='2014-04-10 12:15:41.121 UTC' name='wordpress' resourceUri='module/examples/tutorials/wordpress/wordpress/180' version='180'>
        <authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='true' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='true' publicPut='false'>
            <groupMembers class='java.util.ArrayList'/>
        </authz>
        <commit author='sixsq'>
            <comment>Add public view and run</comment>
        </commit>
    </item>
    <item category='Deployment' lastModified='2014-05-10 14:57:23.511 UTC' name='wordpress' resourceUri='module/examples/tutorials/wordpress/wordpress/478' version='478'>
        <authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='true' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='true' publicPut='false'>
            <groupMembers class='java.util.ArrayList'/>
        </authz>
        <commit author='sixsq'>
            <comment>Add public run</comment>
        </commit>
    </item>
    <item category='Deployment' lastModified='2014-05-04 12:36:13.579 UTC' name='wordpress' resourceUri='module/examples/tutorials/wordpress/wordpress/318' version='318'>
        <authz groupCreateChildren='false' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
            <groupMembers class='java.util.ArrayList'/>
        </authz>
        <commit author='sixsq'>
            <comment>Added logo</comment>
        </commit>
    </item>
    <item category='Deployment' lastModified='2013-12-02 16:57:09.999 UTC' name='wordpress' resourceUri='module/examples/tutorials/wordpress/wordpress/74' version='74'>
        <authz groupCreateChildren='false' groupDelete='false' groupGet='false' groupPost='false' groupPut='false' inheritedGroupMembers='true' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='false' publicDelete='false' publicGet='false' publicPost='false' publicPut='false'>
            <groupMembers class='java.util.ArrayList'/>
        </authz>
        <commit author='sixsq'>
            <comment/>
        </commit>
    </item>
   <user issuper='true' resourceUri='user/super' name='super'></user>
</versionList>")))

