(ns slipstream.ui.views.section
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.views.subsection :as subsection]))

(def template-filename (u/template-path-for "section.html"))

(def section-selected-cls "ss-section-selected")

(def section-group-sel [:#ss-section-group])
(def section-sel [[:.ss-section (ue/first-of-class "ss-section")]])
(def section-anchor-sel (concat section-sel [:.ss-section-activator]))
(def chevron-sel (concat section-anchor-sel [:span [:span (html/nth-child 2)]]))
(def section-panel-sel [[:.panel-collapse :.collapse]])
(def section-title-sel [:.ss-section-title])
(def section-content-sel [:.ss-section-content])

(html/defsnippet section-group-snip template-filename section-group-sel
  [sections]
  ue/this (ue/content-for section-sel [{:keys [title selected? content type] :as section} sections
                   :let [section-uid (->> title uc/keywordize name (str "ss-section-"))
                         unique-section? (-> sections count (= 1))
                         collapsible?    (not unique-section?)]]
    section-sel         (ue/enable-class (uc/first-not-nil selected? unique-section?) section-selected-cls)
    section-sel         (ue/when-add-class type (str "ss-section-" (name type)))
    section-title-sel   (html/content (str title))
    section-content-sel (ue/if-enlive-node content
                          (if (string? content)
                            (html/html-content content)
                            (html/content content))
                          (subsection/build content))
    section-anchor-sel  (ue/if-set-href collapsible?
                                       (str "#" section-uid)
                                       nil)
    chevron-sel         (ue/remove-if-not collapsible?)
    section-panel-sel   (ue/set-id section-uid)))

(defn build
  [sections]
  (html/content (->> sections
                     (remove nil?)
                     u/ensure-one-selected
                     section-group-snip)))
