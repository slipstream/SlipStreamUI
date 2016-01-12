(ns slipstream.ui.models.user-on-single-cloud-context-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.user :as model]
            [slipstream.ui.models.parameters :as parameters-model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_user_on_single_cloud_context.xml"))

(expect
  {:username      "alice"
   :github-login  nil
   :first-name    "Alice"
   :last-name     "The User"
   :uri           "user/alice"
   :organization  "ACME"
   :email         "alice@example.com"
   :state         "ACTIVE"
   :creation      "2015-04-24 19:33:47.209 CEST"
   :deleted?      false
   :super?        false
   :configuration {:configured-clouds nil
                   :available-clouds  nil  ; General.default.cloud.service
                   :keep-running      :on-success             ; General.keep-running
                   :ssh-keys          nil                     ; General.ssh.public.key
                  }}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse (dissoc :parameters))))
