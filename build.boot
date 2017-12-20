(def +version+ "3.42-SNAPSHOT")

(set-env!
  :project 'com.sixsq.slipstream/SlipStreamUI

  :version +version+
  :license {"Apache 2.0" "http://www.apache.org/licenses/LICENSE-2.0.txt"}
  :edition "community"

  :dependencies '[[org.clojure/clojure "1.9.0"]
                  [sixsq/build-utils "0.1.4" :scope "test"]])

(require '[sixsq.build-fns :refer [merge-defaults
                                   sixsq-nexus-url]])

(set-env!
  :repositories
  #(reduce conj % [["sixsq" {:url (sixsq-nexus-url)}]])

  :dependencies
  #(vec (concat %
                (merge-defaults
                 ['sixsq/default-deps (get-env :version)]
                 '[[org.clojure/clojure]

                   [com.taoensso/tower]
                   [clj-http]
                   [clj-time]
                   [enlive]
                   [org.clojure/data.json]
                   [org.clojure/data.xml]
                   [superstring]
                   [org.clojure/tools.reader]
                   [com.taoensso/timbre]
                   
                   ;; test dependencies
                   [expectations]
                   [javax.servlet/javax.servlet-api nil :scope "test"]
                   [net.cgrand/moustache nil :scope "test"]
                   [ring nil :scope "test"]

                   ;; boot tasks
                   [adzerk/boot-test]
                   [adzerk/boot-reload]
                   [onetom/boot-lein-generate]
                   [tolitius/boot-check]]))))

(require
  '[adzerk.boot-test :refer [test]]
  '[adzerk.boot-reload :refer [reload]]
  '[tolitius.boot-check :refer [with-yagni
                                with-eastwood
                                with-kibit
                                with-bikeshed]]
  '[boot.lein :refer [generate]])

(set-env!
  :source-paths #{"clj/test"}
  :resource-paths #{"clj/src" "clj/resources"})

(task-options!
  pom {:project (get-env :project)
       :version (get-env :version)}
  test {:junit-output-to ""}
  install {:pom (str (get-env :project))}
  push {:pom (str (get-env :project))
        :repo "sixsq"})

(deftask run-tests
         "runs all tests and performs full compilation"
         []
         (comp
           (test)
           (aot :all true)))

(deftask build []
         (comp
           (pom)
           (sift :include #{#".*/main\.clj"
                            #".*/utils\.clj"
                            #".*/*test\.clj"
                            #".*/localization_test_helper\.clj"
                            #".*/test_config\.clj"
                            #".*/log4j\.properties"
                            #".*/*\.pem"
                            #".*/metadata*\.xml"
                            #".*/metadata*\.json"
                            #".*/metrics*\.json"}
                 :invert true)
           (aot :all true)
           (jar)))

(deftask mvn-test
         "run all tests of project"
         []
         (run-tests))

(deftask mvn-build
         "build full project through maven"
         []
         (comp
           (build)
           (install)
           (if (= "true" (System/getenv "BOOT_PUSH"))
             (push)
             identity)))
