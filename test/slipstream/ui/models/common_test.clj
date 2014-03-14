(ns slipstream.ui.models.common-test
  (:use
    [expectations]
    [slipstream.ui.models.common]))

(def valid-parsed-data-as-list
  [{:attrs {:name "b"}}
   {:attrs {:name "z"}}
   {:attrs {:name "d"}}
   {:attrs {:name "Z"}}
   {:attrs {:name "A"}}])

(def valid-ordered-data
  [{:attrs {:name "A"}}
   {:attrs {:name "Z"}}
   {:attrs {:name "b"}}
   {:attrs {:name "d"}}
   {:attrs {:name "z"}}])

(expect valid-ordered-data
        (sort-by-name valid-parsed-data-as-list))

(def valid-parsed-data-as-map
  {:a [{:attrs {:name "b"}} {:attrs {:name "a"}} {:attrs {:name "c"}}]
   :z [{:attrs {:name "z"}} {:attrs {:name "z"}} {:attrs {:name "z"}}]
   :f [{:attrs {:name "y"}} {:attrs {:name "x"}} {:attrs {:name "z"}}]})

(def valid-ordered-data-as-map
  {:a [{:attrs {:name "a"}} {:attrs {:name "b"}} {:attrs {:name "c"}}]
   :z [{:attrs {:name "z"}} {:attrs {:name "z"}} {:attrs {:name "z"}}]
   :f [{:attrs {:name "x"}} {:attrs {:name "y"}} {:attrs {:name "z"}}]})

(expect valid-ordered-data-as-map
        (sort-map-vals-by-name valid-parsed-data-as-map))
