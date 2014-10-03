(ns slipstream.ui.util.core-test
  (:use [expectations]
        [slipstream.ui.util.core])
  (:require [slipstream.ui.util.localization :as localization]))

(expect
  [{:selected? true} {}]
  (ensure-one-selected [{} {}]))

(expect
  [{:selected? true} {}]
  (ensure-one-selected [{:selected? true} {}]))

(expect
  [{:selected? true} {}]
  (ensure-one-selected [{:selected? false} {}]))

(expect
  [{} {:selected? true}]
  (ensure-one-selected [{} {:selected? true}]))

;; Enum utils

(def platforms
  [:centos
   :debian
   :fedora
   :opensuse
   :redhat
   :sles
   :ubuntu
   :windows
   :other])

(def expected-enum
  [{:value  "centos",   :text   "CentOS", :selected? true}
   {:value  "debian",   :text   "Debian"}
   {:value  "fedora",   :text   "Fedora"}
   {:value  "opensuse", :text   "OpenSuse"}
   {:value  "redhat",   :text   "RedHat"}
   {:value  "sles",     :text   "Sles"}
   {:value  "ubuntu",   :text   "Ubuntu"}
   {:value  "windows",  :text   "Windows"}
   {:value  "other",    :text   "Other"}])

(expect
  expected-enum
  (localization/with-lang :en
    (enum platforms)))


(def expected-enum-with-custom-selection
  [{:value  "centos",   :text   "CentOS"}
   {:value  "debian",   :text   "Debian"}
   {:value  "fedora",   :text   "Fedora", :selected? true}
   {:value  "opensuse", :text   "OpenSuse"}
   {:value  "redhat",   :text   "RedHat"}
   {:value  "sles",     :text   "Sles"}
   {:value  "ubuntu",   :text   "Ubuntu"}
   {:value  "windows",  :text   "Windows"}
   {:value  "other",    :text   "Other"}])

(expect
  expected-enum-with-custom-selection
  (localization/with-lang :en
    (-> platforms
        enum
        (enum-select :fedora))))

(expect
  expected-enum-with-custom-selection
  (localization/with-lang :en
    (-> platforms
        (enum :fedora))))

(expect
  expected-enum
  (localization/with-lang :en
    (-> platforms
        enum
        (enum-select :fedora)
        (enum-select :value-not-available))))