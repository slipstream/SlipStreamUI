(ns slipstream.ui.util.core-test
  (:require
   [expectations :refer :all]
   [slipstream.ui.util.core :refer :all]
   [superstring.core :as s]
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
  #clojure.data.xml.Element{:tag :a, :attrs {:href "#"}, :content ["text"]}
  (clojurify-raw-metadata-str "<a href=\"#\">text</a>"))


;; Enum utils

(def some-enum
  ["value"
   "another value"
   "the last one"])

(def some-expected-enum
  [{:value  "value",   :text   "value", :selected? true}
   {:value  "another value",   :text   "another value"}
   {:value  "the last one",    :text   "the last one"}])

(expect
  some-expected-enum
  (localization/with-lang :en
    (enum some-enum :some-unlocalized-enum)))

(expect
  some-expected-enum
  (localization/with-lang :en
    (-> some-enum
        (enum :some-unlocalized-enum)
        (enum-select "value"))))

(def some-expected-enum-with-another-value-selected
  [{:value  "value",          :text "value"}
   {:value  "another value",  :text "another value", :selected? true}
   {:value  "the last one",   :text "the last one"}])

(expect
  some-expected-enum-with-another-value-selected
  (localization/with-lang :en
    (-> some-enum
        (enum :some-unlocalized-enum)
        (enum-select "another value"))))

(expect
  some-expected-enum-with-another-value-selected
  (localization/with-lang :en
    (enum some-enum :some-unlocalized-enum "another value")))

; Test with the platforms enum, which is found in the Image edit page.

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
    (enum platforms :cloud-platforms "fedora")))

(expect
  [{:value  "centos",   :text   "CentOS", :selected? true, :original-selection "value-not-available"}
   {:value  "debian",   :text   "Debian"}
   {:value  "fedora",   :text   "Fedora"}
   {:value  "opensuse", :text   "OpenSuse"}
   {:value  "redhat",   :text   "RedHat"}
   {:value  "sles",     :text   "Sles"}
   {:value  "ubuntu",   :text   "Ubuntu"}
   {:value  "windows",  :text   "Windows"}
   {:value  "other",    :text   "Other"}]
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        (enum-select "fedora")
        (enum-select "value-not-available"))))

(expect
  {:value  "fedora",   :text   "Fedora", :selected? true}
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms "fedora")
        enum-selection)))

(expect
  {:value  "debian",   :text   "Debian", :selected? true}
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms "fedora")
        (enum-select "debian")
        enum-selection)))

(expect
  [{:value  "centos",   :text   "CentOS"}
   {:value  "debian",   :text   "Debian *", :default? true}
   {:value  "fedora",   :text   "Fedora"}
   {:value  "opensuse", :text   "OpenSuse"}
   {:value  "redhat",   :text   "RedHat"}
   {:value  "sles",     :text   "Sles"}
   {:value  "ubuntu",   :text   "Ubuntu"}
   {:value  "windows",  :text   "Windows"}
   {:value  "other",    :text   "Other", :selected? true}]
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        (enum-select-default "debian")
        (enum-select "other"))))

(expect
  "other"
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        (enum-select-default "debian")
        (enum-select "other")
        enum-selection
        :value)))

(expect
  "debian"
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        (enum-select-default "debian")
        (enum-select "other")
        enum-default-option
        :value)))

(expect
  [{:value  "centos",   :text   "CentOS"}
   {:value  "debian",   :text   "Debian *", :selected? true, :default? true}
   {:value  "fedora",   :text   "Fedora"}
   {:value  "opensuse", :text   "OpenSuse"}
   {:value  "redhat",   :text   "RedHat"}
   {:value  "sles",     :text   "Sles"}
   {:value  "ubuntu",   :text   "Ubuntu"}
   {:value  "windows",  :text   "Windows"}
   {:value  "other",    :text   "Other"}]
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms "fedora")
        (enum-select-default "debian"))))

(expect
  [{:value  "centos",   :text   "CentOS"}
   {:value  "debian",   :text   "Debian *", :default? true}
   {:value  "fedora",   :text   "Fedora"}
   {:value  "opensuse", :text   "OpenSuse"}
   {:value  "other",    :text   "Other", :selected? true}
   {:value  "redhat",   :text   "RedHat"}
   {:value  "sles",     :text   "Sles"}
   {:value  "ubuntu",   :text   "Ubuntu"}
   {:value  "windows",  :text   "Windows"}
   {:value  "other",    :text   "Other"}]
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        (enum-select-default "debian")
        (enum-select "other")
        (enum-sort-by :text)
        (enum-append-option "other"))))

