(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html :refer [deftemplate defsnippet]]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.messages :as messages]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.alerts :as alerts]
            [slipstream.ui.views.footer :as footer]))

(def head-sel [:head])
(def title-sel (concat head-sel [:> :title]))

(def css-container-sel head-sel)
(def css-sel (concat css-container-sel [:> :link]))
(def bottom-scripts-container-sel [:#bottom-scripts])
(def bottom-scripts-sel (concat bottom-scripts-container-sel [:> :script]))

(def alert-container-sel [:#alert-container])

(def topbar-sel [:#topbar])
(def menubar-sel [:#menubar])
(def header-sel [:#header])
(def content-sel [:#content])
(def footer-sel [:#footer])

(def base-template-filename (common/get-template "base.html"))

;; TODO: Look at slipstream.ui.views.module-base/ischooser? and refactor.
(defn chooser?
  [type]
  (= "chooser" type))

(def base-css
  "Static (computed) snippet with the CSS of the base-template-filename.
  Since the css of the base-template-filename don't change, we don't need
  to compute those at runtime."
  (let [snip (html/snippet base-template-filename css-sel [] identity)]
    (snip)))

(def base-bottom-scripts
  "Static (computed) snippet with the bottom scripts of the base-template-filename.
  Since the bottom scripts of the base-template-filename don't change, we don't need
  to compute those at runtime."
  (let [snip (html/snippet base-template-filename bottom-scripts-sel [] identity)]
    (snip)))

(defn additional
  [resources-sel {:keys [template alerts]}]
  (let [template-resources-snip (html/snippet template resources-sel [] identity)
        template-resources (template-resources-snip)
        alerts-resources (when (not-empty alerts)
                           ((html/snippet alerts/alerts-template-filename resources-sel [] identity)))
        base-resources (condp = resources-sel
                         css-sel base-css
                         bottom-scripts-sel base-bottom-scripts)]
    (remove (set base-resources) (concat alerts-resources template-resources))))

(defn remove-if
  [pred]
  (when-not pred
    identity))

(deftemplate base base-template-filename
  [{:keys [title header content type alerts template] :as context}]
  menubar-sel (remove-if (chooser? type))
  topbar-sel (remove-if (and (chooser? type) (empty? alerts)))
  [:#release-version] (html/content @version/slipstream-release-version)
  footer-sel (remove-if (chooser? type))
  title-sel (html/content (common/title title))
  css-container-sel  (html/append (when template (additional css-sel context)))
  header-sel (html/substitute header)
  content-sel (html/substitute content)
  alert-container-sel (html/content (map alerts/alert alerts))
  ; [:span html/text-node] (html/replace-vars messages/all-messages)
  bottom-scripts-container-sel  (html/append (when template (additional bottom-scripts-sel context)))
  )
