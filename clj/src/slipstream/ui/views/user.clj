(ns slipstream.ui.views.user
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn- category-section
  [{:keys [category parameters]}]
  {:title category
   :content (t/parameters-table parameters)})

(localization/with-prefixed-t :header
  (defn- header
    [user]
    {:icon icons/user
     :title     (cond
                  (page-type/new?)  (t :title.new-user)
                  (:loggedin? user) (t :title.loggedin)
                  :else             (t :title.not-loggedin (:username user)))
     :subtitle  (cond
                  (page-type/new?)  (t :subtitle.new-user)
                  (:super? user)    (t :subtitle.super)
                  :else             (t :subtitle.not-super))}))

(defn page
  [metadata]
  (let [user (user/parse metadata)]
    (base/generate
      {:parsed-metadata user
       :header (header user)
        :html-dependencies {:internal-js-filenames ["user.js"]}
       :secondary-menu-actions [action/edit
                                action/delete]
       :resource-uri (if (current-user/super?) (:uri user) (t :header.title.loggedin))
       :content (into [{:title (t :summary)
                        :selected? true
                        :content (t/user-summary-table user)}]
                      (map category-section (parameters/parse metadata)))})))
