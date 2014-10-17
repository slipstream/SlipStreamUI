(ns slipstream.ui.views.common
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]))

(def content-sel [:#content])

(def interaction-sel [:.interaction])

(def interations-template-html "slipstream/ui/views/interations.html")
(def inputs-template-html "slipstream/ui/views/inputs.html")

(def slipstream "Helix Nebula")

(defn title [value]
  (str slipstream " | " value))

(def drop-module-slash-no-of-chars 7)

; Icon

(defn to-css-class
  [category]
  (str (string/lower-case category) "_category"))  

(defmulti type-to-icon-class
  (fn [type]
    type))

(defmethod type-to-icon-class :default
  [type]
  (type-to-icon-class "Deployment"))

(defmethod type-to-icon-class "Build"
  [type]
  "build_type")

(defmethod type-to-icon-class "Deployment"
  [type]
  "deployment_type")

(defmethod type-to-icon-class "Simple Run"
  [type]
  "simple_run_type")

;
; Breadcrumbs
;

(def breadcrumb-template-html "slipstream/ui/views/breadcrumb.html")

(def breadcrumb-sel [:#breadcrumb])

(defn- breadcrumb-href
  "root-uri, e.g. 'module' in the case of modules, or 'user'
   in the case of users"
  [names index root-uri]
  (if (= "" (names index))
    (str "/" root-uri)
    (str "/"
      root-uri
      "/"
      (reduce 
        #(str %1 (if (= "" %1) "" "/") %2) 
        "" 
        (subvec names 0 (inc index))))))
                                 
(defn clone-breadcrumbs
  [name root-uri]
    (html/clone-for 
      [i (range (count (string/split name #"/")))] 
      [:a]
      (let 
        [names (string/split name #"/")
         href (breadcrumb-href names i root-uri)
         short-name (names i)]
        (html/do-> 
          (html/content (if (= "" short-name)
                          root-uri short-name))
          (html/set-attr :href href)))))

(html/defsnippet breadcrumb-snip breadcrumb-template-html breadcrumb-sel
  [name root-uri]
  [[:li (html/nth-of-type 2)]] (html/content
                                 (if (= "s" (last root-uri))
                                        root-uri
                                        (str root-uri "s"))) ; add an s at the end
  [[:li (html/nth-of-type 3)]] (clone-breadcrumbs name root-uri))

;
; Utility
;

(defn set-same-name-attr
  [attr set?]
  (let [key (keyword attr)
        value (name attr)]
    (if set?
      (html/set-attr key value)
      (html/remove-attr key))))

(defn set-disabled
  [disable?]
  (set-same-name-attr :disabled disable?))

(defn set-checked
  [checked?]
  (set-same-name-attr :checked checked?))

(defn set-input
  [name value]
  (html/do->
    (html/set-attr :name name)
    (html/set-attr :value value)))

(defn to-stars
  [clear-string]
  (apply 
    str 
    (take
      (count clear-string)
      (repeat "●"))))

(declare insert-name)
(declare insert-id)
  
(defn gen-select
  "Generate a select/option element, optionaly disabled"
  ([name options selected] (gen-select name options selected false))
  ([name options selected disabled?]
    (html/html-snippet
      (str "<select"
           (insert-name name)
           (insert-id name)
           (when disabled? " disabled='disabled'")
           ">\n" 
           (apply str
                  (for [option options]
                    (str 
                      "<option value='" option "'"
                      (if (= option selected)
                        " selected='selected'"
                        "")
                      ">"
                      option
                      "</option>\n"
                      )))
           "</select>\n"))))

;
; Parameter
;

(defmulti param-val
  "Extract simple value from parameter"
  (fn [parameter]
    (:type (common-model/attrs parameter))))

(defn- param-val-default
  [parameter]
  (-> parameter (html/select [:value]) first :content first))

;(defmethod param-val "Enum"
;  [parameter]
;  (first (:content (first (html/select parameter [:value])))))

(defmethod param-val :default
  [parameter]
  (param-val-default parameter))

(defmethod param-val "Password"
  [parameter]
  (to-stars (param-val-default parameter)))
  
(defmethod param-val "Boolean"
  [parameter]
  (= "true" (param-val-default parameter)))

;
; Runtime Parameter
;

(defmulti runtime-param-val
  (fn [parameter]
    (:type (common-model/attrs parameter))))

(defn- runtime-param-val-default
  [parameter]
  (-> parameter :content first))

(defmethod runtime-param-val :default
  [parameter]
  (runtime-param-val-default parameter))

(defmethod runtime-param-val "Password"
  [parameter]
  (to-stars (runtime-param-val-default parameter)))

(defn runtime-parameter-value
  [parameter]
  (runtime-param-val parameter))

;
; Input element generation (for edit mode)
;

(defn- tr-id
  [category index]
  (str "parameter-" category "--" index))

(defn- input-name-name
  [tr-id]
  (str tr-id "--name"))

(defn- input-name-type
  [tr-id]
  (str tr-id "--type"))

(defn- input-name-category
  [tr-id]
  (str tr-id "--category"))

(defn- input-name-description
  [tr-id]
  (str tr-id "--description"))

(defn- input-name-value
  [tr-id]
  (str tr-id "--value"))

(defn- insert-name
  "Only insert the name attribute if name is set.
   Useful when input (e.g. checkbox) are used in view mode,
   where we're only using it for display and will never be
   sent as part of a form"
  [name]
  (if name
    (str " name='" name "'")
    ""))

(defn- insert-id
  [id]
  (if id
    (str " id='" id "'")
    ""))

(defn- set-input-name-value
  [tr-id]
  (str tr-id "--value"))
  
(defn- set-input-name-category
  [tr-id]
  (str tr-id "--category"))
  

(html/defsnippet input-snip inputs-template-html [:input]
  [name placeholder value type & [auto?]]
  html/this-node (html/do->
                   (html/set-attr :type type)
                   (html/set-attr :name name)
                   (html/set-attr :placeholder placeholder)
                   (html/set-attr :value value)
                   (if-not auto?
                     (html/set-attr :autocomplete "off"))))

(html/defsnippet textarea-snip inputs-template-html [:textarea]
  [name placeholder value]
  html/this-node (html/do->
                   (if name (html/set-attr :name name)) ; only
                   (html/set-attr :placeholder placeholder)
                   (html/content value)))

(defmulti set-input-value
  "Used to generate input element (in edit mode)"
  (fn [parameter tr-id]
    (-> parameter :attrs :type)))

(defn- set-input-value-string
  [parameter tr-id]
  (let [name (set-input-name-value tr-id)
        defaultvalue (-> parameter :attrs :defaultvalue)
        value (param-val parameter)
        type "text"]
    (input-snip name defaultvalue value type)))

(defn- set-input-value-text
  [parameter tr-id]
  (let [name (set-input-name-value tr-id)
        value (param-val parameter)
        defaultvalue (-> parameter :attrs :defaultvalue)]
  (textarea-snip name defaultvalue value)))

(defmethod set-input-value "String"
  [parameter tr-id]
  (set-input-value-string parameter tr-id))

(defmethod set-input-value :default
  [parameter tr-id]
  (set-input-value-string parameter tr-id))

(defmethod set-input-value "RestrictedString"
  [parameter tr-id]
  (set-input-value-string parameter tr-id))

(defmethod set-input-value "Text"
  [parameter tr-id]
  (set-input-value-text parameter tr-id))

(defmethod set-input-value "RestrictedText"
  [parameter tr-id]
  (set-input-value-text parameter tr-id))

(defmethod set-input-value "Password"
  [parameter tr-id]
  ; call default to avoid getting the value instead of stars (*)
  (let [name (set-input-name-value tr-id)
        defaultvalue ""
        value (param-val-default parameter)
        type "password"]
  (input-snip name defaultvalue value type false)))

(defmethod set-input-value "Enum"
  [parameter tr-id]
  (let [options (map 
                  #(-> % :content first) 
                  (html/select parameter [:enumValues :string]))
        selected (param-val parameter)]
    (gen-select (set-input-name-value tr-id) options selected)))

(defmethod set-input-value "Boolean"
  [parameter tr-id]
  (let [value (param-val parameter)]
    (html/html-snippet 
      (str 
        "<input type='checkbox'"
        (insert-name (set-input-name-value tr-id))
        (if (true? value)
          " checked='checked'"
          "")
        " />"))))

(defmulti parameter-value
  (fn [parameter]
    (:type (common-model/attrs parameter))))

(defmethod parameter-value :default
  [parameter]
  (param-val parameter))

(defmethod parameter-value "Boolean"
  [parameter]
  "Generate a checkbox type input element"
  (set-input-value parameter nil))

(defn parameter-help
  [parameter]
  (common-model/instructions parameter))

;
; Parameters
;

(def parameters-view-template-html "slipstream/ui/views/parameters-view.html")
(def parameters-edit-template-html "slipstream/ui/views/parameters-edit.html")
(def parameter-edit-template-html "slipstream/ui/views/parameter-edit.html")

(def parameters-sel [:#parameters])

(defn- clone-parameters-view
  [parameters]
  (html/clone-for
    [parameter parameters
     :let 
     [attrs (module-model/attrs parameter)
      description (:description attrs)
      value (parameter-value parameter)
      help (parameter-help parameter)]]
    [[:td (html/nth-of-type 2)]] (html/content description)
    [[:td (html/nth-of-type 4)]] (html/content value)
    ; if the value is an input, disable it in view mode
    [[:td (html/nth-of-type 4)] :> :input] (html/set-attr :disabled "disabled")
    [[:td (html/nth-of-type 5)] :> :span] (html/content help)
    [[:td (html/nth-of-type 5)]] (if (empty? help)
                                    (html/content "")
                                    identity)
    [[:td (html/nth-of-type 1)]] nil
    [[:td (html/nth-of-type 2)]] nil))

(defn- clone-parameters-view-with-category
  [parameters]
  (html/clone-for
    [parameter parameters
     :let 
     [attrs (module-model/attrs parameter)
      description (:description attrs)
      category (:category attrs)
      value (parameter-value parameter)
      help (parameter-help parameter)]]
    [[:td (html/nth-of-type 2)]] (html/content description)
    [[:td (html/nth-of-type 3)]] (html/content category)
    [[:td (html/nth-of-type 4)]] (html/content value)
    ; if the value is an input, disable it in view mode
    [[:td (html/nth-of-type 4)] :> :input] (html/set-attr :disabled "disabled")
    [[:td (html/nth-of-type 5)] :> :span] (html/content help)
    [[:td (html/nth-of-type 5)]] (if (empty? help)
                                   (html/content "")
                                   identity)
    [[:td (html/nth-of-type 1)]] nil))

(defn- clone-parameters-view-with-name-and-category
  [parameters]
  (html/clone-for
    [parameter parameters
     :let 
     [attrs (module-model/attrs parameter)
      name (:name attrs)
      description (:description attrs)
      category (:category attrs)
      value (parameter-value parameter)
      help (parameter-help parameter)]]
    [[:td (html/nth-of-type 1)]] (html/content name)
    [[:td (html/nth-of-type 2)]] (html/content description)
    [[:td (html/nth-of-type 3)]] (html/content category)
    [[:td (html/nth-of-type 4)]] (html/content value)
    ; if the value is an input, disable it in view mode
    [[:td (html/nth-of-type 4)] :> :input] (html/set-attr :disabled "disabled")
    [[:td (html/nth-of-type 5)] :> :span] (html/content help)
    [[:td (html/nth-of-type 5)]] (if (empty? help)
                                   (html/content "")
                                   identity)))

(html/defsnippet clone-parameters-edit-value-snip parameter-edit-template-html [:#parameter-description-edit-value :> :table :> :tbody :> :tr]
  [parameters]
  (html/clone-for
    [i (range (count parameters))
     :let [parameter (nth parameters i)
           attrs (common-model/attrs parameter)
           name (:name attrs)
           category (:category attrs)
           description (:description attrs)
           type (:type attrs)
           defaultvalue (:defaultvalue attrs)
           tr-id (tr-id name i)
           readonly? (= "true" (:readonly attrs))
           value (set-input-value parameter tr-id)
           help (parameter-help parameter)]]
    html/this-node (html/set-attr :id tr-id)
    [[:td (html/nth-of-type 1)] :> :span] (html/content description)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 1)]] 
      (set-input 
        (input-name-name tr-id)
        name)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 2)]] 
      (set-input 
        (input-name-category tr-id)
        category)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 3)]] 
      (set-input 
        (input-name-type tr-id)
        type)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 4)]] 
      (set-input 
        (input-name-description tr-id)
        description)
    [[:td (html/nth-of-type 2)] :> :input] 
        (if readonly?
          (html/do-> (html/substitute value)
                     (html/set-attr :disabled "disabled"))
          (html/substitute value))
    [[:td (html/nth-of-type 3)] :> :span] (html/content help)
    [[:td (html/nth-of-type 3)]] (if (empty? help)
                                   (html/content "")
                                   identity)))

