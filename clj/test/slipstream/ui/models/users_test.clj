(ns slipstream.ui.models.users-test
  (:require
   [expectations :refer :all]
   [slipstream.ui.util.core :as u]
   [slipstream.ui.models.users :as model]))

(def raw-metadata-str
  "<list>
    <item firstName='Super' lastName='User' lastOnline='2014-08-14 20:14:20.810 CEST' name='super' online='true' organization='SixSq' resourceUri='user/super' issuper='true' state='ACTIVE'/>
    <item firstName='A Test' lastName='User' lastOnline='2015-08-10 22:09:21.705 CEST' name='atest' online='false' organization='ACME' roles='aws' resourceUri='user/atest' issuper='true' state='ACTIVE'/>
    <item firstName='D Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='dtest' online='true' organization='SixSq' roles='aws,exoscale' resourceUri='user/tdest' issuper='true' state='ACTIVE'/>
    <item firstName='B Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='btest' online='false' organization='ACME' resourceUri='user/btest' issuper='false' state='ACTIVE'/>
    <item firstName='Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='test' online='true' organization='ACME' resourceUri='user/test' issuper='false' state='ACTIVE'/>
    <item firstName='C Test' lastName='User' lastOnline='2014-08-10 22:09:21.705 CEST' name='ctest' online='false' organization='ACME' resourceUri='user/tcest' issuper='false' state='ACTIVE'/>
    <item firstName='SixSq' lastName='Administrator' name='sixsq' online='false' organization='SixSq' resourceUri='user/sixsq' state='ACTIVE'/>
    <user issuper='true' resourceUri='user/super' name='super' defaultCloud='sky'></user>
</list>")

(def parsed-metadata
  [{:username "atest"
    :uri "user/atest"
    :first-name "A Test"
    :last-name "User"
    :organization "ACME"
    :roles "aws"
    :state "ACTIVE"
    :online? false
    :super? true
    :last-online "2015-08-10 22:09:21.705 CEST"}
   {:username "btest"
    :uri "user/btest"
    :first-name "B Test"
    :last-name "User"
    :organization "ACME"
    :roles nil
    :state "ACTIVE"
    :online? false
    :super? false
    :last-online "2014-08-10 22:09:21.705 CEST"}
   {:username "ctest"
    :uri "user/tcest"
    :first-name "C Test"
    :last-name "User"
    :organization "ACME"
    :roles nil
    :state "ACTIVE"
    :online? false
    :super? false
    :last-online "2014-08-10 22:09:21.705 CEST"}
   {:username "dtest"
    :uri "user/tdest"
    :first-name "D Test"
    :last-name "User"
    :organization "SixSq"
    :roles "aws,exoscale"
    :state "ACTIVE"
    :online? true
    :super? true
    :last-online "2014-08-10 22:09:21.705 CEST"}
   {:username "sixsq"
    :uri "user/sixsq"
    :first-name "SixSq"
    :last-name "Administrator"
    :organization "SixSq"
    :roles nil
    :state "ACTIVE"
    :online? false
    :super? nil
    :last-online nil}
   {:username "super"
    :uri "user/super"
    :first-name "Super"
    :last-name "User"
    :organization "SixSq"
    :roles nil
    :state "ACTIVE"
    :online? true
    :super? true
    :last-online "2014-08-14 20:14:20.810 CEST"}
   {:username "test"
    :uri "user/test"
    :first-name "Test"
    :last-name "User"
    :organization "ACME"
    :roles nil
    :state "ACTIVE"
    :online? true
    :super? false
    :last-online "2014-08-10 22:09:21.705 CEST"}])

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
