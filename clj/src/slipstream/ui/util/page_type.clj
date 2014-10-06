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
    :chooser})

(defmacro is
  [page-type & body]
  `(if-let [page-type# (-> ~page-type uc/keywordize valid-page-types)]
     (binding [*current-page-type* page-type#]
       ~@body)
     ~@body))

(defn- is?
  [& page-types]
  (boolean ((set page-types) *current-page-type*)))

; (defn- is-not?
;   [& page-types]
;   (not (apply is? page-types)))

(defn view?
  []
  (is? :view))

(defn chooser?
  []
  (is? :chooser))

(defn view-or-chooser?
  []
  (is? :view :chooser))

(defn edit?
  []
  (is? :edit))

(defn new?
  []
  (is? :new))

(defn edit-or-new?
  []
  (is? :edit :new))
