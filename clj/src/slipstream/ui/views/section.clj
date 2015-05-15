(ns slipstream.ui.views.section
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.subsection :as subsection]))

(def template-filename (u/template-path-for "section.html"))

(def section-selected-cls "ss-section-selected")

(def section-group-sel [:#ss-section-group])
(def section-sel [[:.ss-section (ue/first-of-class "ss-section")]])
(def section-anchor-sel (concat section-sel [:.ss-section-activator]))
(def chevron-sel (concat section-anchor-sel [:span [:span (html/nth-child 2)]]))
(def section-panel-sel [[:.panel-collapse :.collapse]])
(def section-icon-sel [:.ss-section-icon])
(def section-title-sel [:.ss-section-title])
(def section-content-sel [:.ss-section-content])

(html/defsnippet section-group-snip template-filename section-group-sel
  [section-group-index sections]
  ue/this (ue/append-to-id "-" section-group-index)
  ue/this (ue/content-for section-sel [{:keys [icon title selected? content type] :as section} sections
                   :let [section-uid (->> title uc/keywordize name (str "ss-section-"))
                         unique-section? (-> sections count (= 1))
                         collapsible?    (not unique-section?)]]
    section-sel         (ue/enable-class (uc/first-not-nil selected? unique-section?) section-selected-cls)
    section-sel         (ue/when-add-class type (str "ss-section-" (name type)))
    section-icon-sel    (when icon (icons/set (icon :tooltip-placement "bottom")))
    section-title-sel   (html/content (str title))
    section-content-sel (ue/if-enlive-node content
                          (if (string? content)
                            (html/html-content content)
                            (html/content content))
                          (subsection/build content))
    section-anchor-sel  (ue/if-set-href collapsible?
                                       (str "#" section-uid)
                                       nil)
    section-anchor-sel  (ue/set-data :parent ".panel-group")
    chevron-sel         (ue/remove-if-not collapsible?)
    section-panel-sel   (ue/set-id section-uid)))

(defn build
  [sections]
  (html/content (->> sections
                     (remove nil?)
                     u/ensure-one-selected
                     (partition-by (comp :section-group meta))
                     (map-indexed section-group-snip)
                     concat)))
