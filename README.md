# SlipStream is now deprecated -> replaced by https://github.com/nuvla

# SlipStreamUI

This is a sub-repo of [SlipStream](https://github.com/slipstream/SlipStream). Go to the main repo for general
information about the project.

## Enlive

To generate the HTML UI, SlipStream uses Cristophe Grand's templating
framework "Enlive". You might want to check out following
documentation:

  - <https://github.com/cgrand/enlive>
  - <https://github.com/swannodette/enlive-tutorial>

## Static pages

For development and testing purposes there is the possibility to
generate static examples of the different pages of the SlipStream
application, i.e. without the need to couple it with a SlipStream
Server and real data.

To launch the static pages first clone this project and `cd` into its
folder:

``` bash
git clone git@github.com:slipstream/SlipStreamUI.git
cd SlipStreamUI
```

In this folder you will find a basic [Makefile](https://github.com/slipstream/SlipStreamUI/blob/master/Makefile) with the only
purpose of gathering a useful set of terminal commands to launch
common tasks. To launch the static pages we need a clojure [REPL](https://en.wikipedia.org/wiki/Read–eval–print_loop), which can be
started with `make repl`. Then enter `(reload-headless-app)` and open
`http://localhost:8082/`, both actions suggested in the prompt. You
should see something like the following output:

```
$ make repl
(cd clj; lein repl)

nREPL server started on port 59380 on host 127.0.0.1 - nrepl://127.0.0.1:59380
REPL-y 0.3.5, nREPL 0.2.6
Clojure 1.7.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_45-b14

      Docs: (doc function-name-here)
            (find-doc "part-of-name-here")
    Source: (source function-name-here)
   Javadoc: (javadoc java-object-or-class-here)
      Exit: Control+D or (exit) or (quit)
   Results: Stored in vars *1, *2, *3, an exception in *e

SlipStream: (reload-headless-app) to start the headless test server.

user=> (reload-headless-app)
run-server* [slipstream.ui.main]
2015-07-13 18:58:28.222:INFO:oejs.Server:jetty-7.6.1.v20120215
2015-07-13 18:58:28.259:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:8082

The headless test server has started successfully on port 8082.
Go to following URL for a list of existent test pages:

  http://localhost:8082/

If code changes (in clojure or HTML) are not taken into account, just reload the headless app.
#object[org.eclipse.jetty.server.Server 0x54ccb9a7 "org.eclipse.jetty.server.Server@54ccb9a7"]
user=>
```

Opening the URL mentioned above will show a list of the used HTML
templates and the available static pages. Clicking them will open the
corresponding page in a new tab.
