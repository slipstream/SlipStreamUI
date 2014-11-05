(ns slipstream.ui.util.dev
  "Util functions for dev purposes, not prod.")

;; TODO: Use it for localization.clj:58
(def ^:dynamic *dev?* false)

(defmacro with-dev-environment
  [& body]
  `(binding [*dev?* true]
     ~@body))
