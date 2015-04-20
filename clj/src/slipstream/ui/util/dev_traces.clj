(ns slipstream.ui.util.dev-traces)

; NOTE: To copy&paste to the wanted ns for dev:
; (:use slipstream.ui.util.dev-traces)

(def ^:dynamic *enabled*
  false)

(defn enable-globally
  "Use this switch to globally enable or disable the traces. By default they are
  disabled."
  [bool]
  (alter-var-root #'*enabled* (constantly (boolean bool))))

(defmacro with-enabled-traces
  "Use this switch to thread-locally enable or disable the traces. By default the
  value of *enabled* is used."
  [& body]
  `(binding [*enabled* true]
     ~@body))

;; TODO: This macro definitely needs some clean up ;)

(defmacro >>>
  [& body]
  (let [file          *file*
        line          (-> &form meta :line)
        the-ns        (str *ns*)
        trim-last     (fn [s] (some-> s pr-str (subs 0 (-> s str count dec))))
        red           "\033[1;31m"
        green         "\033[1;32m"
        yellow        "\033[1;33m"
        blue          "\033[1;34m"
        purple        "\033[1;35m"
        baby-blue     "\033[1;36m"
        reset-color   "\033[0m"
        prefix        (str yellow file ":" line " >>>" reset-color)
        space-str     (fn [n] (apply str (repeat n \space)))
        prefix-space  (space-str (-> prefix count (- 11)))]
    (printf "%1$s[WARN] Using >>> macro in %3$s:%4$s%2$s\n" yellow reset-color file line)
    `(if-not *enabled*
       ~body
       (let [bindings#        ~(into {} (for [sbl (keys &env)] [`(quote ~sbl) sbl]))
             raw-forms#       (quote ~body)
             bindings-used?#  true
             binded-forms#    (clojure.walk/prewalk-replace bindings# raw-forms#)
             bindings-used?#  (not= raw-forms# binded-forms#)
             body-result#     ~body]
         (print ~prefix (pr-str raw-forms#))
         (cond
           ~(#{"->" "->>" "some->" "some->>" "cond->" "cond->>"} (-> body first str))
           (do
             (println)
             ~@(let [steps      (->> body (reductions conj []) (drop 2) (map list*))
                     last-step? (fn [step] (-> steps count dec (= step)))]
                 (map-indexed #(if (zero? %1)
                                 `(do
                                    (printf "%s %s%s %s"
                                           ~prefix-space
                                           ~baby-blue
                                           (->> (quote ~%2) (clojure.walk/prewalk-replace ~(into {} (for [sbl (keys &env)] [`(quote ~sbl) sbl]))) ~trim-last)
                                           ~purple)
                                     (pr ~(-> %2 vec (conj identity) list*))
                                     (println  ~reset-color))
                                 `(do
                                   (print ~prefix-space ~(space-str 3) ~baby-blue)
                                   (pr (clojure.walk/prewalk-replace ~(into {} (for [sbl (keys &env)] [`(quote ~sbl) sbl])) (quote ~(-> %2 last))))
                                   (when ~(last-step? %1)
                                     (print ")"))
                                   (print " ")
                                   (print ~purple)
                                   (prn ~%2)
                                   (print ~reset-color)
                                   ))
                              steps)))

           bindings-used?#
           (printf " %s%s %s%s\n" ~baby-blue (pr-str binded-forms#) ~purple (pr-str body-result#))

           :else
           (printf " %s%s\n" ~purple (pr-str body-result#)))
         (print ~reset-color)
         body-result#))))


;; TODO: Run following examples in a test:

; (clojure.walk/prewalk-demo '(f (inc a) 3))
; (prn)
; (clojure.walk/postwalk-demo '(f (inc a) 3))

; (defn f [x y] (* x x y -1))

; (>>> f 2 3)

; (let [a 7]
;   (>>> f 2 3))

; (let [a 7]
;   (>>> f a 3))

; (let [a 7]
;   (>>> f (inc a) 3))

; (let [a 7]
;   (>>> f ({:a (inc a)} :a) 3))

; (let [a 3
;       b 5]
;   (>>> -> a
;           (f b)
;           inc
;           str))

;  ; (-> 3 => 3
;  ;     (f 5) => -45
;  ;     inc => -44
;  ;     str) => "-44"

;  (let [a 3
;        b 5]
;   (>>> ->> (inc a)
;           (f b)
;           inc
;           str))

;  (let [a 3
;        b 5]
;   (>>> some->> a
;                (f b)
;                ((constantly nil))
;                inc
;                str))

;  ; (->> 3 => 3
;  ;      (f 5) => -75
;  ;      inc => -74
;  ;      str) => "-74"
