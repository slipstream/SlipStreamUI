(ns slipstream.ui.util.mode
  "Utils for different modes of how the UI can run.")

(def ^:private available-modes
  "Flags available for different modes of how the UI can run. These flags might
  be useful to prevent debug code from being evaluated in a production
  environment using the when-* macros below. They can be setted when launching
  the application with the environment variable 'slipstream.ui.util.dev.mode'."
  #{:prod     ; UI working with the whole server part, as intented for production.
    :dev      ; UI working with the whole server part, with additional loggging and
              ; debuging information.
    })

(def ^:private ^:dynamic *mode*
  (or
    (-> "slipstream.ui.util.dev.mode" System/getProperty keyword)
    :prod))

(def ^:private ^:dynamic *headless?*
  "For dev and testing purposes, the UI can be launched without the server part
  (headless) with 'slipstream.ui.main/run-test-server. In that mode, there is no
  SlipStreamServer involved and the views render the same mockup data which is
  used for the unit tests. This mode can not be set externally with the environment
  variable 'slipstream.ui.util.dev.mode', but only with the macro
  with-headless-environment below."
  false)

(defmacro with-headless-environment
  [& body]
  `(binding [*headless?*  true
             *mode*       :dev]
     ~@body))

(defn prod?
  []
  (= *mode* :prod))

(defmacro when-prod
  [& body]
  `(when (prod?)
     ~@body))

(defn dev?
  []
  (= *mode* :dev))

(defmacro when-dev
  [& body]
  `(when (dev?)
     ~@body))

(defn headless?
  []
  *headless?*)

(def not-headless?
  (complement headless?))

(defmacro when-headless
  [& body]
  `(when (headless?)
     ~@body))
