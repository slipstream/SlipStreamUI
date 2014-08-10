(ns slipstream.ui.data.users
  (:require [net.cgrand.enlive-html :as html]))

(def xml-users-legacy (first (html/html-snippet "<list>
   <item name='sixsq' resourceUri='user/sixsq' firstName='SixSq' lastName='Administrator' state='ACTIVE' />
   <item name='super' resourceUri='user/super' firstName='Super' lastName='User' state='ACTIVE' online='false' />
   <item name='test' resourceUri='user/test' firstName='Test' lastName='User' state='ACTIVE' online='true' />
   <user issuper='true' resourceUri='user/super' name='SuperDooper'></user>
</list>")))

(def xml-users (first (html/html-snippet "<list>
    <item name='sixsq' resourceUri='user/sixsq' firstName='SixSq' lastName='Administrator' state='ACTIVE' lastOnline='2014-03-25 15:59:32.65 UTC' online='true'/>
    <item name='super' resourceUri='user/super' firstName='Super' lastName='Smith' state='NEW' lastOnline='2014-08-05 12:38:40.190 UTC' online='false'/>
    <item name='test' resourceUri='user/test' firstName='Test' lastName='Bros' state='ACTIVE' online='false'/>
</list>")))