(expect
  {:name :cloud-platforms, :type :enum}
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms "fedora")
        (enum-select-default "debian")
        meta)))

(expect
  {:value  "debian",   :text   "Debian *", :selected? true, :default? true}
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms "fedora")
        (enum-select-default "debian")
        enum-selection)))

(expect
 "centos"
  (localization/with-lang :en
    (-> expected-enum
        enum-selection
        :value)))

(expect
  :enum
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        type)))

(expect
  {:type :enum, :name :cloud-platforms}
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        meta)))

(expect
  :enum
  (localization/with-lang :en
    (-> platforms
        (enum :cloud-platforms)
        (enum-select "fedora")
        (enum-select "value-not-available")
        type)))


;; uri

(expect
  "/user/foo"
  (user-uri "foo"))

(expect
  "/user/foo#summary"
  (user-uri "foo" :hash "summary"))

(expect
  "/user/foo?edit=true"
  (user-uri "foo" :edit true))

(expect
  "/user/foo?edit=true#summary"
  (user-uri "foo" :edit true :hash "summary"))

(expect
  "/user/foo?edit=true"
  (user-uri "foo" :edit "true"))

(expect
  "/user/foo?edit=true"
  (user-uri "foo" "edit" "true"))

(expect
  "/user/foo?action=delete&edit=true"
  (user-uri "foo" :action :delete :edit true))

(expect
  "/user/foo?action=delete&edit=true"
  (user-uri "foo" :edit true :action :delete))

(expect
  "/user/foo?action=delete&edit=true#summary"
  (user-uri "foo" :hash "summary" :edit true :action :delete))

(expect
  "/user/foo?action=delete&edit=true"
  (user-uri "foo" :edit "true" :action "delete"))

(expect
  "/user/foo?action=delete&edit=true"
  (user-uri "foo" "edit" "true" "action" "delete"))

(expect
  "/module/foo"
  (module-uri "foo"))

(expect
  "/module/foo"
  (module-uri "/foo"))

(expect
  "/module/foo"
  (module-uri "module/foo"))

(expect
  "/module/foo"
  (module-uri "/module/foo"))

(expect
  "/module/foo#summary"
  (module-uri "foo" :hash "summary"))

(expect
  "/module/foo#summary"
  (module-uri "foo" :hash ["summary"]))

(expect
  "/module/foo#image-configuration+packages"
  (module-uri "foo" :hash ["image-configuration" "packages"]))

(expect
  "/module/foo?edit=true"
  (module-uri "foo" :edit true))

(expect
  "/module/foo?edit=true#summary"
  (module-uri "foo" :edit true :hash "summary"))

(expect
  "/module/foo?edit=true"
  (module-uri "foo" :edit "true"))

(expect
  "/module/foo?edit=true"
  (module-uri "foo" "edit" "true"))

(expect
  "/module/foo?action=run&edit=true"
  (module-uri "foo" :action :run :edit true))

(expect
  "/module/foo?action=run&edit=true#summary"
  (module-uri "foo" :hash "summary" :edit true :action :run))

(expect
  "/module/foo?action=run&edit=true#summary"
  (module-uri "foo" :hash ["summary"] :edit true :action :run))

(expect
  "/module/foo?action=run&edit=true#image-configuration+packages"
  (module-uri "foo" :hash ["image-configuration" "packages"] :edit true :action :run))

(expect
  "/module/foo?action=run&edit=true"
  (module-uri "foo" :edit "true" :action "run"))

(expect
  "/module/foo?action=run&edit=true"
  (module-uri "foo" "edit" "true" "action" "run"))


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
  "Application Component"
  (localization/with-lang :en
    (t-module-category "Image")))

(expect
  "application component"
  (localization/with-lang :en
    (t-module-category "Image" s/lower-case)))

(expect
  "APPLICATION COMPONENT"
  (localization/with-lang :en
    (t-module-category "Image" s/upper-case)))

(expect
  "THE APPLICATION COMPONENTS"
  (localization/with-lang :en
    (t-module-category "Image" (comp s/upper-case (partial format "the %ss")))))

(expect
  "Application Component"
  (localization/with-lang :en
    (t-module-category :image)))

(expect
  "application component"
  (localization/with-lang :en
    (t-module-category :image s/lower-case)))

(expect
  "APPLICATION COMPONENT"
  (localization/with-lang :en
    (t-module-category :image s/upper-case)))
