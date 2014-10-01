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

(defn- cell-html
  [cell]
  (->> (@#'slipstream.ui.views.table/cell-node cell) ; 'cell-node' is on purpose a private var
       html/emit*
       (apply str)))

;; Text cell

; Note: the goal is to test the cell html, but for documentations purposes, we
; test here an enlive node (accessing the private snippet var) to display its
; structure.
(expect
  [{:tag :td
    :attrs {:class "ss-table-cell-text"}
    :content [rand-str]}]
  (@#'slipstream.ui.views.table/cell-text-snip-view {:text rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/text, :content {:text rand-str}}))

(expect
  (str "<td style=\"word-wrap: break-word; max-width: 500px;\" class=\"ss-table-cell-text\">" rand-str-long "</td>")
  (cell-html {:type :cell/text, :content {:text rand-str-long}}))

(expect
  (str "<td class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/text, :content rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text\">" (s/join ", " ["1" "A" rand-str]) "</td>")
  (cell-html {:type :cell/set, :content #{"A" rand-str "1"}}))

(expect
  "<td title=\"2013-03-06 14:30:59.30 UTC\" class=\"ss-table-cell-text\">Wednesday, 6 March 2013, 14:30:59 UTC</td>"
  (localization/with-lang :en
    (cell-html {:type :cell/timestamp, :content "2013-03-06 14:30:59.30 UTC"})))

(expect
  "<td title=\"2013-03-06 14:30:59.30 UTC\" class=\"ss-table-cell-text\">mercredi, 6 mars 2013, 14:30:59 UTC</td>"
  (localization/with-lang :fr
    (cell-html {:type :cell/timestamp, :content "2013-03-06 14:30:59.30 UTC"})))


;; Editable text cell

; Note: the goal is to test the cell html, but for documentations purposes, we
; test here an enlive node (accessing the private snippet var) to display its
; structure.
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
  (@#'slipstream.ui.views.table/cell-text-snip-edit {:text rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text-editable\">
            <input value=\"" rand-str "\" placeholder=\"Text\" class=\"form-control\" type=\"text\" />
          </td>")
  (cell-html {:type :cell/text, :content {:text rand-str}, :editable? true}))

(expect
  "<td class=\"ss-table-cell-text-editable\">
            <input value=\"\" placeholder=\"Text\" class=\"form-control\" type=\"text\" />
          </td>"
  (cell-html {:type :cell/text, :content {:text ""}, :editable? true}))

(expect
  "<td class=\"ss-table-cell-text-editable\">
            <input value=\"\" placeholder=\"Text\" class=\"form-control\" type=\"text\" />
          </td>"
  (cell-html {:type :cell/text, :content {:text nil}, :editable? true}))

(expect
  (str "<td class=\"ss-table-cell-text-editable\">
            <input value=\"" rand-str "\" placeholder=\"Text\" class=\"form-control\" type=\"text\" />
          </td>")
  (cell-html {:type :cell/text, :content rand-str, :editable? true}))


;; Password cell

(expect
  (str "<td class=\"ss-table-cell-text\">***</td>")
  (cell-html {:type :cell/password, :content {:text rand-str}}))


;; Editable password cell

(expect
  "<td class=\"ss-table-cell-password-editable\">
            <input placeholder=\"Password\" class=\"form-control\" type=\"password\" />
          </td>"
  (cell-html {:type :cell/password, :editable? true :content {:text rand-str}}))


;; Enum cell

(expect
  (str "<td title=\"Possible values: Other choice, " rand-str "\" class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/enum, :content [{:value  "other-choice"
                                           :text   "Other choice"}
                                          {:value  rand-url
                                           :selected? true
                                           :text   rand-str}]}))


;; Editable enum cell

(expect
  (str "<td class=\"ss-table-cell-enum-editable\">
            <select class=\"form-control\">
              <option value=\"other-choice\">Other choice</option>
              <option selected=\"\" value=\"" rand-url "\">" rand-str "</option>
            </select>
          </td>")
  (cell-html {:type :cell/enum, :editable? true :content [{:value  "other-choice"
                                                           :text   "Other choice"}
                                                          {:value  rand-url
                                                           :selected? true
                                                           :text   rand-str}]}))


;; Map cell

(expect
  (str "<td class=\"ss-table-cell-map\">
            <dl class=\"dl-horizontal\"><dt>Second-key</dt><dd>will come first since sorted alphabetically</dd><dt>first-key</dt><dd>" rand-str "</dd></dl>
          </td>")
  (cell-html {:type :cell/map, :content {"first-key" rand-str, "Second-key" "will come first since sorted alphabetically"}}))


;; Link cell

(expect
  (str "<td class=\"ss-table-cell-link\"><a href=\"#\">" rand-str "</a></td>")
  (cell-html {:type :cell/link, :content {:text rand-str :href "#"}}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a href=\"#\">" rand-str "</a></td>")
  (cell-html {:type :cell/link, :content {:text rand-str :href "#" :open-in-new-window? false}}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a target=\"_blank\" href=\"#\">" rand-str "</a></td>")
  (cell-html {:type :cell/link, :content {:text rand-str :href "#" :open-in-new-window? true}}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a target=\"_blank\" href=\"" rand-url "\">" rand-str "</a></td>")
  (cell-html {:type :cell/external-link, :content {:text rand-str :href rand-url}}))

(expect
  "<td class=\"ss-table-cell-link\"><a href=\"mailto:some@email.com\">some@email.com</a></td>"
  (cell-html {:type :cell/email, :content "some@email.com"}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a href=\"" rand-url "\">" rand-url "</a></td>")
  (cell-html {:type :cell/url, :content rand-url}))

(expect
  "<td class=\"ss-table-cell-link\"><a href=\"/user/testusername\">testusername</a></td>"
  (cell-html {:type :cell/username, :content "testusername"}))


;; Icon cell

(expect
  "<td class=\"ss-table-cell-icon\"><span class=\"glyphicon glyphicon-home\"></span></td>"
  (cell-html {:type :cell/icon, :content icons/home}))

(expect
  "<td class=\"ss-table-cell-icon\"><span class=\"glyphicon-cloud-upload glyphicon\"></span></td>"
  (cell-html {:type :cell/icon, :content icons/action-import}))

(expect
  IllegalArgumentException
  (cell-html {:type :cell/icon, :content :home}))


;; Boolean cell

(expect
  "<td class=\"ss-table-cell-boolean\">
            <input disabled=\"\" checked=\"\" type=\"checkbox\" />
          </td>"
  (cell-html {:type :cell/boolean, :content true}))

(expect
  "<td class=\"ss-table-cell-boolean\">
            <input disabled=\"\" type=\"checkbox\" />
          </td>"
  (cell-html {:type :cell/boolean, :content false}))

(expect
  "<td class=\"ss-table-cell-boolean\">
            <input disabled=\"\" type=\"checkbox\" />
          </td>"
  (cell-html {:type :cell/boolean, :content nil}))


;; Editable boolean cell

(expect
  "<td class=\"ss-table-cell-boolean-editable\">
            <input checked=\"\" type=\"checkbox\" />
          </td>"
  (cell-html {:type :cell/boolean, :content true, :editable? true}))

(expect
  "<td class=\"ss-table-cell-boolean-editable\">
            <input type=\"checkbox\" />
          </td>"
  (cell-html {:type :cell/boolean, :content false, :editable? true}))

(expect
  "<td class=\"ss-table-cell-boolean-editable\">
            <input type=\"checkbox\" />
          </td>"
  (cell-html {:type :cell/boolean, :content nil, :editable? true}))


;; Module version cell

(let [[_ history version] (re-matches #"(.*)/(\d+)" rand-url)]
  (expect
    (str "<td class=\"ss-table-cell-module-version\"><span>"
         version
         "</span> (<a href=\""
         history
         "\">history</a>)</td>")
    (cell-html {:type :cell/module-version, :content rand-url})))


;; Help hint cell

(expect
  (str "<td class=\"ss-table-cell-help-hint\">
            <div title=\"" rand-str "\" data-placement=\"left\" data-toggle=\"tooltip\" class=\"ss-table-tooltip\">
              <span class=\"glyphicon glyphicon-question-sign\"></span>
            </div>
          </td>")
  (cell-html {:type :cell/help-hint, :content rand-str}))

