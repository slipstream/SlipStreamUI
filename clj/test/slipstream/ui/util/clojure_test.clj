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

;; ->sorted

(expect
  nil
  (->sorted nil))

(expect
  "some string"
  (->sorted "some string"))

(expect
  1234
  (->sorted 1234))

(expect
  [:d :A :a :c]
  (->sorted [:d :A :a :c]))

(expect
  [:d :A :a :c]
  (->sorted '(:d :A :a :c)))

(expect
  {:a 1 :b 2}
  (->sorted {:b 2 :a 1}))

(expect
  [[:a 1] [:b 2]]
  (seq (->sorted {:b 2 :a 1})))

(expect
  IllegalArgumentException
  (->sorted {"b" 2 :a 1}))

(expect
  IllegalArgumentException
  (->sorted #{:b 2 :a 1}))

(expect
  #{:c :d :A :a}
  (->sorted #{:c :d :A :a}))

(expect
  #{:A :a :c :d}
  (->sorted #{:c :d :A :a}))

(expect
  [:A :a :c :d]
  (seq (->sorted #{:c :d :A :a})))

(expect
  [:A :a :c :d]
  (seq (->sorted (sorted-set :c :d :A :a))))

(expect
  [0 1 2 3]
  (seq (->sorted #{1 3 2 0})))

; Note that the original sorting is overidden by ->sorted :

(def a-decreasingly-sorted-set
  (sorted-set-by > 1 3 2 0))

(expect
  [3 2 1 0]
  (seq a-decreasingly-sorted-set))

(expect
  [0 1 2 3]
  (seq (->sorted a-decreasingly-sorted-set)))

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

;; shorten-long-words

(expect
  nil
  (shorten-long-words nil 10))

(expect
  "someword"
  (shorten-long-words "someword"))

(expect
  "someword"
  (shorten-long-words "someword" 50))

(expect
  "someword"
  (shorten-long-words "someword" 8))

(expect
  "so...rd"
  (shorten-long-words "someword" 7))

(expect
  "a startsomewordsomewordsomewordsomewordend"
  (shorten-long-words "a startsomewordsomewordsomewordsomewordend"))

(expect
  "a st...nd"
  (shorten-long-words "a startsomewordsomewordsomewordsomewordend" 7))

(expect
  "a /st...nd"
  (shorten-long-words "a /startsomewordsomewordsomewordsomewordend" 7))

(expect
  "a st...nd"
  (shorten-long-words "a start-someword-someword-someword-someword-end" 7))

(expect
  "a st...nd"
  (shorten-long-words "a start_someword_someword_someword_someword_end" 7))

(expect
  "a st...nd"
  (shorten-long-words "a start.someword-someword_someword/someword\\end" 7))

(expect
  "this is an example with a /very/long/and/deep...d/be/shortened.txt"
  (shorten-long-words "this is an example with a /very/long/and/deep/path/that/should/be/shortened.txt" 40))

(expect
  "startsomewordsomewordso...wordsomewordsomewordend"
  (shorten-long-words "startsomewordsomewordsomewordsomewordsomewordsomewordsomewordsomewordend"))

(expect
  "startsomewordsomewordsomewordsomewordsomewordsomewordsomewordsomewordend"
  (shorten-long-words "startsomewordsomewordsomewordsomewordsomewordsomewordsomewordsomewordend" 100))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; truncate-to-max-length

(expect
  nil
  (truncate-to-max-length nil 10))

(expect
  "someword"
  (truncate-to-max-length "someword"))

(expect
  "someword"
  (truncate-to-max-length "someword" 50))

(expect
  "someword"
  (truncate-to-max-length "someword" 8))

(expect
  "some..."
  (truncate-to-max-length "someword" 7))

(expect
  "a startsomewordsomewordsomewordsomewordend"
  (truncate-to-max-length "a startsomewordsomewordsomewordsomewordend"))

(expect
  "a st..."
  (truncate-to-max-length "a startsomewordsomewordsomewordsomewordend" 7))

(expect
  "a /s..."
  (truncate-to-max-length "a /startsomewordsomewordsomewordsomewordend" 7))

(expect
  "a st..."
  (truncate-to-max-length "a start-someword-someword-someword-someword-end" 7))

(expect
  "a st..."
  (truncate-to-max-length "a start_someword_someword_someword_someword_end" 7))

(expect
  "a st..."
  (truncate-to-max-length "a start.someword-someword_someword/someword\\end" 7))

(expect
  "this is an example with a /very/long/..."
  (truncate-to-max-length "this is an example with a /very/long/and/deep/path/that/should/be/shortened.txt" 40))

(expect
  "startsomewordsomewordsomewordsomewordsomewordso..."
  (truncate-to-max-length "startsomewordsomewordsomewordsomewordsomewordsomewordsomewordsomewordend"))

(expect
  "startsomewordsomewordsomewordsomewordsomewordsomewordsomewordsomewordend"
  (truncate-to-max-length "startsomewordsomewordsomewordsomewordsomewordsomewordsomewordsomewordend" 100))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; parse-boolean

(expect
  nil
  (parse-boolean nil))

(expect
  java.lang.AssertionError
  (parse-boolean :not-a-string-or-nil))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "some string"))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "12 and some string"))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "12"))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "ttrue"))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "True."))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "11"))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "00"))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "01"))

