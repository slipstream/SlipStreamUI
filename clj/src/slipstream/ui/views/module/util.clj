(ns slipstream.ui.views.module.util
  (:require [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.code-area :as code-area]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defn- code-area
  [code id]
  (code-area/build code
                   :id id
                   :editable? (page-type/edit-or-new?)))

(localization/with-prefixed-t :section.targets.subsection

  (defmulti target-subsection :target-type)

  (defmethod target-subsection :script
    [{:keys [target-name target-machine-type script context]}]
    {:title   (->  target-name (str "." (name target-machine-type) ".title") t)
     :content [(-> target-name (str "." (name target-machine-type) ".description") t (ue/text-div-snip :css-class "ss-target-description"
                                                                        :html true))
               (code-area script target-name)
               (when (context :ss-client-access)
                 (ue/text-div-snip (t :ss-commands-documentation)
                                   :css-class "ss-target-description-bottom"
                                   :html true))]})

  (defmethod target-subsection :packages
    [{:keys [target-name target-machine-type packages]}]
    {:title   (-> target-name (str "." (name target-machine-type) ".title") t)
     :content (if (or (page-type/edit-or-new?) (not-empty packages))
                (t/image-creation-packages-table packages)
                (-> target-name (str "." (name target-machine-type) ".empty-content-hint") t))})

) ; End of prefixed-t scope
