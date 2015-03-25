(ns slipstream.ui.util.icons
  "Predefined icons for SlipStream items as an abstraction of the underlying
  icon or icon set used, e.g. Glyphicons or other..."
  (:refer-clojure :exclude [set])
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc :refer [defn-memo]]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]))

(uc/def-this-ns)

(localization/def-scoped-t)

;; TODO: Take into account the html/icon-list.html template to retrieve icons.

;; TODO: Overlays are still beta. They disrupt the layout, specially next to text.

(defmacro deficon
  [icon-symbol icon & {:keys [overlay style]}]
  (let [tooltip-placement-symbol  (symbol "tooltip-placement")
        description-symbol        (symbol "description")
        overlay-symbol            (symbol "overlay")
        style-symbol              (symbol "style")]
   `(def ~icon-symbol
       ^{:type :icon/symbol}
       (fn [& {:keys [~tooltip-placement-symbol ~overlay-symbol ~style-symbol]}]
         (let [~description-symbol (uc/title-case (t ~(str "description." (name icon-symbol))))]
           ~(if (symbol? icon)
              `(assoc (~icon :tooltip-placement ~tooltip-placement-symbol)
                      :description    ~description-symbol
                      :overlay        (or ~overlay-symbol ~overlay)
                      :style          (or ~style-symbol   ~style))
               `(with-meta
                  {:tooltip-placement ~tooltip-placement-symbol
                   :class-suffix      ~icon
                   :description       ~description-symbol
                   :overlay           (or ~overlay-symbol ~overlay)
                   :style             (or ~style-symbol   ~style)}
                  {:type :icon/computed})))))))

(deficon unknown                "question-sign")

(deficon home                   "home")
(deficon project                "folder-open")
(deficon module                 project)
(deficon user                   "user")
(deficon super-user             user :overlay "star")
(deficon users                  "users")
(deficon dashboard              "dashboard")
(deficon vms                    dashboard)
(deficon runs                   dashboard)
(deficon deployment             "th")
(deficon image                  "hdd")
(deficon orchestrator           image)
(deficon vm                     image)
(deficon build                  "tower")
(deficon deployment-run         deployment)
(deficon image-run              image)
(deficon image-build            build)
(deficon run                    deployment)
(deficon node                   "modal-window")
(deficon config                 "cog") ; or "wrench"
(deficon service-catalog        "th-list")
(deficon documentation          "book")

(deficon action-new-project     project)
(deficon action-new-image       image)
(deficon action-new-deployment  deployment)
(deficon action-run             "cloud-upload")
(deficon action-build           build)
(deficon action-import          "floppy-open")
(deficon action-edit            "pencil")
(deficon action-copy            "duplicate")
(deficon action-publish         "eye-open")
(deficon action-unpublish       "eye-close")
(deficon action-log-out         "log-out")
(deficon action-terminate       "ban-circle")
(deficon action-new-user        user)
(deficon action-edit-user       action-edit)
(deficon action-save            "floppy-disk")
(deficon action-create          action-save)
(deficon action-cancel          "stop")
(deficon action-delete          "trash")
(deficon action-ok              "ok")
(deficon action-export-users    "download")

(deficon run-in-transitional-state  "refresh"           :style :info)
(deficon run-with-abort-flag-set    "exclamation-sign"  :style :danger)
(deficon run-successfully-ready     "ok"                :style :success)
(deficon run-terminated             "off"               :style :muted)

(deficon no-icon                nil)

(defn- icon-get
  [icon-symbol]
  (if-let [icon-resolved-symbol (resolve icon-symbol)]
    (var-get icon-resolved-symbol)
    (do
      (println (str "Missing icon: '" icon-symbol "' - Using 'unknown' instead."))
      unknown)))

(defn icon-for
  "Returns the icon keywords given a keyword or a string.
  E.g. given :deployment, 'deployment' or 'Deployment' returns :slipstream.ui.util.icons :as/th.
  Useful to retrieve icons for module categories. If 'item' is 'nil', the icon will be removed."
  [item]
  (->> (or item "no-icon")
       uc/keywordize
       name
       s/lower-case
       (symbol this-ns)
       icon-get))

;; Bootstrap

(defn- glyphicon-icon-cls
  [icon]
  (str "glyphicon-" (name icon)))

(defn-memo ^:private current-glyphicon-icon-cls
  [glyphicon-node]
  (let [cls-list (html/attr-values glyphicon-node :class)]
    (some #(re-matches #"glyphicon-[\w-]+" %) cls-list)))

(defn- set-icon
  [icon]
  (let [icon-computed (case (type icon)
                        :icon/computed   icon
                        :icon/symbol     (icon))
        {:keys [tooltip-placement class-suffix description overlay style]} icon-computed
        show-tooltip? (not-empty tooltip-placement)]
    (fn [icon-node]
      (when (not-empty class-suffix)
        (html/at icon-node
              ue/this   (ue/replace-class
                          (current-glyphicon-icon-cls icon-node)
                          (glyphicon-icon-cls class-suffix))
              ue/this   (ue/when-add-class            show-tooltip? "ss-icon-tooltip")
              ue/this   (ue/when-add-class            (not-empty overlay) (str "ss-icon-overlay-" overlay))
              ue/this   (ue/when-add-class            style               (str "text-" (name style)))
              ue/this   (ue/when-set-title            show-tooltip? description)
              ue/this   (ue/when-set-data :toggle     show-tooltip? "tooltip")
              ue/this   (ue/when-set-data :placement  show-tooltip? tooltip-placement))))))

(defn set
  "Sets the icon in the given node. If icon is nil, the icon node (i.e. <span> element) is removed."
  [icon]
  (when icon
    (set-icon icon)))

(defn when-set
  "Sets the icon in the given node. If icon is nil, the icon existent in the node (if any) is left."
  [icon]
  (if icon
    (set-icon icon)
    identity))
