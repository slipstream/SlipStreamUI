(ns slipstream.ui.util.core-test
  (:use [expectations]
        [slipstream.ui.util.core])
  (:require [clojure.string :as s]
            [slipstream.ui.util.localization :as localization]))

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

;; Clojurifying raw metadata

(expect
  {:foo "bar"}
  (clojurify-raw-metadata-str "{\"foo\":\"bar\"}"))

(expect
  {:tag :a, :attrs {:href "#"}, :content ["text"]}
  (clojurify-raw-metadata-str "<a href=\"#\">text</a>"))


;; Enum utils

(def platforms
  ["centos"
   "debian"
   "fedora"
   "opensuse"
   "redhat"
   "sles"
   "ubuntu"
   "windows"
   "other"])

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
    (enum platforms :cloud-platforms)))


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
        (enum :cloud-platforms)
        (enum-select "fedora"))))

(expect
  expected-enum-with-custom-selection
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms "fedora"))))

(expect
  expected-enum
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        (enum-select "fedora")
        (enum-select "value-not-available"))))


;; t-module-category

(expect
  nil
  (localization/with-lang :en
    (t-module-category nil)))

(expect
  nil
  (localization/with-lang :en
    (t-module-category "")))

(expect
  "Image"
  (localization/with-lang :en
    (t-module-category "Image")))

(expect
  "image"
  (localization/with-lang :en
    (t-module-category "Image" s/lower-case)))

(expect
  "IMAGE"
  (localization/with-lang :en
    (t-module-category "Image" s/upper-case)))

(expect
  "THE IMAGES"
  (localization/with-lang :en
    (t-module-category "Image" (comp s/upper-case (partial format "the %ss")))))

(expect
  "Image"
  (localization/with-lang :en
    (t-module-category :image)))

(expect
  "image"
  (localization/with-lang :en
    (t-module-category :image s/lower-case)))

(expect
  "IMAGE"
  (localization/with-lang :en
    (t-module-category :image s/upper-case)))
