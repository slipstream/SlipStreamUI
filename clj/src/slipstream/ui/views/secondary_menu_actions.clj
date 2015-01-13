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
  (str (t t-path) (t :suffix.ellipsis)))

(defmacro ^:private defaction
  [name & {:keys [option-defaults name-with-ellipsis?]}]
  `(defn ~name
     [& {:as options#}]
     (merge
        {:name        (~(if name-with-ellipsis? t-with-ellipsis t) (keyword '~name))
         :id          (str "ss-secondary-menu-action-" '~name)
         :icon        ~(symbol "slipstream.ui.util.icons" (str "action-" name))
         :disabled?   false
         :disabled-reason nil
         :hidden?     false
         :super-only? false}
        ~option-defaults
        (select-keys options# [:disabled? :hidden? :super-only?]))))

(defaction new-project)
(defaction new-image)
(defaction new-deployment)
(defaction import :option-defaults {:disabled? true}) ;; TODO: Import action still unimplemented
(defaction new-user)

(defaction run        :name-with-ellipsis? true)
(defaction build      :name-with-ellipsis? true)

(defaction copy       :name-with-ellipsis? true)
(defaction publish    :option-defaults {:super-only? true})
(defaction unpublish  :option-defaults {:super-only? true})
(defaction terminate)

(defaction edit)
(defaction save)
(defaction create) ;; like 'save' action for 'new' pages
(defaction cancel)
(defaction delete)

(defaction export-users)
