(ns slipstream.ui.views.utils
  (:require [net.cgrand.enlive-html :as html]
            [clojure.zip :as z]
            [net.cgrand.xml :as xml]))

; TODO: Consider splitting this namespace into three:
;         - slipstream.ui.views.util.core
;         - slipstream.ui.views.util.enlive
;         - slipstream.ui.views.util.bootstrap

;; Clojure

(defmacro def-this-ns
  "Defines a private top level var with the namespace string of the current file."
  []
  `(def ^:private ^:const ~(symbol "this-ns") (str *ns*)))

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
  "Selector to match the whole node within a transformation snippet.
  It seems it should be [:root] as in http://cgrand.github.io/enlive/syntax.html
  but not:
    user=> (html/select (html/html-snippet '<a></a>') [:> html/first-child])
    ({:tag :a, :attrs nil, :content nil})
    user=> (html/select (html/html-snippet '<a></a>') [:root])
    ().
  I found it! It's [:*]"
  ; [:root]
  ; [:> html/first-child] ;; This works
  [:*]
  )

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



; NOTE: Next lines inspired from
;       https://github.com/cgrand/enlive/blob/master/src/net/cgrand/enlive_html.clj#L838-L878

(defn- pred-attr-value
  [attr value]
  #(boolean
      (re-matches
        (re-pattern (str "(?:.*\\s|^)" value "(?:$|\\s.*)"))
        (get-in % [:attrs (keyword attr)] ""))))

(defn- nth-with-attr-value?
  [attr value f a b]
  (let [pred (pred-attr-value attr value)]
    (if (zero? a)
      #(and (-> % z/node pred)
            (= (->> % f (filter pred) count inc) b))
      #(let [an+b (->> % f (filter pred) count inc)
             an (- an+b b)]
        (and (zero? (rem an a)) (<= 0 (quot an a)))))))

(defn nth-of-attr
 "Selector step, tests if the node has an+b-1 siblings of the same attr value on its left. Something like the missing CSS :nth-of-class."
 ([attr value b] (nth-of-attr attr value 0 b))
 ([attr value a b] (html/zip-pred (nth-with-attr-value? attr value z/lefts a b))))

(defn nth-last-of-attr
 "Selector step, tests if the node has an+b-1 siblings of the same attr value on its right.Something like the missinge CSS :nth-laclassf-type."
 ([attr value b] (nth-last-of-attr attr value 0 b))
 ([attr value a b] (html/zip-pred (nth-with-attr-value? attr value z/rights a b))))

(def first-of-attr #(nth-of-attr %1 %2 1))
(def last-of-attr #(nth-last-of-attr %1 %2 1))

(def first-of-class (partial first-of-attr :class))
(def last-of-class (partial last-of-attr :class))

(def first-of-id (partial first-of-attr :id))
(def last-of-id (partial last-of-attr :id))


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
