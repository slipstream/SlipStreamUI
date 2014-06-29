(defproject com.sixsq.slipstream/SlipStreamUI "1.0-SNAPSHOT"
  :description "SlipStream UI ClojureScript"
  :url "http://sixsq.com"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [expectations "2.0.6"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]
                 [org.clojure/clojurescript "0.0-2173"]]
  :plugins [[lein-expectations "0.0.7"]
            [lein-autoexpect "1.0"]
            [lein-cljsbuild "1.0.2"]]

  :source-paths ["src"]
  :target-path "../target/classes/slipstream/ui/views/js/"

  :cljsbuild { 
    :builds [{:id "slipstream-ui-cljs"
              :source-paths ["src"]
              :compiler {
                :output-to "../target/classes/slipstream/ui/views/js/slipstream.js"
                :output-dir "../target/classes/slipstream/ui/views/js/"
                :optimizations :none
                :source-map true}}]})
