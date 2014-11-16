(ns slipstream.ui.views.table-test
  (:use [expectations]
        [slipstream.ui.views.table])
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.time :as time]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]))

;; NOTE: To access the private symbol 'x' in the namespace 'foo.bar',  we use following notation:
;;
;;       @#'foo.bar/x
;;
;; Source: https://github.com/bbatsov/clojure-style-guide#access-private-var


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Test hidden inputs for parameter cells in editable mode

(def ^:private parameter
  {:help-hint nil
   :read-only? false
   :order 2147483647
   :value nil
   :category "stratuslab"
   :description "Requested CPUs"
   :type "String"
   :name "stratuslab.cpu"})

(def ^:private row-index 2)

(expect
  (str "<span>"
         "<input name=\"parameter-stratuslab.cpu--2--description\" value=\"Requested CPUs\" type=\"hidden\" />"
         "<input name=\"parameter-stratuslab.cpu--2--type\" value=\"String\" type=\"hidden\" />"
         "<input name=\"parameter-stratuslab.cpu--2--category\" value=\"stratuslab\" type=\"hidden\" />"
         "<input name=\"parameter-stratuslab.cpu--2--name\" value=\"stratuslab.cpu\" type=\"hidden\" />"
       "</span>")
  (->> (@#'slipstream.ui.views.table/hidden-inputs-for-parameter-snip parameter row-index)
       html/emit*
       (apply str)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def rand-str
  (->> 10000000 rand-int (str "random test string to avoid false positive tests ")))

(def rand-url
  (str "/" (s/replace rand-str " " "/")))

(def rand-str-long
  (->> 10000000 rand-int (repeat 50) (s/join " ") (str "2nd random and long test string to avoid false positive tests ")))

(defn- cell-html
  [cell]
  (->> (@#'slipstream.ui.views.table/cell-snip cell) ; 'cell-node' is on purpose a private var
       html/emit*
       (apply str)))

;; All cell types

(def all-cell-types
  "Including their accepted content types."
  {:cell/text               [             :content/map  :content/plain]
   :cell/textarea           [             :content/map  :content/plain]
   :cell/password           [:content/any :content/map  :content/plain]
   :cell/enum               [             :content/map  :content/plain]
   :cell/set                [             :content/map  :content/plain]
   :cell/timestamp          [             :content/map  :content/plain]
   :cell/relative-timestamp [             :content/map  :content/plain]
   :cell/boolean            [             :content/map  :content/plain]
   :cell/map                [             :content/map                ]
   :cell/link               [:content/any                             ]
   :cell/external-link      [             :content/map                ]
   :cell/email              [             :content/map  :content/plain]
   :cell/url                [             :content/map  :content/plain]
   :cell/username           [                           :content/plain]
   :cell/icon               [                           :content/plain]
   :cell/module-version     [                           :content/plain]
   :cell/help-hint          [                           :content/plain]
   :cell/reference-module   [                           :content/plain]})

(expect
  (-> all-cell-types
      keys
      set)
  (->> @#'slipstream.ui.views.table/cell-snip
       methods
       keys
       (filter vector?)
       (map first)
       set))

;; Cell types with editable mode variant
;; (Leaving the cells without :mode/edit outcommented for reference)

(expect
  #{:cell/text
    :cell/textarea
    :cell/password
    :cell/enum
    :cell/set
    ; :cell/timestamp
    :cell/boolean
    :cell/map
    ; :cell/link
    ; :cell/external-link
    :cell/email
    :cell/url
    ; :cell/username
    ; :cell/icon
    ; :cell/module-version
    ; :cell/help-hint
    :cell/reference-module}
  (->> @#'slipstream.ui.views.table/cell-snip
       methods
       keys
       (filter vector?)
       (filter #(= :mode/edit (second %)))
       (map first)
       set))

;; Content types for all cell types

(expect
  all-cell-types
  (->> @#'slipstream.ui.views.table/cell-snip
       methods
       keys
       (filter vector?)
       (map (juxt first last))
       set
       (group-by first)
       vals
       (map (juxt ffirst (comp sort (partial map second))))
       (into {})))

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
  (str "<td id=\"some-id\" class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/text, :content {:text rand-str :id "some-id"}}))

(expect
  (str "<td id=\"some:id.1\" class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/text, :content {:text rand-str :id "some:id.1"}}))

(expect
  (str "<td style=\"word-wrap: break-word; max-width: 500px;\" class=\"ss-table-cell-text\">" rand-str-long "</td>")
  (cell-html {:type :cell/text, :content {:text rand-str-long}}))

(expect
  (str "<td class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/text, :content rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text\">" (s/join ", " ["1" "A" rand-str]) "</td>")
  (cell-html {:type :cell/set, :content #{"A" rand-str "1"}}))

(localization/with-lang :en
  (let [ss-timestamp (->> 1 t/hours t/ago (f/unparse (f/formatters :date-time)))
        human-readable-long (time/format :human-readable-long ss-timestamp)]
    (expect
      (re-pattern (str "<td title=\"1 hour.* ago\" class=\"ss-table-cell-text\">" human-readable-long "</td>"))
      (localization/with-lang :en
        (cell-html {:type :cell/timestamp, :content ss-timestamp}))))

(expect
  IllegalArgumentException
  (cell-html {:type :cell/timestamp, :content "2013-03-06 14:3"}))

(expect
  (re-pattern "<td title=\".*\" class=\"ss-table-cell-text\">mercredi, 6 mars 2013, 14:30:59 UTC</td>")
  (localization/with-lang :fr
    (cell-html {:type :cell/timestamp, :content "2013-03-06 14:30:59.30 UTC"}))))


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
                       :class "form-control"
                       :type "text"}
                  :content []}
              "\n          "]}]
  (@#'slipstream.ui.views.table/cell-text-snip-edit {:text rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text-editable\">
            <input value=\"" rand-str "\" class=\"form-control\" type=\"text\" />
          </td>")
  (cell-html {:type :cell/text, :content {:text rand-str}, :editable? true}))

(expect
  (str "<td class=\"ss-table-cell-text-editable\">
            <input value=\"" rand-str "\" placeholder=\"Text\" class=\"form-control\" type=\"text\" />
          </td>")
  (cell-html {:type :cell/text, :content {:text rand-str, :placeholder "Text"}, :editable? true}))

(expect
  "<td class=\"ss-table-cell-text-editable\">
            <input value=\"\" class=\"form-control\" type=\"text\" />
          </td>"
  (cell-html {:type :cell/text, :content {:text ""}, :editable? true}))

(expect
  "<td class=\"ss-table-cell-text-editable\">
            <input value=\"\" class=\"form-control\" type=\"text\" />
          </td>"
  (cell-html {:type :cell/text, :content {:text nil}, :editable? true}))

(expect
  (str "<td class=\"ss-table-cell-text-editable\">
            <input value=\"" rand-str "\" class=\"form-control\" type=\"text\" />
          </td>")
  (cell-html {:type :cell/text, :content rand-str, :editable? true}))

; When a parameter is available in the cell content, we append the hidden
; input fields in editable mode required by the current form structure.

(expect
  (str "<td class=\"ss-table-cell-text-editable\">
            <input value=\"" rand-str "\" class=\"form-control\" type=\"text\" />
          <span>"
            "<input name=\"parameter-stratuslab.cpu--2--description\" value=\"Requested CPUs\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--2--type\" value=\"String\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--2--category\" value=\"stratuslab\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--2--name\" value=\"stratuslab.cpu\" type=\"hidden\" />"
          "</span>"
        "</td>")
  (cell-html {:type :cell/text, :editable? true, :content {:text rand-str
                                                           :parameter parameter
                                                           :row-index 2}}))


;; Password cell

(expect
  (str "<td class=\"ss-table-cell-text\">•••••</td>")
  (cell-html {:type :cell/password, :content rand-str}))

(expect
  (str "<td class=\"ss-table-cell-text\">•••••</td>")
  (cell-html {:type :cell/password, :content {:text rand-str}}))


;; Editable password cell

(expect
  "<td class=\"ss-table-cell-password-editable\">
            <input placeholder=\"Password\" class=\"form-control\" type=\"password\" />
          </td>"
  (cell-html {:type :cell/password, :editable? true, :content {:text rand-str}}))

; When a parameter is available in the cell content, we append the hidden
; input fields in editable mode required by the current form structure.

(expect
  (str "<td class=\"ss-table-cell-password-editable\">
            <input placeholder=\"Password\" class=\"form-control\" type=\"password\" />
          <span>"
            "<input name=\"parameter-stratuslab.cpu--0--description\" value=\"Requested CPUs\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--0--type\" value=\"String\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--0--category\" value=\"stratuslab\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--0--name\" value=\"stratuslab.cpu\" type=\"hidden\" />"
          "</span>"
        "</td>")
  (cell-html {:type :cell/password, :editable? true, :content {:text rand-str
                                                               :parameter parameter
                                                               :row-index 0}}))



;; Enum cell

(def enum
  [{:value  "other-choice"
    :text   "Other choice"}
   {:value  rand-url
    :selected? true
    :text   rand-str}])

(expect
  (str "<td title=\"Possible values: Other choice, " rand-str "\" class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/enum, :content enum}))

(expect
  (str "<td title=\"Possible values: Other choice, " rand-str "\" class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/enum, :content {:enum enum}}))

(expect
  (str "<td title=\"Possible values: Other choice, " rand-str "\" id=\"the-id\" class=\"ss-table-cell-text\">" rand-str "</td>")
  (cell-html {:type :cell/enum, :content {:enum enum, :id "the-id"}}))


;; Editable enum cell

(expect
  (str "<td class=\"ss-table-cell-enum-editable\">
            <select class=\"form-control\">
              <option value=\"other-choice\">Other choice</option>
              <option selected=\"\" value=\"" rand-url "\">" rand-str "</option>
            </select>
          </td>")
  (cell-html {:type :cell/enum, :editable? true, :content enum}))

(expect
  (str "<td class=\"ss-table-cell-enum-editable\">
            <select class=\"form-control\">
              <option value=\"other-choice\">Other choice</option>
              <option selected=\"\" value=\"" rand-url "\">" rand-str "</option>
            </select>
          </td>")
  (cell-html {:type :cell/enum, :editable? true, :content {:enum enum}}))

(expect
  (str "<td class=\"ss-table-cell-enum-editable\">
            <select name=\"some-id\" id=\"some-id\" class=\"form-control\">
              <option value=\"other-choice\">Other choice</option>
              <option selected=\"\" value=\"" rand-url "\">" rand-str "</option>
            </select>
          </td>")
  (cell-html {:type :cell/enum, :editable? true, :content {:enum enum, :id "some-id"}}))


; When a parameter is available in the cell content, we append the hidden
; input fields in editable mode required by the current form structure.

(expect
  (str "<td class=\"ss-table-cell-enum-editable\">
            <select name=\"some-id\" id=\"some-id\" class=\"form-control\">
              <option value=\"other-choice\">Other choice</option>
              <option selected=\"\" value=\"" rand-url "\">" rand-str "</option>
            </select>
          <span>"
            "<input name=\"parameter-stratuslab.cpu--7--description\" value=\"Requested CPUs\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--7--type\" value=\"String\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--7--category\" value=\"stratuslab\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--7--name\" value=\"stratuslab.cpu\" type=\"hidden\" />"
          "</span>"
        "</td>")
  (cell-html {:type :cell/enum, :editable? true, :content {:enum enum
                                                           :id "some-id"
                                                           :parameter parameter
                                                           :row-index 7}}))


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
  (str "<td class=\"ss-table-cell-link\"><a href=\"" rand-url "\">" rand-url "</a></td>")
  (cell-html {:type :cell/url, :content {:url rand-url}}))

(expect
  (str "<td class=\"ss-table-cell-link\"><a id=\"the-id\" href=\"" rand-url "\">" rand-url "</a></td>")
  (cell-html {:type :cell/url, :content {:url rand-url, :id "the-id"}}))

(expect
  "<td class=\"ss-table-cell-link\"><a id=\"username\" href=\"/user/testusername\">testusername</a></td>"
  (current-user/with-user {:super? true}
    (cell-html {:type :cell/username, :content "testusername"})))

(expect
  "<td id=\"username\" class=\"ss-table-cell-text\">testusername</td>"
  (current-user/with-user {:super? false}
    (cell-html {:type :cell/username, :content "testusername"})))


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

; When a parameter is available in the cell content, we append the hidden
; input fields in editable mode required by the current form structure.

(expect
  (str "<td class=\"ss-table-cell-boolean-editable\">
            <input checked=\"\" type=\"checkbox\" />
          <span>"
            "<input name=\"parameter-stratuslab.cpu--2--description\" value=\"Requested CPUs\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--2--type\" value=\"String\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--2--category\" value=\"stratuslab\" type=\"hidden\" />"
            "<input name=\"parameter-stratuslab.cpu--2--name\" value=\"stratuslab.cpu\" type=\"hidden\" />"
          "</span>"
        "</td>")
  (cell-html {:type :cell/boolean, :editable? true, :content {:value true
                                                              :parameter parameter
                                                              :row-index 2}}))


;; Module version cell

(let [[_ history version] (re-matches #"(.*/)(\d+)" rand-url)]
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


;; Reference module cell

(expect
  (str "<td class=\"ss-table-cell-link\"><a href=\"/module"
       rand-url
       "\">"
       rand-url
       "</a></td>")
  (cell-html {:type :cell/reference-module, :content rand-url}))


;; Editable reference module cell

(expect
  (str "<td class=\"ss-table-cell-reference-module-editable\">
            <div class=\"input-group\">
              <input value=\"" rand-url "\" name=\"moduleReference\" id=\"module-reference\" type=\"hidden\" />
              <span class=\"ss-reference-module-name\">
                <a target=\"_blank\" href=\"/module" rand-url "\">" rand-url "</a>
              </span>
              <span class=\"ss-reference-module-chooser-button\">
                <button type=\"button\" class=\"btn btn-primary\">Choose reference</button>
              </span>
            </div><!-- /input-group -->
          </td>")
  (localization/with-lang :en
    (cell-html {:type :cell/reference-module, :content rand-url, :editable? true})))

