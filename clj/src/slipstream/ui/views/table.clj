(ns slipstream.ui.views.table
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.util.icons :as icons]))

(def template-filename (common/get-template "table.html"))

(def table-cls "ss-table")

(def table-sel [(html/has-class table-cls)])
(def table-head-sel [:.ss-table-head :> :tr])
(def table-header-sel (concat table-head-sel [:> [:th html/first-of-type]]))
(def table-body-sel [:.ss-table-body])
(def table-row-sel (concat table-body-sel [:> [:tr html/first-of-type]]))

(def cell-icon-sel (concat table-row-sel [:> [:td (u/first-of-class "ss-table-cell-icon")]]))
(def cell-link-sel (concat table-row-sel [:> [:td (u/first-of-class "ss-table-cell-link")]]))
(def cell-text-sel (concat table-row-sel [:> [:td (u/first-of-class "ss-table-cell-text")]]))
(def cell-boolean-sel (concat table-row-sel [:> [:td (u/first-of-class "ss-table-cell-boolean")]]))


;; Cell

(html/defsnippet cell-boolean-snip template-filename cell-boolean-sel
  [value]
  [:input] (u/toggle-checked value)
  [:input] (u/toggle-disabled true))

(html/defsnippet cell-text-snip template-filename cell-text-sel
  [text]
  u/this (html/content (str text)))

(defn cell-password-snip
  [pwd]
  ; (cell-text-snip (when pwd "•••")))
  (cell-text-snip (when pwd "***")))

(html/defsnippet cell-long-text-snip template-filename cell-text-sel
  [text]
  u/this (html/content (str text))
  u/this (u/set-style "word-wrap: break-word; max-width: 500px;"))

(html/defsnippet cell-link-snip template-filename cell-link-sel
  [{:keys [text href open-in-new-window?]}]
    [:a] (html/content (str text))
    [:a] (u/set-href href)
    [:a] (u/when-set-target open-in-new-window? "_blank"))

(defn cell-external-link-snip
  [cell]
  (cell-link-snip (assoc cell :open-in-new-window? true)))

(html/defsnippet cell-icon-snip template-filename cell-icon-sel
  [icon]
  [:span] (icons/set icon))

(defn- cell-snip
  "Get the cell-snip fn corresponding to a given cell type. This is done with a
  case (instead of e.g. a simple map) to detect unexpected cell types via an
  IllegalArgumentException."
  [{:keys [type content] :as cell}]
  (case type
    :cell/boolean        cell-boolean-snip
    :cell/text           (if (-> content count (> 100))
                           cell-long-text-snip
                           cell-text-snip)
    :cell/link           cell-link-snip
    :cell/external-link  cell-external-link-snip
    :cell/icon           cell-icon-snip
    :cell/password       cell-password-snip
    (throw (IllegalArgumentException.
      (str "No cell-snip defined for cell type: " type)))))

(defn- cell-node
  [{:keys [content] :as cell}]
  ((cell-snip cell) content))


;; Row

(html/defsnippet rows-snip template-filename table-row-sel
  [rows]
  u/this (html/clone-for [{:keys [style cells]} rows]
           u/this (html/content (map cell-node cells))
           u/this (u/when-add-class style (name style))))


;; Headers

(html/defsnippet head-snip template-filename table-header-sel
  [headers]
  u/this (html/clone-for [header headers]
            u/this (html/content (str header))))

(html/defsnippet table-snip template-filename table-sel
  [{:keys [headers rows] :as table}]
  table-head-sel (html/content (head-snip headers))
  table-body-sel (html/content (rows-snip rows)))

(defn build
  [table]
  (table-snip table))
