(ns slipstream.ui.views.code-area
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.util.enlive :as ue]
            ))
            ; [slipstream.ui.util.clojure :as uc]
            ; [slipstream.ui.views.subcode-area :as subcode-area]

(def template-filename (common/get-template "code_area.html"))

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
