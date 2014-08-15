(ns slipstream.ui.views.section
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]
            [slipstream.ui.views.subsection :as subsection]
            [slipstream.ui.views.common :as common]))

(def template-filename (common/get-template "section.html"))

(def section-id "ss-section")
(def section-selected-cls "ss-section-selected")

(def section-group-sel [:#ss-section-group])
(def section-sel [[:.ss-section (u/first-of-class "ss-section")]])
(def section-anchor-sel (concat section-sel [:.ss-section-activator]))
(def chevron-sel (concat section-anchor-sel [:span [:span (html/nth-child 2)]]))
(def section-id-sel [(html/id= section-id)])
(def section-title-sel [:.ss-section-title])
(def section-content-sel [:.ss-section-content])

(html/defsnippet section-group-snip template-filename section-group-sel
  [sections]
  u/this (u/content-for section-sel [{:keys [title selected? content type] :as section} sections
                   :let [section-uid (gensym section-id)
                         unique-section? (-> sections count (= 1))
                         collapsible? (if (nil? selected?)
                                        (not unique-section?)
                                        true)]]
    section-sel         (u/enable-class (u/first-not-nil selected? unique-section?) section-selected-cls)
    section-sel         (u/when-add-class type (str "ss-section-" (name type)))
    section-title-sel   (html/content (str title))
    section-content-sel (u/if-enlive-node content
                          (html/content content)
                          (subsection/build content))
    section-anchor-sel  (u/if-set-href collapsible?
                                       (str "#" section-uid)
                                       nil)
    chevron-sel         (u/remove-if-not collapsible?)
    section-id-sel      (u/set-id section-uid)))

(defn build
  [sections]
  (html/content (section-group-snip sections)))