(html/defsnippet clone-parameters-edit-all-snip parameter-edit-template-html [:#parameter-edit-all :> :table :> :tbody :> :tr]
  [parameters]
  (html/clone-for
    [i (range (count parameters))
     :let [parameter (nth parameters i)
           attrs (common-model/attrs parameter)
           name (:name attrs)
           category (:category attrs)
           tr-id (tr-id name i)
           category-select (gen-select
                             (set-input-name-category tr-id) 
                             ["Input" "Output"]
                             category)
           description (:description attrs)
           type (:type attrs)
           defaultvalue (:defaultvalue attrs)
           value (set-input-value parameter tr-id)
           mandatory? (= "true" (:mandatory attrs))]]
    html/this-node (html/set-attr :id tr-id)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 1)]] 
      (set-input 
        (input-name-name tr-id)
        name)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 2)]] 
      (set-input 
        (input-name-type tr-id)
        type)
    [[:td (html/nth-of-type 2)] :> :input] 
      (set-input 
        (input-name-description tr-id)
        description)
    [[:td (html/nth-of-type 3)]] 
      (html/content category-select)
    [[:td (html/nth-of-type 4)]] 
      (html/content value)
    [:td :> #{[:input] [:select]}] (if mandatory?
                                     (html/set-attr :disabled "disabled")
                                     identity)
    [[:td html/last-of-type]] (if mandatory?
                                (html/content "")
                                identity)))

