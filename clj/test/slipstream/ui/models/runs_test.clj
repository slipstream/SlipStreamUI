(ns slipstream.ui.models.runs-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.runs :as model]))

;; NOTE: For request /run?media=xm&offset=0&limit=10&cloud=StratusLab
(def raw-metadata-str
  "<runs offset='0' limit='10' count='2' cloud='StratusLab'>
    <item resourceUri='run/36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae' uuid='36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae' moduleResourceUri='module/first-project/newdeployment/85' status='Initializing' abort='Cloud Username cannot be empty, please edit your &lt;a href=\"/user/super\"&gt;user account&lt;/a&gt;' startTime='2015-01-14 17:23:18.262 CET' cloudServiceNames='StratusLab' username='rob' type='Orchestration' tags=''/>
    <item resourceUri='run/0aa79e3f-878a-40b1-8e88-bb6808a183cc' uuid='0aa79e3f-878a-40b1-8e88-bb6808a183cc' moduleResourceUri='module/calsproject/deployment/54' status='Cancelled' abort='Marketplace endpoint should be set for StratusLab' startTime='2014-12-16 12:40:30.118 CET' cloudServiceNames='StratusLab' username='cal' type='Orchestration' tags='123, asdf, bbb, d, ssd, zzz'/>
    <user issuper='true' resourceUri='user/super' name='super'></user>
  </runs>")

(expect
  {:pagination {:offset 0
                :limit 10
                :count 2
                :cloud-name "StratusLab"}
   :runs [{:status "Initializing"
           :module-uri "module/first-project/newdeployment/85"
           :cloud-names "StratusLab"
           :abort-msg "Cloud Username cannot be empty, please edit your <a href=\"/user/super\">user account</a>"
           :start-time "2015-01-14 17:23:18.262 CET"
           :uri "run/36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae"
           :username "rob"
           :uuid "36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae"
           :type :deployment-run
           :tags ""}
          {:status "Cancelled"
           :module-uri "module/calsproject/deployment/54"
           :cloud-names "StratusLab"
           :abort-msg "Marketplace endpoint should be set for StratusLab"
           :start-time "2014-12-16 12:40:30.118 CET"
           :uri "run/0aa79e3f-878a-40b1-8e88-bb6808a183cc"
           :username "cal"
           :uuid "0aa79e3f-878a-40b1-8e88-bb6808a183cc"
           :type :deployment-run
           :tags "123, asdf, bbb, d, ssd, zzz"}]}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
