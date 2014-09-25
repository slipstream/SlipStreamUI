(ns slipstream.ui.util.core
  "Util functions only related to the SlipStream application.")

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
