(ns slipstream.ui.util.clojure
  (:require [superstring.core :as s]
            [clojure.set :as set]
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
  "Casts 'true', 'on', 'yes' and '1' strings to the boolean value
  'true', and 'false', 'off', 'no' and '0' string to the boolean value
  'false'. All cases are case insensitive and after trimming whitespace
  right and left, but leaving nil and empty string to nil, which is
  equivalent to false, but allows to detect if a value was set or not.
  All other strings will trigger an IllegalArgumentException. In cases
  where throwing an exception is not reasonable, you can pass a default
  value (see 2nd arity)."
  ([s]
   {:pre [(or (nil? s) (string? s))]}
   (when (not-empty s)
     (case (-> s s/trim s/lower-case)
       ("true"  "on"  "yes" "1")  true
       ("false" "off" "no"  "0")  false
       (throw (IllegalArgumentException.
                (str "Cannot parse boolean from string: " (pr-str s)))))))
  ([s value-if-not-parsable]
   (try
     (parse-boolean s)
     (catch Throwable _
       (boolean value-if-not-parsable)))))

(defn update-kvs
  "Recursively update all keys and values with the give function. See tests for expectations."
  [m & {:keys [keys-fn vals-fn] :or {keys-fn identity vals-fn identity}}]
  (walk/postwalk (fn [x] (if (map? x) (into (empty x) (for [[k v] x] [(keys-fn k) (vals-fn v)])) x)) m))

(defn update-keys
  "Recursively applies f to all map keys."
  [m f & {:keys [only-keywords?]}]
  (update-kvs m :keys-fn (if only-keywords? (fn [k] (if (keyword? k) (f k) k)) f)))

(defn update-vals
  "Recursively applies f to all map values (except if the value is a coll). See tests for expectations."
  [m f]
  (update-kvs m :vals-fn (fn [v] (if (coll? v) v (f v)))))

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- KB-mn-metric?
  [m]
  (= "ram" (name m)))

(defn- GB-mn-metric?
  [m]
  (= "disk" (name m)))

(defn- mn-to-h
  [v]
  (/ v 60.0))

(defn- KB-to-GB
  [v]
  (/ v 1024.0))

(defn- value-unit
  [v u]
  (format "%.2f (%s)" v u))

