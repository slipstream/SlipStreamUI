(ns slipstream.ui.util.core
  "Util functions only related to the SlipStream application."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

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

(defn ensure-one-selected
  "If no section in the section-coll is selected, this will ensure that at least
  the first one is. See tests for expectations."
  [sections-coll]
  (if (some :selected? sections-coll)
    sections-coll
    (-> sections-coll
        vec
        (assoc-in [0 :selected?] true))))

(defn clojurify-raw-metadata
  [raw-metadata]
  (first (html/html-snippet raw-metadata)))


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

(defn- parse-enum-option
  [option]
  (localization/with-prefixed-t :enum.option.text
    {:value (name option), :text (-> option uc/keywordize t)}))

(defn enum
  [options & [selected-option]]
  (-> (map parse-enum-option options)
      (enum-select (or selected-option
                       (first options)))))
