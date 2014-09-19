(ns slipstream.ui.util.clojure-test
  (:use [expectations]
        [slipstream.ui.util.clojure]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; coll-grouped-by

(def personas
  [{:age 20 :name "Alice"}
   {:age 20 :name "Bob"}
   {:age 17 :name "Clara"}])


(def result-basic
  [{:age 17
    :items [{:age 17, :name "Clara"}]}
   {:age 20
    :items [{:age 20, :name "Alice"}
            {:age 20, :name "Bob"}]}])

(expect
  result-basic
  (coll-grouped-by :age personas))


(def result-with-type
  [{:age-type :minor
    :age 17
    :members [{:age 17, :name "Clara"}]}
   {:age-type :adult
    :age 20
    :members [{:age 20, :name "Alice"}
              {:age 20, :name "Bob"}]}])

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
    :members [{:age 20, :name "Alice"}
              {:age 20, :name "Bob"}]}])

(expect
  result-with-type-2
  (coll-grouped-by :age personas
                   :items-keyword :members
                   :group-type-keyword :minors
                   :group-type-fn #(< % 18)))