(html/defsnippet clone-parameters-show-all-edit-only-value-snip parameter-edit-template-html [:#parameter-edit-all :> :table :> :tbody :> :tr]
  [parameters]
  (html/clone-for
    [i (range (count parameters))
     :let [parameter (nth parameters i)
           attrs (common-model/attrs parameter)
           name (:name attrs)
           category (:category attrs)
           tr-id (tr-id name i)
           description (:description attrs)
           type (:type attrs)
           defaultvalue (:defaultvalue attrs)
           value (set-input-value parameter tr-id)
           mandatory? (= "true" (:mandatory attrs))]]
    html/this-node (html/set-attr :id tr-id)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 1)]] 
      (html/do-> (set-input 
                   (input-name-name tr-id)
                   name)
                 (html/set-attr :readonly "readonly"))
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 2)]] 
      (html/do-> (set-input 
                   (input-name-type tr-id)
                   type)
                 (html/set-attr :readonly "readonly"))
    [[:td (html/nth-of-type 2)] :> :input] 
      (html/do-> (set-input 
                   (input-name-description tr-id)
                   description)
                 (html/set-attr :readonly "readonly"))
    [[:td (html/nth-of-type 3)]] 
      (html/do-> (html/content category)
                 (html/set-attr :readonly "readonly"))
    [[:td (html/nth-of-type 4)]] 
      (html/content value)
    [:td :> #{[:select]}] (if mandatory?
                      (html/set-attr :disabled "disabled")
                      identity)
    [[:td html/last-of-type]] (if mandatory?
                                (html/content "")
                                identity)))

