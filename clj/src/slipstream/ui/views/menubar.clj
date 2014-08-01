(ns slipstream.ui.views.menubar
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]
            [slipstream.ui.views.common :as common]))

(def template-filename (common/get-template "menubar.html"))

(def navbar-sel [:.navbar])
(def menubar-unlogged-sel (concat navbar-sel [:.menubar-unlogged]))
(def menubar-logged-in-sel (concat navbar-sel [:.menubar-logged-in]))
(def menubar-super-user-item-sel [:.menubar-super-user-item])
(def menubar-username-sel [:#menubar-username])
(def menubar-user-profile-anchor-sel [:#menubar-user-profile-anchor])

(def menubar-unlogged
  (let [snip (html/snippet template-filename menubar-unlogged-sel [] identity)]
    (snip)))

(def menubar-logged-in
  (let [snip (html/snippet template-filename menubar-logged-in-sel [] identity)]
    (snip)))

(defmulti menubar
  (fn [{:keys [type user]}]
    (cond
      (u/chooser? type)   :chooser
      (nil? user)         :unlogged
      (:logged-in? user)  :logged-in
      :else :un-logged)))

(defmethod menubar :chooser
  [_]
  nil)

(defmethod menubar :unlogged
  [_]
  menubar-unlogged)

(defmethod menubar :logged-in
  [{:keys [user]}]
  (html/at menubar-logged-in
           menubar-super-user-item-sel (u/remove-if-not (:super? user))
           menubar-username-sel (html/content (:name user))
           menubar-user-profile-anchor-sel (u/set-href (:uri user))))
