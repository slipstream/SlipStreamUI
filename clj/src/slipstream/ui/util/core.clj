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
  (str @config/template-namespace "/" name))

;; TODO: Look at slipstream.ui.views.common/slipstream and refactor.
(def slipstream "SlipStream")

;; TODO: Look at slipstream.ui.views.common/title and refactor.
(defn page-title
  [s]
  (if s
    (str slipstream " | " s)
    slipstream))

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
           (= (:value option) (name selected-option)))
    (assoc option :selected? true)
    (dissoc option :selected?)))

(defn enum-select
  "If the 'selected-option is not available, the first one will be selected."
  [enum selected-option]
  (->> enum
       (map (partial toggle-option (uc/keywordize selected-option)))
       ensure-one-selected))

(localization/with-prefixed-t :enum.option.text
  (defn- parse-enum-option
    [option]
      {:value (name option), :text (-> option uc/keywordize t)}))

(defn enum
  [options & [selected-option]]
  (-> (map parse-enum-option options)
      (enum-select (or selected-option
                       (first options)))))

(defn assoc-enum-details
  [m parameter]
  (if-not (-> m :type (= "Enum"))
    m
    (let [option-current (:value m)
          option-default (-> parameter
                            :attrs
                            (html/select [:defaultValue html/text-node])
                            first
                            uc/keywordize)
          enum-options (-> parameter
                           (html/select [:enumValues :string html/text-node])
                           vec)]
      (assoc m :value (enum
                        (map uc/keywordize enum-options)
                        (or option-current option-default))))))

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