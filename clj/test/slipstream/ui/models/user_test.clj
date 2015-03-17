(ns slipstream.ui.models.user-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.user :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_user.xml"))

(expect
  {:username      "alice"
   :first-name    "Alice"
   :last-name     "Persona"
   :uri           "user/alice"
   :organization  "ACME"
   :email         "alice@example.com"
   :state         "ACTIVE"
   :creation      "2015-03-12 18:15:20.983 CET"
   :deleted?      false
   :super?        false
   :configuration {:available-clouds  [{:value "Cloud1", :text "Cloud1"}
                                       {:value "Cloud2", :text "Cloud2"}
                                       {:value "Cloud3", :text "Cloud3 *", :default? true, :selected? true}
                                       {:value "Cloud4", :text "Cloud4"}] ; General.default.cloud.service
                   :keep-running  :on-success    ; General.keep-running
                   :ssh-keys      "some-ssh-key" ; General.ssh.public.key
                  }}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))