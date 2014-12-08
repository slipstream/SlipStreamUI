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

(defmacro deficon
  [icon-symbol icon]
  (let [tooltip-placement-symbol  (symbol "tooltip-placement")
        description-symbol        (symbol "description")]
   `(def ~icon-symbol
       ^{:type :icon/symbol}
       (fn [& {:keys [~tooltip-placement-symbol]}]
         (let [~description-symbol (uc/title-case (t ~(str "description." (name icon-symbol))))]
           ~(if (symbol? icon)
              `(assoc (~icon :tooltip-placement ~tooltip-placement-symbol)
                      :description ~description-symbol)
               `(with-meta
                  {:tooltip-placement  ~tooltip-placement-symbol
                   :class-suffix       ~icon
                   :description        ~description-symbol}
                  {:type :icon/computed})))))))

(deficon unknown                "question-sign")

(deficon home                   "home")
(deficon project                "folder-open")
(deficon module                 project)
(deficon user                   "user")
(deficon users                  user)
(deficon dashboard              "dashboard")
(deficon vms                    dashboard)
(deficon deployment             "th")
(deficon image                  "hdd")
(deficon build                  "tower")
(deficon deployment-run         deployment)
(deficon image-run              image)
(deficon image-build            build)
(deficon run                    deployment)
(deficon node                   "unchecked")
(deficon config                 "cog") ; or "wrench"
(deficon service-catalog        "th-list")
(deficon documentation          "book")

(deficon action-new-project     project)
(deficon action-new-image       image)
(deficon action-new-deployment  deployment)
(deficon action-run             "send")
(deficon action-build           build)
(deficon action-import          "cloud-upload")
(deficon action-edit            "pencil")
(deficon action-copy            "repeat")
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
  Useful to retrieve icons for module categories."
  [item]
  (->> (or item "unknown")
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
        {:keys [tooltip-placement class-suffix description]} icon-computed
        show-tooltip? (not-empty tooltip-placement)]
    (fn [icon-node]
      (html/at icon-node
        ue/this   (ue/replace-class
                    (current-glyphicon-icon-cls icon-node)
                    (glyphicon-icon-cls class-suffix))
        ue/this   (ue/when-add-class            show-tooltip? "ss-table-tooltip")
        ue/this   (ue/when-set-title            show-tooltip? description)
        ue/this   (ue/when-set-data :toogle     show-tooltip? "tooltip")
        ue/this   (ue/when-set-data :placement  show-tooltip? tooltip-placement)))))

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
