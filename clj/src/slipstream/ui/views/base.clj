(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html :refer [deftemplate defsnippet]]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.user :as mu]
            [slipstream.ui.views.utils :as u :refer [defn-memo]]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.messages :as messages]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.alerts :as alerts]
            [slipstream.ui.views.menubar :as menubar]
            [slipstream.ui.views.footer :as footer]))

(def base-template-filename (common/get-template "base.html"))

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

(defn-memo ^:private node-from-template
  [template-filename sel]
  ((html/snippet template-filename sel [] identity)))

(defn-memo ^:private node-from-base-template
  [sel]
  (set (node-from-template base-template-filename sel)))

(defn nodes-from-templates
  [sel template-filenames]
  (mapcat #(node-from-template % sel) template-filenames))

(defn additional-html
  [sel template-filenames]
  (->> template-filenames
       (remove nil?)
       (nodes-from-templates sel)
       distinct
       (remove (node-from-base-template sel))))

(deftemplate base base-template-filename
  [{:keys [title header content type alerts involved-templates metadata user] :as context}]
  menubar-sel         (html/content (menubar/menubar context))
  topbar-sel          (u/remove-if (and (u/chooser? type) (empty? alerts)))
  [:#release-version] (html/content @version/slipstream-release-version)
  footer-sel          (u/remove-if (u/chooser? type))
  title-sel           (html/content (common/title title))
  css-container-sel   (html/append (additional-html css-sel involved-templates))
  header-sel          (html/substitute header)
  content-sel         (html/substitute content)
  alert-container-sel (html/content (map alerts/alert alerts))
  ; [:span html/text-node] (html/replace-vars messages/all-messages)
  bottom-scripts-container-sel  (html/append (additional-html bottom-scripts-sel involved-templates))
  )

(defn generate
  [{:keys [metadata template-filename alerts] :as context}]
  (let [user (mu/user-map metadata)
        involved-templates [(when-not (empty? alerts) alerts/template-filename)
                            menubar/template-filename
                            template-filename]]
    (println "user:" user)
    (base (assoc context
            :user user
            :involved-templates involved-templates))))