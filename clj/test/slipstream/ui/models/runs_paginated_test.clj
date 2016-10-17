(ns slipstream.ui.models.runs-paginated-test
  (:require
   [expectations :refer :all]
   [slipstream.ui.util.core :as u]
   [slipstream.ui.models.runs :as model]))

;; NOTE: For request /run?offset=5&limit=4&cloud=CloudA
(def raw-metadata-str
  "<runs offset='5' limit='4' count='4' totalCount='13' cloud='CloudA'>
    <item resourceUri='run/079dd5c1-85af-4387-95a7-b24fc0b2be53' uuid='079dd5c1-85af-4387-95a7-b24fc0b2be53' moduleResourceUri='module/first-project/a-non-native-image/5' status='Initializing' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/super\"&gt;user account&lt;/a&gt;' startTime='2014-12-15 01:03:45.240 CET' cloudServiceNames='CloudA' username='rob' type='Machine' activeVm='0' tags='a----asdf, as, asd, asdf, df, fdfdsa, foij, patata'/>
    <item resourceUri='run/672c2946-578e-4093-8d7a-af3e0f432bac' uuid='672c2946-578e-4093-8d7a-af3e0f432bac' moduleResourceUri='module/first-project/newdeployment/4' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/super\"&gt;user account&lt;/a&gt;' startTime='2014-12-11 14:41:15.310 CET' cloudServiceNames='CloudA' username='rob' type='Orchestration' activeVm='0' tags='19-vms'/>
    <item resourceUri='run/2351f969-3a82-4ffe-a002-a2f76984f02a' uuid='2351f969-3a82-4ffe-a002-a2f76984f02a' moduleResourceUri='module/first-project/a-non-native-image/5' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/super\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 19:36:08.763 CET' cloudServiceNames='CloudA' username='rob' type='Machine' activeVm='0' tags='myDeployment, patata, tata, tata, toto'/>
    <item resourceUri='run/0afb0b57-036e-4d2c-833d-699842cfeaab' uuid='0afb0b57-036e-4d2c-833d-699842cfeaab' moduleResourceUri='module/first-project/a-non-native-image/5' status='Cancelled' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/super\"&gt;user account&lt;/a&gt;' startTime='2014-12-08 18:00:03.8 CET' cloudServiceNames='CloudA' username='rob' type='Machine' activeVm='0' tags=''/>
    <user issuper='true' resourceUri='user/super' name='super'></user>
</runs>")

(expect
  {:offset 5
   :limit 4
   :count-shown 4
   :count-total 13
   :cloud-name "CloudA"}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :pagination))
