(ns slipstream.ui.util.localization
  "Util functions only related to the string localization."
  (:require [clojure.java.io :as io]
            [taoensso.tower :as tower]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private lang-resources-dir "clj/resources/lang/")
; (def ^:private lang-resources-dir "resources/lang/")
(def ^:private spot-missing-t-calls-mode? false)
; (def ^:private spot-missing-t-calls-mode? true)

(def lang-default :en)

(def ^:dynamic *lang*
  "The lang is configured with a local thread-bound binding with the macro
  'localization/with-lang' or 'localization/with-lang-from-metadata'. The default
  value of this dynamic var *lang* is nil to indicate that the is no locale binding
  setup for the current thread."
  nil)

(defn- file->locale-entry
  [file]
  (let [filename (.getName file)
        basename (uc/trim-from-last filename \.)
        extension (uc/trim-up-to-last filename \.)
        iso-language (-> basename keyword tower/iso-languages)]
    (when (and (= extension "edn") iso-language)
      [iso-language (str "lang/" filename)])))


(def ^:private lang-locales
  (into {} (->> lang-resources-dir
                io/file
                file-seq
                rest
                (map file->locale-entry))))

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
     (println ">>>>>>>>>>>> Missing translation:" args))})

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
  "Get iso language code from the servers metadata. "
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
