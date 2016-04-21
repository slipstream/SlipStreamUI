(defproject com.sixsq.slipstream/SlipStreamUI "3.3-SNAPSHOT"
  :description "SlipStream UI Clojure/Enlive"
  :url "http://sixsq.com"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [superstring "2.1.0"]
                 [enlive "1.1.4"]
                 [ring "1.1.8"]
                 [expectations "2.1.1"]
                 [net.cgrand/moustache "1.1.0"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]
                 [com.taoensso/tower "3.0.2"]
                 ;; Chosing clj-json against cheshire or clojure.data.json for performace.
                 ;; Source: http://stackoverflow.com/a/21528391
                 [clj-json "0.5.3"]
                 [org.clojure/data.xml "0.0.8"]
                 [clj-time "0.8.0"]]
  :plugins [[lein-expectations  "0.0.8"]
            [lein-autoexpect    "1.4.2"]
            [jonase/eastwood    "0.2.1"]
            [lein-cloverage     "1.0.3"]]

  :source-paths ["src"]
  :target-path "target/%s/"

  :resource-paths ["resources"]

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
                 }

  ; :aot [slipstream.ui.views.representation
  ;       slipstream.ui.config]
  )
