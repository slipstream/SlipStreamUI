(ns slipstream.ui.views.base
  (:require [net.cgrand.enlive-html :as html :refer [deftemplate defsnippet]]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.dev :as ud]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.clojure :as uc :refer [defn-memo]]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.models.user.loggedin :as user-loggedin]
            [slipstream.ui.models.configuration :as configuration]
            [slipstream.ui.views.messages :as messages]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.alerts :as alerts]
            [slipstream.ui.views.section :as section]
            [slipstream.ui.views.subsection :as subsection]
            [slipstream.ui.views.menubar :as menubar]
            [slipstream.ui.views.table :as table]
            [slipstream.ui.views.breadcrumbs :as breadcrumbs]
            [slipstream.ui.views.secondary-menu :as secondary-menu]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.code-area :as code-area]
            [slipstream.ui.views.modal-dialogs :as modal-dialogs]))

(localization/def-scoped-t)

(def base-template-filename (u/template-path-for "base.html"))

(def head-sel [:head])
(def page-title-sel (concat head-sel [:> :title]))
(def base-sel (concat head-sel [:> :base]))

(def css-container-sel head-sel)
(def css-sel (concat css-container-sel [:> :link]))
(def bottom-scripts-container-sel [:#bottom-scripts])
(def bottom-scripts-sel (concat bottom-scripts-container-sel [:> :script]))

(def alert-container-sel [:#ss-alert-container-floating])

(def topbar-sel [:#topbar])
(def menubar-sel [:#menubar])
(def header-sel [:#header])
(def secondary-menubar-sel [:#ss-secondary-menubar-container])
(def breadcrumbs-sel [:#ss-breadcrumb-container :> :ol])
(def secondary-menu-sel [:#ss-secondary-menu])
(def content-sel [:#ss-content])
(def footer-sel [:#footer])
(def modal-dialogs-placeholder-sel [:#ss-modal-dialogs-placeholder])

(def noscript-title-sel     [:.ss-noscript-error-title])
(def noscript-subtitle-sel  [:.ss-noscript-error-subtitle])


(def error-page-cls "ss-error-page")
(def dev-mode-page-cls "ss-dev-mode-page")
(def beta-page-cls "ss-beta-page")
(def placeholder-page-cls "ss-placeholder-page")
(def in-progress-page-cls "ss-in-progress-page")

(def num-of-main-secondary-menu-actions-default 3)

(def ^:private edit-page-actions
  [action/save
   action/cancel
   action/delete])

(defn- save-page?
  [context]
  (-> context
      :view-name
      #{"configuration" "service-catalog"}))

(def ^:private save-page-actions
  [action/save])

(def ^:private new-page-actions
  [action/create
   action/cancel])


;; Bottom scripts snippets

(ue/def-blank-snippet ^:private bottom-scripts-snip :script
  [filenames]
  ue/this (html/clone-for [filename filenames]
            ue/this (ue/set-src filename)))

(defn- bottom-external-scripts-snip
  [filenames]
  (->> filenames
       (map (partial str "external/"))
       bottom-scripts-snip))

(defn- bottom-internal-scripts-snip
  [filenames]
  (->> (conj filenames "last.js")
       (map (partial str "js/"))
       bottom-scripts-snip))

;; Top CSS link snippet

(ue/def-blank-snippet ^:private css-links-snip :link
  [filenames]
  ue/this (ue/set-rel   "stylesheet")
  ue/this (ue/set-type  "text/css")
  ue/this (html/clone-for [filename filenames]
            ue/this (ue/set-href "css/" filename)))


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

(ue/def-blank-snippet ^:private save-form-snip :form
  [content-transformation-fn]
  ue/this (ue/set-id "save-form")
  ue/this (html/set-attr :accept-charset "utf-8")
  ue/this (html/set-attr :autocomplete "off")
  ue/this (ue/set-data :generic-form-messages {:success (t :generic-form-message.success)
                                               :error   (t :generic-form-message.error)})
  ue/this content-transformation-fn)

(defn- transform-content
  [content]
  (let [transformation-fn (ue/if-enlive-node content
                            (html/substitute content)
                            (section/build content))]
    (if (page-type/edit-or-new?)
      (html/content (save-form-snip transformation-fn))
      transformation-fn)))

(defn- prepend-ss-meta-info
  [context]
  (-> context
      (select-keys [:view-ns :view-name])
      (assoc :page-type (name page-type/*current-page-type*)
             :user-type (current-user/type-name)
             :navigate-away-confirmation-msg (t :navigate-away-confirmation-msg))
      (ue/map->meta-tag-snip :name-prefix "ss-")
      html/prepend))

(deftemplate base base-template-filename
  [{:keys [error-page?
           beta-page?
           placeholder-page?
           in-progress-page?
           page-title
           header
           resource-uri
           secondary-menu-actions
           num-of-main-secondary-menu-actions
           configuration
           content
           page-type
           alerts
           involved-templates]
    {:keys [css-filenames internal-js-filenames external-js-filenames]} :html-dependencies
    :as context}]
  [:head]               (prepend-ss-meta-info context)
  [:body]               (ue/enable-class error-page? error-page-cls)
  [:body]               (ue/enable-class ud/*dev?* dev-mode-page-cls)
  [:body]               (ue/enable-class placeholder-page? placeholder-page-cls)
  [:body]               (ue/enable-class beta-page? beta-page-cls)
  [:body]               (ue/enable-class in-progress-page? in-progress-page-cls)
  [:body]               (html/add-class (str "ss-page-type-" (name page-type/*current-page-type*)))
  [:body]               (html/add-class (str "ss-view-name-" (:view-name context)))
  page-title-sel        (html/content (u/page-title (or page-title (:title header))))
  ; base-sel              (ue/when-set-href (not *dev?*) "/") ;; TODO: Is that needed eventually??!
  base-sel              (ue/set-href "/") ;; TODO: Is that needed eventually??!
  menubar-sel           (html/substitute (menubar/menubar configuration))
  topbar-sel            (ue/remove-if (page-type/chooser?))
  breadcrumbs-sel       (breadcrumbs/transform context)
  secondary-menu-sel    (when-not (page-type/chooser?)
                          (secondary-menu/transform secondary-menu-actions (or
                                                                             num-of-main-secondary-menu-actions
                                                                             num-of-main-secondary-menu-actions-default)))
  secondary-menubar-sel (ue/remove-if-not (or resource-uri secondary-menu-actions))
  [:#release-version]   (html/content @version/slipstream-release-version)
  footer-sel            (ue/remove-if (page-type/chooser?))
  css-container-sel     (html/append (additional-html css-sel involved-templates))
  css-container-sel     (html/append (css-links-snip css-filenames))
  header-sel            (when-not (page-type/chooser?)
                          (ue/if-enlive-node header
                            (html/substitute header)
                            (header/transform header)))
  content-sel           (transform-content content)
  alert-container-sel   (html/content (map alerts/alert alerts))
  alert-container-sel   (html/append (alerts/hidden-templates))
  noscript-title-sel    (ue/when-content (t :noscript-error.title))
  noscript-subtitle-sel (ue/when-content (t :noscript-error.subtitle))
  bottom-scripts-container-sel  (html/append (additional-html bottom-scripts-sel involved-templates))
  bottom-scripts-container-sel  (html/append (bottom-external-scripts-snip external-js-filenames))
  bottom-scripts-container-sel  (html/append (bottom-internal-scripts-snip internal-js-filenames))
  modal-dialogs-placeholder-sel (html/content (modal-dialogs/required context))
  [:input]              (html/set-attr :autocomplete "off") ; NOTE: Disable 'autocomplete' for all inputs (specially required for Firefox: https://bugzilla.mozilla.org/show_bug.cgi?id=654072)
  [[:a (html/but (html/attr-starts :href "#"))]]  (if (page-type/chooser?) ;; TODO: Not do it when generating reports page (which currently uses still the :chooser page-type)
                                                    (ue/append-to-href "?chooser=true")
                                                    identity))

(defn- templates
  [current-template-filename]
  (cond-> []
    :always                   (conj alerts/template-filename)
    :always                   (conj menubar/template-filename)
    :always                   (conj subsection/template-filename)       ;; TODO: only if body has subsections.
    :always                   (conj section/template-filename)          ;; TODO: only if body has sections.
    (page-type/not-chooser?)  (conj modal-dialogs/template-filename)    ;; TODO: only if body has modal-dialogs.
    :always                   (conj table/template-filename)            ;; TODO: only if body has tables.
    :always                   (conj code-area/template-filename)        ;; TODO: only if body has code-areas.
    :always                   (conj current-template-filename)))

(defn- generate-with-ns
  [{:keys [template-filename] :as context}]
  (when ud/*dev?*
    (println "Generating base from ns" (:view-ns context)
             "- View name" (:view-name context)
             "- Title" (-> context :header :title)
             "- Template:" template-filename
             "- Metadata available?" (-> context :metadata boolean)
             "- Service Catalogue enabled?" (-> context
                                                :metadata
                                                configuration/parse
                                                configuration/service-catalog-enabled?)))
  (base
    (cond-> context
      (page-type/edit?)     (assoc :secondary-menu-actions  edit-page-actions)
      (page-type/new?)      (assoc :secondary-menu-actions  new-page-actions)
      (save-page? context)  (assoc :secondary-menu-actions  save-page-actions)
      :always               (assoc :configuration           (-> context :metadata configuration/parse))
      :always               (assoc :involved-templates      (templates template-filename)))))

(defmacro generate
  "This macro includes info about the caller into the 'context' map argument."
  [context]
  (let [view-ns   (str *ns*)
        view-name (->> view-ns (re-matches #"slipstream\.ui\.views\.(.*)") second)]
    `(~generate-with-ns (assoc ~context :metadata  ~(symbol "metadata")
                                        :view-ns   ~view-ns
                                        :view-name ~view-name))))
