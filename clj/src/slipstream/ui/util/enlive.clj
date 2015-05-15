(ns slipstream.ui.util.enlive
  (:require [clojure.zip :as z]
            [net.cgrand.enlive-html :as html]
            [net.cgrand.xml :as xml]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.pattern :as pattern]))

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

;; TODO: Refactor content-when-not-nil, if-content and when-content to make them
;;       consistent with set-attr-* fn family

(defn content-when-not-nil
  [content]
  (if (nil? content)
    identity
    (html/content content)))

(defmacro if-content
  [test content-if-truthy content-if-falsey]
  `(if ~test
    (html/content ~content-if-truthy)
    (html/content ~content-if-falsey)))

(defmacro when-content
  ([content]
   `(when-content (not-empty ~content) ~content))
  ([test content-if-truthy]
   `(when ~test
     (html/content ~content-if-truthy))))

(defmacro when-append
  ([content]
   `(when-append (not-empty ~content) ~content))
  ([test content-if-truthy]
   `(if ~test
      (html/append ~content-if-truthy)
      identity)))

(defmacro when-prepend
  ([content]
   `(when-prepend (not-empty ~content) ~content))
  ([test content-if-truthy]
   `(if ~test
      (html/prepend ~content-if-truthy)
      identity)))

(defmacro when-after
  ([content]
   `(when-after (not-empty ~content) ~content))
  ([test content-if-truthy]
   `(if ~test
      (html/after ~content-if-truthy)
      identity)))

(defn html-content-when-not-nil
  [html-content]
  (if (nil? html-content)
    identity
    (html/html-content (str html-content))))

(defmacro when-wrap
  ([wrap-in-elem]
   `(if (not-empty ~wrap-in-elem)
     (apply html/wrap ~wrap-in-elem)
     identity))
  ([test wrapping-tag-if-truthy & attrs-map]
   `(if ~test
     (apply html/wrap ~wrapping-tag-if-truthy [~@attrs-map])
     identity)))

(defmacro when-add-class
  "Consider using (enable-class enable? cls) function below, instead of this one,
  since it will also ensure it's not present if enabled? is falsey."
  ([cls]
    `(when-add-class (not-empty ~cls) ~cls))
  ([test & classes]
    `(if ~test
      (html/add-class ~@classes)
      identity)))

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
  `(if (or (nil? ~node) (enlive-node? ~node))
     ~form-when-true
     ~form-when-false))

(defmacro at-match
  [& transformations]
  `(fn [match#]
     (html/at match#
       ~@transformations)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Data fn family

(defn set-data
  "See tests for expectations."
  [k v]
  (let [k-data (->> k name (str "data-") keyword)
        v-json (cond
                 (string? v)  v
                 (coll? v)    (uc/->json (not-empty v))
                 :else        (uc/->json v))]
    (if (not-empty v-json)
      (html/set-attr k-data v-json)
      (html/remove-attr k-data))))

(defmacro when-set-data
  ([k v]
   `(when-set-data ~k (if (coll? ~v) (not-empty ~v) ~v) ~v))
  ([k test v]
   `(if ~test
      (set-data ~k ~v)
      identity)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; defn-set-attr familiy

(defmacro defn-set-attr
  "Defines 6 top level functions as a helpers to set attr values.
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
        prepend-to-fn-symbol (symbol (str "prepend-to-" attr-name))
        when-prepend-to-fn-symbol (symbol (str "when-prepend-to-" attr-name))
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
          ([~test-symbol]
            (~when-set-fn-symbol (-> ~test-symbol str not-empty) ~test-symbol))
          ([~test-symbol & ~parts-symbol]
                    (if ~test-symbol
                      (apply ~set-fn-symbol ~parts-symbol)
                      identity)))
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
      (defn ~prepend-to-fn-symbol
          ~(str "Inserts the parts as string at the begining of the attr '" attr-name "'."
                "\n  If parts are not provided or they are nil, this is a no-op."
                doc-str)
          [& ~parts-symbol]
          (~update-fn-symbol #(str (apply str ~parts-symbol) %)))
      (defn ~when-prepend-to-fn-symbol
          ~(str "When test is truthy, inserts the parts as string at the begining of the attr '" attr-name "'."
                "\n  If parts are not provided or they are nil, this is a no-op."
                doc-str)
          ([~test-symbol]
            (~when-prepend-to-fn-symbol (-> ~test-symbol str not-empty) ~test-symbol))
          ([~test-symbol & ~parts-symbol]
            (if ~test-symbol
              (apply ~prepend-to-fn-symbol ~parts-symbol)
              identity)))
      )))

(defn-set-attr :checked)
(defn-set-attr :class)
(defn-set-attr :colspan)
(defn-set-attr :content)
(defn-set-attr :disabled)
(defn-set-attr :disabled-reason)
(defn-set-attr :href)
(defn-set-attr :id)
(defn-set-attr :max)
(defn-set-attr :min)
(defn-set-attr :name)
(defn-set-attr :onclick)
(defn-set-attr :placeholder)
(defn-set-attr :readonly)
(defn-set-attr :rel)
(defn-set-attr :selected)
(defn-set-attr :src)
(defn-set-attr :style)
(defn-set-attr :target)
(defn-set-attr :title)
(defn-set-attr :type)
(defn-set-attr :value)

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


;; Blank node generation

(defmulti blank-node
  "Generate an blank enlive node of the given tag, as a keyword. To generate also
  the blank parent(s) node(s) provide a vector of tags. See tests for expectations."
  (fn [tag-or-tags & _]
    (if (keyword? tag-or-tags)
      :tag
      :tag-nesting)))

(defmethod blank-node :tag
  [tag & {:keys [id class]}]
  (html/html-snippet
    (format "<%s%s%s></%s>"
            (name tag)
            (str (when id (format " id=\"%s\"" id)))
            (str (when class (format " class=\"%s\"" class)))
            (name tag))))

(defmethod blank-node :tag-nesting
  [[tag & nested-tags] & last-tag-args]
  (if nested-tags
    (html/at (blank-node tag)
      this (html/content (apply blank-node nested-tags last-tag-args)))
    (apply blank-node tag last-tag-args)))

(defmacro def-blank-snippet
  "Generates a normal enlive snippet fn for a given tag (or tag hierarchy) without
  providing a template html file. See function slipstream.ui.util.enlive/blank-node
  and tests for expectations."
  [name-symbol tag-or-tags args & body]
  `(def ~name-symbol
    (html/snippet* (blank-node ~tag-or-tags)
      ~args
      ~@body)))


