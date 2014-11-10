(ns slipstream.ui.views.code-area
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]))

(def template-filename (u/template-path-for "code_area.html"))

(def code-viewer-sel [:pre.ss-code-viewer])
(def code-editor-sel [:pre.ss-code-editor])

(html/defsnippet code-viewer-snip template-filename code-viewer-sel
  [code & {:keys [id]}]
  ue/this (html/content (str code))
  ue/this (ue/set-id id))

(html/defsnippet code-editor-snip template-filename code-editor-sel
  [code & {:keys [id]}]
  ue/this (html/content (str code))
  ue/this (ue/set-id id))

(defn build
  [code & {:keys [editable? id]}]
  (if editable?
    (code-editor-snip code :id id)
    (code-viewer-snip code :id id)))
