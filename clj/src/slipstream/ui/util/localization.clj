(ns slipstream.ui.util.localization
  "Util functions only related to the string localization."
  (:require [clojure.java.io :as io]
            [taoensso.tower :as tower]
            [slipstream.ui.util.mode :as mode]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.theme :as theme])
  (:import  java.io.File))

(def ^:private available-languages
  "In dev and in the REPL the lang/ folder can be inspected for the present files
  and automatically load them without having to declare them here.
  However, this doesn't work (i.e. it's much more complicated) from within the
  packajed jar, so that known resources must be directly loaded.
  For one possible solution see: http://stackoverflow.com/a/22363700"
  #{:en
    :ja
    :fr})

(def ^:private lang-resources-dir "lang")

(def ^:private spot-missing-t-calls-mode? false)
; (def ^:private spot-missing-t-calls-mode? true)

(def lang-default
  (or
    (-> "slipstream.ui.util.localization.lang-default" System/getProperty keyword available-languages)
    :en))

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

(defn- prefix-with-theme
  [theme k]
  (->> k name (str (name theme) ".") keyword))

(defn- merge-themes-localizations
  [lang-resource-filename lang-base-dict]
  (loop [dict         lang-base-dict
         theme        (first  theme/available-themes)
         next-themes  (next   theme/available-themes)]
    (let [theme-lang-resource-filename (str (theme/resources-folder theme) lang-resource-filename)
          lang-theme-dict (-> theme-lang-resource-filename
                              (uc/read-resource {})
                              (uc/update-keys (partial prefix-with-theme theme)))
          lang-dict (merge dict lang-theme-dict)]
      (if next-themes
        (recur lang-dict (first next-themes) (next next-themes))
        lang-dict))))

(defn- language-code->cached-locale-entry
  "Providing the proper map the lang dictionary instead of the path of the file makes
  localization faster (since it's cached) and allows merging strings from other themes.
  However, changes in the localization files are not taken into account without restarting
  the service.
  Used in prod."
  [language-code]
  (let [lang-resource-filename (str lang-resources-dir "/" (name language-code) ".edn")
        lang-base-dict (uc/read-resource lang-resource-filename)
        lang-dict (merge-themes-localizations lang-resource-filename lang-base-dict)]
    [language-code lang-dict]))

(def ^:private cached-lang-locales
  (->> available-languages
       (map language-code->cached-locale-entry)
       (into {})))


(defn- language-code->referenced-locale-entry
  "Providing the path of the lang dictionary instead of the proper map allows 'tower' to refresh
  the localization file on changes. If a theme is used, only the specific strings for the theme
  will be loaded, and all other will appear as missing.
  Used in dev."
  [language-code]
  (let [lang-resource-filename (str lang-resources-dir "/" (name language-code) ".edn")
        theme (theme/current)
        theme-lang-resource-filename (str (theme/resources-folder theme) lang-resource-filename)]
    (when (io/resource theme-lang-resource-filename)
      [language-code theme-lang-resource-filename])))

(def ^:private referenced-lang-locales
  (->> available-languages
       (map language-code->referenced-locale-entry)
       (into {})))

(when (empty? cached-lang-locales)
  (throw (IllegalStateException.
           (str "No lang locale files found in: " lang-resources-dir))))

(def ^:private tconfig-base
  {:dictionary {}
   :dev-mode? false ; Set to true for auto dictionary reloading
   :fallback-locale :en
   ; :fmt-fn fmt-str ; (fn [loc fmt args])
   :log-missing-translation-fn
     (fn [{:keys [locale ks scope] :as args}]
         (println ">>> Missing translation:" args))
   })

(def t-prod
  "Localization function for prod. The dictionary is cached for performance."
  (tower/make-t (assoc tconfig-base
                  :dictionary cached-lang-locales
                  :dev-mode?  false)))

(defn t-fallback-prod
  [locale-or-locales k-or-ks & fmt-args]
  (-> (if (vector? k-or-ks) (first k-or-ks) k-or-ks)
      name
      (uc/trim-up-to-last \.)))

(def t-dev
  "Localization function for dev. The dictionary is not cached, and it's reloaded
  when it is updated."
  (if spot-missing-t-calls-mode?
    ; (constantly "XXX")
    (fn [& args] (str (second args)))
    (tower/make-t (assoc tconfig-base
                    :dictionary   referenced-lang-locales
                    :dev-mode?    true))))

(defn t-fallback-dev
  [locale-or-locales k-or-ks & fmt-args]
  (str [locale-or-locales (if (vector? k-or-ks) (first k-or-ks) k-or-ks)]))

(defn t
  "Main localization function."
  [k & args]
  (when-not *lang*
    (throw (IllegalStateException.
             "Running global localization fn 't' (i.e. not scoped) outside the context of one 'with-lang...' macro.")))
  (let [k-or-ks (if-let [theme (theme/current)]
                  [(prefix-with-theme theme k) k]
                  k)]
    (if (mode/headless?)
      (or (apply t-dev  *lang* k-or-ks args) (apply t-fallback-dev  *lang* k-or-ks args))
      (or (apply t-prod *lang* k-or-ks args) (apply t-fallback-prod *lang* k-or-ks args)))))

(defn- replace-nil-args-with-blank-str
  "If we pass nil to the 't' fn as an argument, it will be displayed as 'null',
  since the clojure.core/format fn is used in the background.
  So we replace all nil arguments with the blank string to not show 'nul' in the
  interface."
  [args]
  (when (not-empty args)
    (map #(if (nil? %) "" %) args)))

(def ^:private nil-t-path
  "If the keyword path provided to t is nil, we have a NPE. Replacing a nil t-path
  with a default t-path makes it easier to trace."
  :nil-t-path)

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
       (let [t-path# (keyword (str ~scope-ns (name (or t-key# ~nil-t-path))))]
         (apply t t-path# (~replace-nil-args-with-blank-str args#))))))

(defmacro with-prefixed-t
  [prefix & body]
  (let [scoped-t (-> "t" symbol resolve)]
   (if scoped-t
     `(let [~(symbol "t") (fn [t-path# & args#]
                             (when t-path#
                               (apply ~scoped-t (keyword (str (name ~prefix) "." (name (or t-path# ~nil-t-path)))) args#)))]
         ~@body)
      (throw (IllegalStateException. (str "Scopped t function not found for namespace" *ns*))))))

(defmacro with-lang
  [lang & body]
  `(binding [*lang* (-> ~lang keyword ~available-languages (or lang-default))]
    ~@body))

;; Utils

(defmacro section-title
  "A util macro to use the same localization key for section titles.
  NB: it must be a macro and not a fn, because the 't fn to be used is the one
  in the calling namespace (since might be prefixed), instead of the 't defined
  here."
  [section-key]
  `(->> ~section-key name (format "section.%s.title") keyword ~(symbol "t")))