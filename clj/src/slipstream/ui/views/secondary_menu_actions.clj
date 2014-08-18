(ns slipstream.ui.views.secondary-menu-actions
  "Predefined actions for the secondary menu."
  (:refer-clojure :exclude [import])
  (:require [slipstream.ui.util.icons :as icons]))

(def new-project
  {:name "New project"
   :uri  "module/new"
   :icon icons/action-new-project})

(def new-image
  {:name "New machine image"
   :uri  "module/new"
   :icon icons/action-new-image})

(def new-deployment
  {:name "New deployment"
   :uri  "module/new"
   :icon icons/action-new-deployment})

(def import
  {:name "Import"
   :uri  "module/new"
   :icon icons/action-import})

(def new-user
  {:name "New user"
   :uri  "user/new"
   :icon icons/action-new-user})

(def edit-user
  {:name "Edit user (url missing yet)"
   :uri  "" ; TODO: provide right URL hele
   :icon icons/action-edit-user})

(def run
  {:name "Run..."
   :uri  "module/new"
   :icon icons/action-run})

(def edit
  {:name "Edit"
   :uri  "module/new"
   :icon icons/action-edit
   :super-only? true})

(def copy
  {:name "Copy..."
   :uri  "module/new"
   :icon icons/action-copy
   :disabled? false
   :super-only? true})

(def unpublish
  {:name "Un-publish"
   :uri  "module/new"
   :icon icons/action-unpublish
   :disabled? true})
