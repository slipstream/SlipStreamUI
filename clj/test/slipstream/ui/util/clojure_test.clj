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
