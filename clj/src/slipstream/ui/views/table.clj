(ns slipstream.ui.views.table
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.time :as ut]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.models.pagination :as pagination]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "table.html"))

(declare build)
(declare cell-snip)

(def table-cls "ss-table")

(def table-sel [(html/has-class table-cls)])
(def table-head-sel [:.ss-table-head :> :tr])
(def table-header-sel (concat table-head-sel [:> [:th html/first-of-type]]))
(def table-body-sel [:.ss-table-body])
(def table-row-sel (concat table-body-sel [:> [:tr html/first-of-type]]))

(defn- sel-for-cell
  [cls & variation]
  (concat table-body-sel [[:td (ue/first-of-class (str "ss-table-cell-" (name cls) (when (= (first variation) :editable) "-editable")))]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Hidden inputs for parameter cells in editable mode

(ue/def-blank-snippet ^:private hidden-inputs-for-parameter-snip [:span :input]
  [parameter row-index]
  [:input]  (ue/set-type "hidden")
  ue/this   (ue/content-for [:input] [[k v] (select-keys parameter [:name :category :type :description])]
                    ue/this (ue/set-value v)
                    ue/this (->> k
                                 name
                                 (str "parameter-" (:name parameter) "--" row-index "--")
                                 ue/set-name)))

(defn- append-hidden-inputs-when-parameter-in
  [cell-content]
  (html/append
    (when-let [parameter (:parameter cell-content)]
      (hidden-inputs-for-parameter-snip parameter (:row-index cell-content)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- cell-text-with-tooltip-snip
  [{:keys [text tooltip]}]
  (ue/text-with-tooltip-snip
    :text       text
    :tooltip    tooltip
    :class      "ss-table-tooltip"
    :placement  "bottom"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ue/def-blank-snippet ^:private input-group-addon-snip :span
  [content]
  ue/this (html/add-class "input-group-addon")
  ue/this (html/content   content))

(defn- decorate-textual-input
  [{:keys [before after]}]
  (if (or before after)
    (fn [match]
      (html/at match
        ue/this (html/wrap :div {:class "input-group"})
        ue/this (ue/when-prepend  (not-empty before)  (input-group-addon-snip before))
        ue/this (ue/when-append   (not-empty after)   (input-group-addon-snip after))))
    identity))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Enlive cell snips

; Text cell

(html/defsnippet ^:private cell-text-snip-view template-filename (sel-for-cell :text)
  [{:keys [text tooltip id class colspan html-content?] :as cell-content}]
  ue/this  (if (not-empty tooltip)
             (html/content (cell-text-with-tooltip-snip cell-content))
             ((if html-content? html/html-content html/content) (str text)))
  ue/this (ue/when-set-style (-> text str count (> 100))
                             "word-wrap: break-word; max-width: 500px;")
  ue/this (ue/set-id id)
  ue/this (ue/when-add-class class)
  ue/this (ue/set-colspan colspan))

(html/defsnippet ^:private cell-text-snip-edit template-filename (sel-for-cell :text :editable)
  [{:keys [text tooltip id read-only? disabled? placeholder class data required?] :as cell-content}]
  [:input]  (ue/set-id id)
  [:input]  (ue/set-name id)
  [:input]  (ue/set-value (str text))
  [:input]  (ue/set-placeholder (or
                                  placeholder
                                  (when required? (t :input-text.placeholder.required))))
  [:input]  (ue/toggle-readonly (and (current-user/not-super?) read-only?))
  [:input]  (ue/toggle-disabled disabled?)
  [:input]  (ue/add-requirements cell-content)
  [:input]  (decorate-textual-input cell-content)
  ue/this   (ue/when-set-title (not-empty tooltip) (str tooltip))
  ue/this   (ue/when-add-class class)
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))


; Positive integer cell

(html/defsnippet ^:private cell-positive-integer-snip-edit template-filename (sel-for-cell :positive-integer :editable)
  [{:keys [value tooltip id read-only? disabled? placeholder max-value min-value required?] :as cell-content}]
  [:input]  (ue/set-id id)
  [:input]  (ue/set-name id)
  [:input]  (ue/set-value value)
  [:input]  (ue/when-set-min min-value)
  [:input]  (ue/add-requirements cell-content)
  [:input]  (ue/when-set-max max-value)
  [:input]  (ue/set-placeholder placeholder)
  [:input]  (ue/toggle-readonly (and (current-user/not-super?) read-only?))
  [:input]  (ue/toggle-disabled disabled?)
  ue/this   (ue/when-set-title (not-empty tooltip) (str tooltip)))


; Editable textarea cell

(html/defsnippet ^:private cell-textarea-snip-edit template-filename (sel-for-cell :text-area :editable)
  [{:keys [text tooltip id placeholder] :as cell-content}]
  [:textarea]  (ue/set-id id)
  [:textarea]  (ue/set-name id)
  [:textarea]  (ue/set-placeholder placeholder)
  [:textarea]  (ue/add-requirements cell-content)
  [:textarea]  (html/content (str text))
  ue/this   (ue/when-set-title (not-empty tooltip) (str tooltip))
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Editable password cell

(html/defsnippet ^:private cell-password-snip-edit template-filename (sel-for-cell :password :editable)
  [{:keys [id password required?] :as cell-content}]
  [:input]  (ue/set-id id)
  [:input]  (ue/set-name id)
  [:input]  (ue/set-value password)
  [:input]  (ue/add-requirements cell-content)
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Timestamp cell

(defn- cell-timestamp-snip-view
  [{:keys [timestamp] :as content} text-format tooltip-format]
  (cell-text-snip-view (-> content
                           (dissoc :timestamp)
                           (assoc :text (or
                                          (ut/format text-format timestamp)
                                          (t :timestamp.unknown))
                                  :tooltip (ut/format tooltip-format timestamp)))))

; Boolean cell

(html/defsnippet ^:private cell-boolean-snip-view template-filename (sel-for-cell :boolean)
  [{:keys [value id] :as cell-content}]
  ; [:input] (ue/toggle-disabled true)
  [:input] (ue/set-id id)
  [:input] (ue/set-name id)
  [:input] (ue/toggle-checked value))

(html/defsnippet ^:private cell-boolean-snip-edit template-filename (sel-for-cell :boolean :editable)
  [{:keys [value id] :as cell-content}]
  ; [:input] (ue/toggle-disabled false)
  [:input] (ue/toggle-checked value)
  [:input] (ue/set-id id)
  [:input] (ue/set-name id)
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Enum cell

(ue/def-blank-snippet ^:private hidden-input-for-disabled-select-snip [:input]
  [selected-option id]
  [:input]  (ue/set-type "hidden")
  [:input]  (ue/set-value (:value selected-option))
  [:input]  (ue/set-id id)
  [:input]  (ue/set-name id))

(html/defsnippet ^:private cell-enum-snip-edit template-filename (sel-for-cell :enum :editable)
  [{:keys [enum id read-only? disabled? class] :as cell-content}]
  [:select] (ue/set-id id)
  [:select] (ue/set-name id)
  [:select] (ue/when-add-class class)
  ; NOTE: <select> tags do not have a readonly attribute
  ;       so we disable it and add a hidden input field instead,
  ;       since disabled fields are NOT submitted with the form.
  [:select] (ue/toggle-disabled (or disabled? read-only?))
  ue/this   (ue/when-append read-only? (hidden-input-for-disabled-select-snip (u/enum-selection enum) id))
  [:select] (ue/content-for [[:option html/first-of-type]] [{:keys [value text selected?]} enum]
                            ue/this (ue/set-value value)
                            ue/this (html/content (str text))
                            ue/this (ue/toggle-selected selected?))
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Map cell

(defn- map-key-str
  [map-key]
  (if (keyword? map-key)
    (->> map-key name (str "map-cell.key.") keyword t)
    (str map-key)))

(defn- cell-map-entry-sel
  [& [variation]]
  (concat (sel-for-cell :map variation) [#{[:dt html/first-of-type] [:dd html/first-of-type]}]))

(ue/def-blank-snippet map-empty-value-snip :span
  []
  ue/this (html/add-class     "ss-map-empty-value")
  ue/this (html/html-content  (-> :map-cell.empty-value t s/lower-case)))

(html/defsnippet ^:private term-dict-entry-snip-view template-filename (cell-map-entry-sel)
  [ids [k v]]
  [:dt] (html/content (map-key-str k))
  [:dd] (ue/set-id          (or (get ids k) (name k)))
  [:dd] (ue/when-add-class  (:class ids))
  [:dd] (if-let [v-str (-> v str s/trim not-empty)]
            (html/content v-str)
            (html/content (map-empty-value-snip))))

(html/defsnippet ^:private cell-map-snip-view template-filename (sel-for-cell :map)
  [m]
  [:dl] (html/content (->> m (into (sorted-map)) (mapcat (partial term-dict-entry-snip-view (-> m meta :ids))))))

; Editable map cell

(html/defsnippet ^:private term-dict-entry-snip-edit template-filename (cell-map-entry-sel :editable)
  [ids [k v]]
  [:dt]         (html/content       (map-key-str k))
  [:dd :input]  (ue/set-placeholder nil)
  [:dd :input]  (ue/set-name        (or (get ids k) (name k)))
  [:dd :input]  (ue/set-id          (or (get ids k) (name k)))
  [:dd :input]  (ue/when-add-class  (:class ids))
  [:dd :input]  (ue/set-value       v))

(html/defsnippet ^:private cell-map-snip-edit template-filename (sel-for-cell :map)
  [m]
  [:dl] (html/content (->> m
                           (into (sorted-map))
                           (mapcat (partial term-dict-entry-snip-edit (-> m meta :ids))))))

; Link cell

(html/defsnippet ^:private cell-link-snip-view template-filename (sel-for-cell :link)
  [{:keys [text href open-in-new-window? id colspan class] :as cell-content}]
    [:a] (html/content (str text))
    [:a] (ue/set-href href)
    [:a] (ue/set-id id)
    [:a] (ue/when-add-class class)
    ue/this (ue/set-colspan colspan)
    [:a] (ue/when-set-target open-in-new-window? "_blank"))

; Icon cell

(html/defsnippet ^:private cell-icon-snip-view template-filename (sel-for-cell :icon)
  [icon]
  [:span] (icons/set (case (type icon)
                       :icon/computed icon
                       :icon/symbol   (icon :tooltip-placement "left"))))

; Module version cell

(html/defsnippet ^:private cell-module-version-snip-view template-filename (sel-for-cell :module-version)
  [uri]
  [:span] (html/content (uc/last-path-segment uri))
  [:a]    (ue/set-href  (uc/trim-last-path-segment uri) "/" ))

; Help hint cell

(html/defsnippet ^:private cell-help-hint-snip-view template-filename (sel-for-cell :help-hint)
  [{:keys [title content]}]
  [:span]  (ue/set-title title)
  [:span]  (ue/set-data :content content)
  [:span]  (ue/remove-if-not (not-empty content)))

; Reference module cell

(html/defsnippet ^:private cell-reference-module-snip-edit template-filename (sel-for-cell :reference-module :editable)
  [{:keys [value colspan] :as reference-module}]
  ue/this                           (ue/set-colspan colspan)
  [:#module-reference]              (ue/set-value value)
  [:.ss-reference-module-chooser-button :button] (html/content (t :reference-module-cell.chooser-button.label))
  [:.ss-reference-module-name :a]   (ue/set-href (u/module-uri value))
  [:.ss-reference-module-name :a]   (html/content (u/module-name value)))

; Hidden form input cell

(ue/def-blank-snippet ^:private cell-hidden-input-snip-edit [:input]
  [{:keys [value id]}]
  [:input]  (ue/set-type "hidden")
  [:input]  (ue/set-value value)
  [:input]  (ue/set-id id)
  [:input]  (ue/set-name id))

; Toggle button cell

(html/defsnippet ^:private cell-toggle-button-snip-edit template-filename (sel-for-cell :toggle-button :editable)
  [{:keys [action-type id text-pressed text class size]}]
  [:button]   (html/content   (str text))
  [:button]   (html/set-attr   :data-active-text (str (or text-pressed text)))
  [:button]   (html/add-class (str "btn-" (name (or action-type :primary))))
  [:button]   (ue/when-add-class size (str "btn-" (name size)))
  [:td]       (ue/when-add-class size "ss-vertical-align")
  [:button]   (ue/when-add-class class)
  [:button]   (ue/set-id      id))

; Action button cell

(html/defsnippet ^:private cell-action-button-snip-edit template-filename (sel-for-cell :action-button :editable)
  [{:keys [action-type id text class size]}]
  [:button]   (html/content   (str text))
  [:button]   (html/add-class (str "btn-" (name (or action-type :primary))))
  [:button]   (ue/when-add-class size (str "btn-" (name size)))
  [:td]       (ue/when-add-class size "ss-vertical-align")
  [:button]   (ue/when-add-class class)
  [:button]   (ue/set-id      id))

; Inner table cell

(html/defsnippet ^:private cell-inner-table-snip-view template-filename (sel-for-cell :inner-table)
  [table]
  [:tbody]    (html/substitute   (html/select (build table) [:tbody])))

; Multi content cell

(defn- cell-multi-snip
  "This allows to group several cells together and toggle between them."
  [cell-group-id visible-cell-index cell-index cell]
  (html/at (cell-snip cell)
    ue/this (html/add-class "ss-table-cell-multi")
    ue/this (html/set-attr  :data-cell-multi-group cell-group-id)
    ue/this (html/set-attr  :data-cell-multi-index cell-index)
    ue/this (ue/when-add-class (-> visible-cell-index (or 0) (not= cell-index)) "hidden")))

; Blank cell

(ue/def-blank-snippet ^:private cell-toggle-blank-snip [:td]
  []
  identity)

; Pagination cell

(html/defsnippet ^:private pagination-separation-snip template-filename [(ue/first-of-class "ss-pagination-separator-btn")]
  []
  identity)

(localization/with-prefixed-t :pagination.action
  (html/defsnippet ^:private cell-pagination-snip-view template-filename (sel-for-cell :pagination)
    [{:keys [colspan msg]
      {:keys [first-page previous-page next-page last-page show-all pages]} :params}]
    ue/this                     (ue/set-colspan colspan)
    [:.ss-pagination-msg]       (html/content msg)
    ; Set up "Previous page" button(s)
    [:.ss-pagination-previous]  (ue/when-set-data   :pagination-params previous-page)
    [:.ss-pagination-previous]  (ue/set-title       (t :tooltip.previous-page))
    [:.ss-pagination-previous]  (ue/when-add-class  (not previous-page) "disabled")
    ; Set up "Go to page number" button(s)
    [:.ss-pagination-buttons]   (ue/content-for [[:button html/first-of-type]] [page pages]
                                                ue/this (html/content       (-> page :page-number str))
                                                ue/this (ue/set-data        :pagination-params page)
                                                ue/this (ue/when-add-class  (:hidden page) "hidden")
                                                ue/this (ue/when-add-class  (:current-page page) "disabled ss-pagination-current-page")
                                                ue/this (ue/when-after      (:last-hidden page) (pagination-separation-snip)))
    ; Set up "Next page" button(s)
    [:.ss-pagination-next]      (ue/when-set-data   :pagination-params next-page)
    [:.ss-pagination-next]      (ue/set-title       (t :tooltip.next-page))
    [:.ss-pagination-next]      (ue/when-add-class  (not next-page) "disabled")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Cell snip dispatching on type, mode and content

(derive :content/map   :content/any)
(derive :content/plain :content/any)

(derive :mode/view  :mode/any)
(derive :mode/edit  :mode/any)

(defmulti ^:private cell-snip
  "See tests for an explicit list of cell types and an overview of their variants."
  (fn [{:keys [type content editable?] :as cell}]
    [(or type :cell/blank)
     (if editable? :mode/edit :mode/view)
     (if (map? content) :content/map :content/plain)]))

(defmethod cell-snip :default
  [cell]
  (throw (IllegalArgumentException.
    (str "No cell-snip defined for cell: " cell))))

(defmethod cell-snip [:cell/text :mode/view :content/map]
  [{content :content}]
  (cell-text-snip-view content))

(defmethod cell-snip [:cell/text :mode/view :content/plain]
  [{text :content}]
  (cell-text-snip-view {:text text}))

(defmethod cell-snip [:cell/text :mode/edit :content/map]
  [{content :content}]
  (cell-text-snip-edit content))

(defmethod cell-snip [:cell/text :mode/edit :content/plain]
  [{text :content}]
  (cell-text-snip-edit {:text text}))

(defmethod cell-snip [:cell/html :mode/view :content/map]
  [{content :content}]
  (cell-text-snip-view (assoc content :html-content? true)))

(defmethod cell-snip [:cell/html :mode/view :content/plain]
  [{text :content}]
  (cell-text-snip-view {:text text, :html-content? true}))

;; NOTE: For security reasons, there should not be an editable :cell/html.
;;       If called, an exception will be thrown, since it's not defined here.

(defmethod cell-snip [:cell/positive-number :mode/view :content/map]
  [{{:keys [value] :as content} :content}]
  (cell-text-snip-view (assoc content :text value)))

(defmethod cell-snip [:cell/positive-number :mode/edit :content/map]
  [{content :content}]
  (cell-positive-integer-snip-edit content))

(defmethod cell-snip [:cell/textarea :mode/view :content/map]
  [{content :content}]
  (cell-text-snip-view content))

(defmethod cell-snip [:cell/textarea :mode/view :content/plain]
  [{text :content}]
  (cell-text-snip-view {:text text}))

(defmethod cell-snip [:cell/textarea :mode/edit :content/map]
  [{content :content}]
  (cell-textarea-snip-edit content))

(defmethod cell-snip [:cell/textarea :mode/edit :content/plain]
  [{text :content}]
  (cell-textarea-snip-edit {:text text}))

(defmethod cell-snip [:cell/password :mode/view :content/any]
  [{pwd :content}]
  (cell-text-snip-view {:text (when pwd "•••••")}))

(defmethod cell-snip [:cell/password :mode/edit :content/plain]
  [{password :content}]
  (cell-password-snip-edit {:password password}))

(defmethod cell-snip [:cell/password :mode/edit :content/map]
  [{password :content}]
  (cell-password-snip-edit password))


(defn- options-str
  [enum]
  (->> enum
    (map :text)
    uc/join-as-str))

(defn- selected-options-str
  [enum]
  (->> enum
       (filter :selected?)
       options-str))

(defmethod cell-snip [:cell/enum :mode/view :content/map]
  [{{:keys [enum id]} :content}]
  (cell-text-snip-view {:text (selected-options-str enum)
                        :id id
                        :tooltip (t :enum-cell.view-mode.tooltip (options-str enum))}))

(defmethod cell-snip [:cell/enum :mode/view :content/plain]
  [{enum :content :as cell}]
  (cell-snip (assoc cell :content {:enum enum})))

(defmethod cell-snip [:cell/enum :mode/edit :content/map]
  [{content :content}]
  (cell-enum-snip-edit content))

(defmethod cell-snip [:cell/enum :mode/edit :content/plain]
  [{enum :content}]
  (cell-enum-snip-edit {:enum enum}))

(defmethod cell-snip [:cell/set :mode/view :content/plain]
  [{set :content}]
  (cell-text-snip-view {:text (uc/join-as-str set)}))

(defmethod cell-snip [:cell/set :mode/view :content/map]
  [{content :content}]
  (cell-text-snip-view (-> content
                           (assoc :text (-> content :set uc/join-as-str))
                           (dissoc :set))))

(defmethod cell-snip [:cell/set :mode/edit :content/plain]
  [{set :content}]
  (cell-text-snip-edit {:text (uc/join-as-str set)}))

(defmethod cell-snip [:cell/set :mode/edit :content/map]
  [{content :content}]
  (cell-text-snip-edit (-> content
                           (assoc :text (-> content :set uc/join-as-str))
                           (dissoc :set))))

(defmethod cell-snip [:cell/timestamp-long :mode/any :content/map]
  [{content :content}]
  (cell-timestamp-snip-view content :human-readable-long :relative))

(defmethod cell-snip [:cell/timestamp :mode/any :content/map]
  [{content :content}]
  (cell-timestamp-snip-view content :human-readable :relative))

(defmethod cell-snip [:cell/relative-timestamp :mode/any :content/map]
  [{content :content}]
  (cell-timestamp-snip-view content :relative :human-readable-long))

(defmethod cell-snip [:cell/timestamp-long :mode/any :content/plain]
  [{timestamp :content}]
  (cell-timestamp-snip-view {:timestamp timestamp} :human-readable-long :relative))

(defmethod cell-snip [:cell/timestamp :mode/any :content/plain]
  [{timestamp :content}]
  (cell-timestamp-snip-view {:timestamp timestamp} :human-readable :relative))

(defmethod cell-snip [:cell/relative-timestamp :mode/any :content/plain]
  [{timestamp :content}]
  (cell-timestamp-snip-view {:timestamp timestamp} :relative :human-readable-long))

(defmethod cell-snip [:cell/boolean :mode/view :content/plain]
  [{value :content}]
  (cell-boolean-snip-view {:value value}))

(defmethod cell-snip [:cell/boolean :mode/view :content/map]
  [{value :content}]
  (cell-boolean-snip-view value))

(defmethod cell-snip [:cell/boolean :mode/edit :content/plain]
  [{value :content}]
  (cell-boolean-snip-edit {:value value}))

(defmethod cell-snip [:cell/boolean :mode/edit :content/map]
  [{value :content}]
  (cell-boolean-snip-edit value))

(defmethod cell-snip [:cell/map :mode/view :content/any]
  [{m :content}]
  (cell-map-snip-view m))

(defmethod cell-snip [:cell/map :mode/edit :content/any]
  [{m :content}]
  (cell-map-snip-edit m))

(defmethod cell-snip [:cell/link :mode/view :content/any]
  [{content :content}]
  (cell-link-snip-view content))

(defmethod cell-snip [:cell/external-link :mode/view :content/map]
  [{content :content}]
  (cell-link-snip-view (assoc content :open-in-new-window? true)))

(defmethod cell-snip [:cell/email :mode/view :content/plain]
  [{email :content}]
  (cell-link-snip-view {:text email :href (str "mailto:" email)}))

(defmethod cell-snip [:cell/email :mode/view :content/map]
  [{{:keys [email] :as content} :content}]
  (cell-link-snip-view (assoc content :text email :href (str "mailto:" email))))

(defmethod cell-snip [:cell/email :mode/edit :content/plain]
  [{email :content}]
  (cell-text-snip-edit {:text email}))

(defmethod cell-snip [:cell/email :mode/edit :content/map]
  [{{:keys [email] :as content} :content}]
  (cell-text-snip-edit (assoc content :text email)))

(defmethod cell-snip [:cell/external-url :mode/view :content/map]
  [{{:keys [url id]} :content}]
  (cell-link-snip-view {:text url
                        :href url
                        :id id
                        :open-in-new-window? true}))

(defmethod cell-snip [:cell/url :mode/view :content/map]
  [{{:keys [url id]} :content}]
  (cell-link-snip-view {:text url :href url, :id id}))

(defmethod cell-snip [:cell/url :mode/view :content/plain]
  [{url :content}]
  (cell-link-snip-view {:text url :href url}))

(defmethod cell-snip [:cell/url :mode/edit :content/map]
  [{{:keys [url id]} :content}]
  (cell-text-snip-edit {:text url, :id id}))

(defmethod cell-snip [:cell/url :mode/edit :content/plain]
  [{url :content}]
  (cell-text-snip-edit {:text url}))

(defmethod cell-snip [:cell/username :mode/any :content/plain]
  [{username :content :as cell}]
  (cell-snip (assoc cell :content {:username username})))

(defmethod cell-snip [:cell/username :mode/any :content/map]
  [{content :content}]
  (let [username (:username content)
        content-base (assoc content :text username)
        display-as-link? (and
                           (page-type/not-chooser?)         ; We don't want to be able to link outside of the chooser scope.
                           (or
                             (current-user/super?)          ; Only super-users have access to all user profiles.
                             (current-user/is? username)))] ; But for regular users it still makes sense to add the link to their own profile though.
    (if display-as-link?
      (cell-link-snip-view (assoc content-base :href (u/user-uri username)))
      (cell-text-snip-view content-base))))

(defmethod cell-snip [:cell/icon :mode/any :content/any]
  [{icon :content}]
  (cell-icon-snip-view icon))

(defmethod cell-snip [:cell/module-version :mode/any :content/plain]
  [{uri :content}]
  (cell-module-version-snip-view uri))

(defmethod cell-snip [:cell/help-hint :mode/view :content/map]
  [{help-content :content}]
  (cell-help-hint-snip-view help-content))

(defmethod cell-snip [:cell/reference-module :mode/view :content/plain]
  [{reference-module :content}]
  (cell-link-snip-view {:text (u/module-name reference-module)
                        :href (u/module-uri reference-module)
                        :class "ss-reference-module-name"}))

(defmethod cell-snip [:cell/reference-module :mode/edit :content/plain]
  [{reference-module :content}]
  (cell-reference-module-snip-edit {:value reference-module}))

(defmethod cell-snip [:cell/reference-module :mode/view :content/map]
  [{{:keys [value] :as reference-module} :content}]
  (cell-link-snip-view (assoc reference-module :text (u/module-name value) :href (u/module-uri value))))

(defmethod cell-snip [:cell/reference-module :mode/edit :content/map]
  [{reference-module :content}]
  (cell-reference-module-snip-edit reference-module))

(defmethod cell-snip [:cell/hidden-input :mode/any :content/map]
  [{content :content}]
  ; This is not a real cell, but a hidden input fiel in the row (<tr> tag) as requested by some form.
  (cell-hidden-input-snip-edit content))

(defmethod cell-snip [:cell/toggle-button :mode/any :content/map]
  [{content :content}]
  (cell-toggle-button-snip-edit content))

(defmethod cell-snip [:cell/remove-row-button :mode/any :content/map]
  [{{:keys [item-name size]} :content}]
  (cell-toggle-button-snip-edit {:class         "ss-remove-row-btn"
                                 :action-type   :danger
                                 :text          (if (not-empty item-name)
                                                  (t :remove-row-button.text-custom item-name)
                                                  (t :remove-row-button.text))
                                 :size          size
                                 :text-pressed  (t :remove-row-button.text-pressed)}))

(defmethod cell-snip [:cell/action-button :mode/any :content/map]
  [{content :content}]
  (cell-action-button-snip-edit content))

(defmethod cell-snip [:cell/inner-table :mode/any :content/any]
  [{table :content}]
  (cell-inner-table-snip-view table))

(defmethod cell-snip [:cell/multi :mode/any :content/any]
  [{cells :content visible-cell-index :visible-cell-index}]
  (let [cell-group-id (gensym "ss-table-cell-multi-group-")]
    (-> cell-multi-snip
        (partial cell-group-id visible-cell-index)
        (map-indexed cells))))

(defmethod cell-snip [:cell/pagination :mode/any :content/any]
  [{content :content}]
  (cell-pagination-snip-view content))

(defmethod cell-snip [:cell/blank :mode/any :content/any]
  [_]
  (cell-toggle-blank-snip))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Row

(html/defsnippet ^:private rows-snip template-filename table-row-sel
  [rows]
  ue/this (html/clone-for [{:keys [style cells class data hidden?]} rows]
           ue/this (ue/set-data :from-server data)
           ue/this (ue/when-add-class style (name style))
           ue/this (ue/when-add-class class)
           ue/this (ue/enable-class hidden? "hidden")
           ue/this (html/content (map cell-snip cells))))

(defn- rows-with-pagination
  [{:keys [pagination headers rows] :as table}]
  (let [num-of-cols (count headers)
        {:keys [msg params]} (pagination/info pagination)
        pagination-info-row {:cells [{:type :cell/pagination
                                      :content {:colspan  num-of-cols
                                                :msg      msg
                                                :params   params}}]}]
    (concat rows [pagination-info-row])))

(defn- paginated?
  [{:keys [pagination]}]
  (-> pagination
      not-empty
      boolean))

(defn- rows
  [{:keys [rows] :as table}]
  (if (paginated? table)
    (rows-with-pagination table)
    rows))


;; Headers

(defn- header-str
  [header]
  (if (keyword? header)
    (->> header name (str "header.") keyword t)
    (str header)))

(html/defsnippet ^:private head-snip template-filename table-header-sel
  [headers]
  ue/this (html/clone-for [header headers]
            ue/this (html/content (header-str header))))

(html/defsnippet ^:private table-snip template-filename table-sel
  [{:keys [headers] :as table}]
  ue/this        (html/add-class (:class table))
  ue/this        (ue/when-add-class (-> headers not-empty not) "ss-table-without-headers")
  ue/this        (ue/when-add-class (paginated? table) "ss-table-with-pagination")
  table-head-sel (ue/when-content (not-empty headers) (head-snip headers))
  table-body-sel (html/content (->> (rows table)
                                    (remove :remove?)
                                    rows-snip)))

(defn build
  [table]
  (table-snip table))
