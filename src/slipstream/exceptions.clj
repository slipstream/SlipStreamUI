;(ns slipstream.exception
;  "Utilities for dealing with exceptions."
;  (:require [clojure.string :as str]
;            [clojure.tools.logging :as log]))
;
;(defn rethrow+
;  "Intended to be used only by the try+ macro below. But since it is a macro, this
;  function cannot be declared as private."
;  [status msg]
;  (let [info {:status status
;              :msg msg
;              :rethrown true}]
;    (throw (ex-info msg info))))
;
;
;(defn throw+
;  [status & words]
;  (let [msg (str/join " " words)
;        info {:status status :msg msg}]
;    (throw (ex-info msg info))))
;
;(defmacro try-or-throw+
;  [status-code msg & body]
;  `(try
;     ~@body
;     (catch Throwable t#
;       (log/error ~msg " -- Throwable: " t#)
;       (let [reason# (:msg (ex-data t#))
;             msg# (apply str ~msg (when reason# [" -- Reason: " reason#]))]
;         (throw+ ~status-code msg#)))))
;
;(defmacro try+
;  [msg & body]
;  `(try
;     ~@body
;     (catch Throwable t#
;       (let [info# (ex-data t#)
;             msg# (str "Error while " ~msg)
;             msg-ext# (str msg# ": " (or (:msg info#) "internal service error; please file bug report"))]
;         (when-not (:rethrown info#) (log/error t#))
;         (log/error msg#)
;         (rethrow+ (or (:status info#) 500) msg-ext#)))))