(defn format-metric-value
  [v m]
  {:pre [(number? v)]}
  (cond
    (KB-mn-metric? m) (-> v KB-to-GB mn-to-h (value-unit "GBh"))
    (GB-mn-metric? m) (-> v mn-to-h          (value-unit "GBh"))
    :else             (-> v mn-to-h          (value-unit "h"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:private flat-map-key-separator ".")
(def ^:private flat-map-key-separator-re #"\.")

;; SOURCE: http://stackoverflow.com/a/17902228
(defn flatten-map
  "Flattens a nested map into a one-level map:
  {:a {:b 1 :c 2} :b 3} => {:a.b 1 :a.c 2 :b 3}
  See tests for expectations."
  ([m]
    (into {} (flatten-map m nil)))
  ([m pre]
    (mapcat (fn [[k v]]
              (let [prefix (if pre (str pre flat-map-key-separator (name k)) (name k))]
                (if (map? v)
                  (flatten-map v prefix)
                  [[(keyword prefix) v]])))
              m)))

(defn- key-to-assoc
  [m key-path]
  (->> key-path
       (reductions (fn [[res path-acc] path-segment] (let [new-path (conj path-acc path-segment)] [(get-in m new-path) new-path])) [[] []])
       rest
       (partition-by #(or (-> % first map?) (-> % first nil?)))
       first
       last
       last))

(defn deflatten-map
  "Inverse of flatten-map. See tests for expectations."
  [m]
  {:pre [(or (nil? m) (map? m))]}
  (reduce (fn [m [k v]]
            (let [key-path  (map keyword (s/split (name k) flat-map-key-separator-re))
                  assoc-at  (key-to-assoc m key-path)
                  assoc-key (->> key-path
                                 (drop (count assoc-at))
                                 (map name)
                                 (s/join flat-map-key-separator)
                                 not-empty
                                 keyword)]
              (if assoc-key
                (update-in m assoc-at assoc assoc-key v)
                (assoc-in m key-path v))))
          {}
          (sort-by first m)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; SOURCE: http://www.w3schools.com/tags/ref_urlencode.asp
; SOURCE: http://www.degraeve.com/reference/urlencoding.php
; SOURCE: https://en.wikipedia.org/wiki/Percent-encoding#Percent-encoding_reserved_characters

(def ^:private strict-url-encode-chars
  { \! "%21"
    \# "%23"
    \$ "%24"
    \& "%26"
    \' "%27"
    \( "%28"
    \) "%29"
    \* "%2A"
    \+ "%2B"
    \, "%2C"
    \/ "%2F"
    \: "%3A"
    \; "%3B"
    \= "%3D"
    \? "%3F"
    \@ "%40"
    \[ "%5B"
    \] "%5D"})

(def ^:private url-encode-chars
  (merge strict-url-encode-chars
         {\backspace  "%08"
          \tab        "%09"
          \newline    "%0A"
          \return     "%0D"
          \space      "%20"
          \"          "%22"
          \%          "%25"
          \-          "%2D"
          \.          "%2E"
          \<          "%3C"
          \>          "%3E"
          \\          "%5C"
          \^          "%5E"
          \_          "%5F"
          \`          "%60"
          \{          "%7B"
          \|          "%7C"
          \}          "%7D"
          \~          "%7E"
          \¢          "%A2"
          \£          "%A3"
          \¥          "%A5"
          \¦          "%A6"
          \§          "%A7"
          \«          "%AB"
          \¬          "%AC"
          \¯          "%AD"
          \º          "%B0"
          \±          "%B1"
          \ª          "%B2"
          \´          "%B4"
          \µ          "%B5"
          \»          "%BB"
          \¼          "%BC"
          \½          "%BD"
          \¿          "%BF"
          \À          "%C0"
          \Á          "%C1"
          \Â          "%C2"
          \Ã          "%C3"
          \Ä          "%C4"
          \Å          "%C5"
          \Æ          "%C6"
          \Ç          "%C7"
          \È          "%C8"
          \É          "%C9"
          \Ê          "%CA"
          \Ë          "%CB"
          \Ì          "%CC"
          \Í          "%CD"
          \Î          "%CE"
          \Ï          "%CF"
          \Ð          "%D0"
          \Ñ          "%D1"
          \Ò          "%D2"
          \Ó          "%D3"
          \Ô          "%D4"
          \Õ          "%D5"
          \Ö          "%D6"
          \Ø          "%D8"
          \Ù          "%D9"
          \Ú          "%DA"
          \Û          "%DB"
          \Ü          "%DC"
          \Ý          "%DD"
          \Þ          "%DE"
          \ß          "%DF"
          \à          "%E0"
          \á          "%E1"
          \â          "%E2"
          \ã          "%E3"
          \ä          "%E4"
          \å          "%E5"
          \æ          "%E6"
          \ç          "%E7"
          \è          "%E8"
          \é          "%E9"
          \ê          "%EA"
          \ë          "%EB"
          \ì          "%EC"
          \í          "%ED"
          \î          "%EE"
          \ï          "%EF"
          \ð          "%F0"
          \ñ          "%F1"
          \ò          "%F2"
          \ó          "%F3"
          \ô          "%F4"
          \õ          "%F5"
          \ö          "%F6"
          \÷          "%F7"
          \ø          "%F8"
          \ù          "%F9"
          \ú          "%FA"
          \û          "%FB"
          \ü          "%FC"
          \ý          "%FD"
          \þ          "%FE"
          \ÿ          "%FF"}))


(def ^:private url-decode-chars
  (set/map-invert url-encode-chars))

(defn url-encode
  [^String s]
  {:pre [(or (nil? s) (string? s))]}
  (and s
       (s/escape s url-encode-chars)))

(defn url-decode
  [^String s]
  {:pre [(or (nil? s) (string? s))]}
  (and s
       (s/replace s #"%[0-9A-Z]{2}" (comp str url-decode-chars))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
