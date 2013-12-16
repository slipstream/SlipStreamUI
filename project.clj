(defproject com.sixsq.slipstream/SlipStreamUI "1.0-SNAPSHOT"
  :description "SlipStream UI"
  :url "http://sixsq.com"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [enlive "1.1.4"]
                 [ring "1.1.8"]
                 [net.cgrand/moustache "1.1.0"]]
  :aot [slipstream.ui.views.representation])
