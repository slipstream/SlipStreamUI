run: clean install

test: clean
	mvn clojure:test 2>&1 | grep -v "at clojure."

expect:
	$(info )
	$(info After creating a new clj file, quit (CTRL-C) and reload.)
	$(info )
	(cd clj; lein autoexpect)

expectandnotify:
	$(info )
	$(info After creating a new clj file, quit (CTRL-C) and reload.)
	$(info )
	(cd clj; lein autoexpect :notify)

repl:
	$(info )
	$(info Execute following forms to start the test server:)
	$(info ;  (require '[slipstream.ui.main :as s] :reload-all) @s/run-test-server)
	$(info )
	$(info If the test server starts correctly, you'll find test pages at port 8082.)
	$(info Go to following URL for a list of existent test pages:)
	$(info )
	$(info  - http://localhost:8082/ )
	$(info )
	$(info If you change the enlive code, just reload the concerned namespace (or the main one as above).)
	$(info If you change the HTML templates, you'll have to restart the server, i.e. the REPL.)
	$(info )
	(cd clj; lein repl)

compile:
	@mvn compile -Dmaven.test.skip=true 2>&1 | egrep -v "at clojure.|^\[\w*\] " | sed \
	  -e "s/^\(.*\[ERROR\].*\)/$(shell tput setaf 1)\1$(shell tput sgr0)/" \
	  -e "s/^\(.*\[WARNING\].*\)/$(shell tput setaf 3)\1$(shell tput sgr0)/" \
	  -e "s/^\(.*at clojure.*\)/$(shell tput setaf 4)\1$(shell tput sgr0)/" \
	  -e "s/^.*\(at slipstream.*\)(\(\(.*\)\:\([0-9]*\)\))/$(shell tput bold)$(shell tput setaf 1)$(shell tput smso)  >>>>> $(shell tput rmso)\1 $(shell tput smso)\2$(shell tput rmso)$(shell tput setaf 3) => subl \$$(find . -path '*src*\3' | head -1):\4$(shell tput sgr0)/" \
	  -e "s/^Exception in[^:]*: \(.*compiling:(\(slipstream.*\))\)/$(shell tput bold)$(shell tput setaf 1)\1$(shell tput setaf 3) => subl clj\/src\/\2$(shell tput sgr0)/" \
	  -e "s/^Exception in[^:]*: \(.*compiling:(\([^:]*\)\(:[0-9:]*\))\)/$(shell tput bold)$(shell tput setaf 1)\1$(shell tput setaf 3) => subl \$$(find . -path '*src*\2' | head -1)\3$(shell tput sgr0)/"

install:
	@mvn install -Dmaven.test.skip=true 2>&1 | egrep -v "at clojure.|^\[\w*\] " | sed \
	  -e "s/^\(.*\[ERROR\].*\)/$(shell tput setaf 1)\1$(shell tput sgr0)/" \
	  -e "s/^\(.*\[WARNING\].*\)/$(shell tput setaf 3)\1$(shell tput sgr0)/" \
	  -e "s/^\(.*at clojure.*\)/$(shell tput setaf 4)\1$(shell tput sgr0)/" \
	  -e "s/^.*\(at slipstream.*\)(\(\(.*\)\:\([0-9]*\)\))/$(shell tput bold)$(shell tput setaf 1)$(shell tput smso)  >>>>> $(shell tput rmso)\1 $(shell tput smso)\2$(shell tput rmso)$(shell tput setaf 3) => subl \$$(find . -path '*src*\3' | head -1):\4$(shell tput sgr0)/" \
	  -e "s/^Exception in[^:]*: \(.*compiling:(\(slipstream.*\))\)/$(shell tput bold)$(shell tput setaf 1)\1$(shell tput setaf 3) => subl clj\/src\/\2$(shell tput sgr0)/" \
	  -e "s/^Exception in[^:]*: \(.*compiling:(\([^:]*\)\(:[0-9:]*\))\)/$(shell tput bold)$(shell tput setaf 1)\1$(shell tput setaf 3) => subl \$$(find . -path '*src*\2' | head -1)\3$(shell tput sgr0)/"

clean:
	mvn clean

