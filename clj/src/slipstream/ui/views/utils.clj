(ns slipstream.ui.views.utils)

;; TODO: Look at slipstream.ui.views.module-base/ischooser? and refactor.
(defn chooser?
  [type]
  (= "chooser" type))

(defn remove-if
  [test]
  (when-not test
    identity))

(defn remove-if-not
  [test]
  (remove-if (not test)))

(defmacro defn-memo
  [fname & body]
  (when-not (symbol? fname)
    (throw (IllegalArgumentException.
             "First argument to defn-memo must be a symbol.")))
  (when (string? (first body))
    (throw (IllegalArgumentException.
             (str "Doc-string is not implemented for defn-memo: " (first body)))))
  `(def ~fname
     (memoize
       (fn ~@body))))

; (def ^:private valid-symbols
;   #{:template-filename})

; (defn- value
;   "Returns the value bound to the symbol in the given namespace."
;   [page-ns sym]
;   (if-let [valid-ns (find-ns page-ns)]
;     (when-let [valid-sym (valid-symbols sym)]
;       (let [s (intern valid-ns (symbol (name valid-sym)))]
;         (when (bound? s) @s)))
;     (throw (Exception. (str "Namespace not found: " page-ns)))))

