(ns slipstream.ui.views.utils
  (:require [net.cgrand.enlive-html :as html]))

;; SlipStream

;; TODO: Look at slipstream.ui.views.module-base/ischooser? and refactor.
(defn chooser?
  [type]
  (= "chooser" type))

;; TODO: Look at slipstream.ui.views.common/slipstream and refactor.
(def slipstream "SlipStream")

;; TODO: Look at slipstream.ui.views.common/title and refactor.
(defn page-title
  [s]
  (if s
    (str slipstream " | " s)
    slipstream))

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

(defmacro when-add-class
  [test & classes]
  `(if ~test
    (html/add-class ~@classes)
    identity))

(defn replace-class
  [class-to-remove class-to-add]
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

(defmacro if-enlive-node
  [node form-when-true form-when-false]
  `(if (or (nil? ~node) (u/enlive-node? ~node))
     ~form-when-true
     ~form-when-false))

(defn set-href
  [uri]
  (html/set-attr :href uri))

(defn set-id
  [id]
  (html/set-attr :id id))

(defn style
  [node]
  (html/attr-values node :style))

(defn set-style
  [style]
  (html/set-attr :style style))



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