(html/defsnippet parameters-view-with-category-snip parameters-view-template-html [:#fragment-parameters-something]
  [parameters with-add-parameter-button?]
  [:table :> :thead :> [:th (html/nth-of-type 1)]] nil
  [:table :> :tbody :> :tr] (clone-parameters-view-with-category parameters))

(html/defsnippet parameters-view-snip parameters-view-template-html [:#fragment-parameters-something :> :table]
  [parameters with-add-parameter-button?]
  [:thead :> :tr :> [:th (html/nth-of-type 1)]] nil
  [:thead :> :tr :> [:th (html/nth-of-type 2)]] nil
  [:tbody :> :tr] (clone-parameters-view parameters))

(html/defsnippet parameters-view-with-name-and-category-snip parameters-view-template-html [:#fragment-parameters-something]
  [parameters with-add-parameter-button?]
  [:table :> :tbody :> :tr] (clone-parameters-view-with-name-and-category parameters))

(html/defsnippet parameters-edit-with-category-snip parameters-edit-template-html [:#fragment-parameters-something]
  [parameters with-add-parameter-button?]
  [:table :> :thead :> [:th (html/nth-of-type 1)]] nil
  [:table :> :tbody :> :tr] (clone-parameters-view-with-category parameters)
  [:button] (if with-add-parameter-button? identity nil))

(html/defsnippet parameters-edit-snip parameters-edit-template-html [:#fragment-parameters-something]
  [parameters with-add-parameter-button?]
  [:table :> :thead :> :tr :> [:th (html/nth-of-type 1)]] nil
  [:table :> :thead :> :tr :> [:th (html/nth-of-type 2)]] nil
  [:table :> :tbody :> :tr] (html/substitute (clone-parameters-edit-value-snip parameters))
  [:button] (if with-add-parameter-button? identity nil))

(html/defsnippet parameters-edit-all-snip parameters-edit-template-html [:#fragment-parameters-something]
  [parameters with-add-parameter-button?]
  [:table :> :tbody :> :tr] (html/substitute (clone-parameters-edit-all-snip parameters))
  [:button] (if with-add-parameter-button? identity nil))

(html/defsnippet parameters-show-all-edit-only-value-snip parameters-edit-template-html [:#fragment-parameters-something]
  [parameters with-add-parameter-button?]
  [:table :> :tbody :> :tr] (html/substitute (clone-parameters-show-all-edit-only-value-snip parameters))
  [:button] (if with-add-parameter-button? identity nil))

;; Generic tabs layout generation

(defn tab-headers
  "Generate the tabs headers section for elements in grouped-by using
   the fragment-name name for the ids."
  [grouped-by fragment-name]
  (html/clone-for
    [group (keys grouped-by)]
    [:a] (html/do->
           (html/set-attr :href (str "#fragment-" fragment-name "-" group))
           (html/content group))))

(defn tab-sections-with-add-button-option
  "Same as tab-sections, but with add button"
  [grouped-by fragment-name snip-fn add-button?]
  (html/clone-for
    [group grouped-by]
    (html/do->
      (html/set-attr :id (str "fragment-" fragment-name "-" (key group)))
      (html/content (snip-fn (val group) add-button?)))))

(defn tab-sections
  "Generate the tab sections for grouped-by items, in the
   fragment-name fragment section and apply the snip function for each
   item"
  [grouped-by fragment-name snip-fn]
  (tab-sections-with-add-button-option grouped-by fragment-name snip-fn false))

;; Parameter tabs section

(defn define-tabs-for-parameters
  [parameters-grouped-by-category]
  (tab-headers parameters-grouped-by-category "parameters"))

(defn define-tab-sections-for-parameters-view 
  [parameters-grouped-by-category]
  (tab-sections parameters-grouped-by-category "parameters" parameters-view-snip))

(defn define-tab-sections-for-parameters-edit
  [parameters-grouped-by-category]
  (html/clone-for
    [grouped parameters-grouped-by-category]
    (html/do->
      (html/set-attr :id (str "fragment-parameters-" (key grouped)))
      (html/content (parameters-edit-snip (val grouped) false)))))

(html/defsnippet parameters-view-tabs-by-category-snip parameters-view-template-html parameters-sel
  [parameters-grouped-by-category]
  [:ul :> :li] (define-tabs-for-parameters parameters-grouped-by-category) 
  [:#fragment-parameters-something]
    (define-tab-sections-for-parameters-view parameters-grouped-by-category))

(html/defsnippet parameters-edit-tabs-by-category-snip parameters-view-template-html parameters-sel
  [parameters-grouped-by-category]
  [:ul :> :li] (define-tabs-for-parameters parameters-grouped-by-category) 
  [:#fragment-parameters-something]
    (define-tab-sections-for-parameters-edit parameters-grouped-by-category))

(defn emtpy-section
  "Insert a message when the section is empty"
  [message]
  (html/do->
    (html/content message)
    (html/add-class "empty-section")))

(defn ellipse-left
  [s max]
  (let [l (count s)
        pre "..."
        pre-l (count pre)]
  (if (> l max)
    (str "..." (apply str (take-last (- max pre-l) s)))
    s)))

(defn ellipse-right
  [s max]
  (let [l (count s)
        post "..."
        post-l (count post)]
  (if (> l max)
    (str (apply str (take (- max post-l) s)) "...")
    s)))

