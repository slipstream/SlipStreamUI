(ns slipstream.ui.views.util.icons
  "Predefined icons for SlipStream items as an abstraction of the underlying
  icon or icon set used, e.g. Glyphicons or other..."
  (:refer-clojure :exclude [set])
  (:require [slipstream.ui.views.utils :as u :refer [defn-memo]]
            [net.cgrand.enlive-html :as html]))

(u/def-this-ns)

(def home           ::home)
(def project        ::folder-open)
(def module         project)
(def user           ::user)
(def users          user)
(def dashboard      ::dashboard)
(def run            ::th)
(def image          ::hdd)
(def config         ::cog) ; or ::wrench
(def documentation  ::book)

(def action-new-project project)
(def action-new-user    user)
(def action-run         ::send)
(def action-edit        ::pencil)
(def action-copy        ::repeat)
(def action-unpublish   ::globe)
(def action-log-out     ::log-out)

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
    (throw (Exception. (format "Invalid icon %s. Use predefined icons in namespace %s."
                               icon
                               this-ns))))
  (fn [icon-node]
    ((u/replace-class
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
