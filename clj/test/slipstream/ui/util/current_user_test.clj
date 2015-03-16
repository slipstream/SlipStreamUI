(ns slipstream.ui.util.current-user-test
  (:use [expectations])
  (:require [slipstream.ui.util.current-user :as current-user :refer [with-user-from-metadata]]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.dashboard-test :as dashboard]
            [slipstream.ui.models.welcome-test :as welcome]))

(def dashboard-metadata
  (u/clojurify-raw-metadata-str dashboard/raw-metadata-str))

(expect
  nil
  (let [metadata dashboard-metadata]
    (current-user/username)))

(expect
  ["super" "user/super" true]
  (let [metadata dashboard-metadata]
    (with-user-from-metadata
          (vector
            (current-user/username)
            (current-user/uri)
            (current-user/super?)))))

(def welcome-metadata
  (u/clojurify-raw-metadata-str welcome/raw-metadata-str))

(expect
  ["meb" "user/meb" true]
  (let [metadata welcome-metadata]
    (with-user-from-metadata
          (vector
            (current-user/username)
            (current-user/uri)
            (current-user/super?)))))

(expect
  [true true]
  (let [metadata welcome-metadata]
    (with-user-from-metadata
          (vector
            (current-user/is? "meb")
            (current-user/is? {:username "meb"})))))

(def user-metadata
  (-> "slipstream/ui/mockup_data/metadata_user.xml"
       uc/slurp-resource
       u/clojurify-raw-metadata-str))

(expect
  {:username      "bob"
   :first-name    "Bob"
   :last-name     "O'Manager"
   :uri           "user/bob"
   :organization  "Example Corp"
   :email         "bob@example.com"
   :state         "ACTIVE"
   :creation      "2015-03-13 11:22:59.220 CET"
   :deleted?      false
   :super?        true
   :configuration {:cloud     "Cloud2"  ; General.default.cloud.service
                   :ssh-keys  nil       ; General.ssh.public.key
                  }}
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/get)))))

(expect
  "2015-03-13 11:22:59.220 CET"
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/get :creation)))))

(expect
  "Cloud2"
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/get-in [:configuration :cloud])))))

(expect
  {:cloud     "Cloud2"
   :ssh-keys  nil}
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/configuration)))))

(expect
  "Cloud2"
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/configuration :cloud)))))
