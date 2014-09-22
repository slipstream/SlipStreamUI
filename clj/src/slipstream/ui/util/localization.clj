(ns slipstream.ui.util.localization
  "Util functions only related to the string localization."
  (:require [clojure.java.io :as io]
            [taoensso.tower :as tower]
            [slipstream.ui.util.clojure :as uc])
  (:import  java.io.File))

(def ^:private available-languages
  "In dev and in the REPL the lang/ folder can be inspected for the present files
  and automatically load them without having to declare them here.
  However, this doesn't work (i.e. it's much more complicated) from within the
  packajed jar, so that known resources must be directly loaded.
  For one possible solution see: http://stackoverflow.com/a/22363700"
  #{:en :ja})

(def ^:private lang-resources-dir "lang/")

(def ^:private spot-missing-t-calls-mode? false)
; (def ^:private spot-missing-t-calls-mode? true)

(def lang-default :en)

(def ^:dynamic *lang*
  "The lang is configured with a local thread-bound binding with the macro
  'localization/with-lang' or 'localization/with-lang-from-metadata'. The default
  value of this dynamic var *lang* is nil to indicate that the is no locale binding
  setup for the current thread."
  nil)

(defn locale
  "Returns valid Locale matching given the current *lang*"
  []
  (if *lang*
    (tower/locale *lang*)
    (throw (IllegalStateException.
             "Requesting Locale outside the context of one 'with-lang...' macro."))))

(defn- language-code->locale-entry
  [language-code]
  (let [lang-resource-filename (str lang-resources-dir (name language-code) ".edn")]
    (when-not (io/resource lang-resource-filename)
      (throw (IllegalArgumentException.
           (str "No lang locale file '" lang-resource-filename
                "' found in resource dir '" lang-resources-dir "'"))))
    [language-code lang-resource-filename]))

(def ^:private lang-locales
  (->> available-languages
       (map language-code->locale-entry)
       (into {})))

(when (empty? lang-locales)
  (throw (IllegalStateException.
           (str "No lang locale files found in: " lang-resources-dir))))

(def ^:private my-tconfig
  {:dictionary lang-locales
   :dev-mode? true ; Set to true for auto dictionary reloading ; TODO: Set to false on prod
   :fallback-locale :en
   ; :fmt-fn fmt-str ; (fn [loc fmt args])
   :log-missing-translation-fn
   (fn [{:keys [locale ks scope] :as args}]
     (println ">>> Missing translation:" args))})

(def t
  (if spot-missing-t-calls-mode?
    ; (constantly "XXX")
    (fn [& args] (str (second args)))
    (tower/make-t my-tconfig))) ; Create translation fn

(defmacro def-scoped-t
  "Defines a top level private var 't' for scoped translation requests.
  E.g. calling (localization/def-scoped-t) in the namespace 'slipstream.ui.views.user'
  will define a private function 't' so that (t :foo) within the namespace
  'slipstream.ui.views.user' will be equivalent to (localization/t :slipstream.ui.views.user.foo)."
  []
  (let [fn-symbol   (symbol "t")
        scope-ns    (str *ns* ".")
        ex-msg       (str "Running localization fn 't'"
                         " (scoped to namespace " scope-ns ")"
                         " outside the context of one 'with-lang...' macro.")]
    `(defn- ~fn-symbol
       [t-key# & args#]
       (when-not *lang*
         (throw (IllegalStateException. ~ex-msg)))
       (let [t-path# (keyword (str ~scope-ns (name t-key#)))]
          (if args#
            (apply t *lang* t-path# args#)
            (t *lang* t-path#))))))

(defn lang
  "Get iso language code from the server's metadata. "
  [metadata]
  ;; TODO: Parse server metadata to retrieve the wanted iso language code.
  lang-default)

(defmacro with-lang
  [lang & body]
  `(if-let [lang# (-> ~lang keyword tower/iso-languages)]
     (binding [*lang* lang#]
       ~@body)
     (throw (IllegalArgumentException. (str "with-lang: wrong language code: " ~lang)))))

(defmacro with-lang-from-metadata
  [& body]
  (when-not (-> &env keys set (get 'metadata))
    (throw (IllegalArgumentException. "with-lang-from-metadata: Unable to find metadata symbol in this context")))
  `(with-lang (lang ~(symbol "metadata"))
    ~@body))