(expect
  true
  (parse-boolean "true"))

(expect
  true
  (parse-boolean "True"))

(expect
  true
  (parse-boolean "TRUE"))

(expect
  true
  (parse-boolean "     tRue  "))

(expect
  true
  (parse-boolean "on"))

(expect
  true
  (parse-boolean "On"))

(expect
  true
  (parse-boolean "ON"))

(expect
  true
  (parse-boolean "          oN  "))

(expect
  true
  (parse-boolean "yes"))

(expect
  true
  (parse-boolean "Yes"))

(expect
  true
  (parse-boolean "YES"))

(expect
  true
  (parse-boolean "        yEs    "))

(expect
  true
  (parse-boolean "1"))

(expect
  true
  (parse-boolean "  1    "))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "ffalse"))

(expect
  java.lang.IllegalArgumentException
  (parse-boolean "False."))

(expect
  false
  (parse-boolean "false"))

(expect
  false
  (parse-boolean "False"))

(expect
  false
  (parse-boolean "FALSE"))

(expect
  false
  (parse-boolean "     faLSe  "))

(expect
  false
  (parse-boolean "off"))

(expect
  false
  (parse-boolean "Off"))

(expect
  false
  (parse-boolean "OFF"))

(expect
  false
  (parse-boolean "     oFf  "))

(expect
  false
  (parse-boolean "no"))

(expect
  false
  (parse-boolean "No"))

(expect
  false
  (parse-boolean "NO"))

(expect
  false
  (parse-boolean "     nO  "))

(expect
  false
  (parse-boolean "0"))

(expect
  false
  (parse-boolean "  0   "))

; With default values, to prevent expection

(expect
  true
  (parse-boolean "true" true))

(expect
  true
  (parse-boolean "true" false))

(expect
  nil
  (parse-boolean nil false))

(expect
  nil
  (parse-boolean nil true))

(expect
  true
  (parse-boolean :not-a-string-or-nil true))

(expect
  true
  (parse-boolean "some string" true))

(expect
  true
  (parse-boolean "12 and some string" true))


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

;; keyword?->is-keyword (private fn)

