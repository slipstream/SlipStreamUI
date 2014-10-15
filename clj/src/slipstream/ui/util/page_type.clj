(ns slipstream.ui.util.page-type
  (:require [slipstream.ui.util.clojure :as uc]))

(def ^:dynamic *current-page-type*
  "The page-type is configured with a local thread-bound binding with the macro
  'page-type/is'. The default value of this dynamic var *current-page-type*
  is :view. To check the *current-page-type* use the 'page-type? family of fns
  below instead of referencing this var."
  :view)

(def valid-page-types
  #{:view
    :edit
    :new
    :chooser
    :reports})

(defmacro is
  [page-type & body]
  `(if-let [page-type# (-> ~page-type uc/keywordize valid-page-types)]
     (binding [*current-page-type* page-type#]
       ~@body)
     ~@body))

(defn- is?
  [& page-types]
  (boolean ((set page-types) *current-page-type*)))

(defmacro defn-page-type-checkers
  "Defs a 2 top levels vars for each valid-page-type,
  e.g. page-type/view? and page-type/not-view?"
  []
  `(do
   ~@(for [page-type valid-page-types
           :let [fn-symbol (->> page-type name (format "%s?") symbol)
                 not-fn-symbol (->> page-type name (format "not-%s?") symbol)]]
      `(do
         (defn ~fn-symbol
           []
           (is? ~page-type))
         (defn ~not-fn-symbol
           []
           (not (is? ~page-type)))))))

(defn-page-type-checkers)

(defn view-or-chooser?
  []
  (is? :view :chooser))

(defn edit-or-new?
  []
  (is? :edit :new))
