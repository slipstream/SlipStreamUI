(ns slipstream.ui.views.utils
  (:require [net.cgrand.enlive-html :as html]))

;; TODO: Look at slipstream.ui.views.module-base/ischooser? and refactor.

;; SlipStream

(defn chooser?
  [type]
  (= "chooser" type))


;; Enlive

(defn enlive-node?
  "To differenciate between maps that represent enlive-generated nodes from
  normal clojure maps."
  [n]
  (or
    (instance? clojure.lang.PersistentStructMap n)
    (instance? clojure.lang.PersistentStructMap (first n))))

(defn when-content
  [content]
  (fn [match]
    (if (nil? content)
      (identity match)
      ((html/content (str content)) match))))

(defn when-html-content
  [html-content]
  (fn [match]
    (if (nil? html-content)
      (identity match)
      ((html/html-content (str html-content)) match))))

(defn when-add-class
  [test & classes]
  (fn [match]
    (if test
      ((apply html/add-class classes) match)
      (identity match))))

(defn replace-class
  [class-to-remove class-to-add]
  (prn "class-to-remove" class-to-remove)
  (prn "class-to-add" class-to-add)
  (html/do->
    (html/remove-class class-to-remove)
    (html/add-class class-to-add)))

(defmacro when-replace-class
  [test class-to-remove class-to-add]
  `(if ~test
    (replace-class ~class-to-remove ~class-to-add)
    identity))

(defn remove-if
  [test]
  (when-not test
    identity))

(defn remove-if-not
  [test]
  (remove-if (not test)))


;; Clojure

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

; (def ^:private valid-symbols
;   #{:template-filename})

; (defn- value
;   "Returns the value bound to the symbol in the given namespace."
;   [page-ns sym]
;   (if-let [valid-ns (find-ns page-ns)]
;     (when-let [valid-sym (valid-symbols sym)]
;       (let [s (intern valid-ns (symbol (name valid-sym)))]
;         (when (bound? s) @s)))
;     (throw (Exception. (str "Namespace not found: " page-ns)))))


;; Bootstrap

(defn glyphicon-icon-cls
  [icon]
  (str "glyphicon-" (name icon)))