;; Generic blank snips
;; See tests for expectations

(def-blank-snippet text-div-snip :div
  [text & {:keys [css-class]}]
  this (set-class css-class)
  this (html/content (str text)))

(def-blank-snippet map->meta-tag-snip :meta
  [m & {:keys [name-prefix]}]
  this (html/clone-for [[k v] (uc/->sorted m)]
         this (set-name (name k))
         this (->> k name (str name-prefix) set-name)
         this (-> v str  set-content)))

(def-blank-snippet text-with-tooltip-snip :span
  [ & {:keys [text tooltip class placement]}]
  this (html/html-content   (str text))
  this (when-add-class      class)
  this (set-data :toggle    "tooltip")
  this (set-data :placement (or placement "bottom"))
  this (set-title           (str tooltip)))

(def-blank-snippet text-with-popover-snip :span
  [ & {:keys [text title content class placement html]}]
  this (html/html-content   (str text))
  this (when-add-class      class)
  this (set-data :toggle    "popover")
  this (set-data :trigger   "hover")
  this (set-data :placement (or placement "bottom"))
  this (set-data :html      (or (boolean html) false))
  this (set-title           (str title))
  this (set-data :content   (str content)))


;; Add input validation to form input fields.

(def input-to-validate-cls "ss-input-needs-validation")
(def required-input-cls "ss-required-input")
(def input-has-requirements-cls "ss-input-has-requirements")

(defn add-requirements
  "See tests for expectations."
  [{:keys [required? validation]}]
  (if (or required? (not-empty validation))
    (let [required? (or required? (-> validation :requirements pattern/requires-not-empty?))]
      (fn [match]
          (html/at match
            ;; NOTE: We target the [:input] and [:textarea] specifically since 'this' (i.e. 'match') might select
            ;;       a div.input-group containing itself an input. However, what we need to wrap
            ;;       within a div.form-group is the top level tag, either input or div.input-group.
            [#{:input :textarea}]  (html/add-class input-to-validate-cls)
            [#{:input :textarea}]  (enable-class required? required-input-cls)
            [#{:input :textarea}]  (enable-class (not-empty validation) input-has-requirements-cls)
            [#{:input :textarea}]  (set-data :validation validation)
             ; NOTE: Bootstrap requires .form-group to be able to flag the input as non valid,
             ;       and .has-feedback if the input also shows an icon for the validation state.
            this      (html/wrap :div {:class "form-group has-feedback ss-form-group-with-validation"})
            this      (html/append (blank-node :span :class "ss-validation-help-hint help-block hidden"))
            this      (html/append (blank-node :span :class "glyphicon glyphicon-ok form-control-feedback hidden")))))
    identity))
