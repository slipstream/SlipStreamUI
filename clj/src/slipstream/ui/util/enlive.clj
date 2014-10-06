(ns slipstream.ui.util.enlive
  (:require [clojure.zip :as z]
            [net.cgrand.enlive-html :as html]
            [net.cgrand.xml :as xml]))

(def this
  "Selector to match the whole node within a transformation snippet.
  It seems it should be [:root] as in http://cgrand.github.io/enlive/syntax.html
  but not:
    user=> (html/select (html/html-snippet '<a></a>') [:> html/first-child])
    ({:tag :a, :attrs nil, :content nil})
    user=> (html/select (html/html-snippet '<a></a>') [:root])
    ()."
  ; [:root]
  ; [identity]
  ; [:*] ;; This obviously selects all nodes.
  ; [:> html/first-child] ;; This works
  html/this-node ;; EUREKA! https://github.com/cgrand/enlive/blob/master/src/net/cgrand/enlive_html.clj#L941
  )

(defn enlive-node?
  "To differenciate between maps that represent enlive-generated nodes from
  normal clojure maps."
  [n]
  (or
    (nil? n)
    (empty? n)
    (string? n)
    (instance? clojure.lang.PersistentStructMap n)
    (instance? clojure.lang.PersistentStructMap (first n))
    (instance? clojure.lang.PersistentStructMap (ffirst n))))

(defn content-when-not-nil
  [content]
  (if (nil? content)
    identity
    (html/content (str content))))

(defmacro if-content
  [test content-if-truthy content-if-falsey]
  `(if ~test
    (html/content (str ~content-if-truthy))
    (html/content (str ~content-if-falsey))))

(defn html-content-when-not-nil
  [html-content]
  (if (nil? html-content)
    identity
    (html/html-content (str html-content))))

(defmacro when-add-class
  "Consider using (enable-class enable? cls) function below, instead of this one,
  since it will also ensure it's not present if enabled? is falsey."
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

(defn enable-class
  "Add the class if 'enable?' and remove it if not. Using (when-add-class) will
  not remove the class if the test is falsey, thus often we want rahter to turn
  on/off the class, rather than only adding it, since it might already be present
  in the node being transformed, e.g. from the template."
  [enable? cls]
  (if enable?
    (html/add-class cls)
    (html/remove-class cls)))

(defn remove-if
  [test]
  (when-not test
    identity))

(defn remove-if-not
  [test]
  (remove-if (not test)))

(defmacro if-enlive-node
  [node form-when-true form-when-false]
  `(if (or (nil? ~node) (ue/enlive-node? ~node))
     ~form-when-true
     ~form-when-false))

