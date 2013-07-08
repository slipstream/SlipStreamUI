(ns slipstream.ui.views.common
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]))

(def content-sel [:#content])

(def interaction-sel [:.interaction])

(def interations-template-html "slipstream/ui/views/interations.html")

(def slipstream-with-trademark "SlipStream™")

(defn title [value]
  (str slipstream-with-trademark " | " value))

(defn hidden-input-elem [value id]
  "Returns a value + hidden input element"
  (html/html-content
    (str value "<input name='name' id='" id "' type='hidden' value='" value "'>")))

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

(defn to-stars
  [clear-string]
  (apply 
    str 
    (take
      (count clear-string)
      (repeat "●"))))

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

(defmethod param-val "Enum"
  [parameter]
  (first (:content (first (html/select parameter [:value])))))

(defmethod param-val :default
  [parameter]
  (param-val-default parameter))

(defmethod param-val "Password"
  [parameter]
  (to-stars (param-val-default parameter)))
  
(defmethod param-val "Dummy"
  [parameter]
  nil)

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
  "Only insert the name attribute if tr-id is set.
   Useful when input (e.g. checkbox) are used in view mode,
   where we're only using it for display and will never be
   sent as part of a form"
  [tr-id]
  (if tr-id
    (str " name='" 
    (input-name-value tr-id) "'")
    ""))

(defmulti set-input-value
  "Used to generate input element (in edit mode)"
  (fn [parameter tr-id]
    (-> parameter :attrs :type)))

(defn- set-input-value-string
  [parameter tr-id]
  (let [value (param-val parameter)
        defaultvalue (-> parameter :attrs :defaultvalue)]
  (html/html-snippet 
    (str 
      "<input type='text'"
      (insert-name tr-id)
      " placeholder='" 
      defaultvalue 
      "' value='" 
      value 
      "' />"))))

(defn- set-input-value-text
  [parameter tr-id]
  (let [value (param-val parameter)
        defaultvalue (-> parameter :attrs :defaultvalue)]
  (html/html-snippet 
    (str 
      "<textarea"
      (insert-name tr-id)
      " placeholder='" defaultvalue
      "'>" value "</textarea>"))))

(defmethod set-input-value "Dummy"
  [parameter tr-id]
  (set-input-value-string parameter tr-id))

(defmethod set-input-value "String"
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
  (let [value (param-val parameter)]
  (html/html-snippet 
    (str 
      "<input type='password'" 
      (insert-name tr-id)
      " value='" 
      value 
      "' />"))))

(defmethod set-input-value "Enum"
  [parameter tr-id]
  (let [options (map 
                  #(-> % :content first) 
                  (html/select parameter [:enumValues :string]))
        selected (param-val parameter)]
    (html/html-snippet
      (str "<select"
           (insert-name tr-id)
           ">\n" 
           (apply str
                  (for [option options]
                    (str 
                      "<option value='" option "'"
                      (if (= option selected)
                        " selected"
                        "")
                      "'>"
                      option
                      "</option>\n"
                      )))
           "</select>\n"))))

(defmethod set-input-value "Boolean"
  [parameter tr-id]
  (let [value (param-val parameter)]
    (html/html-snippet 
      (str 
        "<input type='checkbox'"
        (insert-name tr-id)
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
      value (parameter-value parameter)]]
    [[:td (html/nth-of-type 1)]] (html/content description)
    [[:td (html/nth-of-type 2)]] (html/content value)
    ; if the value is an input, disable it in view mode
    [[:td (html/nth-of-type 2)] :> :input] (html/set-attr :disabled "disabled")
    [[:td (html/nth-of-type 3)]] (html/content "")
    [[:td html/last-of-type]] nil
    [[:td html/last-of-type]] nil))

(defn- clone-parameters-view-with-category
  [parameters]
  (html/clone-for
    [parameter parameters
     :let 
     [attrs (module-model/attrs parameter)
      description (:description attrs)
      category (:category attrs)
      value (parameter-value parameter)]]
    [[:td (html/nth-of-type 1)]] (html/content description)
    [[:td (html/nth-of-type 2)]] (html/content category)
    [[:td (html/nth-of-type 3)]] (html/content value)
    ; if the value is an input, disable it in view mode
    [[:td (html/nth-of-type 3)] :> :input] (html/set-attr :disabled "disabled")
    [[:td html/last-of-type]] nil))

(defn- clone-parameters-view-with-name-and-category
  [parameters]
  (html/clone-for
    [parameter parameters
     :let 
     [attrs (module-model/attrs parameter)
      name (:name attrs)
      description (:description attrs)
      category (:category attrs)
      value (parameter-value parameter)]]
    [[:td (html/nth-of-type 1)]] (html/content name)
    [[:td (html/nth-of-type 2)]] (html/content description)
    [[:td (html/nth-of-type 3)]] (html/content category)
    [[:td (html/nth-of-type 4)]] (html/content value)
    ; if the value is an input, disable it in view mode
    [[:td (html/nth-of-type 4)] :> :input] (html/set-attr :disabled "disabled")))

(html/defsnippet clone-parameters-edit-snip parameter-edit-template-html [:#parameter :> :table :> :tbody :> :tr]
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
           tr-id (tr-id category i)
           value (set-input-value parameter tr-id)]]
    html/this-node (html/set-attr :id tr-id)
    [:td :> :span] (html/content description)
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 1)]] 
      (html/do->
        (html/set-attr :name (input-name-name tr-id))
        (html/set-attr :value name))
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 2)]] 
      (html/do->
        (html/set-attr :name (input-name-category tr-id))
        (html/set-attr :value category))
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 3)]] 
      (html/do->
        (html/set-attr :name (input-name-type tr-id))
        (html/set-attr :value type))
    [[:td (html/nth-of-type 1)] :> [:input (html/nth-of-type 4)]] 
      (html/do->
        (html/set-attr :name (input-name-description tr-id))
        (html/set-attr :value description))
    [[:td (html/nth-of-type 2)] :> [:input (html/nth-of-type 1)]] 
      (html/substitute value)))

