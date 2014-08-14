(ns slipstream.ui.views.user
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.user.core :as user]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.base :as base]))

(defn- category-section
  [{:keys [category parameters]}]
  {:title category
   :content (t/parameters-table parameters)})

(defn- header
  [user]
  {:icon icons/user
   :title (if (:loggedin? user)
            "Your profile"
            (str (:username user) "'s user profile"))
   :subtitle (if (:super? user)
               "Administrator"
               "Regular user")})

(defn page [metadata type]
  (let [user (user/parse metadata)]
    (base/generate
      {:metadata metadata
       :header (header user)
       :secondary-menu-actions [action/edit-user] ;; TODO: Only if (or (:loggedin? user) (:super? user))
       :resource-uri (:uri user)
       :content (into [{:title "Summary"
                        :selected? true
                        :content (t/user-summary-table user)}]
                      (map category-section (parameters/parse metadata)))})))
