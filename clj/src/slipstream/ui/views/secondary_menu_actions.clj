(ns slipstream.ui.views.secondary-menu-actions
  "Predefined actions for the secondary menu."
  (:require [slipstream.ui.views.util.icons :as icons]))

(def new-project
  {:name "New project"
   :uri  "module/new"
   :icon icons/action-new-project})

(def new-user
  {:name "New user"
   :uri  "user/new"
   :icon icons/action-new-user})

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