(expect
  nil
  (@#'slipstream.ui.util.clojure/keyword?->is-keyword nil))

(expect
  :is-key
  (@#'slipstream.ui.util.clojure/keyword?->is-keyword :key?))

(expect
  :key
  (@#'slipstream.ui.util.clojure/keyword?->is-keyword :key))

(expect
  :is-some-key
  (@#'slipstream.ui.util.clojure/keyword?->is-keyword :some-key?))

(expect
  "key?"
  (@#'slipstream.ui.util.clojure/keyword?->is-keyword "key?"))

(expect
  1
  (@#'slipstream.ui.util.clojure/keyword?->is-keyword 1))

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
  "{\"isSomeOtherKey\":true,\"oneKey\":\"foo\"}"
  (->json {:one-key "foo", :some-other-key? true}))

(expect
  (str "[{\"isSomeOtherKey\":true,\"oneKey\":\"foo\"},"
        "{\"isSomeOtherKey\":true,\"oneKey\":\"foo\"},"
        "{\"isSomeOtherKey\":true,\"oneKey\":\"foo\"}]")
  (->json [{:one-key "foo", :some-other-key? true}
           {:one-key "foo", :some-other-key? true}
           {:one-key "foo", :some-other-key? true}]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; update-keys

(expect
  nil
  (update-keys nil name))

(expect
  1
  (update-keys 1 name))

(expect
  {"a" 1}
  (update-keys {:a 1} name))

(expect
  [{"a" 1}]
  (update-keys [{:a 1}] name))

(expect
  '({"a" 1} {"b" 2})
  (update-keys (list {:a 1} {:b 2}) name))

(expect
  [[[{"a" 1}]]]
  (update-keys [[[{:a 1}]]] name))

(expect
  [[[{"a" 1}]{"b" 1}]]
  (update-keys [[[{:a 1}]{:b 1}]] name))

(expect
  [[[{"a" 1}]{"b" {"b-one" "foo" "b-two" {"b-two-one" "bar"}}}]]
  (update-keys [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar"}}}]] name))

(expect
  [[[{"a" 1}]{"b" {"b-one" "foo" "b-two" {"b-two-one" "bar" "b-two-two" #{{"even-here" "the key is updated"}}}}}]]
  (update-keys [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the key is updated"}}}}}]] name))

(expect
  [[[{"a" 1}]{"b" {"bOne" "foo" "bTwo" {"bTwoOne" "bar" "bTwoTwo" #{{"evenHere" "the key is updated"}}}}}]]
  (update-keys [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the key is updated"}}}}}]] ->camelCaseString))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; update-vals

(expect
  nil
  (update-vals nil str))

(expect
  1
  (update-vals 1 str))

(expect
  {:a "1"}
  (update-vals {:a 1} str))

(expect
  [{:a 2}]
  (update-vals [{:a 1}] inc))

(expect
  '({:a 2} {:b 3})
  (update-vals (list {:a 1} {:b 2}) inc))

(expect
  [[[{:a "1"}]]]
  (update-vals [[[{:a 1}]]] str))

(expect
  [[[{:a "1"}]{:b "1"}]]
  (update-vals [[[{:a 1}]{:b 1}]] str))

(expect
  [[[{:a :1}]{:b {:b-one :foo :b-two {:b-two-one :bar}}}]]
  (update-vals [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar"}}}]] keywordize))

(expect
  [[[{:a :1}]{:b {:b-one :foo :b-two {:b-two-one :bar :b-two-two #{{:even-here :the-value-is-updated}}}}}]]
  (update-vals [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the value is updated"}}}}}]] keywordize))

(expect
  [[[{:a "1"}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "theValueIsUpdated"}}}}}]]
  (update-vals [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the value is updated"}}}}}]] ->camelCaseString))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; update-kvs

(expect
  nil
  (update-kvs nil))

(expect
  1
  (update-kvs 1 :vals-fn str))

(expect
  {:a "1"}
  (update-kvs {:a 1} :vals-fn str))

(expect
  [{:a 2}]
  (update-kvs [{:a 1}] :vals-fn inc))

(expect
  {"a" "1"}
  (update-kvs {:a 1} :keys-fn name :vals-fn str))

(expect
  [{"a" 2}]
  (update-kvs [{:a 1}] :keys-fn name :vals-fn inc))

(expect
  '({:a 1} {:b 2})
  (update-kvs (list {:a 1} {:b 2})))

(expect
  '({:a 2} {:b 3})
  (update-kvs (list {:a 1} {:b 2}) :vals-fn inc))

(expect
  '({"a" 2} {"b" 3})
  (update-kvs (list {:a 1} {:b 2}) :keys-fn name :vals-fn inc))

(expect
  [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar"}}}]]
  (update-kvs [[[{"a" 1}]{"b" {"b-one" "foo" "b-two" {"b-two-one" "bar"}}}]] :keys-fn keywordize))

(expect
  [[[{:a :1}]{:b {:b-one :foo :b-two {:b-two-one :bar}}}]]
  (update-kvs [[[{"a" 1}]{"b" {"b-one" "foo" "b-two" {"b-two-one" "bar"}}}]] :keys-fn keywordize :vals-fn #(if (coll? %) % (keywordize %))))

(expect
  [[[{:a "1"}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "theValueIsUpdated"}}}}}]]
  (update-kvs [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the value is updated"}}}}}]] :vals-fn #(if (coll? %) % (->camelCaseString %))))

(expect
  [[[{"a" "1"}]{"b" {"b-one" "foo" "b-two" {"b-two-one" "bar" "b-two-two" #{{"even-here" "theValueIsUpdated"}}}}}]]
  (update-kvs [[[{:a 1}]{:b {:b-one "foo" :b-two {:b-two-one "bar" :b-two-two #{{:even-here "the value is updated"}}}}}]] :keys-fn name :vals-fn #(if (coll? %) % (->camelCaseString %))))

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; mmap

(expect
  AssertionError
  (mmap nil nil))

(expect
  AssertionError
  (mmap nil [:a :b]))

(expect
  AssertionError
  (mmap nil [:a [:b]]))

(expect
  AssertionError
  (mmap str [:a [:b]]))

(expect
  '((":a") (":b"))
  (mmap str [[:a] [:b]]))

;; NOTE: Compare to normal 'map:

(expect
  '("[:a]" "[:b]")
  (map str [[:a] [:b]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; html-safe-str

(expect
  ""
  (html-safe-str nil))

(expect
  ""
  (html-safe-str ""))

(expect
  "1"
  (html-safe-str 1))

(expect
  ":a"
  (html-safe-str :a))

(expect
  "some string"
  (html-safe-str "some string"))

(expect
  "940f2131-10b1-4fd5-acef-b13b6de37567"
  (html-safe-str "940f2131-10b1-4fd5-acef-b13b6de37567"))

(expect
  "some string &lt;a href=&quot;#&quot;&gt;with html tags&lt;/a&gt;"
  (html-safe-str "some string <a href=\"#\">with html tags</a>"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ensure-vector

(expect
  nil
  (ensure-vector nil))

(expect
  [1]
  (ensure-vector 1))

(expect
  [1]
  (ensure-vector [1]))

(expect
  [1]
  (ensure-vector '(1)))

(expect
  [:a]
  (ensure-vector :a))

(expect
  [:a]
  (ensure-vector [:a]))

(expect
  [:a]
  (ensure-vector '(:a)))

(expect
  [{:a 1}]
  (ensure-vector {:a 1}))

(expect
  [{:a 1}]
  (ensure-vector [{:a 1}]))

(expect
  [{:a 1}]
  (ensure-vector '({:a 1})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; format-metric-value

(expect "0.27 (h)"        (format-metric-value 16.133333333333333 "instance-type.Small"))
(expect "0.37 (h)"        (format-metric-value 22.48333333333333 "instance-type.Huge"))
(expect "24.00 (h)"       (format-metric-value 1440.0 "instance-type.Huge"))
(expect "1.40 (h)"        (format-metric-value 84.2 "instance-type.Medium" ))
(expect "26.09 (h)"       (format-metric-value 1565.416666666667 "instance-type.Micro"  ))
(expect "28.14 (h)"       (format-metric-value 1688.2333333333331 "vm"                  ))
(expect "0.00 (h)"        (format-metric-value 0.0 "vm"))

;; 47185920 is the value for Huge instance (32GB RAM) running for a full day
;; 768 is 32 * 24
(expect "768.00 (GBh)"    (format-metric-value 47185920.0 "ram" ))
(expect "768.00 (GBh)"    (format-metric-value 47185920   "ram" ))
(expect "0.00 (GBh)"      (format-metric-value 0.0        "ram" ))
(expect "7.92 (GBh)"      (format-metric-value 474.93     "disk"))

(expect "0.27 (h)"        (format-metric-value 16.133333333333333 "anything"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; flatten-map

(expect
  {}
  (flatten-map nil))

(expect
  {}
  (flatten-map {}))

(expect
  {:a 1}
  (flatten-map {:a 1}))

(expect
  {:a 1, :b 2}
  (flatten-map {:a 1, :b 2}))

(expect
  {:a 1, :b nil}
  (flatten-map {:a 1, :b nil}))

(expect
  {:a 1}
  (flatten-map {:a 1, :b {}}))

(expect
  {:a 1, :b.c 3}
  (flatten-map {:a 1, :b {:c 3}}))

(expect
  {:a 1, :b.c 3, :b.d.e 5}
  (flatten-map {:a 1, :b {:c 3 :d {:e 5}}}))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; deflatten-map

(expect
  {}
  (deflatten-map nil))

(expect
  {}
  (deflatten-map {}))

(expect
  {:a 1}
  (deflatten-map {:a 1}))

(expect
  {:a 1, :b 2}
  (deflatten-map {:a 1 :b 2}))

(expect
  {:a 1, :b nil}
  (deflatten-map {:a 1, :b nil}))

(expect
  {:a 1, :b {:c 3}}
  (deflatten-map {:a 1, :b.c 3}))

(expect
  {:a 1, :b {:c 3, :d {:e 5}}}
  (deflatten-map {:a 1, :b.c 3, :b.d.e 5}))

(expect
  {:a 1, :b {:c 3, :d {:e 5} :f 6}}
  (deflatten-map {:a 1, :b.c 3, :b.d.e 5, :b.f 6}))

(expect
  {:a 1, :b {:c 3, :d 6, :d.e 5, :d.e.f 7}}
  (deflatten-map {:a 1, :b.c 3, :b.d 6, :b.d.e 5, :b.d.e.f 7}))

(expect
  {:a 1, :b {:c 3, :d 6, :d.e 5, :d.e.f 7}}
  (deflatten-map {:b.d.e.f 7, :b.c 3, :a 1, :b.d 6, :b.d.e 5}))

(expect
  {:a 1, :b {:c 3, :f.e 5 :f 6}}
  (deflatten-map {:a 1, :b.c 3, :b.f.e 5, :b.f 6}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; url-encode

(expect
  nil
  (url-encode nil))

(expect
  ""
  (url-encode ""))

(expect
  AssertionError
  (url-encode 1))

(expect
  "foo"
  (url-encode "foo"))

(expect
  "foo%20bar"
  (url-encode "foo bar"))

(expect
  "foo%20bar%2Bbaz"
  (url-encode "foo bar+baz"))

(expect
  "f%F4%D6%2Eb%E1r%2Bb%E0z"
  (url-encode "fôÖ.bár+bàz"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; url-decode

(expect
  nil
  (url-decode nil))

(expect
  ""
  (url-decode ""))

(expect
  AssertionError
  (url-decode 1))

(expect
  "foo"
  (url-decode "foo"))

(expect
  "foo bar"
  (url-decode "foo%20bar"))

(expect
  "foo bar+baz"
  (url-decode "foo%20bar%2Bbaz"))

(expect
  "fôÖ.bár+bàz"
  (url-decode "f%F4%D6%2Eb%E1r%2Bb%E0z"))

(doseq [x [nil
           ""
           "asdf"
           "hello world!"
           "http://www.example.org"
           "!\"·$!\"·$  adsf ((!#--__--!)'')"]]
       (expect false  (= x (url-encode x)))
       (expect x      (-> x url-encode url-decode)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
