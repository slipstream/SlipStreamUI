(ns slipstream.ui.util.clojure
  (:require [clojure.string :as s]))

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
  "Reads a positive integer number from a string or int. Returns nil if not a positive integer."
  [x]
  (binding [*read-eval* false]
    (cond
      (not x) nil
      (and (string? x) (re-find #"^\+?\d+$" x)) (read-string x)
      (and (integer? x) (pos? x)) x)))

(defn ensure-prefix
  [s prefix]
  {:pre [(or (nil? s) (string? s))]}
  (when s
    (if (-> prefix (str ".*") re-pattern (re-matches s))
      s
      (str prefix s))))

(defn trim-from
  "Returns the input string without the characters appearing after the first instance of the char passed,
  and without the char, e.g. '(trim-from 'a.b.c.d' \\.) => 'a'. See tests for expectations."
  [s c]
  {:pre [(char? c)]}
  (if-not ((set s) c)
    s
    (->> s
         (take-while #(not (= c %)))
         (apply str))))

(defn trim-from-last
  "Returns the input string without the characters appearing after the last char passed,
  and without the char, e.g. '(trim-from-last 'a.b.c.d' \\.) => 'a.b.c'. See tests for expectations."
  [s c]
  {:pre [(char? c)]}
  (if-not ((set s) c)
    s
    (->> s
         reverse
         (drop-while #(not (= c %)))
         rest
         reverse
         (apply str))))

(defn trim-up-to-last
  "Returns the input string without the characters appearing before the last char passed,
  and without the char, e.g. '(trim-up-to-last 'a.b.c.d' \\.) => 'd'. See tests for expectations."
  [s c]
  {:pre [(char? c)]}
  (if-not ((set s) c)
    s
    (->> s
         reverse
         (take-while #(not (= c %)))
         reverse
         (apply str))))

(defn trim-last-path-segment
  [path-str]
  (when path-str
    (if ((set path-str) \/)
      (trim-from-last path-str \/)
      "")))

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

(defn keywordize
  "Takes anything and returns it if it is a keyword. Else return a sanitized
  idiomatic Clojure keyword. See tests for expectations."
  [x]
  (cond
    (nil? x) nil
    (keyword? x) x
    :else (-> x
              str
              (s/replace #"[\s_]+" "-")
              (s/replace #"[^\w-]+" "")
              (s/replace #"(?<!(?:-|^))([A-Z])" "-$1")
              s/lower-case
              keyword)))

(defn coll-grouped-by
  "Primary intended to 'better' group a coll of maps sorting the result by the
  grouping value. There are some options available to specify the keywords used
  in the result. See tests for expectations."
  [f coll & {:keys [group-keyword group-sort-fn items-keyword items-sort-fn group-type-fn group-type-keyword]
             :or   {group-keyword :group
                    group-sort-fn identity
                    items-keyword :items}}]
  (let [group-keyword (if (keyword? f) f group-keyword)
        group-type-keyword (or
                             group-type-keyword
                             (-> group-keyword name (str "-type") keyword))
        items-sort (if items-sort-fn (partial sort-by items-sort-fn) identity)
        grouped-items (->> coll (group-by f) (sort-by (comp group-sort-fn first)))]
    (for [[group items] grouped-items]
      (let [res {group-keyword group, items-keyword (items-sort items)}]
        (if (ifn? group-type-fn)
          (assoc res group-type-keyword (group-type-fn group))
          res)))))