(defmacro defn-set-attr
  "Defines 3 top level functions as a helpers to set attr values.
  Ex: (defn-set-attr :href) will create 'set-href, 'if-set-href and 'when-set-href
  functions in the current namespace. See doc-str of generated functions
  for details."
  [attr]
  (let [attr (keyword attr)
        attr-name (name attr)
        doc-str (str "\n  Defined with the macro " (first &form) " on namespace " *ns* ", line " (-> &form meta :line) ".")
        set-fn-symbol (symbol (str "set-" attr-name))
        if-set-fn-symbol (symbol (str "if-set-" attr-name))
        when-set-fn-symbol (symbol (str "when-set-" attr-name))
        toggle-fn-symbol (symbol (str "toggle-" attr-name))
        update-fn-symbol (symbol (str "update-" attr-name))
        append-to-fn-symbol (symbol (str "append-to-" attr-name))
        ;; NOTE: Not using gemsym higienic symbols, since not needed and it makes the code and (doc) weird.
        parts-symbol (symbol "parts")
        f-symbol (symbol "f")
        args-symbol (symbol "args")
        truthy-value-symbol (symbol "value-if-truthy")
        falsey-value-symbol (symbol "value-if-falsey")
        test-symbol (symbol "test")]
    `(do
      (defn ~set-fn-symbol
          ~(str "If parts are not nil, shortcut to (html/set-attr " attr " (apply str parts)).\n"
               "  Note that if parts are nil, attr '" attr-name "' will be removed, instead of setting a\n"
               "  blank string as value, as done by default by enlive."
               doc-str)
          [& ~parts-symbol]
          ; (list 'println "Setting" attr-name "to value" '(apply str ~parts-symbol)) ;; TODO: Only for dev
          (if (apply = nil ~parts-symbol)
             (html/remove-attr ~attr)
             (html/set-attr ~attr (apply str ~parts-symbol))))
      (defn ~if-set-fn-symbol
          ~(str "Sets the attr '" attr-name "' to 'value-if-truthy' if test is truthy, and to"
                " 'value-if-falsey' otherwise."
                "\n  On both branches, a nil value removes the attr '" attr-name "', instead of setting a blank string as value,"
                "\n  as done by default by enlive."
                doc-str)
          [~test-symbol ~truthy-value-symbol ~falsey-value-symbol]
          (if ~test-symbol
            (~set-fn-symbol ~truthy-value-symbol)
            (~set-fn-symbol ~falsey-value-symbol)))
      (defn ~when-set-fn-symbol
          ~(str "Sets the attr '" attr-name "' if test is truthy, and leaves it untouched otherwise."
                "\n  Note that if parts are nil, attr '" attr-name "' will be removed, instead of setting a"
                "\n  blank string as value, as done by default by enlive."
                doc-str)
          [~test-symbol & ~parts-symbol]
          (if ~test-symbol
            (apply ~set-fn-symbol ~parts-symbol)
            identity))
      (defn ~toggle-fn-symbol
          ~(str "Sets the attr '" attr-name "' if test is truthy, and removes it otherwise. Note the difference"
                "\n  with '" (name when-set-fn-symbol) "'. If parts are not provided or they are nil,"
                "\n  the attr '" attr-name "' will be toggled with a blank string as value, instead of removing"
                "\n  it as done by other fns of the '" (name set-fn-symbol) "' family."
                doc-str)
          [~test-symbol & ~parts-symbol]
          (if ~test-symbol
            (~set-fn-symbol (apply str ~parts-symbol))
            (html/remove-attr ~attr)))
      (defn ~update-fn-symbol
          ~(str "Updates the attr '" attr-name "' with update-in semantics."
                doc-str)
          [~f-symbol & ~args-symbol]
          (fn [node#]
            (apply update-in node# [:attrs ~attr] ~f-symbol ~args-symbol)))
      (defn ~append-to-fn-symbol
          ~(str "Adds the parts as string to the attr '" attr-name "'."
                "\n  If parts are not provided or they are nil, this is a no-op."
                doc-str)
          [& ~parts-symbol]
          (apply ~update-fn-symbol str ~parts-symbol))
      )))

(defn-set-attr :href)
(defn-set-attr :onclick)
(defn-set-attr :id)
(defn-set-attr :src)
(defn-set-attr :class)
(defn-set-attr :style)
(defn-set-attr :target)
(defn-set-attr :checked)
(defn-set-attr :disabled)
(defn-set-attr :selected)
(defn-set-attr :value)
(defn-set-attr :title)
(defn-set-attr :placeholder)

(defmacro content-for
  "Replaces the content of the matched node with clones of the child matching
  the 'seed-node-sel' parameter. Useful e.g. to build a table cloning just one row
  of the template and removing the (dummy) other rows. To avoid errors related to
  inline-block displaying, it preserves the original whitespace of the template
  source, if any."
  [seed-node-sel comprehension & forms]
  `(fn [node#]
     (let [seed-node# (html/select node# ~seed-node-sel)
           node-whitespace# (html/select node# (concat this [:> html/whitespace]))
           heading-whitespace# (first node-whitespace#)
           inter-node-whitespace# (-> node-whitespace# butlast second)
           trailing-whitespace# (last node-whitespace#)
           cloned-nodes# (for ~comprehension (html/at seed-node# ~@forms))
           content# (-> (interpose inter-node-whitespace# cloned-nodes#)
                        (conj heading-whitespace#)
                        vec
                        (conj trailing-whitespace#))]
       ((html/content content#) node#))))


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
