(ns slipstream.ui.models.runs-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.runs :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_runs.xml"))

(expect
  {:pagination {:offset 0
                :limit 20
                :count-shown 20
                :count-total 26
                :cloud-name nil}
 :runs [{:status "Initializing"
         :display-status :run-in-transitional-state
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-03-02 18:50:18.263 CET"
         :uri "run/6269f657-3311-402b-a71b-528ef76fea05"
         :username "alice"
         :uuid "6269f657-3311-402b-a71b-528ef76fea05"
         :type :deployment-run
         :service-url nil
         :abort-flag? false
         :terminable? true
         :tags "tag-initializing-without-abort-flag"}

        {:status "Initializing"
         :display-status :run-with-abort-flag-set
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a>"
         :start-time "2015-02-09 11:37:28.811 CET"
         :uri "run/53ea9de1-fff2-44d7-b053-f4fd4307b53a"
         :username "alice"
         :uuid "53ea9de1-fff2-44d7-b053-f4fd4307b53a"
         :type :deployment-run
         :service-url nil
         :abort-flag? true
         :terminable? true
         :tags "tag-initializing-with-abort-flag"}

        {:status "Provisioning"
         :display-status :run-in-transitional-state
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-02-06 11:12:13.486 CET"
         :uri "run/e4a03073-e964-4e20-a3ab-ae0bf06c6354"
         :username "alice"
         :uuid "e4a03073-e964-4e20-a3ab-ae0bf06c6354"
         :type :deployment-run
         :service-url nil
         :abort-flag? false
         :terminable? true
         :tags "tag-provisioning-without-abort-flag"}

        {:status "Provisioning"
         :display-status :run-with-abort-flag-set
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a>"
         :start-time "2015-02-05 14:43:10.205 CET"
         :uri "run/b8dd73f1-c673-451b-b95c-33fbd6b550ea"
         :username "alice"
         :uuid "b8dd73f1-c673-451b-b95c-33fbd6b550ea"
         :type :image-build
         :service-url nil
         :abort-flag? true
         :terminable? true
         :tags "tag-provisioning-with-abort-flag"}

        {:status "Executing"
         :display-status :run-in-transitional-state
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-02-05 14:43:02.430 CET"
         :uri "run/ec745e78-be4b-43a7-83fa-6536c9d7fc92"
         :username "alice"
         :uuid "ec745e78-be4b-43a7-83fa-6536c9d7fc92"
         :type :image-build
         :service-url nil
         :abort-flag? false
         :terminable? true
         :tags "tag-executing-without-abort-flag"}

        {:status "Executing"
         :display-status :run-with-abort-flag-set
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a>"
         :start-time "2015-02-05 14:42:57.113 CET"
         :uri "run/60d038d2-4069-41b5-a747-99d901f426cf"
         :username "alice"
         :uuid "60d038d2-4069-41b5-a747-99d901f426cf"
         :type :image-build
         :service-url nil
         :abort-flag? true
         :terminable? true
         :tags "tag-executing-with-abort-flag"}

        {:status "Sending reports"
         :display-status :run-in-transitional-state
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-02-05 14:42:51.593 CET"
         :uri "run/d0ac87b8-d150-40b1-9bbb-1c8cbaf90bad"
         :username "alice"
         :uuid "d0ac87b8-d150-40b1-9bbb-1c8cbaf90bad"
         :type :image-build
         :service-url nil
         :abort-flag? false
         :terminable? true
         :tags "tag-sending-reports-without-abort-flag"}

        {:status "Sending reports"
         :display-status :run-with-abort-flag-set
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a>"
         :start-time "2015-02-05 14:42:43.866 CET"
         :uri "run/5151dc47-4bb5-4b72-990e-ab6cbd565cfc"
         :username "alice"
         :uuid "5151dc47-4bb5-4b72-990e-ab6cbd565cfc"
         :type :image-build
         :service-url nil
         :abort-flag? true
         :terminable? true
         :tags "tag-sending-reports-with-abort-flag"}

        {:status "Ready"
         :display-status :run-successfully-ready
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-02-05 14:42:38.369 CET"
         :uri "run/c03075a0-da00-42bf-a5ad-41d13170e64e"
         :username "alice"
         :uuid "c03075a0-da00-42bf-a5ad-41d13170e64e"
         :type :image-build
         :service-url "http://10.0.0.1:8080"
         :abort-flag? false
         :terminable? true
         :tags "tag-ready-without-abort-flag"}

        {:status "Ready"
         :display-status :run-with-abort-flag-set
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a>"
         :start-time "2015-02-05 14:42:32.591 CET"
         :uri "run/3b559cb9-1769-4c6d-aca8-9c5aea9c1662"
         :username "alice"
         :uuid "3b559cb9-1769-4c6d-aca8-9c5aea9c1662"
         :type :image-build
         :service-url "http://10.0.0.1:8080"
         :abort-flag? true
         :terminable? true
         :tags "tag-ready-with-abort-flag"}

        {:status "Finalizing"
         :display-status :run-in-transitional-state
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-02-05 14:42:25.603 CET"
         :uri "run/5842acd7-ce29-47ef-8f21-403e10db11ed"
         :username "alice"
         :uuid "5842acd7-ce29-47ef-8f21-403e10db11ed"
         :type :image-build
         :service-url nil
         :abort-flag? false
         :terminable? false
         :tags "tag-finalizing-without-abort-flag"}

        {:status "Finalizing"
         :display-status :run-with-abort-flag-set
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a> b9638f6cj-d19b0jd4e6-2jdb2f6jd-498843e7aa7e6869be33jd9fe4jd4c80jdbbb1jd1665f6bad443ab7953e9jd2812-jd4591jdbc8fjd194a95b9f70ed3caa272jd97a7jd4a25jd8a2ajdb13f3fd16851eb8eb8aajd163ejd4a87jd86a2jdafb2a4eb3982"
         :start-time "2015-02-04 14:58:54.543 CET"
         :uri "run/555dccf8-4312-4328-8c70-b01e20a9ffa8"
         :username "alice"
         :uuid "555dccf8-4312-4328-8c70-b01e20a9ffa8"
         :type :deployment-run
         :service-url nil
         :abort-flag? true
         :terminable? false
         :tags "tag-finalizing-with-very-long-abort-flag"}

        {:status "Done"
         :display-status :run-terminated
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-02-04 11:46:09.704 CET"
         :uri "run/4589cc93-4c28-4362-873a-3dc84a3be58e"
         :username "alice"
         :uuid "4589cc93-4c28-4362-873a-3dc84a3be58e"
         :type :deployment-run
         :service-url nil
         :abort-flag? false
         :terminable? false
         :tags "tag-done-without-abort-flag"}

        {:status "Done"
         :display-status :run-with-abort-flag-set
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a>"
         :start-time "2015-02-03 13:36:35.374 CET"
         :uri "run/07596eb7-f1f7-4e45-8561-5dbbc55cc817"
         :username "alice"
         :uuid "07596eb7-f1f7-4e45-8561-5dbbc55cc817"
         :type :deployment-run
         :service-url nil
         :abort-flag? true
         :terminable? false
         :tags "tag-done-with-abort-flag"}

        {:status "Aborted"
         :display-status :run-terminated
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2015-01-20 11:06:13.675 CET"
         :uri "run/8ded8e5b-46ad-4bec-bcff-d761155f0e2e"
         :username "alice"
         :uuid "8ded8e5b-46ad-4bec-bcff-d761155f0e2e"
         :type :deployment-run
         :service-url nil
         :abort-flag? false
         :terminable? false
         :tags "tag-aborted-without-abort-flag"}

        {:display-status :run-terminated
         :module-uri "module/first-project/newdeployment/24"
         :username "alice"
         :status "Aborted"
         :cloud-names "StratusLab"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a> b9638f6cjd19b0jd4e62jdb2f6jd498843e7aa7e6869be33jd9fe4jd4c80jdbbb1jd1665f6bad443ab7953e9jd2812jd4591jdbc8fjd194a95b9f70ed3caa272jd97a7jd4a25jd8a2ajdb13f3fd16851eb8eb8aajd163ejd4a87jd86a2jdafb2a4eb3982"
         :start-time "2015-01-14 17:23:18.262 CET"
         :uri "run/36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae"
         :uuid "36be1b6b-d4f1-4d99-9a0f-1e65fe4bbaae"
         :type :deployment-run
         :service-url nil
         :abort-flag? true
         :terminable? false
         :tags "tag-aborted-with-very-long-abort-flag"}

        {:status "Cancelled"
         :display-status :run-terminated
         :module-uri "module/first-project/newdeployment/24"
         :cloud-names "Exoscale"
         :abort-msg nil
         :username "alice"
         :type :image-build
         :service-url nil
         :abort-flag? false
         :terminable? false
         :tags "tag-cancelled-without-abort-flag"
         :start-time "2015-01-07 13:36:32.26 CET"
         :uri "run/b36dfcc0-b77b-4746-b7fd-0acd7038a02d"
         :uuid "b36dfcc0-b77b-4746-b7fd-0acd7038a02d"}

        {:status "Cancelled"
         :display-status :run-terminated
         :module-uri "module/first-project/newdeployment/24"
         :username "alice"
         :type :deployment-run
         :service-url nil
         :abort-flag? true
         :cloud-names "StratusLab"
         :abort-msg "Marketplace endpoint should be set for StratusLab"
         :start-time "2014-12-16 12:40:30.118 CET"
         :uri "run/0aa79e3f-878a-40b1-8e88-bb6808a183cc"
         :uuid "0aa79e3f-878a-40b1-8e88-bb6808a183cc"
         :terminable? false
         :tags "tag-cancelled-with-abort-flag-1"}

        {:display-status :run-terminated
         :module-uri "module/first-project/newdeployment/24"
         :username "alice"
         :status "Cancelled"
         :cloud-names "Exoscale"
         :abort-msg nil
         :start-time "2014-12-15 01:03:45.240 CET"
         :uri "run/079dd5c1-85af-4387-95a7-b24fc0b2be53"
         :uuid "079dd5c1-85af-4387-95a7-b24fc0b2be53"
         :type :image-build
         :service-url nil
         :abort-flag? false
         :terminable? false
         :tags "tag-cancelled-without-abort-flag"}

        {:status "Cancelled"
         :display-status :run-terminated
         :module-uri "module/first-project/newdeployment/24"
         :username "alice"
         :type :deployment-run
         :service-url nil
         :abort-flag? true
         :terminable? false
         :tags "tag-cancelled-with-very-long-abort-flag"
         :cloud-names "Exoscale"
         :abort-msg "Cloud Username cannot be empty, please edit your <a href='/user/bob'> user account</a> b9638f6cjd19b0jd4e62jdb2f6jd498843e7aa7e6869be33jd9fe4jd4c80jdbbb1jd1665f6bad443ab7953e9jd2812jd4591jdbc8fjd194a95b9f70ed3caa272jd97a7jd4a25jd8a2ajdb13f3fd16851eb8eb8aajd163ejd4a87jd86a2jdafb2a4eb3982"
         :start-time "2014-12-10 14:00:00.110 CET"
         :uri "run/672c2946-578e-4093-8d7a-af3e0f432bac"
         :uuid "672c2946-578e-4093-8d7a-af3e0f432bac"}

        ]}
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
