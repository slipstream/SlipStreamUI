(ns slipstream.ui.util.clojure)

(defmacro def-this-ns
  "Defines a private top level var with the namespace string of the current file."
  []
  `(def ^:private ^:const ~(symbol "this-ns") (str *ns*)))

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

(defn first-not-nil
  [& args]
  "Returns the first args which is not nil. Useful to deal with boolean vars,
  to differenciate nil from a defined 'false'."
  (first (drop-while nil? args)))

(defn parse-pos-int
  "Reads a positive integer number from a string. Returns nil if not a positive integer."
  [s]
  (binding [*read-eval* false]
    (when (and s (re-find #"^\d+$" s))
      (read-string s))))

(defn trim-up-to-last
  "Returns the input string without the characters appearing before the last char passed,
  and without the char, e.g. '(trim-up-to-last \\. 'a.b.c.d') => 'd'"
  [s c]
  {:pre [(char? c)]}
  (when s
    (->> s
         reverse
         (take-while #(not (= c %)))
         reverse
         (apply str))))

(defn trim-from-last
  "Returns the input string without the characters appearing after the last char passed,
  and without the char, e.g. '(trim-from-last \\. 'a.b.c.d') => 'a.b.c'"
  [s c]
  {:pre [(char? c)]}
  (when s
    (->> s
         reverse
         (drop-while #(not (= c %)))
         rest
         reverse
         (apply str))))

(defn trim-last-path-segment
  [path-str]
  (trim-from-last path-str \/))

(defn last-path-segment
  [path-str]
  (trim-up-to-last path-str \/))

(defn trim-last-slash
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (if (and s (-> s last (= \/)))
    (->> s butlast (apply str))
    s))

;; NOTE: Memoizing this function decreases performance
(defn parse-boolean
  "Casts 'true' and 'false' strings to their corresponding boolean
  values, both case insensitive and after trimming whitespace right
  and left, but leaving nil and empty string to nil, which is equivalent
  to false, but allows to detect if a value was set or not. All other
  strings will trigger an IllegalArgumentException."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when (not-empty s)
    (cond
      ; NOTE: (?i) means case-insensitive
      (re-matches #"(?i)^\s*true\s*$" s)  true
      (re-matches #"(?i)^\s*false\s*$" s) false
      :else (throw (IllegalArgumentException.
                     (str "Cannot parse boolean from string: " s))))))
