(ns slipstream.ui.util.page-type
  (:require [slipstream.ui.util.clojure :as uc]))

(def ^:dynamic *current-page-type*
  "The page-type is configured with a local thread-bound binding with the macro
  'page-type/with-page-type'. The default value of this dynamic var *current-page-type*
  is :view. To check the *current-page-type* use the 'page-type? family of fns
  below instead of referencing this var."
  :page-type/view)

(defn current
  "Same as *current-page-type* but as a variadic fn, useful as multimethod dispatch fn."
  [& _]
  *current-page-type*)

(derive :page-type-category/read-only  :page-type/any)
(derive :page-type-category/editable   :page-type/any)

(derive :page-type/view     :page-type-category/read-only)
(derive :page-type/chooser  :page-type-category/read-only)
(derive :page-type/edit     :page-type-category/editable)
(derive :page-type/new      :page-type-category/editable)

(def valid-page-types
  #{:page-type/view
    :page-type/edit
    :page-type/new
    :page-type/chooser})

(defmacro with-page-type
  [page-type & body]
  `(if-let [page-type# (->> (or ~page-type "view") (keyword "page-type") valid-page-types)]
     (binding [*current-page-type* page-type#]
       ~@body)
     (do
       ~@body)))

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
  (is? :page-type/view :page-type/chooser))

(defn edit-or-new?
  []
  (is? :page-type/edit :page-type/new))
