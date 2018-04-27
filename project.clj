(def +version+ "3.51-SNAPSHOT")

(defproject com.sixsq.slipstream/SlipStreamUI "3.51-SNAPSHOT"

  :description "Legacy Web Browser Interface"

  :url "https://github.com/slipstream/SlipStreamUI"

  :license {:name "Apache 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.txt"
            :distribution :repo}

  :plugins [[lein-parent "0.3.2"]
            [lein-expectations "0.0.8"]]

  :parent-project {:coords  [sixsq/slipstream-parent "5.1.1"]
                   :inherit [:min-lein-version
                             :managed-dependencies
                             :repositories
                             :deploy-repositories]}

  :source-paths ["clj/src"]

  :resource-paths ["clj/resources"]

  :pom-location "target/"

  :aot :all

  :dependencies
  [[org.clojure/clojure]

   [com.taoensso/tower]
   [clj-http]
   [clj-time]
   [enlive]
   [org.clojure/data.json]
   [org.clojure/data.xml]
   [superstring]
   [org.clojure/tools.reader]
   [com.taoensso/timbre]]

  :profiles
  {:test
   {:dependencies [[expectations]
                   [javax.servlet/javax.servlet-api]
                   [net.cgrand/moustache]
                   [ring]]
    :source-paths ["clj/test" "src/test"]}
   :dev
   {:dependencies [[expectations]
                   [javax.servlet/javax.servlet-api]
                   [net.cgrand/moustache]
                   [ring]]
    :source-paths ["clj/src" "clj/test"]
    :repl-options {;; What to print when the repl session starts.
                   :welcome
                         (println (str
                                    ;; These first lines are the default ones:
                                    "\n"
                                    "      Docs: (doc function-name-here)\n"
                                    "            (find-doc \"part-of-name-here\")\n"
                                    "    Source: (source function-name-here)\n"
                                    "   Javadoc: (javadoc java-object-or-class-here)\n"
                                    "      Exit: Control+D or (exit) or (quit)\n"
                                    "   Results: Stored in vars *1, *2, *3, an exception in *e\n"
                                    "\n"
                                    ;; This line is related to the SlipStream project:
                                    "SlipStream: (reload-headless-app) to start the headless test server.\n"))
                   ;; This expression will run when first opening a REPL
                   :init (defmacro reload-headless-app
                           [& {:keys [port]}]
                           `(do
                              (require '[slipstream.ui.main :as s] :reload-all)
                              (slipstream.ui.main/reload-headless-app ~@(when port `(:port ~port)))))
                   }}
   })
