(ns slipstream.ui.models.welcome-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.welcome :as model]))

(def raw-metadata-str
  "<welcome>
   <modules>
      <item resourceUri=\"module/EBU_TTF/53\" category=\"Project\" description=\"Global module for TTF relates deployments and tasks\" version=\"53\" name=\"EBU_TTF\">
         <authz owner=\"rob\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"false\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item resourceUri=\"module/SlipStream/280\" category=\"Project\" description=\"SlipStream dog fooding :-)\" version=\"280\" name=\"SlipStream\">
         <authz owner=\"sixsq_dev\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"false\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"false\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>lionel</string>
               <string>konstan</string>
               <string>loomis</string>
               <string>meb</string>
               <string>rob</string>
               <string>seb</string>
            </groupMembers>
         </authz>
      </item>
      <item resourceUri=\"module/apps/528\" category=\"Project\" description=\"\" version=\"528\" name=\"apps\">
         <authz owner=\"super\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"true\" publicGet=\"true\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"false\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item resourceUri=\"module/examples/56\" category=\"Project\" description=\"Examples highlighting SlipStream features.  See User Guide on the documentation page.\" version=\"56\" name=\"examples\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"false\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"false\" groupDelete=\"false\" groupCreateChildren=\"true\" publicGet=\"true\" publicPut=\"false\" publicPost=\"false\" publicDelete=\"false\" publicCreateChildren=\"true\" inheritedGroupMembers=\"false\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/tutorials/wordpress/wordpress/478\" category=\"Deployment\" description=\"simple, single node deployment of WordPress\" published=\"true\" logoLink=\"http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png\" version=\"478\" name=\"wordpress\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/apps/minecraft-app/481\" category=\"Deployment\" description=\"A Minecraft Server\" published=\"true\" logoLink=\"http://www.esourceengine.com/downloads/minecraft/14085_minecraft.jpg\" version=\"481\" name=\"minecraft-app\">
         <authz owner=\"seb\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/apps/openerp/336\" category=\"Deployment\" description=\"An OpenERP instance backed with PostgresSQL\" published=\"true\" logoLink=\"http://www.techreceptives.com/wp-content/uploads/2013/06/openerp-logo2.png\" version=\"336\" name=\"openerp\">
         <authz owner=\"seb\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"true\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\">
               <string>meb</string>
               <string>lionel</string>
               <string>konstan</string>
               <string>rob</string>
            </groupMembers>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/images/windows-server-2012/391\" category=\"Image\" description=\"Standard installation of the Windows Server 2012 R2 operating system (For Exoscale: Please ensure that port 5985 is open in your security group named &quot;default&quot;)\" published=\"true\" logoLink=\"http://blogs.technet.com/resized-image.ashx/__size/375x0/__key/communityserver-blogs-components-weblogfiles/00-00-00-57-46/6457.ws2012img.jpg\" version=\"391\" name=\"windows-server-2012\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/images/ubuntu-12.04/517\" category=\"Image\" description=\"Minimal installation of the Ubuntu 12.04 (LTS) operating system.\" published=\"true\" logoLink=\"http://design.ubuntu.com/wp-content/uploads/ubuntu-logo14.png\" version=\"517\" name=\"ubuntu-12.04\">
         <authz owner=\"super\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
      <item class=\"com.sixsq.slipstream.module.ModuleViewPublished\" resourceUri=\"module/examples/images/centos-6/479\" category=\"Image\" description=\"Minimal installation of the CentOS 6 operating system.\" published=\"true\" logoLink=\"http://blog.quadranet.com/wp-content/uploads/2014/01/centos.png\" version=\"479\" name=\"centos-6\">
         <authz owner=\"sixsq\" ownerGet=\"true\" ownerPut=\"true\" ownerPost=\"true\" ownerDelete=\"true\" ownerCreateChildren=\"true\" groupGet=\"true\" groupPut=\"false\" groupPost=\"true\" groupDelete=\"false\" groupCreateChildren=\"false\" publicGet=\"true\" publicPut=\"false\" publicPost=\"true\" publicDelete=\"false\" publicCreateChildren=\"false\" inheritedGroupMembers=\"true\">
            <groupMembers class=\"java.util.ArrayList\"/>
         </authz>
      </item>
   </modules>
   <user issuper='true' resourceUri='user/meb' name='meb'></user>
</welcome>")

(def parsed-metadata
  {:published-apps [{:publisher "sixsq"
                     :image "http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png"
                     :uri "module/examples/tutorials/wordpress/wordpress/478"
                     :version "478"
                     :name "wordpress"
                     :description "simple, single node deployment of WordPress"}
                    {:publisher "seb"
                     :image "http://www.esourceengine.com/downloads/minecraft/14085_minecraft.jpg"
                     :uri "module/apps/minecraft-app/481"
                     :version "481"
                     :name "minecraft-app"
                     :description "A Minecraft Server"}
                    {:publisher "seb"
                     :image "http://www.techreceptives.com/wp-content/uploads/2013/06/openerp-logo2.png"
                     :uri "module/apps/openerp/336"
                     :version "336"
                     :name "openerp"
                     :description "An OpenERP instance backed with PostgresSQL"}
                    {:publisher "sixsq"
                     :image "http://blogs.technet.com/resized-image.ashx/__size/375x0/__key/communityserver-blogs-components-weblogfiles/00-00-00-57-46/6457.ws2012img.jpg"
                     :uri "module/examples/images/windows-server-2012/391"
                     :version "391"
                     :name "windows-server-2012"
                     :description "Standard installation of the Windows Server 2012 R2 operating system (For Exoscale: Please ensure that port 5985 is open in your security group named \"default\")"}
                    {:publisher "super"
                     :image "http://design.ubuntu.com/wp-content/uploads/ubuntu-logo14.png"
                     :uri "module/examples/images/ubuntu-12.04/517"
                     :version "517"
                     :name "ubuntu-12.04"
                     :description "Minimal installation of the Ubuntu 12.04 (LTS) operating system."}
                    {:publisher "sixsq"
                     :image "http://blog.quadranet.com/wp-content/uploads/2014/01/centos.png"
                     :uri "module/examples/images/centos-6/479"
                     :version "479"
                     :name "centos-6"
                     :description "Minimal installation of the CentOS 6 operating system."}]
   :projects [{:owner "rob"
               :uri "module/EBU_TTF/53"
               :name "EBU_TTF"
               :version "53"
               :description "Global module for TTF relates deployments and tasks"
               :category "Project"}
              {:owner "sixsq_dev"
               :uri "module/SlipStream/280"
               :name "SlipStream"
               :version "280"
               :description "SlipStream dog fooding :-)"
               :category "Project"}
              {:owner "super"
               :uri "module/apps/528"
               :name "apps"
               :version "528"
               :description ""
               :category "Project"}
              {:owner "sixsq"
               :uri "module/examples/56"
               :name "examples"
               :version "56"
               :description "Examples highlighting SlipStream features.  See User Guide on the documentation page."
               :category "Project"}]})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))