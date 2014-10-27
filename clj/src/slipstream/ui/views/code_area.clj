(ns slipstream.ui.views.code-area
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]))

(def template-filename (u/template-path-for "code_area.html"))

(def code-viewer-sel [:pre.ss-code-viewer])
(def code-editor-sel [:pre.ss-code-viewer])

(html/defsnippet code-viewer-snip template-filename code-viewer-sel
  [code]
  ue/this (html/content (str code)))

(html/defsnippet code-editor-snip template-filename code-editor-sel
  [code]
  ue/this (html/content (str code)))

(defn build
  [code editable?]
  (if editable?
    (code-editor-snip code)
    (code-viewer-snip code)))
