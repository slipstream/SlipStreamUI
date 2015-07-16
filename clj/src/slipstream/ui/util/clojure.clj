(ns slipstream.ui.util.clojure
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.walk :as walk]
            [clj-json.core :as json])
  (:import  [java.io FileNotFoundException]))

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol ConvertibleToSortable
  "Note that the original sorting will be overidden (if any). See tests for expectations."
  (->sorted [o]))

(extend-protocol ConvertibleToSortable

  java.util.Map
  (->sorted [o]
    (when (some->> o not-empty keys (map type) (apply not=))
      (throw (IllegalArgumentException. (str "All keys of the map must be of the same type: " (pr-str o)))))
    (into (sorted-map) o))

  java.util.Set
  (->sorted [o]
    (when (some->> o not-empty (map type) (apply not=))
      (throw (IllegalArgumentException. (str "All items in the set must be of the same type: " (pr-str o)))))
    (into (sorted-set) o))

  java.lang.Object
  (->sorted [o]
    o)

  nil
  (->sorted [_]
    nil))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn slurp-resource
  [path-str]
  (if-let [file (io/resource path-str)]
    (slurp file)
    (throw (FileNotFoundException.
             (format "Resource file %s not found!" path-str)))))

(defn read-resource
  "Returns the clojure structure contained in the resource file. If the file is not
  found, a default 'not-found' structure can be provided. If not provided, an exception
  will be thrown if the file doesn't exists."
  ([path-str]
    (-> path-str
        slurp-resource
        edn/read-string))
  ([path-str not-found]
    (if (io/resource path-str)
      (read-resource path-str)
      not-found)))

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
  "See tests for expectations."
  [s prefix]
  {:pre [(or (nil? s) (string? s))]}
  (when s
    (if (-> prefix (str ".*") re-pattern (re-matches s))
      s
      (str prefix s))))

(def ^:private ^String shorten-indicator-str "...")
(def ^:private shorten-char-threshold-default 50)

(defn- shorten-long-word
  [^String word char-threshold]
  {:pre [(integer? char-threshold)
         (pos? char-threshold)
         (> (dec char-threshold) (.length shorten-indicator-str))]}
  (if (-> word .length (> char-threshold))
    (let [chars-per-side (-> char-threshold (- (.length shorten-indicator-str)) (/ 2) int)]
      (str
        (subs word 0 chars-per-side)
        shorten-indicator-str
        (subs word (- (.length word) chars-per-side))))
    word))

(defn shorten-long-words
  "Trims words with more than 'char-threshold' characters in the middle. Note
  that words containing dots, dashes, slahes or backslahes are handled are one
  word. See tests for expectations."
  ([^String s]
    (shorten-long-words s shorten-char-threshold-default))
  ([^String s char-threshold]
    (if (and s (> (.length s) char-threshold))
      (s/replace s #"\b\w[\w./\\-]*\b" #(shorten-long-word % char-threshold))
      s)))

(defn truncate-to-max-length
  "Truncates the input string up to 'char-threshold' chars."
  ([^String s]
    (truncate-to-max-length s shorten-char-threshold-default))
  ([^String s char-threshold]
    (if (and s (> (.length s) char-threshold))
      (str
        (subs s 0 (- char-threshold (.length shorten-indicator-str)))
        shorten-indicator-str)
      s)))

(defn trim-prefix
  "See tests for expectations."
  [s prefix]
  {:pre [(or (nil? s) (string? s))]}
  (when s
    (if-let [[_ s-without-prefix] (-> "(?s)%s(.*)" ;; (?s) Dot matches all (including newline)
                                      (format prefix)
                                      re-pattern
                                      (re-matches s))]
      s-without-prefix
      s)))

(defn ensure-suffix
  "See tests for expectations."
  [s suffix]
  {:pre [(or (nil? s) (string? s))]}
  (when s
    (if (->> s
             reverse
             (take (count (str suffix)))
             reverse
             (apply str)
             (= (str suffix))) ; NOTE: Using a regex is not safe because suffix might be e.g. "."
      s
      (str s suffix))))

(defn ensure-unquoted
  "See tests for expectations."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when s
    (-> "^['\"]?(.*?)['\"]?$"
        re-pattern
        (re-matches s)
        last)))

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
  "Casts 'true', 'on' and 'yes' strings to the boolean value 'true',
  and 'false', 'off' and 'no' string to the boolean value 'false'.
  All cases are case insensitive and after trimming whitespace right
  and left, but leaving nil and empty string to nil, which is equivalent
  to false, but allows to detect if a value was set or not. All other
  strings will trigger an IllegalArgumentException."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when (not-empty s)
    (condp re-matches s
      ; NOTE: (?i) means case-insensitive
      #"(?i)^\s*true\s*$"   true
      #"(?i)^\s*false\s*$"  false
      #"(?i)^\s*on\s*$"     true
      #"(?i)^\s*off\s*$"    false
      #"(?i)^\s*yes\s*$"    true
      #"(?i)^\s*no\s*$"     false
      (throw (IllegalArgumentException.
               (str "Cannot parse boolean from string: " s))))))

