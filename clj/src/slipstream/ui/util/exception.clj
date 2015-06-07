(ns slipstream.ui.util.exception)

(defn- exception-name
  [e]
  (condp instance? e
    java.lang.NullPointerException "NPE"
    java.lang.ArithmeticException  "Arithmetic exception"
    clojure.lang.ExceptionInfo     "Guarded exception"
    "Exception"))

(defmacro guard*
  "Provides extra context for the exception in the logs."
  [print-bindings? msg & body]
  `(try
     ~@body
     (catch java.lang.Exception e#
       (let [root-cause# (or (.getCause e#) e#)
             guarded-level# (or (some-> e# ex-data :guarded-levels inc) 1)
             info# (-> ~(meta &form)
                       (or {})
                       (assoc :ex       e#
                              :file     ~*file*
                              :ns       ~(str *ns*)
                              :msg      ~msg
                              :forms    (quote ~(->> &form (drop 3) vec))
                              :bindings ~(if print-bindings?
                                           (into {} (for [sbl (keys &env)] [`(quote ~sbl) sbl]))
                                           :muted)))]
         (println)
         (println "Guarded exception" guarded-level# "on the UI generation. Context:")
         (prn info#) ; (clojure.pprint/pprint info#) ; NOTE: Not sure which has a more useful output
         (println)
         ; NOTE: Using directly (clojure.lang.ExceptionInfo.) instead of (ex-info) to avoid
         ;       one irrelevant line in the stacktrace.
         (throw (clojure.lang.ExceptionInfo.
                  (str (~exception-name e#) " thrown while trying to " ~msg)
                  {:root-cause (str root-cause#) :guarded-levels guarded-level#}
                  e#))))))

(defmacro guard
  "Provides extra context for the exception in the logs, printing the direct bindings."
  [msg & body]
  `(guard* true ~msg ~@body))

(defmacro quietly-guard
  "Provides extra context for the exception in the logs. This does not print the
  bindings and their values. To be used when the data in the bindings might be very
  large (thus polluting the logs), might contain sensitive data or will never be
  relevant for debugging."
  [msg & body]
  `(guard* false ~msg ~@body))
