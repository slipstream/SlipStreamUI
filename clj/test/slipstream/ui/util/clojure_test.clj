(ns slipstream.ui.util.clojure-test
  (:use [expectations]
        [slipstream.ui.util.clojure]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; keywordize

(expect
  :deployment
  (keywordize "Deployment"))

(expect
  :a-keyword
  (keywordize :a-keyword))

(expect
  :a-Keyword
  (keywordize :a-Keyword))

(expect
  :keyword
  (keywordize "Keyword"))

(expect
  :keyword
  (keywordize ":Keyword"))

(expect
  :-keyword
  (keywordize "-Keyword"))

(expect
  :some-dash-case-string
  (keywordize "some-dash-case-string"))

(expect
  :some-snake-case-string
  (keywordize "some_snake_case_string"))

(expect
  :some-camel-case-string
  (keywordize "someCamelCaseString"))

(expect
  :u-might-want-2-do-that
  (keywordize [\U :might :Want 2 'do "that"]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; coll-grouped-by

(def personas
  [{:age 17 :name "Clara"}
   {:age 20 :name "Bob"}
   {:age 20 :name "Alice"}])


(def result-basic
  [{:age 17
    :items [{:age 17, :name "Clara"}]}
   {:age 20
    :items [{:age 20, :name "Bob"}
            {:age 20, :name "Alice"}]}])

(expect
  result-basic
  (coll-grouped-by :age personas))


(def result-with-type
  [{:age-type :minor
    :age 17
    :members [{:age 17, :name "Clara"}]}
   {:age-type :adult
    :age 20
    :members [{:age 20, :name "Bob"}
              {:age 20, :name "Alice"}]}])

(expect
  result-with-type
  (coll-grouped-by :age personas
                   :items-keyword :members
                   :group-type-fn (fn [age]
                                    (if (< age 18)
                                      :minor
                                      :adult))))

(def result-with-type-2
  [{:minors true
    :age 17
    :members [{:age 17, :name "Clara"}]}
   {:minors false
    :age 20
    :members [{:age 20, :name "Bob"}
              {:age 20, :name "Alice"}]}])

(expect
  result-with-type-2
  (coll-grouped-by :age personas
                   :items-keyword :members
                   :group-type-keyword :minors
                   :group-type-fn #(< % 18)))


(def result-with-sorting
  [{:minors false
    :age 20
    :members [{:age 20, :name "Alice"}
              {:age 20, :name "Bob"}]}
   {:minors true
    :age 17
    :members [{:age 17, :name "Clara"}]}])

(expect
  result-with-sorting
  (coll-grouped-by :age personas
                   :group-sort-fn -
                   :items-keyword :members
                   :items-sort-fn :name
                   :group-type-keyword :minors
                   :group-type-fn #(< % 18)))
