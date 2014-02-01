(ns slipstream.ui.data.users
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-users (first (html/html-snippet "<list>
   <item name='sixsq' resourceUri='user/sixsq' firstName='SixSq' lastName='Administrator' state='ACTIVE' />
   <item name='super' resourceUri='user/super' firstName='Super' lastName='User' state='ACTIVE' online='false' />
   <item name='test' resourceUri='user/test' firstName='Test' lastName='User' state='ACTIVE' online='true' />
   <user issuper='true' resourceUri='user/super' name='SuperDooper'></user>
</list>")))
