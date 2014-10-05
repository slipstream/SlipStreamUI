(ns slipstream.ui.views.table
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.time :as ut]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.util.icons :as icons]))

(localization/def-scoped-t)

(def template-filename (common/get-template "table.html"))

(def table-cls "ss-table")

(def table-sel [(html/has-class table-cls)])
(def table-head-sel [:.ss-table-head :> :tr])
(def table-header-sel (concat table-head-sel [:> [:th html/first-of-type]]))
(def table-body-sel [:.ss-table-body])
(def table-row-sel (concat table-body-sel [:> [:tr html/first-of-type]]))


(defn- sel-for-cell
  [cls & variation]
  (concat table-body-sel [[:td (ue/first-of-class (str "ss-table-cell-" (name cls) (when (= (first variation) :editable) "-editable")))]]))


;; Text cell

(html/defsnippet ^:private cell-text-snip-view template-filename (sel-for-cell :text)
  [{:keys [text tooltip id]}]
  ue/this (html/content (str text))
  ue/this (ue/when-set-style (-> text str count (> 100))
                             "word-wrap: break-word; max-width: 500px;")
  ue/this (ue/when-set-id    id (str id))
  ue/this (ue/when-set-title (not-empty tooltip)
                             (str tooltip)))

(html/defsnippet ^:private cell-text-snip-edit template-filename (sel-for-cell :text :editable)
  [{:keys [text tooltip]}]
  [:input] (ue/set-value (str text))
  ue/this (ue/when-set-title (not-empty tooltip)
                             (str tooltip)))

(defn- cell-plain-text-snip-view
  [text]
  (cell-text-snip-view {:text text}))

(defn- cell-plain-text-snip-edit
  [text]
  (cell-text-snip-edit {:text text}))

(defn- cell-password-snip-view
  [pwd]
  ; (cell-text-snip-view (when pwd "•••")))
  (cell-text-snip-view {:text (when pwd "***")}))

(html/defsnippet ^:private cell-password-snip-edit template-filename (sel-for-cell :password :editable)
  [_]
  identity)

;; Enum cells

(defn- options-str
  [enum-options & ]
  (->> enum-options
    (map :text)
    uc/join-as-str))

(defn- selected-options-str
  [enum-options]
  (->> enum-options
       (filter :selected?)
       options-str))

(defn- cell-enum-snip-view
  [enum-options]
  (cell-text-snip-view {:text (selected-options-str enum-options)
                        :tooltip (str "Possible values: " (options-str enum-options))}))

(html/defsnippet ^:private cell-enum-snip-edit template-filename (sel-for-cell :enum :editable)
  [enum-options]
  [:select] (ue/content-for [[:option html/first-of-type]] [{:keys [value text selected?]} enum-options]
                            ue/this (ue/set-value value)
                            ue/this (html/content (str text))
                            ue/this (ue/toggle-selected selected?)))

;; Set cells

(defn- cell-set-snip-view
  [set]
  (cell-text-snip-view {:text (uc/join-as-str set)}))

(defn- cell-set-snip-edit
  [set]
  (cell-text-snip-edit {:text (uc/join-as-str set)}))

(defn- cell-timestamp-snip
  [timestamp]
  (cell-text-snip-view {:text (ut/format :human-readable-long timestamp)
                        :tooltip timestamp}))


;; Module name

(defn- cell-module-name-snip-view
  [module-name]
  (cell-text-snip-view {:text module-name, :id "ss-module-name"}))


(defn- map-key-str
  [map-key]
  (if (keyword? map-key)
    (->> map-key name (str "map-cell.key.") keyword t)
    (str map-key)))

