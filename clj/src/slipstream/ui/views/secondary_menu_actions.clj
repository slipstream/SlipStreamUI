(ns slipstream.ui.views.secondary-menu-actions
  "Predefined actions for the secondary menu.")

(def new-project
  {:name "New project"
   :uri  "module/new"
   :icon :folder-open})

(def run
  {:name "Run..."
   :uri  "module/new"
   :icon :send})

(def edit
  {:name "Edit"
   :uri  "module/new"
   :icon :pencil
   :super-only? true})

(def copy
  {:name "Copy..."
   :uri  "module/new"
   :icon :repeat
   :disabled? false
   :super-only? true})

(def unpublish
  {:name "Un-publish"
   :uri  "module/new"
   :icon :globe
   :disabled? true})