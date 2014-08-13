(ns slipstream.ui.views.menubar
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]
            [slipstream.ui.views.common :as common]))

(def template-filename (common/get-template "menubar.html"))

(def navbar-sel [:.navbar])
(def menubar-unlogged-sel (concat navbar-sel [:.ss-menubar-unlogged]))
(def menubar-logged-in-sel (concat navbar-sel [:.ss-menubar-logged-in]))
(def super-user-item-sel [:.ss-menubar-super-user-item])
(def username-sel [:#ss-menubar-username])
(def user-profile-anchor-sel [:#ss-menubar-user-profile-anchor])

(def menubar-unlogged-node
  (let [snip (html/snippet template-filename menubar-unlogged-sel [] identity)]
    (snip)))

(def menubar-logged-in-node
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
  menubar-unlogged-node)

(defmethod menubar :logged-in
  [{:keys [user]}]
  (html/at menubar-logged-in-node
           super-user-item-sel (u/remove-if-not (:super? user))
           username-sel (html/content (:username user))
           user-profile-anchor-sel (u/set-href (:uri user))))
