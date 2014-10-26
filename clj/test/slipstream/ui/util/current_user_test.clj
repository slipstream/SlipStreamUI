(ns slipstream.ui.util.current-user-test
  (:use [expectations])
  (:require [slipstream.ui.util.current-user :as current-user :refer [with-user-from-metadata]]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.models.dashboard-test :as dashboard]
            [slipstream.ui.models.welcome-test :as welcome]))

(def dashboard-metadata
  (u/clojurify-raw-metadata-str dashboard/raw-metadata-str))

(def welcome-metadata
  (u/clojurify-raw-metadata-str welcome/raw-metadata-str))

(expect
  nil
  (let [metadata dashboard-metadata]
    (current-user/username)))

(expect
  ["super" "user/super" true]
  (let [metadata dashboard-metadata]
    (with-user-from-metadata
          (vector
            (current-user/username)
            (current-user/uri)
            (current-user/super?)))))

(expect
  ["meb" "user/meb" true]
  (let [metadata welcome-metadata]
    (with-user-from-metadata
          (vector
            (current-user/username)
            (current-user/uri)
            (current-user/super?)))))
