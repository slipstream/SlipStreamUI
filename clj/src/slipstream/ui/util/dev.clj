(ns slipstream.ui.util.dev
  "Util functions for dev purposes, not prod.")

(def ^:dynamic *dev?* false)

(defmacro with-dev-environment
  [& body]
  `(binding [*dev?* true]
     ~@body))
