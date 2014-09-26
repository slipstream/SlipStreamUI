(ns slipstream.ui.views.secondary-menu-actions
  "Predefined actions for the secondary menu."
  (:refer-clojure :exclude [import])
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]))

(localization/def-scoped-t)

(defn- t-with-ellipsis
  [t-path]
  (str (t t-path) (t :ellipsis)))

(defn new-project
  []
  {:name (t :new-project)
   :uri  "module/new"
   :icon icons/action-new-project})

(defn new-image
  []
  {:name (t :new-image)
   :uri  "module/new"
   :icon icons/action-new-image})

(defn new-deployment
  []
  {:name (t :new-deployment)
   :uri  "module/new"
   :icon icons/action-new-deployment})

(defn import
  []
  {:name (t :import)
   :uri  "module/new"
   :icon icons/action-import})

(defn new-user
  []
  {:name (t :new-user)
   :uri  "user/new"
   :icon icons/action-new-user})

(defn edit-user
  []
  {:name (t :edit-user)
   :uri  "" ; TODO: provide right URL hele
   :icon icons/action-edit-user})

(defn run
  []
  {:name (t-with-ellipsis :run)
   :uri  "module/new"
   :icon icons/action-run})

(defn build
  []
  {:name (t-with-ellipsis :build)
   :uri  "module/build"
   :icon icons/action-build})

(defn edit
  []
  {:name (t :edit)
   :uri  "module/new"
   :icon icons/action-edit
   :super-only? true})

(defn copy
  []
  {:name (t-with-ellipsis :copy)
   :uri  "module/new"
   :icon icons/action-copy
   :disabled? false
   :super-only? true})

(defn publish
  []
  {:name (t :publish)
   :uri  "module/new"
   :icon icons/action-publish
   :disabled? true})

(defn unpublish
  []
  {:name (t :unpublish)
   :uri  "module/new"
   :icon icons/action-unpublish
   :disabled? true})

(defn terminate
  []
  {:name (t :terminate)
   :uri  "run/terminate"
   :icon icons/action-terminate})
