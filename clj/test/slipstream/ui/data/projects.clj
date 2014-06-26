(ns slipstream.ui.data.projects
  (:require [net.cgrand.enlive-html :as html]))

(def xml-projects (first (html/html-snippet "<list>
<item category='Project' description='Description...' name='MyProject' resourceUri='module/Public/1' version='123'>
<authz groupCreateChildren='true' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='false' owner='sixsq' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='true' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<version>111.222.333</version>
<groupMembers class='java.util.ArrayList'></groupMembers>
</authz>
</item>
<item published='true' category='Project' description='A description' name='A Published Project' resourceUri='module/Other/2' version='321'>
<authz groupCreateChildren='true' groupDelete='false' groupGet='true' groupPost='false' groupPut='false' inheritedGroupMembers='false' owner='Joe' ownerCreateChildren='true' ownerDelete='true' ownerGet='true' ownerPost='true' ownerPut='true' publicCreateChildren='true' publicDelete='false' publicGet='true' publicPost='false' publicPut='false'>
<version>aaa.222.333</version>
<groupMembers class='java.util.ArrayList'></groupMembers>
</authz>
</item>
<user issuper='true' resourceUri='user/super' name='SuperDooper'></user>
</list>")))
