(defproject com.sixsq.slipstream/SlipStreamUI "1.0-SNAPSHOT"
  :description "SlipStream UI"
  :url "http://sixsq.com"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [enlive "1.1.4"]
                 [ring "1.1.8"]
                 [expectations "2.0.6"]
                 [net.cgrand/moustache "1.1.0"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]
                 [org.clojure/clojurescript "0.0-2173"]]
  :plugins [[lein-expectations "0.0.7"]
            [lein-autoexpect "1.0"]
            [lein-cljsbuild "1.0.2"]]

  :source-paths ["cljs/src"]
  :target-path "cljs/target"

  :cljsbuild { 
    :builds [{:id "slipstream-ui"
              :source-paths ["cljs/src"]
              :compiler {
                :output-to "cljs/slipstream.js"
                :output-dir "cljs/out"
                :optimizations :none
                :source-map true}}]}
  :aot [slipstream.ui.views.representation])
