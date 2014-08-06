(ns slipstream.ui.views.section
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]
            [slipstream.ui.views.common :as common]))

(def template-filename (common/get-template "section.html"))

(def section-id "ss-section")
(def section-selected-cls "ss-section-selected")

(def section-group-sel [:#ss-section-group])
(def section-sel [:.ss-section])
(def section-anchor-sel (concat section-sel [:.ss-section-activator]))
(def section-id-sel [(html/id= section-id)])
(def section-title-sel [:.ss-section-title])
(def section-content-sel [:.ss-section-content])

(defn section-nodes
  [sections]
  (html/clone-for [{:keys [title selected? content type] :as section} sections
                   :let [section-uid (gensym section-id)]]
    section-sel         (u/enable-class selected? section-selected-cls)
    section-sel         (u/when-add-class type (str "ss-section-" (name type)))
    section-title-sel   (html/content (str title))
    section-content-sel (html/content content)
    section-anchor-sel  (u/set-href "#" section-uid)
    section-id-sel      (u/set-id section-uid)))

(html/defsnippet section-group-snip template-filename section-group-sel
  [sections]
  section-sel (section-nodes sections))

(defn build
  [sections]
  (html/content (section-group-snip sections)))
