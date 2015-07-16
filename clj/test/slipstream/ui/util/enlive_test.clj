(ns slipstream.ui.util.enlive-test
  (:use [expectations]
        [slipstream.ui.util.enlive])
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.utils :as u :refer [expect-html]]))

(defn- replace-quotes
  "For readability only."
  [s]
  (s/replace s "&quot;" "'"))

(defmacro sniptest-quoted
  [& args]
  `(replace-quotes (html/sniptest ~@args))) ;; NOTE: Only for test readability

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def example-html-page
  "<html>
       <head>
           <meta title=\"the title\" name=\"viewport\"/>
       </head>
       <body>
           <p class=\"not_own_log\">
               A paragraph of <span class=\"highlighted\">text</span> in the body of the document.
           </p>
           A second paragraph of text.
          <ul>
              <li class=\"class-one class-two\">First item</li>
              <li class=\"class-one class-three\">Second item</li>
              <li class=\"class-two\">Third item</li>
              <li>Fourth item</li>
              <li class=\"extra-class\">Extra item</li>
              <li class=\"extra-class\">Extra item</li>
          </ul>
          <ul>
              <li class=\"extra-class\">Extra separated item</li>
          </ul>
       </body>
   </html>")

(def parsed-example-html-page
  (html/html-snippet example-html-page))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Basic Enlive examples

(expect
  "viewport"
  (-> example-html-page html/html-snippet (html/select [:html :head :meta]) first :attrs :name))

(expect
  "viewport"
  (-> parsed-example-html-page (html/select [:html :head :meta]) first :attrs :name))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; touch

(expect
  ""
  (touch nil))

(expect
  ""
  (touch ""))

(expect
  "1"
  (touch 1))

(expect
  "1"
  (touch "1"))

(expect
  "<a href=\"bar\">foo</a>"
  (touch "<a href='bar'>foo</a>"))

(expect
  "<a href=\"bar\"></a><div>wrong</div>foo" ;; NOTE: The parser used does not expect a <div> inside an <a>
  (touch "<a href='bar'><div>wrong</div>foo</a>"))

(expect-html
  example-html-page
  (touch example-html-page))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Data fn family

(def input-html "<input data-test=\"foo\" class=\"some-class\" type=\"text\" />")

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server nil)))

(expect-html
  "<input data-from-server=\"1\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server 1)))

(expect-html
  "<input data-from-server=\"1\" type=\"text\" class=\"some-class\" data-test=\"1\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server 1)
                   this (set-data :test 1)))

(expect-html
  "<input data-from-server=\"1\" type=\"text\" class=\"some-class\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server 1)
                   this (set-data :test nil)))

(expect-html
  "<input data-from-server=\"1\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server "1")))

(expect-html
  "<input data-from-server=\"blah\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server "blah")))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server [])))

(expect-html
  "<input data-from-server=\"['a','b','c']\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server [:a :b "c"])))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server {})))

(expect-html
  "<input data-from-server=\"{'a':1}\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server {:a 1})))

;; NOTE: Map keyword keys are camelCase'd, including the idiomatic transformation of Clojure :some-boolean? keys.

(expect-html
  "<input data-from-server=\"{'isFooBar':false,'someDashCasedKeyword':'foo','someSnakeCasedKeyword':'bar'}\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server {:some-dash-cased-keyword "foo", :some_snake_cased_keyword "bar", :foo-bar? false})))

(expect-html
  "<input data-from-server=\"{'foo-bar?':false,'some-dash-cased-string':'foo','some_snake_cased_string':'bar'}\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server {"some-dash-cased-string" "foo", "some_snake_cased_string" "bar", "foo-bar?" false})))

;; NOTE: Clojure sets are transformed into JSON vectors.

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server #{})))

(expect-html
  "<input data-from-server=\"['a','b']\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server #{:a :b})))

;; NOTE: Clojure re-patterns cannot be transformed into JSON. Use string instead.

(expect
  java.lang.Exception
  (sniptest-quoted input-html
                   this (set-data :from-server {:uncompiled-pattern #"\w+"})))

(expect-html
  "<input data-from-server=\"{'uncompiledPattern':'\\\\w+'}\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server {:uncompiled-pattern (str #"\w+")})))

;; NOTE: That the map keys are camelCase'd. This allows to use dot notation on Javascript
;;       when retrieving the data with jQuery.fn.data()

(expect-html
  "<input data-from-server=\"{'someKey':'some-value'}\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (set-data :from-server {:some-key "some-value"})))

;; when-set-data

(expect-html
  "<input data-from-server=\"{'a':1}\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server true {:a 1})))

(expect-html
  "<input data-from-server=\"{'a':1}\" type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server {:a 1})))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server false {:a 1})))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server false {:a 1})
                   this (when-set-data :test nil)))

(expect-html
  "<input type=\"text\" class=\"some-class\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server false {:a 1})
                   this (when-set-data :test true nil)))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"{'a':1}\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server false {:a 1})
                   this (when-set-data :test {:a 1})))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"{'a':1}\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server false {:a 1})
                   this (when-set-data :test true {:a 1})))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-test=\"foo\" />"
  (sniptest-quoted input-html
                   this (when-set-data :from-server false {:a 1})
                   this (when-set-data :test false {:a 1})))

(expect-html
  "<input type=\"text\" class=\"some-class\" data-some-boolean-2=true data-test=\"foo\"/>"
  (sniptest-quoted input-html
                   this (when-set-data :some-boolean-1 false)
                   this (when-set-data :some-boolean-2 true)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; defn-set-attr familiy

(def anchor-html "<a href=\"#\"></a>")

(expect-html
  "<a href=\"http://some.url.com\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (set-href "http://some.url.com")))

(expect-html
  "<a>some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (set-href nil)))

(expect-html
  "<a href=\"the.url.if.true\"></a>"
  (html/sniptest anchor-html
                 this (if-set-href true
                        "the.url.if.true"
                        "xxxxxxxxxxxxx")))

(expect-html
  "<a href=\"the.url.if.false\"></a>"
  (html/sniptest anchor-html
                 this (if-set-href false
                        "xxxxxxxxxxxxx"
                        "the.url.if.false")))

(expect-html
  "<a href=\"the.url.if.true\"></a>"
  (html/sniptest anchor-html
                 this (when-set-href true
                        "the.url.if.true")))

(expect-html
  "<a href=\"#\"></a>"
  (html/sniptest anchor-html
                 this (when-set-href false
                        "the.url.if.true")))

(expect-html
  "<a disabled=\"\" href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled true)))

(expect-html
  "<a disabled=\"disabled\" href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled true "disabled")))

(expect-html
  "<a disabled=\"disabled-and-more\" href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled true "disabled" "-and-more")))

(expect-html
  "<a href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled false)))

(expect-html
  "<a href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled false "disabled" "-and-more")))

(expect-html
  "<a href=\"#\">some text</a>"
  (html/sniptest (html/sniptest anchor-html this (toggle-disabled true))
                 this (html/content "some text")
                 this (toggle-disabled false)))

(expect-html
  "<a href=\"#\">some text</a>"
  (html/sniptest (html/sniptest anchor-html this (toggle-disabled true))
                 this (html/content "some text")
                 this (toggle-disabled false "disabled" "-and-more")))

(expect-html
  "<a href=\"#\">some text</a>"
  (html/sniptest "<a disabled=\"disabled\" href=\"#\"></a>"
                 this (html/content "some text")
                 this (toggle-disabled false)))

(expect-html
  "<a href=\"#\">some text</a>"
  (html/sniptest "<a disabled=\"disabled\" href=\"#\"></a>"
                 this (html/content "some text")
                 this (toggle-disabled false "disabled" "-and-more")))

(expect-html
  "<a href=\"some/url?query-param=value\">some text</a>"
  (html/sniptest "<a disabled=\"disabled\" href=\"some/url\"></a>"
                 this (html/content "some text")
                 this (toggle-disabled false)
                 this (append-to-href "?query-param=value")))

(expect-html
  "<a href=\"sxmx/xrl?qxxry-pxrxm=vxlxx\">some text</a>"
  (html/sniptest "<a href=\"some/url\"></a>"
                 this (html/content "some text")
                 this (append-to-href "?query-param=value")
                 this (update-href s/replace #"[aeiou]" "x")))
(expect-html
  "<a href=\"some/url\">some text</a>"
  (html/sniptest "<a href=\"some/url?query-param=value\">some text</a>"
                 this (update-href s/replace #"\?.*" "")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; content-for

(def example-html-page-with-new-list-items
  "<html>
       <head>
           <meta name=\"viewport\" title=\"the title\" />
       </head>
       <body>
           <p class=\"not_own_log\">
               A paragraph of <span class=\"highlighted\">text</span> in the body of the document.
           </p>
           A second paragraph of text.
          <ul>
              <li class=\"class-one class-two\">one</li>
              <li class=\"class-one class-two\">two</li>
              <li class=\"class-one class-two\">three</li>
              <li class=\"class-one class-two\">four</li>
              <li class=\"class-one class-two\">five</li>
          </ul>
          <ul>
              <li class=\"extra-class\">Extra separated item</li>
          </ul>
       </body>
   </html>")


(expect-html
  example-html-page-with-new-list-items
  (html/sniptest example-html-page
                 [[:ul html/first-of-type]] (content-for [[:li (first-of-class "class-one")]] [item ["one" "two" "three" "four" "five"]]
                                    this (html/content item))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; nth-of-attr family

(expect
   [{:tag :li
     :attrs {:class "class-one class-two"}
     :content ["First item"]}
    {:tag :li
     :attrs {:class "class-one class-three"}
     :content ["Second item"]}
    {:tag :li
     :attrs {:class "class-two"}
     :content ["Third item"]}
    {:tag :li
     :attrs nil
     :content ["Fourth item"]}
    {:tag :li
     :attrs {:class "extra-class"}
     :content ["Extra item"]}
    {:tag :li
     :attrs {:class "extra-class"}
     :content ["Extra item"]}
    {:tag :li
     :attrs {:class "extra-class"}
     :content ["Extra separated item"]}]
  (html/select parsed-example-html-page [:li]))


(expect
   [{:tag :li
     :attrs {:class "class-one class-two"}
     :content ["First item"]}]
  (html/select parsed-example-html-page [[:li (first-of-class "class-one")]]))


;; Note: first-of-x family of selectors pick the first **within** a same parent.

(expect
   [{:tag :li
     :attrs {:class "extra-class"}
     :content ["Extra item"]}
    {:tag :li
     :attrs {:class "extra-class"}
     :content ["Extra separated item"]}]
  (html/select parsed-example-html-page [[:li (first-of-class "extra-class")]]))

(expect
   [{:tag :li
     :attrs {:class "class-one class-three"}
     :content ["Second item"]}]
  (html/select parsed-example-html-page [[:li (last-of-class "class-one")]]))

(expect
   [{:tag :li
     :attrs {:class "class-one class-three"}
     :content ["Second item"]}]
  (html/select parsed-example-html-page [[:li.class-one (first-of-class "class-three")]]))

(expect
   [{:tag :li
     :attrs {:class "class-one class-three"}
     :content ["Second item"]}]
  (html/select parsed-example-html-page [[:li (html/has-class "class-one") (first-of-class "class-three")]]))


(def ul-snip
  (html/snippet* (html/select parsed-example-html-page [:ul])
    [content]
    [:li] (html/content content)))

(expect-html
  "<ul>
              <li class=\"class-one class-two\">some item</li>
              <li class=\"class-one class-three\">some item</li>
              <li class=\"class-two\">some item</li>
              <li>some item</li>
              <li class=\"extra-class\">some item</li>
              <li class=\"extra-class\">some item</li>
          </ul><ul>
              <li class=\"extra-class\">some item</li>
          </ul>"
  (->> "some item" ul-snip html/emit* (apply str)))


(def li-snip
  (html/snippet* (html/select parsed-example-html-page [:li])
    [content]
    this (html/content content)))

(expect-html
  (str
    "<li class=\"class-one class-two\">some item</li>"
    "<li class=\"class-one class-three\">some item</li>"
    "<li class=\"class-two\">some item</li>"
    "<li>some item</li>"
    "<li class=\"extra-class\">some item</li>"
    "<li class=\"extra-class\">some item</li>"
    "<li class=\"extra-class\">some item</li>")
  (->> "some item" li-snip html/emit* (apply str)))


(def one-li-snip
  (html/snippet* (html/select parsed-example-html-page [[:li (last-of-class "class-two")]])
    [content]
    this (html/content content)))

(expect-html
  "<li class=\"class-two\">some item</li>"
  (->> "some item" one-li-snip html/emit* (apply str)))


(def one-separated-li-snip
  (html/snippet* (html/select parsed-example-html-page [[:li (first-of-class "extra-class")]])
    [content]
    this (html/content content)))

(expect-html
  "<li class=\"extra-class\">some item</li><li class=\"extra-class\">some item</li>"
  (->> "some item" one-separated-li-snip html/emit* (apply str)))


(def smart-ul-snip
  (html/snippet* (html/select parsed-example-html-page [:ul])
    [items]
    this (content-for [[:li (first-of-class "extra-class")]] [item items]
                       this (html/content (str item)))))

(expect-html
  "<ul>
              <li class=\"extra-class\">item1</li>
              <li class=\"extra-class\">item2</li>
              <li class=\"extra-class\">item3</li>
          </ul><ul>
              <li class=\"extra-class\">item1</li><li class=\"extra-class\">item2</li><li class=\"extra-class\">item3</li>
          </ul>"
  (->> ["item1" "item2" "item3"]
       smart-ul-snip
       html/emit*
       (apply str)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; blank-node

(expect-html
  "<div></div>"
  (->> (blank-node :div)
       html/emit*
       (apply str)))

(expect-html
  "<blah></blah>"
  (->> (blank-node :blah)
       html/emit*
       (apply str)))

(expect-html
  "<div id=\"blah\"></div>"
  (->> (blank-node :div
                   :id "blah")
       html/emit*
       (apply str)))

(expect-html
  "<div class=\"some-class\" id=\"blah\"></div>"
  (->> (blank-node :div
                   :id "blah"
                   :class "some-class")
       html/emit*
       (apply str)))

;; Envlive handles the correct rendering of self-closing tags for us

(expect-html
  "<br />"
  (->> (blank-node :br)
       html/emit*
       (apply str)))

(expect-html
  "<img id=\"some-img-id\" />"
  (->> (blank-node :img
                   :id "some-img-id")
       html/emit*
       (apply str)))

;; blank node with parent nodes

(expect-html
  "<blah></blah>"
  (->> (blank-node [:blah])
       html/emit*
       (apply str)))

(expect-html
  "<div><span><blah></blah></span></div>"
  (->> (blank-node [:div :span :blah])
       html/emit*
       (apply str)))

(expect-html
  "<div><blah class=\"test\"></blah></div>"
  (->> (blank-node [:div :blah] :class "test")
       html/emit*
       (apply str)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; blank-snippet

(def some-link
  {:name "the link name"
   :uri "http://some.uri.com"})

(def-blank-snippet some-link-snip :a
  [link]
  this (html/content (:name link))
  this (set-href (:uri link))
  this (set-target "_blank"))

(expect
  [{:tag :a
    :attrs {:target "_blank"
            :href "http://some.uri.com"}
    :content ["the link name"]}]
  (some-link-snip some-link))

(expect-html
  "<a target=\"_blank\" href=\"http://some.uri.com\">the link name</a>"
  (->> some-link
       some-link-snip
       html/emit*
       (apply str)))

;; blank-snippet with parent tags

(def-blank-snippet some-link-in-li-and-span-snip [:li :span :a]
  [link]
  this    (set-id "top-level-id")
  [:span] (set-class "some-span-class")
  [:a]    (html/content (:name link))
  [:a]    (set-href (:uri link))
  [:a]    (set-target "_blank"))

(expect-html
  (str "<li id=\"top-level-id\"><span class=\"some-span-class\"><a target=\"_bla"
       "nk\" href=\"http://some.uri.com\">the link name</a></span></li>")
  (->> some-link
       some-link-in-li-and-span-snip
       html/emit*
       (apply str)))

;; blank-snippet with parent tags and self-closing tag

(def-blank-snippet some-img-in-li-and-span-snip [:li :span :img]
  [img]
  this    (set-id "top-level-img-id-for-img-" (-> img :name (s/replace \space \-)))
  [:span] (set-class "some-span-img-class")
  [:img]  (set-class "some-img-class")
  [:img]  (set-title (:name img))
  [:img]  (set-src (:uri img)))

(expect-html
  (str "<li id=\"top-level-img-id-for-img-the-image-name\"><span class=\"some-sp"
       "an-img-class\"><img title=\"the image name\" class=\"some-img-class\" />"
       "</span></li>")
  (->> {:name "the image name", :src "http:/the.image/source.png"}
       some-img-in-li-and-span-snip
       html/emit*
       (apply str)))

;; blank-snippet with parent tags and cloning

(def-blank-snippet link-list-snip [:ul :li :span :a]
  [links]
  this    (set-id "list-id")
  [:li]   (set-class "list-item-class")
  [:span] (set-class "some-span-class")
  this    (content-for [:li] [link links]
              [:a]    (html/content (:name link))
              [:a]    (set-href (:uri link))
              [:a]    (set-target "_blank")))

(expect-html
  (str "<ul id=\"list-id\">"
           "<li class=\"list-item-class\">"
               "<span class=\"some-span-class\">"
                  "<a target=\"_blank\" href=\"http://some.uri.com\">the link name</a>"
               "</span>"
           "</li>"
           "<li class=\"list-item-class\">"
               "<span class=\"some-span-class\">"
                  "<a target=\"_blank\" href=\"http://some.uri.com\">the link name</a>"
               "</span>"
           "</li>"
           "<li class=\"list-item-class\">"
               "<span class=\"some-span-class\">"
                  "<a target=\"_blank\" href=\"http://some.uri.com\">the link name</a>"
               "</span>"
           "</li>"
           "<li class=\"list-item-class\">"
               "<span class=\"some-span-class\">"
                  "<a target=\"_blank\" href=\"http://some.uri.com\">the link name</a>"
               "</span>"
           "</li>"
       "</ul>"
       )
  (->> [some-link some-link some-link some-link]
       link-list-snip
       html/emit*
       (apply str)))


;; Generic blank snippets

; text-div-snip

(expect-html
  "<div></div>"
  (->> nil
       text-div-snip
       html/emit*
       (apply str)))

(expect-html
  "<div></div>"
  (->> ""
       text-div-snip
       html/emit*
       (apply str)))

(expect-html
  "<div>blah</div>"
  (->> "blah"
       text-div-snip
       html/emit*
       (apply str)))

(expect-html
  "<div class=\"ss-some-class\">tada</div>"
  (->> (text-div-snip "tada" :css-class "ss-some-class")
       html/emit*
       (apply str)))


; map->meta-tag-snip

(expect
  ""
  (->> (map->meta-tag-snip nil)
       html/emit*
       (apply str)))

(expect
  ""
  (->> (map->meta-tag-snip {})
       html/emit*
       (apply str)))

(expect-html
  "<meta content=\"1\" name=\"a\" />"
  (->> (map->meta-tag-snip {:a 1})
       html/emit*
       (apply str)))

(expect-html
  "<meta content=\"1\" name=\"a\" /><meta content=\"the string value\" name=\"some-string\" />"
  (->> (map->meta-tag-snip {:a 1, :some-string "the string value"})
       html/emit*
       (apply str)))

(expect-html
  (str "<meta content=\"\" name=\"blank-string-value\" />"
       "<meta content=\"false\" name=\"boolean-value\" />"
       "<meta content=\":some-keyword\" name=\"keyword-value\" />"
       "<meta content=\"\" name=\"nil-value\" />")
  (->> (map->meta-tag-snip {:nil-value nil, :blank-string-value "", :boolean-value false, :keyword-value :some-keyword})
       html/emit*
       (apply str)))

(expect
  ""
  (->> (map->meta-tag-snip nil :name-prefix "ss-")
       html/emit*
       (apply str)))

(expect
  ""
  (->> (map->meta-tag-snip {} :name-prefix "ss-")
       html/emit*
       (apply str)))

(expect-html
  "<meta content=\"1\" name=\"ss-a\" />"
  (->> (map->meta-tag-snip {:a 1} :name-prefix "ss-")
       html/emit*
       (apply str)))

(expect-html
  "<meta content=\"1\" name=\"ss-a\" /><meta content=\"the string value\" name=\"ss-some-string\" />"
  (->> (map->meta-tag-snip {:a 1, :some-string "the string value"} :name-prefix "ss-")
       html/emit*
       (apply str)))

(expect-html
  (str "<meta content=\"\" name=\"ss-blank-string-value\" />"
       "<meta content=\"false\" name=\"ss-boolean-value\" />"
       "<meta content=\":some-keyword\" name=\"ss-keyword-value\" />"
       "<meta content=\"\" name=\"ss-nil-value\" />")
  (->> (map->meta-tag-snip {:nil-value nil, :blank-string-value "", :boolean-value false, :keyword-value :some-keyword} :name-prefix "ss-")
       html/emit*
       (apply str)))


;; Required input tweaking

(def-blank-snippet input-list-snip [:ul :li :input]
  [inputs]
  this    (set-id "input-list-id")
  [:li]   (set-class "input-list-item-class")
  this    (content-for [:li] [input inputs]
              [:input]    (add-requirements input)
              [:input]    (set-value (:value input))))

(expect-html
  (str "<ul id=\"input-list-id\">"
           "<li class=\"input-list-item-class\">"
               "<input value=\"foo\" />" ; NOTE: This one is not wrapped into a div.form-group because is not 'required?'.
           "</li>"
           "<li class=\"input-list-item-class\">"
               "<div class=\"form-group has-feedback ss-form-group-with-validation\">"
                   "<input value=\"bar\" class=\"ss-required-input ss-input-needs-validation\" />"
                   "<span class=\"ss-validation-help-hint help-block hidden\"></span>"
                   "<span class=\"glyphicon glyphicon-ok form-control-feedback hidden\"></span>"
               "</div>"
           "</li>"
           "<li class=\"input-list-item-class\">"
               "<div class=\"form-group has-feedback ss-form-group-with-validation\">"
                   "<input value=\"baz\" data-validation=\"{'requirements':[{'somePattern':'.+'}]}\" class=\"ss-input-has-requirements ss-input-needs-validation\" />"
                   "<span class=\"ss-validation-help-hint help-block hidden\"></span>"
                   "<span class=\"glyphicon glyphicon-ok form-control-feedback hidden\"></span>"
               "</div>"
           "</li>"
           "<li class=\"input-list-item-class\">"
               "<div class=\"form-group has-feedback ss-form-group-with-validation\">"
                   "<input value=\"foobar\" data-validation=\"{'requirements':[{'somePattern':'\\\\d+'}]}\" class=\"ss-required-input ss-input-has-requirements ss-input-needs-validation\" />"
                   "<span class=\"ss-validation-help-hint help-block hidden\"></span>"
                   "<span class=\"glyphicon glyphicon-ok form-control-feedback hidden\"></span>"
               "</div>"
           "</li>"
           "<li class=\"input-list-item-class\">"
               "<div class=\"form-group has-feedback ss-form-group-with-validation\">"
                   "<input value=\"foobar\" data-validation=\"{'genericHelpHints':{'error':'foo bar','warning':'bar baz'},'requirements':[{'somePattern':'\\\\w+'}]}\" class=\"ss-required-input ss-input-has-requirements ss-input-needs-validation\" />"
                   "<span class=\"ss-validation-help-hint help-block hidden\"></span>"
                   "<span class=\"glyphicon glyphicon-ok form-control-feedback hidden\"></span>"
               "</div>"
           "</li>"
       "</ul>")
  (->> [{:value "foo", :required? false}
        {:value "bar", :required? true}
        {:value "baz", :required? false, :validation {:requirements [{:some-pattern ".+"}]}}
        {:value "foobar", :required? true, :validation {:requirements [{:some-pattern "\\d+"}]}}
        {:value "foobar", :required? true, :validation {:requirements [{:some-pattern "\\w+"}], :generic-help-hints {:error "foo bar", :warning "bar baz"}}}]
       input-list-snip
       html/emit*
       (apply str)
       replace-quotes))