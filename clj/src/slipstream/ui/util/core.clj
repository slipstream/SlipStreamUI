(ns slipstream.ui.util.core
  "Util functions only related to the SlipStream application."
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [clj-json.core :as json]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.config :as config]))

(localization/def-scoped-t)

(defn template-path-for
  [name]
  (str @config/template-namespace "/html/" name))

(defn page-title
  [s]
  (if (not-empty s)
    (str (t :page-title.application-name) " | " s)
    (t :page-title.application-name)))

(defn ensure-one-selected
  "If no section in the section-coll is selected, this will ensure that at least
  the first one is. See tests for expectations."
  [sections-coll]
  (if (some :selected? sections-coll)
    sections-coll
    (-> sections-coll
        vec
        (assoc-in [0 :selected?] true))))

(defn clojurify-raw-metadata-str
  "raw-metadata-str is assumed trimmed. See test for expectations."
  [raw-metadata-str]
  (if (->> raw-metadata-str first (= \{))
    (json/parse-string raw-metadata-str true)
    (first (html/html-snippet raw-metadata-str))))


;; Enum

(defn- toggle-option
  [selected-option option]
  (if (and selected-option
           (= (:value option) selected-option))
    (assoc option :selected? true)
    (dissoc option :selected?)))

(defn enum-selection
  "Returns the selected enum option. See tests for expectations."
  [enum]
  (->> enum
       (filter :selected?)
       first))

(defn- type-enum
  [enum]
  (with-meta enum {:type :enum}))

(def ^:private enums-with-localization
  "By default we display the values itselves in the combobox of a 'select' form
  input. However, for some known enum parameters we use de localized string."
  #{:cloud-platforms                ; Values in slipstream.ui.models.module.image/platforms
    :general-verbosity-level        ; Values: ["0" "1" "2" "3"]
    :deployment-parameter-category  ; Values: ["Output" "Input"] in slipstream.ui.views.tables/deployment-parameter-row
    :mapping-options                ; Values: [:parameter.bind-to-output :parameter.bind-to-value]
    :atos-ip-type                   ; Values: ["public" "local" "private"]
    :network                        ; Values: ["Public" "Private"]
    :cloudsigma-location})          ; Values: ["LVS" "ZRH"]

(defn- enum-text
  [enum-name option]
  (if-not (enums-with-localization enum-name)
    option
    (->> option
         uc/keywordize
         name
         (str "enum.option.text." (name enum-name) ".")
         keyword
         t)))

(defn- enum-value
  [option]
  (when option
    (if (string? option)
      option
      (-> option
          uc/keywordize
          name))))

(defn enum-select
  "If the 'selected-option is not available, the first one will be selected."
  [enum selected-option]
  (->> enum
       (map (partial toggle-option (enum-value selected-option)))
       ensure-one-selected
       type-enum))

(defn- parse-enum-option
  [enum-name option]
  {:value (enum-value option), :text (enum-text enum-name option)})

(defn enum
  [options enum-name & [selected-option]]
  (let [enum-base (map (partial parse-enum-option enum-name) options)]
    (-> enum-base
        (enum-select (or selected-option (first options))))))

(defn assoc-enum-details
  [m parameter]
  (if-not (-> m :type (= "Enum"))
    m
    (let [enum-name (-> parameter :attrs :name uc/keywordize)
          option-current (:value m)
          option-default (-> parameter
                            (html/select [:defaultValue html/text-node])
                            first)
          enum-options (-> parameter
                           (html/select [:enumValues :string html/text-node])
                           vec)]
      (assoc m :value (enum enum-options enum-name (or option-current option-default))))))

;; Boolean parameter

(defn normalize-value
  [m]
  (if-not (-> m :type (= "Boolean"))
    m
    (update-in m [:value] uc/parse-boolean)))

;; Parameter order

(defn order
  [parameter]
  (-> parameter
      (get-in [:attrs :order])
      uc/parse-pos-int
      (or Integer/MAX_VALUE)))

;; Resource creation

(defn not-default-new-name
  "When creating a new resource, the server passes a blank metadata object with
  the resource-name set to '...new', which has to be ignored."
  [resource-name]
  (when-not (page-type/new?)
    resource-name))

;; Resource URIs for a give resource name

(defn- parse-query-param
  [[k v]]
  (str (name k) "=" (if (keyword? v) (name v) v)))

(defn uri
  "See tests for expectations."
  [pathname & {hash-parameter :hash :as query-params}]
  (if-not query-params
    pathname
    (let [hash-parameter  (some->> (if (vector? hash-parameter)
                                     (s/join "+" hash-parameter)
                                     hash-parameter)
                                   (str "#"))
          query-string    (some->> (dissoc query-params :hash)
                                   not-empty
                                   (map parse-query-param)
                                   sort
                                   (s/join "&")
                                   (str "?"))]
      (str pathname query-string hash-parameter))))

(defn user-uri
  [username & query-params]
  (apply uri
         (str "/user/" username)
         query-params))

(defn module-uri
  [module & query-params]
  (apply uri
         (-> module
             (uc/ensure-prefix "/")
             (uc/ensure-prefix "/module"))
         query-params))

(defn module-name
  [module-uri]
  (-> module-uri
   (uc/trim-prefix "/")
   (uc/trim-prefix "module/")))

;; Module categories names

(uc/defn-memo t-module-category
  [category & [format-fn]]
  (when-let [category-name (some->> category name not-empty s/lower-case (str "module-category.") keyword t)]
    ((or format-fn identity) category-name)))
