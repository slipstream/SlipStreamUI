(ns slipstream.ui.views.user
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.views.secondary-menu-actions :as action]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn- category-section
  [{:keys [category category-type parameters]}]
  (with-meta
    {:icon (case category-type
             :general icons/user-section-general
             :global  icons/user-section-cloud
             nil)
     :title category
     :content (t/parameters-table parameters)}
    (when (= category-type :global) {:section-group :cloud})))

(localization/with-prefixed-t :header
  (defn- header
    [user]
    {:icon icons/user
     :title     (cond
                  (page-type/new?)        (t :title.new-user)
                  (current-user/is? user) (t :title.loggedin)
                  :else                   (t :title.not-loggedin (:username user)))
     :subtitle  (cond
                  (page-type/new?)  (t :subtitle.new-user)
                  (:super? user)    (t :subtitle.super)
                  :else             (t :subtitle.not-super))}))

(defn- no-cloud-configured-alert
  [user own-profile?]
  (when-not (-> user :configuration :configured-clouds)
    {:type      :warning
     :container :fixed
     :title     (t (if own-profile?
                     :alert.no-clouds-configured.own-profile.title
                     :alert.no-clouds-configured.others-profile.title))
     :msg       (t (if own-profile?
                     :alert.no-clouds-configured.own-profile.msg
                     :alert.no-clouds-configured.others-profile.msg))}))

(defn- no-ssh-keys-configured-alert
  [user own-profile?]
  (when-not (or own-profile? (-> user :configuration :ssh-keys))
    {:type      :warning
     :container :fixed
     :title     (t :alert.no-ssh-keys-configured.others-profile.title)
     :msg       (t :alert.no-ssh-keys-configured.others-profile.msg)}))

(defn page
  [metadata]
  (let [user (user/parse metadata)
        own-profile? (current-user/is? user)]
    (base/generate
      {:parsed-metadata user
       :header (header user)
       :html-dependencies {:internal-js-filenames ["user.js"]}
       :secondary-menu-actions [action/edit
                                action/delete]
       :resource-uri (if (current-user/super?) (:uri user) (t :header.title.loggedin))
       :alerts (when (page-type/view?)
                 [(no-cloud-configured-alert    user own-profile?)
                  (no-ssh-keys-configured-alert user own-profile?)])
       :content (into [{:icon       icons/user-section-summary
                        :title      (t :summary)
                        :selected?  true
                        :content    (t/user-summary-table user)}]
                      (map category-section (:parameters user)))})))
