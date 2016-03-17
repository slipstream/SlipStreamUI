(ns slipstream.ui.views.user
  (:require [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
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
  [configured-clouds {:keys [category category-type parameters]}]
  (with-meta
    {:icon (case category-type
             :general icons/user-section-general
             :global  icons/user-section-cloud
             nil)
     :type  (and
              (= category-type :global)
              (if (and configured-clouds (configured-clouds category))
                :configured-connector
                :not-configured-connector))
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
  (let [cloud-default (->> user :configuration :available-clouds (filter :default?) first)]
    (when-not (-> user :configuration :configured-clouds)
      {:type      :error
       :container :fixed
       :title     (t (if own-profile?
                       :alert.no-clouds-configured.own-profile.title
                       :alert.no-clouds-configured.others-profile.title))
       :msg       (t (if own-profile?
                       :alert.no-clouds-configured.own-profile.msg
                       :alert.no-clouds-configured.others-profile.msg)
                     (:value cloud-default)
                     (current-user/username)
                     (some-> cloud-default :value uc/keywordize name))})))

(defn- cloud-default-not-configured-alert
  [user own-profile?]
  (let [cloud-default (->> user :configuration :available-clouds (filter :default?) first)]
    (when-not (:configured? cloud-default)
      {:type      :warning
       :container :fixed
       :title     (t (if own-profile?
                       :alert.cloud-default-not-configured.own-profile.title
                       :alert.cloud-default-not-configured.others-profile.title))
       :msg       (t (if own-profile?
                       :alert.cloud-default-not-configured.own-profile.msg
                       :alert.cloud-default-not-configured.others-profile.msg)
                     (:value cloud-default)
                     (current-user/username)
                     (some-> cloud-default :value uc/keywordize name))})))

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
        configured-clouds (-> user :configuration :configured-clouds)
        own-profile? (current-user/is? user)]
    (base/generate
      {:parsed-metadata user
       :header (header user)
       :html-dependencies {:internal-js-filenames ["user.js"]}
       :secondary-menu-actions [action/edit
                                action/delete]
       :alerts (when (page-type/view?)
                 [(or
                    (no-cloud-configured-alert          user own-profile?)
                    (cloud-default-not-configured-alert user own-profile?))
                  (no-ssh-keys-configured-alert       user own-profile?)])
       :content (-> [{:icon       icons/user-section-summary
                      :title      (t :summary)
                      :selected?  true
                      :content    (t/user-summary-table user)}]
                  (into (map (partial category-section configured-clouds) (:parameters user)))
                  (conj {:type    :flat-section
                         :content (when (page-type/view?)
                                    (ue/text-div-snip (t :footnote-about-adding-connectors (current-user/uri)) :html true))}))})))
