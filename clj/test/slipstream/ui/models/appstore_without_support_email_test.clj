(ns slipstream.ui.models.appstore-without-support-email-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.module-list :as model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_appstore.xml"))

(def parsed-metadata
  [{:description "Deployment of a WordPress appliance"
    :name "wordpress-deployment"
    :version "4"
    :uri "module/example/wordpress-deployment/4"
    :image "https://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png"
    :publisher "bob"}])


(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :published-apps))
