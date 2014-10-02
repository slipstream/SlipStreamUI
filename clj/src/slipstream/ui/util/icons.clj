(ns slipstream.ui.util.icons
  "Predefined icons for SlipStream items as an abstraction of the underlying
  icon or icon set used, e.g. Glyphicons or other..."
  (:refer-clojure :exclude [set])
  (:require [clojure.string :as s]
            [slipstream.ui.util.clojure :as uc :refer [defn-memo]]
            [slipstream.ui.util.enlive :as ue]
            [net.cgrand.enlive-html :as html]))

(uc/def-this-ns)

(def unknown        ::question-sign)

(def home           ::home)
(def project        ::folder-open)
(def module         project)
(def user           ::user)
(def users          user)
(def dashboard      ::dashboard)
(def run            ::th)
(def deployment     run)
(def image          ::hdd)
(def config         ::cog) ; or ::wrench
(def documentation  ::book)

(def action-new-project project)
(def action-new-image   image)
(def action-new-deployment   deployment)
(def action-run         ::send)
(def action-build       ::tower)
(def action-import      ::cloud-upload)
(def action-edit        ::pencil)
(def action-copy        ::repeat)
(def action-publish     ::eye-open)
(def action-unpublish   ::eye-close)
(def action-log-out     ::log-out)
(def action-terminate   ::ban-circle)
(def action-new-user    user)
(def action-edit-user   action-edit)
(def action-save        ::floppy-disk)
(def action-cancel      ::stop)
(def action-delete      ::trash)
(def action-ok          ::ok)

(defn icon-for
  "Returns the icon keywords given a keyword or a string.
  E.g. given :deployment, 'deployment' or 'Deployment' returns :slipstream.ui.util.icons :as/th.
  Useful to retrieve icons for module categories."
  [item]
  (->> (or item "unknown")
       name
       s/lower-case
       (symbol this-ns)
       resolve
       var-get))

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
  (when-not (= (namespace icon) this-ns)
    (throw (IllegalArgumentException. (format "Invalid icon %s. Use predefined icons in namespace %s."
                               icon
                               this-ns))))
  (fn [icon-node]
    ((ue/replace-class
        (current-glyphicon-icon-cls icon-node)
        (glyphicon-icon-cls icon))
     icon-node)))

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
