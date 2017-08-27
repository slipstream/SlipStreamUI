(defproject
  com.sixsq.slipstream/SlipStreamUI
  "3.29-SNAPSHOT"
  :license
  {"Apache 2.0" "http://www.apache.org/licenses/LICENSE-2.0.txt"}
  :repositories
  [["clojars" {:url "https://repo.clojars.org/"}]
   ["maven-central" {:url "https://repo1.maven.org/maven2"}]
   ["sixsq"
    {:url
     "http://nexus.sixsq.com/content/repositories/snapshots-community-rhel7",
     :username "admin",
     :password "siXsQ2013"}]]
  :dependencies
  [[org.clojure/clojure "1.9.0-alpha19"]
   [sixsq/build-utils "0.1.4" :scope "test"]
   [org.clojure/clojure "1.9.0-alpha19"]
   [com.taoensso/tower "3.1.0-beta5"]
   [clj-http "3.3.0"]
   [clj-time "0.12.1"]
   [enlive "1.1.6"]
   [org.clojure/data.json "0.2.6"]
   [org.clojure/data.xml "0.0.8"]
   [superstring "2.1.0"]
   [com.taoensso/timbre "4.7.4"]
   [expectations "2.1.9" :scope "test"]
   [javax.servlet/javax.servlet-api "3.1.0" :scope "test"]
   [net.cgrand/moustache "1.1.0" :scope "test"]
   [ring "1.5.1" :scope "test"]
   [adzerk/boot-test "1.2.0" :scope "test"]
   [adzerk/boot-reload "0.5.1" :scope "test"]
   [onetom/boot-lein-generate "0.1.3" :scope "test"]
   [tolitius/boot-check "0.1.4" :scope "test"]]
  :source-paths
  ["clj/test"]
  :resource-paths
  ["clj/src" "clj/resources"])