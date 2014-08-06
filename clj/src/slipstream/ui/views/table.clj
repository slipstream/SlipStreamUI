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


;; Cell

(html/defsnippet cell-text-snip template-filename cell-text-sel
  [text]
  u/this (html/content (str text)))

(html/defsnippet cell-link-snip template-filename cell-link-sel
  [{:keys [text href]}]
    [:a] (html/content (str text))
    [:a] (u/set-href href))


(html/defsnippet cell-icon-snip template-filename cell-icon-sel
  [icon]
  [:span] (icons/set icon))

(def cell-snip-dispatching
  {:cell/text cell-text-snip
   :cell/link cell-link-snip
   :cell/icon cell-icon-snip})

(defn- cell-node
  [{:keys [type content]}]
  (let [cell-snip (get cell-snip-dispatching type cell-text-snip)]
    (cell-snip content)))


;; Row

(html/defsnippet rows-snip template-filename table-row-sel
  [rows]
  u/this (html/clone-for [row rows]
           u/this (html/content (map cell-node row))))


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
