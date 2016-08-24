(ns slipstream.ui.models.versions-test
  (:require
   [expectations :refer :all]
   [slipstream.ui.util.core :as u]
   [slipstream.ui.models.versions :as model]))

(def raw-metadata-str
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

(def parsed-metadata
  {:versions [{:version 74
               :uri "module/examples/tutorials/wordpress/wordpress/74"
               :commit {:author "sixsq"
                        :comment nil
                        :date "2013-12-02 16:57:09.999 UTC"}}
              {:version 180
               :uri "module/examples/tutorials/wordpress/wordpress/180"
               :commit {:author "sixsq"
                        :comment "Add public view and run"
                        :date "2014-04-10 12:15:41.121 UTC"}}
              {:version 318
               :uri "module/examples/tutorials/wordpress/wordpress/318"
               :commit {:author "sixsq"
                        :comment "Added logo"
                        :date "2014-05-04 12:36:13.579 UTC"}}
              {:version 478
               :uri "module/examples/tutorials/wordpress/wordpress/478"
               :commit {:author "sixsq"
                        :comment "Add public run"
                        :date "2014-05-10 14:57:23.511 UTC"}}]
   :resource-uri "module/examples/tutorials/wordpress/wordpress"
   :module-name "wordpress"
   :category "Deployment"})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))