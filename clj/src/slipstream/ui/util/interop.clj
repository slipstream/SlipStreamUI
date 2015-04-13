(ns slipstream.ui.util.interop
  "Clojurify the data structures received from Java since they don't work as
  expected all the time. E.g. a java.util.HashMap cannot be used as clj meta.")

(defprotocol ConvertibleToClojure
  "See tests for expectations."
  ;; NOTE: Inspired from
  ;;       http://grokbase.com/t/gg/clojure/11afb4wmb3/recursively-convert-java-map-to-clojure-map
  ;;       http://clojure.org/protocols
  (->clj [o]))

(extend-protocol ConvertibleToClojure

  ;; NOTE: We specifically target java.util.HashMap instead of java.util.Map, to
  ;;       leave untouched clojure maps, which also implement java.util.Map.
  java.util.HashMap
  (->clj [o]
    (let [entries (.entrySet o)]
      (reduce (fn [m [k v]]
                  (assoc m (keyword k) (->clj v)))
        {}
        entries)))

  java.util.List
  (->clj [o]
    (vec (map ->clj o)))


  java.lang.Object
  (->clj [o]
    o)

  nil
  (->clj [_]
    nil))
