(ns slipstream.ui.views.secondary-menu-actions
  "Predefined actions for the secondary menu."
  (:refer-clojure :exclude [import])
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]))

(localization/def-scoped-t)

(defn- action-id
  [id]
  (str "ss-secondary-menu-action-" id))

(defn- t-with-ellipsis
  [t-path]
  (str (t t-path) (t :ellipsis)))

(defmacro ^:private defaction
  [name & {:keys [disabled? super-only? name-with-ellipsis?] :as options}]
  `(defn ~name
     []
     {:name (~(if name-with-ellipsis? t-with-ellipsis t) (keyword '~name))
      :id (str "ss-secondary-menu-action-" '~name)
      :icon ~(symbol "slipstream.ui.util.icons" (str "action-" name))
      :disabled? ~disabled?
      :super-only? ~super-only?}))

(defaction new-project)
(defaction new-image)
(defaction new-deployment)
(defaction import)
(defaction new-user)

(defaction run    :name-with-ellipsis? true)
(defaction build  :name-with-ellipsis? true)

(defaction copy   :name-with-ellipsis? true)
(defaction publish :disabled? true)
(defaction unpublish)
(defaction terminate)

(defaction edit)
(defaction save)
(defaction cancel)
(defaction delete)