(defn- clone-parameters-edit-with-name-and-category
  [parameters]
  (html/clone-for
    [parameter parameters
     :let 
     [attrs (module-model/attrs parameter)
      name (:name attrs)
      description (:description attrs)
      category (:category attrs)
      value (set-input-value parameter)]]
    [[:td (html/nth-of-type 1)]] (html/content name)
    [[:td (html/nth-of-type 2)]] (html/content description)
    [[:td (html/nth-of-type 3)]] (html/content category)
    [[:td (html/nth-of-type 4)]] (html/content value)))

(html/defsnippet parameters-view-with-category-snip parameters-view-template-html [:#fragment-parameters-something]
  [parameters]
  [:table :> :thead :> [:th (html/nth-of-type 1)]] nil
  [:table :> :tbody :> :tr] (clone-parameters-view-with-category parameters))

(html/defsnippet parameters-view-snip parameters-view-template-html [:#fragment-parameters-something :> :table]
  [parameters]
  [:thead :> :tr :> [:th (html/nth-of-type 1)]] nil
  [:thead :> :tr :> [:th (html/nth-of-type 2)]] nil
  [:tbody :> :tr] (clone-parameters-view parameters))

(html/defsnippet parameters-view-with-name-and-category-snip parameters-view-template-html [:#fragment-parameters-something]
  [parameters]
  [:table :> :thead] identity
  [:table :> :tbody :> :tr] (clone-parameters-view-with-name-and-category parameters))

(html/defsnippet parameters-edit-with-category-snip parameters-edit-template-html [:#fragment-parameters-something]
  [parameters]
  [:table :> :thead :> [:th (html/nth-of-type 1)]] nil
  [:table :> :tbody :> :tr] (clone-parameters-view-with-category parameters))

(html/defsnippet parameters-edit-snip parameters-edit-template-html [:#fragment-parameters-something]
  [parameters]
  [:table :> :thead :> :tr :> [:th (html/nth-of-type 1)]] nil
  [:table :> :thead :> :tr :> [:th (html/nth-of-type 2)]] nil
  [:table :> :tbody :> :tr] (html/substitute (clone-parameters-edit-snip parameters)))

(html/defsnippet parameters-edit-with-name-and-category-snip parameters-edit-template-html [:#fragment-parameters-something]
  [parameters]
  [:table :> :thead] identity
  [:table :> :tbody :> :tr] (clone-parameters-edit-with-name-and-category parameters))

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

(defn tab-sections
  "Generate the tab sections for grouped-by items, in the
   fragment-name fragment section and apply the snip function for each
   item"
  [grouped-by fragment-name snip]
  (html/clone-for
    [group grouped-by]
    (html/do->
      (html/set-attr :id (str "fragment-" fragment-name "-" (key group)))
      (html/content (snip (val group))))))

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
      (html/content (parameters-edit-snip (val grouped))))))

(html/defsnippet parameters-view-tabs-by-category-snip parameters-view-template-html parameters-sel
  [parameters-grouped-by-category]
  ;image-cloud-configuration-sel (html/remove-attr :id)
  [:ul :> :li] (define-tabs-for-parameters parameters-grouped-by-category) 
  [:#fragment-parameters-something]
    (define-tab-sections-for-parameters-view parameters-grouped-by-category))

(html/defsnippet parameters-edit-tabs-by-category-snip parameters-view-template-html parameters-sel
  [parameters-grouped-by-category]
  ;image-cloud-configuration-sel (html/remove-attr :id)
  [:ul :> :li] (define-tabs-for-parameters parameters-grouped-by-category) 
  [:#fragment-parameters-something]
    (define-tab-sections-for-parameters-edit parameters-grouped-by-category))

(defn emtpy-section
  "Insert a message when the section is empty"
  [message]
  (html/do->
    (html/content message)
    (html/add-class "empty-section")))
