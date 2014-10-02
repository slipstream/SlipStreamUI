(ns slipstream.ui.models.versions-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.versions :as model]))

(def raw-metadata
  "<versionList>
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
</versionList>")

(expect
  "parsed-metadata"
  (-> raw-metadata u/clojurify-raw-metadata model/parse))