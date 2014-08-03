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

(def this
  "Selector to match the whole node within a transformation snippet."
  [:> html/first-child])

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

(defmacro defn-set-attr
  "Defines 2 top level functions as a helpers to set attr values.
  Ex: (defn-set-attr :href) will create 'set-href and 'when-set-href
  functions in the current namespace. See doc-str of generated functions
  for details."
  [attr-name]
  (let [doc-str (str "\n  Defined with the macro " (first &form) " on namespace " *ns* ", line " (-> &form meta :line) ".")
        set-fn-symbol (symbol (str "set-" (name attr-name)))
        when-set-fn-symbol (symbol (str "when-set-" (name attr-name)))]
    (list 'do
      (list 'defn set-fn-symbol
          (str "Shortcut to (html/set-attr " (keyword attr-name) " (apply str parts))." doc-str)
          '[& parts]
          (list 'println "Setting" attr-name "to value" '(apply str parts)) ;; TODO: Only for dev
          (list 'html/set-attr (keyword attr-name) '(apply str parts)))
      (list 'defn when-set-fn-symbol
          (str "Sets the attr " attr-name " if test is truthy." doc-str)
          '[test & parts]
          (list 'if 'test
            (list 'apply set-fn-symbol 'parts)
            'identity)))))

(defn-set-attr :href)
(defn-set-attr :onclick)
(defn-set-attr :id)
(defn-set-attr :src)
(defn-set-attr :class)


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


;; Bootstrap

(defn glyphicon-icon-cls
  [icon]
  (str "glyphicon-" (name icon)))

(defn set-icon
  [icon]
  (when icon
    (set-class "glyphicon glyphicon-" (name icon))))
