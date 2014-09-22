(ns slipstream.ui.views.table
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.time :as ut]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.util.icons :as icons]))

(def template-filename (common/get-template "table.html"))

(def table-cls "ss-table")

(def table-sel [(html/has-class table-cls)])
(def table-head-sel [:.ss-table-head :> :tr])
(def table-header-sel (concat table-head-sel [:> [:th html/first-of-type]]))
(def table-body-sel [:.ss-table-body])
(def table-row-sel (concat table-body-sel [:> [:tr html/first-of-type]]))

(def cell-text-sel (concat table-body-sel [[:td (ue/first-of-class "ss-table-cell-text")]]))
(def cell-link-sel (concat table-body-sel [[:td (ue/first-of-class "ss-table-cell-link")]]))
(def cell-icon-sel (concat table-body-sel [[:td (ue/first-of-class "ss-table-cell-icon")]]))
(def cell-boolean-sel (concat table-body-sel [[:td (ue/first-of-class "ss-table-cell-boolean")]]))
(def cell-module-version-sel (concat table-body-sel [[:td (ue/first-of-class "ss-table-cell-module-version")]]))
(def cell-help-hint-sel (concat table-body-sel [[:td (ue/first-of-class "ss-table-cell-help-hint")]]))
(def cell-map-sel (concat table-body-sel [[:td (ue/first-of-class "ss-table-cell-map")]]))
(def cell-map-entry-sel (concat cell-map-sel [#{[:dt html/first-of-type] [:dd html/first-of-type]}]))


;; Cell

(html/defsnippet cell-text-snip template-filename cell-text-sel
  [{:keys [text tooltip]}]
  ue/this (html/content (str text))
  ue/this (ue/when-set-style (-> text str count (> 100))
                             "word-wrap: break-word; max-width: 500px;")
  ue/this (ue/when-set-title (not-empty tooltip)
                             (str tooltip)))

(defn cell-plain-text-snip
  [text]
  (cell-text-snip {:text text}))

(defn cell-password-snip
  [pwd]
  ; (cell-text-snip (when pwd "•••")))
  (cell-plain-text-snip (when pwd "***")))

(defn cell-set-snip
  [set]
  (cell-plain-text-snip (s/join ", " set)))

(defn- cell-timestamp-snip
  [timestamp]
  (cell-text-snip {:text (ut/format :human-readable-long timestamp)
                   :tooltip timestamp}))

(html/defsnippet term-dict-entry-snip template-filename cell-map-entry-sel
  [[k v]]
  [:dt] (html/content (if (keyword? k) (name k) (str k)))
  [:dd] (html/content (str v)))

(html/defsnippet cell-map-snip template-filename cell-map-sel
  [m]
  [:dl] (html/content (mapcat term-dict-entry-snip m)))

(html/defsnippet cell-link-snip template-filename cell-link-sel
  [{:keys [text href open-in-new-window?]}]
    [:a] (html/content (str text))
    [:a] (ue/set-href href)
    [:a] (ue/when-set-target open-in-new-window? "_blank"))

(defn cell-external-link-snip
  [cell]
  (cell-link-snip (assoc cell :open-in-new-window? true)))

(defn cell-email-snip
  [email]
  (cell-link-snip {:text email :href (str "mailto:" email)}))

(defn cell-url-snip
  [url]
  (cell-link-snip {:text url :href url}))

(defn- cell-username-snip
  [username]
  (cell-link-snip {:text username :href (str "/user/" username)}))

(html/defsnippet cell-icon-snip template-filename cell-icon-sel
  [icon]
  [:span] (icons/set icon))

(html/defsnippet cell-boolean-snip template-filename cell-boolean-sel
  [value]
  ; [:input] (ue/toggle-disabled true)
  [:input] (ue/toggle-checked value))

(html/defsnippet cell-module-version-snip template-filename cell-module-version-sel
  [uri]
  [:span] (html/content (uc/last-path-segment uri))
  [:a]    (ue/set-href   (uc/trim-last-path-segment uri)))

(html/defsnippet cell-help-hint-snip template-filename cell-help-hint-sel
  [help-text]
  [:.ss-table-tooltip]  (ue/set-title help-text)
  [:.ss-table-tooltip]  (ue/remove-if-not (not-empty help-text)))

(defn- cell-snip
  "Get the cell-snip fn corresponding to a given cell type. This is done with a
  case (instead of e.g. a simple map) to detect unexpected cell types via an
  IllegalArgumentException."
  [{:keys [type content] :as cell}]
  (case type
    :cell/text           (if (string? content) cell-plain-text-snip cell-text-snip)
    :cell/password       cell-password-snip
    :cell/set            cell-set-snip
    :cell/timestamp      cell-timestamp-snip
    :cell/username       cell-username-snip
    :cell/map            cell-map-snip
    :cell/link           cell-link-snip
    :cell/external-link  cell-external-link-snip
    :cell/email          cell-email-snip
    :cell/url            cell-url-snip
    :cell/icon           cell-icon-snip
    :cell/boolean        cell-boolean-snip
    :cell/module-version cell-module-version-snip
    :cell/help-hint      cell-help-hint-snip
    (throw (IllegalArgumentException.
      (str "No cell-snip defined for cell type: " type)))))

(defn- cell-node
  [{:keys [content] :as cell}]
  ((cell-snip cell) content))


;; Row

(html/defsnippet rows-snip template-filename table-row-sel
  [rows]
  ue/this (html/clone-for [{:keys [style cells]} rows]
           ue/this (html/content (map cell-node cells))
           ue/this (ue/when-add-class style (name style))))


;; Headers

(html/defsnippet head-snip template-filename table-header-sel
  [headers]
  ue/this (html/clone-for [header headers]
            ue/this (html/content (str header))))

(html/defsnippet table-snip template-filename table-sel
  [{:keys [headers rows] :as table}]
  table-head-sel (html/content (head-snip headers))
  table-body-sel (html/content (rows-snip rows)))

(defn build
  [table]
  (table-snip table))
