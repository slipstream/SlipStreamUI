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
            [slipstream.ui.views.section :as section]
            [slipstream.ui.views.content :as content]
            [slipstream.ui.views.menubar :as menubar]
            [slipstream.ui.views.breadcrumbs :as breadcrumbs]
            [slipstream.ui.views.secondary-menu :as secondary-menu]
            ))

(def base-template-filename (common/get-template "base.html"))

(def head-sel [:head])
(def page-title-sel (concat head-sel [:> :title]))

(def css-container-sel head-sel)
(def css-sel (concat css-container-sel [:> :link]))
(def bottom-scripts-container-sel [:#bottom-scripts])
(def bottom-scripts-sel (concat bottom-scripts-container-sel [:> :script]))

(def alert-container-sel [:#alert-container])

(def topbar-sel [:#topbar])
(def menubar-sel [:#menubar])
(def header-sel [:#header])
(def secondary-menubar-sel [:#ss-secondary-menubar-container])
(def breadcrumbs-sel [:#ss-breadcrumb-container :> :ol])
(def secondary-menu-sel [:#ss-secondary-menu])
(def content-sel [:#ss-content])
(def footer-sel [:#footer])

(def error-page-cls "ss-error-page")
(def beta-page-cls "ss-beta-page")
(def placeholder-page-cls "ss-placeholder-page")


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
  [{:keys [error-page?
           beta-page?
           placeholder-page?
           page-title
           header
           breadcrumbs
           secondary-menu-actions
           content
           type
           alerts
           involved-templates]
    :as context}]
  [:body]               (u/enable-class error-page? error-page-cls)
  [:body]               (u/enable-class placeholder-page? placeholder-page-cls)
  [:body]               (u/enable-class beta-page? beta-page-cls)
  page-title-sel        (html/content (u/page-title (or page-title (:title header))))
  menubar-sel           (html/substitute (menubar/menubar context))
  topbar-sel            (u/remove-if (and (u/chooser? type) (empty? alerts)))
  breadcrumbs-sel       (breadcrumbs/transform context)
  secondary-menu-sel    (secondary-menu/transform context)
  secondary-menubar-sel (u/remove-if-not (or breadcrumbs secondary-menu-actions))
  [:#release-version]   (html/content @version/slipstream-release-version)
  footer-sel            (u/remove-if (u/chooser? type))
  css-container-sel     (html/append (additional-html css-sel involved-templates))
  header-sel            (u/if-enlive-node header
                          (html/substitute header)
                          (header/transform header))
  content-sel           (u/if-enlive-node content
                          (html/substitute content)
                          (content/build content))
  alert-container-sel   (html/content (map alerts/alert alerts))
  alert-container-sel   (html/append (alerts/hidden-templates))
  ; [:span html/text-node] (html/replace-vars messages/all-messages)
  bottom-scripts-container-sel  (html/append (additional-html bottom-scripts-sel involved-templates))
  )

(defn generate
  [{:keys [header metadata template-filename content alerts] :as context}]
  (let [user (mu/user-map metadata)
        involved-templates [alerts/template-filename
                            menubar/template-filename
                            section/template-filename ;; TODO: only if sections in body.
                            template-filename]]
    (println "Generating base for" template-filename)
    ; (println "   user:" user)
    ; (println "   content type:" (type (first content)))
    (base (assoc context
            :user user
            ; :beta-page? true
            :involved-templates involved-templates))))