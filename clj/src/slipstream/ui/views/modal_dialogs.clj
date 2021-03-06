(ns slipstream.ui.views.modal-dialogs
  (:require [superstring.core :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.pattern :as pattern]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.parameters :as p]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.parameters :as parameters]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "modal_dialogs.html"))

(def ^:private title-sel [:.modal-title])
(def ^:private body-sel [:.modal-body])
(def ^:private body-text-sel [:.ss-dialog-body-text])
(def ^:private footnote-sel [:.ss-dialog-footnote])
(def ^:private first-button-sel [:.modal-footer [:button html/first-of-type]])
(def ^:private second-button-sel [:.modal-footer [:button (html/nth-of-type 2)]])
(def ^:private last-button-sel [:.modal-footer [:button html/last-of-type]])
(def ^:private info-sel [(html/attr= :name "dialog-info")])
(def ^:private verification-sel [(html/attr= :name "dialog-verification")])
(def ^:private message-sel [(html/attr= :name "dialog-message")])


(localization/with-prefixed-t :start-tour-dialog
  (html/defsnippet ^:private start-tour-dialog template-filename [:#ss-start-tour-dialog]
    []
    [:.ss-start-tour-dialog-title]      (html/content       (t :title))
    [:.ss-start-tour-dialog-text-body]  (html/html-content  (t :text-body))
    [:.ss-take-tour-btn]                (html/content       (t :button.take-tour))
    [:.ss-no-tour-btn]                  (html/content       (t :button.dont-take-tour-dont-ask-again))
    [:.ss-ask-next-time-btn]            (html/content       (t :button.dont-take-tour-but-ask-again))
    footnote-sel                        (html/html-content  (t :footnote))))

(localization/with-prefixed-t :save-dialog
  (html/defsnippet ^:private save-dialog template-filename [:#ss-save-dialog]
    [resource-name]
    title-sel                   (html/content       (t :title resource-name))
    [:.modal-body :textarea]    (ue/set-placeholder (t :placeholder.commit-message))
    footnote-sel                (html/html-content  (t :footnote))
    first-button-sel            (html/content       (t :button.cancel))
    last-button-sel             (html/content       (t :button.save resource-name))))

(localization/with-prefixed-t :delete-dialog
  (html/defsnippet ^:private delete-dialog template-filename [:#ss-delete-dialog]
    [resource-name resource-id]
    title-sel                 (html/content       (t :title resource-name))
    body-sel                  (html/html-content  (t :question resource-name resource-id))
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.delete resource-name))))


(localization/with-prefixed-t :soft-reset-dialog
                              (html/defsnippet ^:private soft-reset-dialog template-filename [:#ss-soft-reset-dialog]
                                               []
                                               title-sel (html/content (t :title))
                                               body-sel (html/html-content (t :question))
                                               first-button-sel (html/content (t :button.cancel))
                                               last-button-sel (html/content (t :button.reset))))

(localization/with-prefixed-t :confirmation-dialog
                              (html/defsnippet ^:private confirmation-dialog template-filename [:#ss-confirmation-dialog]
                                               []
                                               title-sel (html/content (t :title))
                                               body-sel (html/html-content (t :question))
                                               first-button-sel (html/content (t :button.cancel))
                                               last-button-sel (html/content (t :button.apply))))

(localization/with-prefixed-t :delete-all-dialog
  (html/defsnippet ^:private delete-all-dialog template-filename [:#ss-delete-all-dialog]
    [resource-name resource-id]

    title-sel                 (html/content       (t :title resource-name))
    info-sel                  (html/html-content  (t :info resource-name))
    message-sel               (html/html-content  (t :question resource-name resource-id))
    verification-sel          (ue/set-placeholder (t :placeholder resource-name))
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.delete resource-name))
    verification-sel          (ue/add-requirements {:required? true
                                                    :validation {:requirements [{:pattern-name "validation-pattern"
                                                                                 :pattern (str "^/?" resource-id "$")
                                                                                 :help-hint {}
                                                                                 :status {:when-true  "success", :when-false "error"}}]}})))

(localization/with-prefixed-t :image-chooser-dialog
  (html/defsnippet ^:private image-chooser-dialog template-filename [:#ss-image-chooser-dialog]
    [reference-image]
    title-sel                 (html/content (t :title))
    [:iframe]                 (ue/when-set-src (not-empty reference-image) (u/module-uri reference-image) (page-type/query-params :page-type/chooser))
    first-button-sel          (html/content (t :button.cancel))
    [:.ss-select-exact-version-btn] (html/content (t :button.select-exact-version))
    [:.ss-select-btn]         (html/content (t :button.select))))

(localization/with-prefixed-t :terminate-deployment-dialog

  (html/defsnippet ^:private terminate-deployment-dialog template-filename [:#ss-terminate-deployment-dialog]
    []
    title-sel                 (html/content (t :title))
    body-sel                  (html/content (t :question))
    first-button-sel          (html/content (t :button.cancel))
    last-button-sel           (html/content (t :button.terminate)))

  (html/defsnippet ^:private terminate-deployment-from-table-dialog template-filename [:#ss-terminate-deployment-from-table-dialog]
    []
    [title-sel :#ss-terminate-deployment-from-table-dialog-label]   (html/content (t :title))
    [body-sel :.message]                (html/content (t :question))
    first-button-sel                    (html/content (t :button.cancel))
    last-button-sel                     (html/content (t :button.terminate))
    [:.ss-deployment-run-id-label]      (html/content (t :label.deployment-run-id))
    [:.ss-deployment-run-type-label]    (html/content (t :label.deployment-run-type))
    [:.ss-deployment-module-label]      (html/content (t :label.deployment-module))
    [:.ss-deployment-state-label]       (html/content (t :label.deployment-state))
    [:.ss-deployment-start-time-label]  (html/content (t :label.deployment-start-time))
    [:.ss-deployment-clouds-label]      (html/content (t :label.deployment-clouds))
    [:.ss-deployment-service-url-label] (html/content (t :label.deployment-service-url))
    [:.ss-deployment-user-label]        (html/content (t :label.deployment-user))
    [:.ss-deployment-tags-label]        (html/content (t :label.deployment-tags)))

  ; end of localization/with-prefixed-t :terminate-deployment-dialog
  )

(localization/with-prefixed-t :publish-module-confirmation-dialog
  (html/defsnippet ^:private publish-module-confirmation-dialog template-filename [:#ss-publish-module-confirmation-dialog]
    [resource-name resource-id module-version]
    title-sel                 (html/content       (t :title resource-name))
    body-text-sel             (html/html-content  (t :question resource-name resource-id module-version))
    footnote-sel              (html/html-content  (t :footnote resource-name))
    [:code]                   (html/add-class     "text-primary")
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.publish resource-name))))

(localization/with-prefixed-t :unpublish-module-confirmation-dialog
  (html/defsnippet ^:private unpublish-module-confirmation-dialog template-filename [:#ss-unpublish-module-confirmation-dialog]
    [resource-name resource-id module-version]
    title-sel                 (html/content       (t :title resource-name))
    body-text-sel             (html/html-content  (t :question resource-name resource-id module-version))
    footnote-sel              (html/html-content  (t :footnote resource-name))
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.unpublish resource-name))))

(localization/with-prefixed-t :copy-module-dialog
  (html/defsnippet ^:private copy-module-dialog template-filename [:#ss-copy-module-dialog]
    [resource-name resource-id module-version]
    title-sel                 (html/content       (t :title resource-name))
    [:iframe]                 (ue/when-set-src (not-empty resource-id) (-> resource-id u/module-uri uc/trim-last-path-segment) (page-type/query-params :page-type/project-chooser))
    [:#ss-module-copy-source-uri] (ue/set-value (-> resource-id u/module-uri (uc/trim-prefix "/") (str "/" module-version)))
    [:#ss-module-copy-target-name-label] (html/content (t :new-module-name.label resource-name))
    [:#ss-module-copy-target-name] (ue/set-value (-> resource-id uc/last-path-segment (str (t :new-module-name.default-suffix module-version))))
    [:#ss-module-copy-target-name] (ue/set-placeholder (t :new-module-name.placeholder))
    [:.input-group] (ue/add-requirements {:required? true
                                          :validation {:requirements (pattern/requirements :module-name)
                                                       :generic-help-hints {:error (t :copy-image.choose-project.generic-error-help-hint resource-name)}}})
    footnote-sel              (html/html-content  (t :footnote resource-name))
    first-button-sel          (html/content       (t :button.cancel))
    last-button-sel           (html/content       (t :button.copy resource-name))))


(defn- run-module-global-parameters
  [[module-metadata module]]
  (let [available-clouds (current-user/configuration :available-clouds)]
    {:deployment-target-cloud       (some-> available-clouds
                                            (u/enum-append-option :specify-for-each-node))
     :image-target-cloud            available-clouds
     :build-image-target-cloud      available-clouds
     :launch-mutable-run?           false
     :tolerate-deployment-failures? false
     :tags                          nil
     :keep-running-behaviour        (-> [:always
                                         :on-success
                                         :on-error
                                         :never]
                                      (u/enum :keep-running-behaviour-for-deployment)
                                      (u/enum-select (or
                                                       (current-user/configuration :keep-running)
                                                       :on-success))
                                      u/enum-flag-selected-as-default)
     :ssh-key-available?            (boolean (current-user/configuration :ssh-keys))}))

(def ^:private dialog-id
  {:run   "ss-run-module-dialog"
   :build "ss-build-module-dialog"})

(localization/with-prefixed-t :run-image-dialog
  (html/defsnippet ^:private run-image-dialog template-filename [:#ss-run-image-dialog]
    [image-metadata resource-id module-version]
    ue/this                       (-> :run dialog-id ue/set-id)
    title-sel                     (html/content       (t :title))
    [:#ss-run-image-cloud-label]  (html/content       (t :cloud-service.label))
    [:.ss-run-image-global-section-title]              (html/content       (t :global-section.title))
    [:.ss-run-image-global-section-content]            (html/content       (-> [image-metadata :image] run-module-global-parameters t/run-image-global-section-table))
    [:.ss-run-image-input-parameters-section]          (let [generic-cloud-params (-> image-metadata
                                                                                      :cloud-configuration
                                                                                      (parameters/parameters-of-category "Cloud")
                                                                                      first)
                                                             input-parameters (-> image-metadata :input-parameters not-empty)]
                                                         (ue/at-match
                                                          [:.ss-run-image-input-parameters-section-title]    (html/html-content  (t :input-parameters-section.title))
                                                          [:.ss-run-image-input-parameters-section-content]  (html/content       (t/run-image-input-parameters-table
                                                                                                                                   input-parameters
                                                                                                                                   generic-cloud-params))))
    footnote-sel                  (html/html-content  (t :footnote resource-id module-version))
    [:#ss-run-image-id]           (ue/set-value       (-> resource-id u/module-uri (uc/trim-prefix "/") (str "/" module-version)))
    first-button-sel              (html/content       (t :button.cancel))
    last-button-sel               (html/content       (t :button))))

(localization/with-prefixed-t :build-image-dialog
  (html/defsnippet ^:private build-image-dialog template-filename [:#ss-build-image-dialog]
    [resource-id module-version]
    ue/this                         (-> :build dialog-id ue/set-id)
    title-sel                       (html/content       (t :title))
    [:.ss-build-image-input-parameters-section-content] (html/content   (-> [nil :image-build] run-module-global-parameters t/build-image-global-section-table))
    footnote-sel                    (html/html-content  (t :footnote resource-id module-version))
    [:#ss-build-image-id]           (ue/set-value       (-> resource-id u/module-uri (uc/trim-prefix "/") (str "/" module-version)))
    first-button-sel                (html/content       (t :button.cancel))
    last-button-sel                 (html/content       (t :button))))

(localization/with-prefixed-t :run-deployment-dialog
  (html/defsnippet ^:private run-deployment-dialog template-filename [:#ss-run-deployment-dialog]
    [deployment-metadata resource-id module-version]
    ue/this                                       (-> :run dialog-id ue/set-id)
    title-sel                                     (html/content       (t :title))
    [:#ss-run-deployment-id]                      (ue/set-value (-> resource-id u/module-uri (uc/trim-prefix "/") (str "/" module-version)))
    [:.ss-run-deployment-global-section-title]    (html/html-content  (t :global-section.title))
    [:.ss-run-deployment-global-section-content]  (html/content       (-> [deployment-metadata :deployment] run-module-global-parameters t/run-deployment-global-section-table))
    [:.ss-run-deployment-nodes-section-title]     (html/html-content  (t :nodes-section.title))
    [:.ss-run-deployment-nodes-section-content]   (html/content       (-> deployment-metadata :nodes t/run-deployment-node-parameters-table))
    footnote-sel                                  (html/html-content  (t :footnote))
    first-button-sel                              (html/content       (t :button.cancel))
    last-button-sel                               (html/content       (t :button.run))))

(localization/with-prefixed-t :resource-name
  (defn- resource-name
    [{:keys [view-name parsed-metadata]}]
    (case view-name
      "user"      (-> :user t s/lower-case)
      "module"    (-> parsed-metadata :summary :category u/t-module-category s/lower-case)
      "versions"  (-> parsed-metadata :category u/t-module-category s/lower-case)
      nil)))

(defn- module-version
  [{:keys [parsed-metadata]}]
  (-> parsed-metadata :summary :version))

(defn- resource-id
  [{:keys [view-name parsed-metadata]}]
  (case view-name
    "user"      (-> parsed-metadata :username)
    "module"    (-> parsed-metadata :summary :name)
    "versions"  (-> parsed-metadata :module-name)
    ""))

(defn- module-category?
  [{:keys [view-name parsed-metadata]} & categories]
  (and
    (= "module" view-name)
    (-> parsed-metadata :summary :category uc/keywordize hash-set (some categories))))

(defn- chooser-required?
  [context]
  (and
    (page-type/edit-or-new?)
    (module-category? context :image :deployment)))

(defn- terminate-required?
  [{:keys [view-name]}]
  (#{"run"} view-name))

(defn- terminate-from-table-required?
  [{:keys [view-name]}]
  (#{"dashboard" "module" "runs"} view-name))

(defn- publish-required?
  [context]
  (and
    (page-type/view?)
    (module-category? context :image :deployment)))

(def ^:private copy-required?
  publish-required?)

(defn- run-image-required?
  [context]
  (and
    (page-type/view?)
    (module-category? context :image)))

(defn- run-deployment-required?
  [context]
  (and
    (page-type/view?)
    (module-category? context :deployment)))

(defn- start-tour-required?
  [{:keys [view-name tour]}]
  (#{"appstore" "dashboard" "modules"} view-name))

(defn- nuvlabox-modal-dialogs-required?
       [{:keys [view-name]}]
       (= "nuvlabox-admin" view-name))

(defn required
  [context]

  (when (page-type/not-chooser?)
    (let [resource-name (resource-name context)
          resource-id (resource-id context)
          module-version (module-version context)]
      (cond-> []
        (start-tour-required? context)      (conj (start-tour-dialog))
        (page-type/edit?)                   (conj (save-dialog resource-name)
                                                  (delete-dialog resource-name resource-id)
                                                  (delete-all-dialog resource-name resource-id))
        (page-type/view?)                   (conj (delete-dialog resource-name resource-id)
                                                  (delete-all-dialog resource-name resource-id))
        (chooser-required? context)         (conj (image-chooser-dialog
                                                    (or
                                                      (-> context :parsed-metadata :cloud-image-details :reference-image)
                                                      (-> context :parsed-metadata :summary :parent-uri))))
        (terminate-required? context)       (conj (terminate-deployment-dialog))
        (terminate-from-table-required? context) (conj (terminate-deployment-from-table-dialog))
        (publish-required? context)         (conj (publish-module-confirmation-dialog   resource-name resource-id module-version)
                                                  (unpublish-module-confirmation-dialog resource-name resource-id module-version))
        (copy-required? context)            (conj (copy-module-dialog resource-name resource-id module-version))
        (run-image-required? context)       (conj (run-image-dialog   (:parsed-metadata context) resource-id module-version)
                                                  (build-image-dialog resource-id module-version))
        (run-deployment-required? context)   (conj (run-deployment-dialog (:parsed-metadata context) resource-id module-version ))
        (nuvlabox-modal-dialogs-required? context) (conj (soft-reset-dialog) (confirmation-dialog))))))
