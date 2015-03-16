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
   :configuration {:cloud     "Cloud3"       ; General.default.cloud.service
                   :ssh-keys  "some-ssh-key" ; General.ssh.public.key
                  }}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse)))