(defn update-keys
  "Recursively applies f to all map keys."
  [m f & {:keys [only-keywords?]}]
  (let [g (if only-keywords?
            (fn [k] (if (keyword? k) (f k) k))
            f)]
    (walk/postwalk (fn [x] (if (map? x) (into (empty x) (for [[k v] x] [(g k) v])) x)) m)))

(defn keywordize
  "Takes anything and returns it if it is a keyword. Else return a sanitized
  idiomatic Clojure keyword. See tests for expectations."
  [x]
  (cond
    (nil? x) nil
    (keyword? x) x
    :else (-> x
              str
              (s/replace #"[\s_\.]+" "-")
              (s/replace #"[^\w-]+" "")
              (s/replace #"(?<!(?:-|^))([A-Z])(?!(?:[A-Z]|$))" "-$1")
              s/lower-case
              keyword)))

(defn title-case
  "Makes Only The First Letter Of Each Word Uppercase. See Tests For Expectations."
  [s]
  (when s
    (-> s
        s/lower-case
        (s/replace #"(\w)[-\.](\w)" "$1 $2") ; NOTE: dash-separated and dot.separated words become space separated.
        (s/replace #"(?:^|\b)\w" s/upper-case))))

(defn- normalise-sorting
  "In order to generate a deterministic string, we sort all naturally unsorted
  data types."
  [x]
  (walk/postwalk ->sorted x))

(defn ->camelCaseString
  "Takes anything and returns a camelCase'd string. See tests for expectations."
  [x]
  (when x
    (-> x
        normalise-sorting
        str
        keywordize
        name
        (s/replace #"-\w" (comp s/upper-case last)))))

(defn- keyword?->is-keyword
  "Transfors a Clojure keyword :keyword? into :is-keyword."
  [x]
  (if-let [[_ name-without-?] (when (keyword? x) (re-matches #"(.*)\?" (name x)))]
    (keyword (str "is-" name-without-?))
    x))

(defn ->json
  "Takes a clojure data structure and returns a json data structure. The thing is
  that here we camelCase map keys transforming also :key? into 'isKey', so that the
  new keys are idiomatic in JSON and, in Javascript, they can be accessed with
  point notation (i.e. object.key) instead of object['key-string'].
  See tests for expectations."
  [x]
  (some-> x
          (update-keys keyword?->is-keyword :only-keywords? true)
          (update-keys ->camelCaseString    :only-keywords? true)
          normalise-sorting
          json/generate-string))

(defn coll-grouped-by
  "Primary intended to 'better' group a coll of maps sorting the result by the
  grouping value. There are some options available to specify the keywords used
  in the result. See tests for expectations."
  [f coll & {:keys [group-keyword group-sort-fn items-keyword items-sort-fn group-type-fn group-type-keyword]
             :or   {group-keyword :group
                    group-sort-fn identity ;; NOTE: The input of the group-sort-fn will be the value of the group
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

(defn- stringify
  [x]
  (cond
    (string? x)   (s/trim x)
    (keyword? x)  (name x)
    :else         (str x)))

(defn join-as-str
  "Comma-separated string with sorted coll elements. To allow consistent results
  set items are joined in alphabetical order. Other collections (e.g. vecs and
  lists) are joined in the exitent order. See tests for expectations."
  [coll]
  {:pre [(and
           (coll? coll)
           (not (map? coll)))]}
  (cond->> coll
   :always      (map stringify)
   (set? coll)  sort
   :always      (s/join ", ")))

(defn dashless-str
  "Returns the string without dashes. Everything other than a string, returns nil.
  See tests for expectations."
  [s]
  (when (string? s)
    (s/replace s "-" "")))

(defn- map-fn-in
  "Map f on s with update-in semantics. See tests for expectations."
  [map-fn ks f s]
  (if-let [k (first ks)]
    (map-fn
      (fn [m]
        (update-in m [k]
          #(map-fn-in map-fn (rest ks) f %)))
      s)
    (map-fn f s)))

(def map-in   (partial map-fn-in map))
(def mapv-in  (partial map-fn-in mapv))

(defn mmap
  "Like 'ffirst for 'map. See tests for expectations."
  [f s]
  {:pre [(coll? s)
         (every? coll? s)]}
  (map #(map f %) s))

(defn html-safe-str
 "Like clojure.core/str but escapes < > & and \"."
 [x]
  (-> x
      str
      (.replace "&" "&amp;")
      (.replace "<" "&lt;")
      (.replace ">" "&gt;")
      (.replace "\"" "&quot;")))

(defn ensure-vector
  [v]
  (when v
    (cond
      (vector?  v)  v
      (map?     v)  (vector v)
      (coll?    v)  (vec v)
      :else         (vector v))))
