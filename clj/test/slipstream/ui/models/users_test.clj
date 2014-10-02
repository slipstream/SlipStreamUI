(ns slipstream.ui.models.users-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.users :as model]))

(def raw-metadata
  "<list>
    <item firstName='Super' lastName='User' lastOnline='2014-08-14 20:14:20.810 CEST' name='super' online='true' organization='SixSq' resourceUri='user/super' state='ACTIVE'/>
    <item firstName='A Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='atest' online='false' organization='ACME' resourceUri='user/atest' state='ACTIVE'/>
    <item firstName='D Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='dtest' online='true' organization='SixSq' resourceUri='user/tdest' state='ACTIVE'/>
    <item firstName='B Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='btest' online='false' organization='ACME' resourceUri='user/btest' state='ACTIVE'/>
    <item firstName='Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='test' online='true' organization='ACME' resourceUri='user/test' state='ACTIVE'/>
    <item firstName='C Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='ctest' online='false' organization='ACME' resourceUri='user/tcest' state='ACTIVE'/>
    <item firstName='SixSq' lastName='Administrator' name='sixsq' online='false' organization='SixSq' resourceUri='user/sixsq' state='ACTIVE'/>
</list>")

(expect
  "parsed-metadata"
  (-> raw-metadata u/clojurify-raw-metadata model/parse))
