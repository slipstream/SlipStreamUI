(ns slipstream.ui.util.current-user-test
  (:use [expectations])
  (:require [slipstream.ui.util.current-user :as current-user :refer [with-user-from-metadata]]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.dashboard-test :as dashboard]
            [slipstream.ui.models.welcome-test :as welcome]))

(def user-metadata
  (-> "slipstream/ui/mockup_data/metadata_user.xml"
       uc/slurp-resource
       u/clojurify-raw-metadata-str))

(expect
  ["bob" "user/bob" true]
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
          (vector
            (current-user/username)
            (current-user/uri)
            (current-user/super?))))))

(expect
  [true true]
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
          (vector
            (current-user/is? "bob")
            (current-user/is? {:username "bob"}))))))

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
   :configuration {:configured-clouds nil
                   :available-clouds  [{:value "Cloud1", :configured? false, :disabled? true, :text "Cloud1 (not configured)"}
                                       {:value "Cloud2", :configured? false, :disabled? true, :text "Cloud2 (not configured)"}
                                       {:value "Cloud3", :configured? false, :disabled? true, :text "Cloud3 (not configured) *", :default? true, :selected? true, :original-selection "CloudNotInTheList"}
                                       {:value "Cloud4", :configured? false, :disabled? true, :text "Cloud4 (not configured)"}] ; General.default.cloud.service
                   :keep-running      :on-success ; General.keep-running
                   :ssh-keys          nil         ; General.ssh.public.key
                  }}
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (dissoc (current-user/get) :parameters)))))

(expect
  "2015-03-13 11:22:59.220 CET"
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/get :creation)))))

(expect
  [{:value "Cloud1", :configured? false, :disabled? true, :text "Cloud1 (not configured)"}
   {:value "Cloud2", :configured? false, :disabled? true, :text "Cloud2 (not configured)"}
   {:value "Cloud3", :configured? false, :disabled? true, :text "Cloud3 (not configured) *", :default? true, :selected? true, :original-selection "CloudNotInTheList"}
   {:value "Cloud4", :configured? false, :disabled? true, :text "Cloud4 (not configured)"}]
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/get-in [:configuration :available-clouds])))))

(expect
  {:configured-clouds nil
   :available-clouds  [{:value "Cloud1", :configured? false, :disabled? true, :text "Cloud1 (not configured)"}
                       {:value "Cloud2", :configured? false, :disabled? true, :text "Cloud2 (not configured)"}
                       {:value "Cloud3", :configured? false, :disabled? true, :text "Cloud3 (not configured) *", :default? true, :selected? true, , :original-selection "CloudNotInTheList"}
                       {:value "Cloud4", :configured? false, :disabled? true, :text "Cloud4 (not configured)"}]
   :keep-running  :on-success
   :ssh-keys      nil}
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/configuration)))))

(expect
  [{:value "Cloud1", :configured? false, :disabled? true, :text "Cloud1 (not configured)"}
   {:value "Cloud2", :configured? false, :disabled? true, :text "Cloud2 (not configured)"}
   {:value "Cloud3", :configured? false, :disabled? true, :text "Cloud3 (not configured) *", :default? true, :selected? true, :original-selection "CloudNotInTheList"}
   {:value "Cloud4", :configured? false, :disabled? true, :text "Cloud4 (not configured)"}]
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (current-user/configuration :available-clouds)))))

(expect
  {:type :enum, :name :available-clouds}
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (-> :available-clouds
            current-user/configuration
            meta)))))

(expect
  "Cloud3"
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (-> :available-clouds
            current-user/configuration
            u/enum-selection
            :value)))))

(expect
  "Cloud3"
  (let [metadata user-metadata]
    (localization/with-lang :en
      (with-user-from-metadata
        (-> :available-clouds
            current-user/configuration
            u/enum-default-option
            :value)))))
