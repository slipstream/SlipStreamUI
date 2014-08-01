(ns slipstream.ui.views.section
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u :refer [defn-memo]]
            [slipstream.ui.views.common :as common]))

(def template-filename (common/get-template "section.html"))

(def section-group-sel [:.ss-section-group])
(def section-sel [:.ss-section])
(def section-sel [:#ss-section.panel-group])

; (html/defsnippet header-snip template-filename base/header-sel
;   []
;   identity)

; (html/defsnippet content-snip template-filename base/content-sel
;   []
;   identity)

; (defn-memo header-icon-default-cls
;   [header-node]
;   (let [icon-span (first (html/select header-node header-icon-sel))
;         icon-cls-list (html/attr-values icon-span :class)]
;     (some #(re-matches #"glyphicon-[\w-]+" %) icon-cls-list)))

; (gensym "collapse")

; (defn transform
;   [sections]
;   (fn [match]
;     (html/at match
;              header-icon-sel  (u/when-replace-class icon
;                                 (header-icon-default-cls match)
;                                 (u/glyphicon-icon-cls icon))
;              status-code-sel  (u/when-html-content status-code)
;              title-sel        (u/when-html-content title)
;              subtitle-sel     (u/when-html-content subtitle))))
