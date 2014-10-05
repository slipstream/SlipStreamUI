(ns slipstream.ui.views.user
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.user.core :as user]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn- category-section
  [{:keys [category parameters]}]
  {:title category
   :content (t/parameters-table parameters)})

(defn- header
  [user]
  {:icon icons/user
   :title (if (:loggedin? user)
            (t :header.title.loggedin)
            (t :header.title.not-loggedin (:username user)))
   :subtitle (if (:super? user)
               (t :header.subtitle.super)
               (t :header.subtitle.not-super))})

(defn page
  [metadata]
  (localization/with-lang-from-metadata
    (let [user (user/parse metadata)]
      (base/generate
        {:metadata metadata
         :header (header user)
         :secondary-menu-actions [action/edit] ;; TODO: Only if (or (:loggedin? user) (:super? user))
         :resource-uri (:uri user)
         :content (into [{:title (t :summary)
                          :selected? true
                          :content (t/user-summary-table user)}]
                        (map category-section (parameters/parse metadata)))}))))
