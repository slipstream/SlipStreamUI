(ns slipstream.ui.views.table-test
  (:use [expectations]
        [slipstream.ui.views.table])
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.localization :as localization]))

(def rand-str
  (->> 10000000 rand-int (str "random test string to avoid false positive tests ")))

(def rand-url
  (str "/" (s/replace rand-str " " "/")))

(def rand-str-long
  (->> 10000000 rand-int (repeat 50) (s/join " ") (str "2nd random and long test string to avoid false positive tests ")))

(defn- emit-html
  [cell-snip content]
  (->> content cell-snip html/emit* (apply str)))

;; Text cell

(expect
  [{:tag :td
    :attrs {:class "ss-table-cell-text"}
    :content [rand-str]}]
  (cell-text-snip {:text rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text\">" rand-str "</td>")
  (emit-html cell-text-snip {:text rand-str}))

(expect
  (str "<td style=\"word-wrap: break-word; max-width: 500px;\" class=\"ss-table-cell-text\">" rand-str-long "</td>")
  (emit-html cell-text-snip {:text rand-str-long}))

(expect
  (str "<td class=\"ss-table-cell-text\">" rand-str "</td>")
  (emit-html cell-plain-text-snip rand-str))

(expect
  (str "<td class=\"ss-table-cell-text\">" (s/join ", " ["1" "A" rand-str]) "</td>")
  (emit-html cell-set-snip #{"A" rand-str "1"}))

(expect
  "<td title=\"2013-03-06 14:30:59.30 UTC\" class=\"ss-table-cell-text\">Wednesday, 6 March 2013, 14:30:59 UTC</td>"
  (localization/with-lang :en
    (emit-html cell-timestamp-snip "2013-03-06 14:30:59.30 UTC")))

(expect
  "<td title=\"2013-03-06 14:30:59.30 UTC\" class=\"ss-table-cell-text\">mercredi, 6 mars 2013, 14:30:59 UTC</td>"
  (localization/with-lang :fr
    (emit-html cell-timestamp-snip "2013-03-06 14:30:59.30 UTC")))


;; Editable text cell

(expect
  [{:tag :td
    :attrs {:class "ss-table-cell-text-editable"}
    :content ["\n            "
              {:tag :input
               :attrs {:value rand-str
                       :placeholder "Text"
                       :class "form-control"
                       :type "text"}
                  :content []}
              "\n          "]}]
  (cell-editable-text-snip {:text rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text-editable\">
            <input value=\"" rand-str "\" placeholder=\"Text\" class=\"form-control\" type=\"text\" />
          </td>")
  (emit-html cell-editable-text-snip {:text rand-str}))


;; Password cell

(expect
  (str "<td class=\"ss-table-cell-text\">***</td>")
  (emit-html cell-password-snip {:text rand-str}))


;; Map cell

(expect
  (str "<td class=\"ss-table-cell-map\">
            <dl class=\"dl-horizontal\"><dt>Second-key</dt><dd>will come first since sorted alphabetically</dd><dt>first-key</dt><dd>" rand-str "</dd></dl>
          </td>")
  (emit-html cell-map-snip {"first-key" rand-str, "Second-key" "will come first since sorted alphabetically"}))


;; Link cell

(expect
  (str "<td class=\"ss-table-cell-link\"><a href=\"#\">" rand-str "</a></td>")
  (emit-html cell-link-snip {:text rand-str, :href "#"}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a href=\"#\">" rand-str "</a></td>")
  (emit-html cell-link-snip {:text rand-str, :href "#", :open-in-new-window? false}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a target=\"_blank\" href=\"#\">" rand-str "</a></td>")
  (emit-html cell-link-snip {:text rand-str, :href "#", :open-in-new-window? true}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a target=\"_blank\" href=\"" rand-url "\">" rand-str "</a></td>")
  (emit-html cell-external-link-snip {:text rand-str, :href rand-url}))

(expect
  "<td class=\"ss-table-cell-link\"><a href=\"mailto:some@email.com\">some@email.com</a></td>"
  (emit-html cell-email-snip "some@email.com"))

(expect
  (str "<td class=\"ss-table-cell-link\"><a href=\"" rand-url "\">" rand-url "</a></td>")
  (emit-html cell-url-snip rand-url))

(expect
  "<td class=\"ss-table-cell-link\"><a href=\"/user/testusername\">testusername</a></td>"
  (emit-html cell-username-snip "testusername"))


;; Icon cell

(expect
  "<td class=\"ss-table-cell-icon\"><span class=\"glyphicon glyphicon-home\"></span></td>"
  (emit-html cell-icon-snip icons/home))

(expect
  "<td class=\"ss-table-cell-icon\"><span class=\"glyphicon-cloud-upload glyphicon\"></span></td>"
  (emit-html cell-icon-snip icons/action-import))

(expect
  IllegalArgumentException
  (emit-html cell-icon-snip :home))


;; Boolean cell

(expect
  "<td class=\"ss-table-cell-boolean\"><input disabled=\"\" checked=\"\" type=\"checkbox\" /></td>"
  (emit-html cell-boolean-snip true))

(expect
  "<td class=\"ss-table-cell-boolean\"><input disabled=\"\" type=\"checkbox\" /></td>"
  (emit-html cell-boolean-snip false))

(expect
  "<td class=\"ss-table-cell-boolean\"><input disabled=\"\" type=\"checkbox\" /></td>"
  (emit-html cell-boolean-snip nil))


;; Module version cell

(let [[_ history version] (re-matches #"(.*)/(\d+)" rand-url)]
  (expect
    (str "<td class=\"ss-table-cell-module-version\"><span>"
         version
         "</span> (<a href=\""
         history
         "\">history</a>)</td>")
    (emit-html cell-module-version-snip rand-url)))


;; Help hint cell

(expect
  (str "<td class=\"ss-table-cell-help-hint\">
            <div title=\"" rand-str "\" data-placement=\"left\" data-toggle=\"tooltip\" class=\"ss-table-tooltip\">
              <span class=\"glyphicon glyphicon-question-sign\"></span>
            </div>
          </td>")
  (emit-html cell-help-hint-snip rand-str))