(def cell-map-entry-sel (concat (sel-for-cell :map) [#{[:dt html/first-of-type] [:dd html/first-of-type]}]))

(html/defsnippet ^:private term-dict-entry-snip template-filename cell-map-entry-sel
  [[k v]]
  [:dt] (html/content (map-key-str k))
  [:dd] (html/content (str v)))

(html/defsnippet ^:private cell-map-snip template-filename (sel-for-cell :map)
  [m]
  [:dl] (html/content (->> m (into (sorted-map)) (mapcat term-dict-entry-snip))))

(html/defsnippet ^:private cell-link-snip template-filename (sel-for-cell :link)
  [{:keys [text href open-in-new-window?]}]
    [:a] (html/content (str text))
    [:a] (ue/set-href href)
    [:a] (ue/when-set-target open-in-new-window? "_blank"))

(defn- cell-external-link-snip
  [cell]
  (cell-link-snip (assoc cell :open-in-new-window? true)))

(defn- cell-email-snip-view
  [email]
  (cell-link-snip {:text email :href (str "mailto:" email)}))

(defn- cell-email-snip-edit
  [email]
  (cell-text-snip-edit {:text email}))


;; URL Cell

(defn- cell-url-snip-view
  [url]
  (cell-link-snip {:text url :href url}))

(defn- cell-url-snip-edit
  [url]
  (cell-text-snip-edit {:text url}))


(defn- cell-username-snip
  [username]
  (cell-link-snip {:text username :href (str "/user/" username)}))

(html/defsnippet ^:private cell-icon-snip template-filename (sel-for-cell :icon)
  [icon]
  [:span] (icons/set icon))

(html/defsnippet ^:private cell-boolean-snip-view template-filename (sel-for-cell :boolean)
  [value]
  ; [:input] (ue/toggle-disabled true)
  [:input] (ue/toggle-checked value))

(html/defsnippet ^:private cell-boolean-snip-edit template-filename (sel-for-cell :boolean :editable)
  [value]
  ; [:input] (ue/toggle-disabled false)
  [:input] (ue/toggle-checked value))

(html/defsnippet ^:private cell-module-version-snip template-filename (sel-for-cell :module-version)
  [uri]
  [:span] (html/content (uc/last-path-segment uri))
  [:a]    (ue/set-href   (uc/trim-last-path-segment uri)))

(html/defsnippet ^:private cell-help-hint-snip template-filename (sel-for-cell :help-hint)
  [help-text]
  [:.ss-table-tooltip]  (ue/set-title help-text)
  [:.ss-table-tooltip]  (ue/remove-if-not (not-empty help-text)))

;; Reference module cell snippets

(defn- cell-reference-module-snip-view
  [reference-module-uri]
  (cell-url-snip-view reference-module-uri))

(html/defsnippet ^:private cell-reference-module-snip-edit template-filename (sel-for-cell :reference-module :editable)
  [reference-module-uri]
  [:.ss-reference-module-name :a]  (ue/set-href reference-module-uri)
  [:.ss-reference-module-name :a]  (html/content reference-module-uri))



(defn- cell-snip
  "Get the cell-snip fn corresponding to a given cell type. This is done with a
  case (instead of e.g. a simple map) to detect unexpected cell types via an
  IllegalArgumentException."
  [{:keys [type content editable?] :as cell}]
  (case [type (if editable? :edit :view)]
    [:cell/text :view]           (if (map? content) cell-text-snip-view cell-plain-text-snip-view)
    [:cell/text :edit]           (if (map? content) cell-text-snip-edit cell-plain-text-snip-edit)
    [:cell/password :view]          cell-password-snip-view
    [:cell/password :edit]          cell-password-snip-edit
    [:cell/enum :view]              cell-enum-snip-view
    [:cell/enum :edit]              cell-enum-snip-edit
    [:cell/set :view]               cell-set-snip-view
    [:cell/set :edit]               cell-set-snip-edit
    [:cell/timestamp :view]         cell-timestamp-snip
    [:cell/boolean :view]           cell-boolean-snip-view
    [:cell/boolean :edit]           cell-boolean-snip-edit
    [:cell/username :view]          cell-username-snip
    [:cell/map :view]               cell-map-snip
    [:cell/map :edit]               cell-map-snip
    [:cell/link :view]              cell-link-snip
    [:cell/external-link :view]     cell-external-link-snip
    [:cell/email :view]             cell-email-snip-view
    [:cell/email :edit]             cell-email-snip-edit
    [:cell/url :view]               cell-url-snip-view
    [:cell/url :edit]               cell-url-snip-edit
    [:cell/icon :view]              cell-icon-snip
    [:cell/module-version :view]    cell-module-version-snip
    [:cell/module-name :view]       cell-module-name-snip-view
    [:cell/help-hint :view]         cell-help-hint-snip
    [:cell/reference-module :view]  cell-reference-module-snip-view
    [:cell/reference-module :edit]  cell-reference-module-snip-edit
    (throw (IllegalArgumentException.
      (str "No cell-snip defined for cell: " cell)))))

(defn- cell-node
  [{:keys [content] :as cell}]
  ((cell-snip cell) content))


;; Row

(html/defsnippet ^:private rows-snip template-filename table-row-sel
  [rows]
  ue/this (html/clone-for [{:keys [style cells]} rows]
           ue/this (html/content (map cell-node cells))
           ue/this (ue/when-add-class style (name style))))


;; Headers

(defn- header-str
  [header]
  (if (keyword? header)
    (->> header name (str "header.") keyword t)
    (str header)))

(html/defsnippet ^:private head-snip template-filename table-header-sel
  [headers]
  ue/this (html/clone-for [header headers]
            ue/this (html/content (header-str header))))

(html/defsnippet ^:private table-snip template-filename table-sel
  [{:keys [headers rows] :as table}]
  table-head-sel (html/content (head-snip headers))
  table-body-sel (html/content (->> rows
                                    (remove :hidden?)
                                    rows-snip)))

(defn build
  [table]
  (table-snip table))
