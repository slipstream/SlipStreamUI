(ns slipstream.ui.util.clojure-test
  (:use [expectations]
        [slipstream.ui.util.clojure]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; def-this-ns

(def-this-ns)

(expect
  "slipstream.ui.util.clojure-test"
  this-ns)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ensure-prefix

(expect
  nil
  (ensure-prefix nil nil))

(expect
  nil
  (ensure-prefix nil "some prefix"))

(expect
  "some string"
  (ensure-prefix "some string" nil))

(expect
  "prefix some string"
  (ensure-prefix "prefix some string" "prefix some string"))

(expect
  "prefix some string"
  (ensure-prefix "" "prefix some string"))

(expect
  "prefix some string"
  (ensure-prefix "prefix some string" "prefix "))

(expect
  "prefix some string"
  (ensure-prefix "some string" "prefix "))

(expect
  "/some/path/or/url"
  (ensure-prefix "some/path/or/url" "/"))

(expect
  "/some/path/or/url"
  (ensure-prefix "/some/path/or/url" "/"))

(expect
  "/some/path/or/url"
  (ensure-prefix "some/path/or/url" \/))

(expect
  "/some/path/or/url"
  (ensure-prefix "/some/path/or/url" \/))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; trim-prefix

(expect
  nil
  (trim-prefix nil nil))

(expect
  nil
  (trim-prefix nil "some prefix"))

(expect
  "some string"
  (trim-prefix "some string" nil))

(expect
  "some string"
  (trim-prefix "some string" ""))

(expect
  "ing"
  (trim-prefix "prefix some string" "prefix some str"))

(expect
  ""
  (trim-prefix "prefix some string" "prefix some string"))

(expect
  "prefix some string"
  (trim-prefix "prefix some string" "prefix some string longer"))

(expect
  ""
  (trim-prefix "" "prefix some string"))

(expect
  "some string"
  (trim-prefix "prefix some string" "prefix "))

(expect
  "some string"
  (trim-prefix "some string" "prefix "))

(expect
  "some/path/or/url"
  (trim-prefix "some/path/or/url" "/"))

(expect
  "some/path/or/url"
  (trim-prefix "/some/path/or/url" "/"))

(expect
  "some/path/or/url\n"
  (trim-prefix "some/path/or/url\n" "/"))

(expect
  "some/path/or/url\n"
  (trim-prefix "/some/path/or/url\n" "/"))

(expect
  "some/path/or/url"
  (trim-prefix "some/path/or/url" \/))

(expect
  "some/path/or/url"
  (trim-prefix "/some/path/or/url" \/))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ensure-suffix

(expect
  nil
  (ensure-suffix nil nil))

(expect
  nil
  (ensure-suffix nil "some suffix"))

(expect
  "some string"
  (ensure-suffix "some string" nil))

(expect
  "suffix some string"
  (ensure-suffix "suffix some string" "suffix some string"))

(expect
  "suffix some string"
  (ensure-suffix "" "suffix some string"))

(expect
  "some string with suffix"
  (ensure-suffix "some string with suffix" "suffix"))

(expect
  "some string with suffix"
  (ensure-suffix "some string with " "suffix"))

(expect
  "/some/path/or/url/"
  (ensure-suffix "/some/path/or/url" "/"))

(expect
  "/some/path/or/url/"
  (ensure-suffix "/some/path/or/url/" "/"))

(expect
  "/some/path/or/url/"
  (ensure-suffix "/some/path/or/url" \/))

(expect
  "/some/path/or/url/"
  (ensure-suffix "/some/path/or/url/" \/))

(expect
  "some.dot.separated.words."
  (ensure-suffix "some.dot.separated.words" "."))

(expect
  "some.dot.separated.words."
  (ensure-suffix "some.dot.separated.words." "."))

(expect
  "some.dot.separated.words."
  (ensure-suffix "some.dot.separated.words" \.))

(expect
  "some.dot.separated.words."
  (ensure-suffix "some.dot.separated.words." \.))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; parse-pos-int

(expect
  nil
  (parse-pos-int nil))

(expect
  nil
  (parse-pos-int :keyword))

(expect
  nil
  (parse-pos-int "some string"))

(expect
  nil
  (parse-pos-int "12 and some string"))

(expect
  12
  (parse-pos-int "12"))

; octal
(expect
  16
  (parse-pos-int "020"))

(expect
  12
  (parse-pos-int "+12"))

(expect
  12
  (parse-pos-int 12))

(expect
  12
  (parse-pos-int +12))

(expect
  nil
  (parse-pos-int "-12"))

(expect
  nil
  (parse-pos-int -12))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; trim-from

(expect
  AssertionError
  (trim-from nil nil))

(expect
  AssertionError
  (trim-from nil "not a char"))

(expect
  nil
  (trim-from nil \.))

(expect
  "a"
  (trim-from "a.b.c....." \.))

(expect
  "e8d0b957"
  (trim-from "e8d0b957-14a8-4e96-8677-85c7bd9eb64e" \-))

(expect
  "abc"
  (trim-from "abc" \-))

(expect
  ""
  (trim-from "a" \a))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; trim-from-last

(expect
  AssertionError
  (trim-from-last nil nil))

(expect
  AssertionError
  (trim-from-last nil "not a char"))

(expect
  nil
  (trim-from-last nil \.))

(expect
  "a.b"
  (trim-from-last "a.b.c" \.))

(expect
  "a.b.c."
  (trim-from-last "a.b.c.." \.))

(expect
  "e8d0b957-14a8-4e96-8677"
  (trim-from-last "e8d0b957-14a8-4e96-8677-85c7bd9eb64e" \-))

(expect
  "abc"
  (trim-from-last "abc" \-))

(expect
  ""
  (trim-from-last "a" \a))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; trim-up-to-last

(expect
  AssertionError
  (trim-up-to-last nil nil))

(expect
  AssertionError
  (trim-up-to-last nil "not a char"))

(expect
  nil
  (trim-up-to-last nil \.))

(expect
  "c"
  (trim-up-to-last "a.b.c" \.))

(expect
  ""
  (trim-up-to-last "a.b.c.." \.))

(expect
  "85c7bd9eb64e"
  (trim-up-to-last "e8d0b957-14a8-4e96-8677-85c7bd9eb64e" \-))

(expect
  "abc"
  (trim-up-to-last "abc" \-))

(expect
  ""
  (trim-up-to-last "a" \a))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; trim-last-path-segment

(expect
  nil
  (trim-last-path-segment nil))

(expect
  "module/examples/tutorials/wordpress/wordpress"
  (trim-last-path-segment "module/examples/tutorials/wordpress/wordpress/180"))

(expect
  ""
  (trim-last-path-segment "module"))

(expect
  ""
  (trim-last-path-segment "/module"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; last-path-segment

(expect
  nil
  (last-path-segment nil))

(expect
  "180"
  (last-path-segment "module/examples/tutorials/wordpress/wordpress/180"))

(expect
  "module"
  (last-path-segment "module"))

(expect
  "module"
  (last-path-segment "/module"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ensure-unquoted

(expect
  nil
  (ensure-unquoted nil))

(expect
  "foo"
  (ensure-unquoted "foo"))

(expect
  "foo"
  (ensure-unquoted "'foo'"))

(expect
  "foo"
  (ensure-unquoted "\"foo\""))

(expect
  "foo"
  (ensure-unquoted "\"foo"))

(expect
  "foo"
  (ensure-unquoted "foo\""))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; title-case

(expect
  nil
  (title-case nil))

(expect
  ""
  (title-case ""))

(expect
  "1"
  (title-case 1))

(expect
  "[:A :B :C]"
  (title-case [:a :b :c]))

(expect
  "[:Foo :Bar :Baz]"
  (title-case [:foo :bar :baz]))

(expect
  "A"
  (title-case "a"))

(expect
  "A"
  (title-case "A"))

(expect
  "Word"
  (title-case "word"))

(expect
  "Word"
  (title-case "WORD"))

(expect
  "Look! Some Longer Sentence. And Only The 1st Letter (Of Each Word) Should Be 'Upper Case'."
  (title-case "Look! some longer sentence. and only the 1st letter (of each word) should be 'upper-case'."))

(expect
  "Look! Some Longer Sentence. And Only The 1st Letter (Of Each Word) Should Be 'Upper Case'."
  (title-case "Look! SOME LONGER SENTENCE. AND ONLY THE 1ST LETTER (OF EACH WORD) SHOULD BE 'UPPER-CASE'."))

(expect
  "Look! Some Longer Sentence. And Only The 1st Letter (Of Each Word) Should Be 'Upper Case'."
  (title-case "Look! some Longer Sentence. And Only The 1st Letter (of Each Word) Should Be 'upper-case'."))

(expect
  "Some Dash Case String"
  (title-case "some-dash-case-string"))

(expect
  "Some Dash Case String"
  (title-case "SOME-DASH-CASE-STRING"))

(expect
  "Some String Dot Separated"
  (title-case "some.string.dot.separated"))

(expect
  "Some String Dot Separated"
  (title-case "SOME.STRING.DOT.SEPARATED"))


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
  :some------dash-case-string
  (keywordize "some------dash-case-string"))

(expect
  :some-dash-case-string-ending-with-a-dash-
  (keywordize "some-dash-case-string-ending-with-a-dash-"))

(expect
  :-some-dash-case-string-begining-with-a-dash
  (keywordize "-some-dash-case-string-begining-with-a-dash"))

(expect
  :some-snake-case-string
  (keywordize "some_snake_case_string"))

(expect
  :some-camel-case-string
  (keywordize "someCamelCaseString"))

(expect
  :some-camel-case-string-ending-with-an-upper-case-letter
  (keywordize "someCamelCaseStringEndingWithAnUpperCaseLetteR"))

(expect
  :some-camel-case-string-begining-with-an-upper-case-letter
  (keywordize "SomeCamelCaseStringBeginingWithAnUpperCaseLetter"))

(expect
  :some-point-case-string
  (keywordize "some.point.case.string"))

(expect
  :some-point-case-string
  (keywordize "some.....point.case.string"))

(expect
  :some-point-case-string-ending-with-a-point-
  (keywordize "some.point.case.string.ending.with.a.point."))

(expect
  :-some-point-case-string-begining-with-a-point
  (keywordize ".some.point.case.string.begining.with.a.point"))

(expect
  :someuppercasestring
  (keywordize "SOMEUPPERCASESTRING"))

(expect
  :auca
  (keywordize "AUCA")) ; An Upper Case Acronym

(expect
  :u-might-want-2-do-that
  (keywordize [\U :might :Want 2 'do "that"]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ->camelCaseString

(expect
  nil
  (->camelCaseString nil))

(expect
  "someCamelCaseString"
  (->camelCaseString "someCamelCaseString"))

(expect
  "someCamelCaseKeyword"
  (->camelCaseString :someCamelCaseKeyword))

(expect
  "someDashCaseString"
  (->camelCaseString "some-dash-case-string"))

(expect
  "someDashCaseKeyword"
  (->camelCaseString :some-dash-case-keyword))

(expect
  "someStringWithDots"
  (->camelCaseString "some.string.with.dots"))

(expect
  "someKeywordWithDots"
  (->camelCaseString :some.keyword.with.dots))

(expect
  "someStringWithSpaces"
  (->camelCaseString "some string with spaces"))

(expect
  "uMightWant2DoThat"
  (->camelCaseString [\U :might :Want 2 'do "that"]))

(expect
  "a1B2"
  (->camelCaseString {:a 1, :b 2}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; key?->isKey (private fn)

(expect
  nil
  (@#'slipstream.ui.util.clojure/key?->isKey nil))

(expect
  "is-key"
  (@#'slipstream.ui.util.clojure/key?->isKey :key?))

(expect
  :key
  (@#'slipstream.ui.util.clojure/key?->isKey :key))

(expect
  "is-some-key"
  (@#'slipstream.ui.util.clojure/key?->isKey :some-key?))

(expect
  "key?"
  (@#'slipstream.ui.util.clojure/key?->isKey "key?"))

(expect
  1
  (@#'slipstream.ui.util.clojure/key?->isKey 1))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ->json

(expect
  nil
  (->json nil))

(expect
  "1"
  (->json 1))

(expect
  "\"\""
  (->json ""))

(expect
  "{\"oneKey\":\"foo\",\"isSomeOtherKey\":true}"
  (->json {:one-key "foo", :some-other-key? true}))

(expect
  "[{\"oneKey\":\"foo\",\"isSomeOtherKey\":true},{\"oneKey\":\"foo\",\"isSomeOtherKey\":true},{\"oneKey\":\"foo\",\"isSomeOtherKey\":true}]"
  (->json [{:one-key "foo", :some-other-key? true}
           {:one-key "foo", :some-other-key? true}
           {:one-key "foo", :some-other-key? true}]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; update-map-keys

(expect
  nil
  (update-map-keys nil name))

(expect
  1
  (update-map-keys 1 name))

(expect
  {"a" 1}
  (update-map-keys {:a 1} name))

(expect
  [{"a" 1}]
  (update-map-keys [{:a 1}] name))

(expect
  '({"a" 1} {"b" 2})
  (update-map-keys (list {:a 1} {:b 2}) name))

(expect
  [[[{"a" 1}]]]
  (update-map-keys [[[{:a 1}]]] name))

(expect
  [[[{"a" 1}]{"b" 1}]]
  (update-map-keys [[[{:a 1}]{:b 1}]] name))

(expect
  [[[{"a" 1}]{"b" {"b-one" "foo" "b-two" {"b-two-one" "bar"}}}]]
  (update-map-keys [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar"}}}]] name))

(expect
  [[[{"a" 1}]{"b" {"b-one" "foo" "b-two" {"b-two-one" "bar" "b-two-two" #{{"even-here" "the key is updated"}}}}}]]
  (update-map-keys [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the key is updated"}}}}}]] name))

(expect
  [[[{"a" 1}]{"b" {"bOne" "foo" "bTwo" {"bTwoOne" "bar" "bTwoTwo" #{{"evenHere" "the key is updated"}}}}}]]
  (update-map-keys [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the key is updated"}}}}}]] ->camelCaseString))

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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; join-as-str

(expect
  "b, a, c"
  (join-as-str ["b"
                "a"
                "c"]))

(expect
  "a, b, c"
  (join-as-str #{"b"
                 "a"
                 "c"}))

(expect
  "b, a, c"
  (join-as-str ["b "
                " a"
                " c"]))

(expect
  "b, a, c, b, b"
  (join-as-str ["b"
                "a"
                "c"
                "b "
                " b"]))

(expect
  "1, 2, 3, 5, A, B, B, B, B4, C, D, a, b, b, b, c, d"
  (join-as-str (set [" B"
                     "b"
                     "c"
                     "2"
                     "b "
                     " b"
                     "B"
                     "C"
                     "B "
                     :d
                     "B4"
                     :D
                     "a"
                     "1"
                     "A"
                     "3"
                     " 5"])))

(expect
"B, b, c, 2, b, b, B, C, B, d, B4, D, a, 1, A, 3, 5"
  (join-as-str [" B"
                "b"
                "c"
                "2"
                "b "
                " b"
                "B"
                "C"
                "B "
                :d
                "B4"
                :D
                "a"
                "1"
                "A"
                "3"
                " 5"]))

(expect
  "a, b, c"
  (join-as-str #{"b" "a" "c"}))

(expect
  "a, b, c"
  (join-as-str #{:b "a" "c"}))

(expect
  AssertionError
  (join-as-str {:b "a" 1 "c"}))

(expect
  AssertionError
  (join-as-str "some string"))

(expect
  AssertionError
  (join-as-str 123))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; dashless-str

(expect
  "940f213110b14fd5acefb13b6de37567"
  (dashless-str "940f2131-10b1-4fd5-acef-b13b6de37567"))

(expect
  "940f213110b14fd5acefb13b6de37567"
  (dashless-str "940f213110b14fd5acefb13b6de37567"))

(expect
  ""
  (dashless-str ""))

; Everything other than a string, returns nil.

(expect
  nil
  (dashless-str ["asfd" "q-wre" "zx--cv"]))

(expect
  nil
  (dashless-str {:some-keyword 1 :some-other-keyword 2}))

(expect
  nil
  (dashless-str nil))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; map-in

(expect
  [{:a [2 3 4]} {:a [12 13 14]}]
  (map-in [:a] inc [{:a [1 2 3]} {:a [11 12 13]}]))

(def some-sequence
  [{:name "foo" :b [{:a [1 2 3]}
                    {:a [11 12 13]}]}
   {:name "bar" :b [{:a [0 2 4]}
                    {:a [9 11 13]}]}])

(expect
  [{:name "foo" :b [{:a [2 3 4]}
                    {:a [12 13 14]}]}
   {:name "bar" :b [{:a [1 3 5]}
                    {:a [10 12 14]}]}]
  (map-in [:b :a] inc some-sequence))

(expect
  [{:name "foo" :b [{:a [20 30 40]}
                    {:a [120 130 140]}]}
   {:name "bar" :b [{:a [10 30 50]}
                    {:a [100 120 140]}]}]
  (->> some-sequence
       (map-in [:b :a] inc)
       (map-in [:b :a] #(* % 10))))

(expect
  [{:name ["f" "o" "o"] :b [{:a [10 20 30]}
                            {:a [110 120 130]}]}
   {:name ["b" "a" "r"] :b [{:a [0 20 40]}
                            {:a [90 110 130]}]}]
  (->> some-sequence
       (mapv-in [:name] str)
       (mapv-in [:b :a] #(* % 10))))

(expect
  [{:name ["f" "o" "o"] :b [[10 20 30]
                            [110 120 130]]}
   {:name ["b" "a" "r"] :b [[0 20 40]
                            [90 110 130]]}]
  (->> some-sequence
       (mapv-in [:name] str)
       (mapv-in [:b :a] #(* % 10))
       (mapv-in [:b] :a)))

(expect
  [[[10 20 30]
    [110 120 130]]
   [[0 20 40]
    [90 110 130]]]
  (->> some-sequence
       (mapv-in [:name] str)
       (mapv-in [:b :a] #(* % 10))
       (mapv-in [:b] :a)
       (mapv :b)))

(expect
  [[[11 21 31]
    [110 120 130]]
   [[1 21 41]
    [90 110 130]]]
  (->> some-sequence
       (mapv-in [:name] str)
       (mapv-in [:b :a] #(* % 10))
       (mapv-in [:b] :a)
       (mapv :b)
       (mapv-in [0] inc)))

(expect
  [{:name "foo" :b [{:a :foo, :b :bar}
                    {:a :foo, :b :bar}]}
   {:name "bar" :b [{:a :foo, :b :bar}
                    {:a :foo, :b :bar}]}]
  (->> some-sequence
       (mapv-in [:b] #(assoc % :a :foo :b :bar))))

(expect
  [{:name "foo" :b [{:a :foo, :aa [0 1 2]}
                    {:a :foo, :aa [0 1 2]}]}
   {:name "bar" :b [{:a :foo, :aa [0 1 2]}
                    {:a :foo, :aa [0 1 2]}]}]
  (->> some-sequence
       (mapv-in [:b] #(assoc % :a :foo :aa (range 3)))))

