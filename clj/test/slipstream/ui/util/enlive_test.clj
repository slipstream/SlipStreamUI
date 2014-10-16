(ns slipstream.ui.util.enlive-test
  (:use [expectations]
        [slipstream.ui.util.enlive])
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]))

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

;; defn-set-attr familiy

(def anchor-html "<a href=\"#\"></a>")

(expect
  "<a href=\"http://some.url.com\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (set-href "http://some.url.com")))

(expect
  "<a>some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (set-href nil)))

(expect
  "<a href=\"the.url.if.true\"></a>"
  (html/sniptest anchor-html
                 this (if-set-href true
                        "the.url.if.true"
                        "xxxxxxxxxxxxx")))

(expect
  "<a href=\"the.url.if.false\"></a>"
  (html/sniptest anchor-html
                 this (if-set-href false
                        "xxxxxxxxxxxxx"
                        "the.url.if.false")))

(expect
  "<a href=\"the.url.if.true\"></a>"
  (html/sniptest anchor-html
                 this (when-set-href true
                        "the.url.if.true")))

(expect
  "<a href=\"#\"></a>"
  (html/sniptest anchor-html
                 this (when-set-href false
                        "the.url.if.true")))

(expect
  "<a disabled=\"\" href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled true)))

(expect
  "<a disabled=\"disabled\" href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled true "disabled")))

(expect
  "<a disabled=\"disabled-and-more\" href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled true "disabled" "-and-more")))

(expect
  "<a href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled false)))

(expect
  "<a href=\"#\">some text</a>"
  (html/sniptest anchor-html
                 this (html/content "some text")
                 this (toggle-disabled false "disabled" "-and-more")))

(expect
  "<a href=\"#\">some text</a>"
  (html/sniptest (html/sniptest anchor-html this (toggle-disabled true))
                 this (html/content "some text")
                 this (toggle-disabled false)))

(expect
  "<a href=\"#\">some text</a>"
  (html/sniptest (html/sniptest anchor-html this (toggle-disabled true))
                 this (html/content "some text")
                 this (toggle-disabled false "disabled" "-and-more")))

(expect
  "<a href=\"#\">some text</a>"
  (html/sniptest "<a disabled=\"disabled\" href=\"#\"></a>"
                 this (html/content "some text")
                 this (toggle-disabled false)))

(expect
  "<a href=\"#\">some text</a>"
  (html/sniptest "<a disabled=\"disabled\" href=\"#\"></a>"
                 this (html/content "some text")
                 this (toggle-disabled false "disabled" "-and-more")))

(expect
  "<a href=\"some/url?query-param=value\">some text</a>"
  (html/sniptest "<a disabled=\"disabled\" href=\"some/url\"></a>"
                 this (html/content "some text")
                 this (toggle-disabled false)
                 this (append-to-href "?query-param=value")))

(expect
  "<a href=\"sxmx/xrl?qxxry-pxrxm=vxlxx\">some text</a>"
  (html/sniptest "<a href=\"some/url\"></a>"
                 this (html/content "some text")
                 this (append-to-href "?query-param=value")
                 this (update-href s/replace #"[aeiou]" "x")))
(expect
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


(expect
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

(expect
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

(expect
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

(expect
  "<li class=\"class-two\">some item</li>"
  (->> "some item" one-li-snip html/emit* (apply str)))


(def one-separated-li-snip
  (html/snippet* (html/select parsed-example-html-page [[:li (first-of-class "extra-class")]])
    [content]
    this (html/content content)))

(expect
  "<li class=\"extra-class\">some item</li><li class=\"extra-class\">some item</li>"
  (->> "some item" one-separated-li-snip html/emit* (apply str)))


(def smart-ul-snip
  (html/snippet* (html/select parsed-example-html-page [:ul])
    [items]
    this (content-for [[:li (first-of-class "extra-class")]] [item items]
                       this (html/content (str item)))))

(expect
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

(expect
  "<div></div>"
  (->> (blank-node :div)
       html/emit*
       (apply str)))

(expect
  "<blah></blah>"
  (->> (blank-node :blah)
       html/emit*
       (apply str)))

(expect
  "<div id=\"blah\"></div>"
  (->> (blank-node :div
                   :id "blah")
       html/emit*
       (apply str)))

(expect
  "<div class=\"some-class\" id=\"blah\"></div>"
  (->> (blank-node :div
                   :id "blah"
                   :class "some-class")
       html/emit*
       (apply str)))

;; Envlive handles the correct rendering of self-closing tags for us

(expect
  "<br />"
  (->> (blank-node :br)
       html/emit*
       (apply str)))

(expect
  "<img id=\"some-img-id\" />"
  (->> (blank-node :img
                   :id "some-img-id")
       html/emit*
       (apply str)))

;; blank node with parent nodes

(expect
  "<blah></blah>"
  (->> (blank-node [:blah])
       html/emit*
       (apply str)))

(expect
  "<div><span><blah></blah></span></div>"
  (->> (blank-node [:div :span :blah])
       html/emit*
       (apply str)))

(expect
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

(expect
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

(expect
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

(expect
  (str "<li id=\"top-level-img-id-for-img-the-image-name\"><span class=\"some-sp"
       "an-img-class\"><img title=\"the image name\" class=\"some-img-class\" />"
       "</span></li>")
  (->> {:name "the image name", :src "http:/the.image/source.png"}
       some-img-in-li-and-span-snip
       html/emit*
       (apply str)))
