(ns slipstream.ui.views.table
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.time :as ut]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.current-user :as current-user]))

(localization/def-scoped-t)

(def template-filename (u/template-path-for "table.html"))

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

;; Enlive cell snips

; Text cell

(html/defsnippet ^:private cell-text-snip-view template-filename (sel-for-cell :text)
  [{:keys [text tooltip id] :as cell-content}]
  ue/this (html/html-content (str text))
  ue/this (ue/when-set-style (-> text str count (> 100))
                             "word-wrap: break-word; max-width: 500px;")
  ue/this (ue/set-id id)
  ue/this (ue/when-set-title (not-empty tooltip)
                             (str tooltip)))

(html/defsnippet ^:private cell-text-snip-edit template-filename (sel-for-cell :text :editable)
  [{:keys [text tooltip id read-only?] :as cell-content}]
  [:input]  (ue/set-id id)
  [:input]  (ue/set-name id)
  [:input]  (ue/set-value (str text))
  [:input]  (ue/toggle-readonly (and (current-user/not-super?) read-only?))
  ue/this   (ue/when-set-title (not-empty tooltip) (str tooltip))
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Editable textarea cell

(html/defsnippet ^:private cell-textarea-snip-edit template-filename (sel-for-cell :text-area :editable)
  [{:keys [text tooltip id] :as cell-content}]
  [:textarea]  (ue/set-id id)
  [:textarea]  (ue/set-name id)
  [:textarea]  (html/content (str text))
  ue/this   (ue/when-set-title (not-empty tooltip) (str tooltip))
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Editable password cell

(html/defsnippet ^:private cell-password-snip-edit template-filename (sel-for-cell :password :editable)
  [{:keys [id password] :as cell-content}]
  [:input]  (ue/set-id id)
  [:input]  (ue/set-name id)
  [:input]  (ue/set-value password)
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Boolean cell

(html/defsnippet ^:private cell-boolean-snip-view template-filename (sel-for-cell :boolean)
  [{:keys [value id] :as cell-content}]
  ; [:input] (ue/toggle-disabled true)
  [:input] (ue/toggle-checked value))

(html/defsnippet ^:private cell-boolean-snip-edit template-filename (sel-for-cell :boolean :editable)
  [{:keys [value id] :as cell-content}]
  ; [:input] (ue/toggle-disabled false)
  [:input] (ue/toggle-checked value)
  [:input] (ue/set-id id)
  [:input] (ue/set-name id)
  ue/this   (append-hidden-inputs-when-parameter-in cell-content))

; Enum cell

(html/defsnippet ^:private cell-enum-snip-edit template-filename (sel-for-cell :enum :editable)
  [{:keys [enum id] :as cell-content}]
  [:select] (ue/set-id id)
  [:select] (ue/set-name id)
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

(def cell-map-entry-sel (concat (sel-for-cell :map) [#{[:dt html/first-of-type] [:dd html/first-of-type]}]))

(html/defsnippet ^:private term-dict-entry-snip template-filename cell-map-entry-sel
  [[k v]]
  [:dt] (html/content (map-key-str k))
  [:dd] (html/content (str v)))

(html/defsnippet ^:private cell-map-snip-view template-filename (sel-for-cell :map)
  [m]
  [:dl] (html/content (->> m (into (sorted-map)) (mapcat term-dict-entry-snip))))

; Link cell

(html/defsnippet ^:private cell-link-snip-view template-filename (sel-for-cell :link)
  [{:keys [text href open-in-new-window? id] :as cell-content}]
    [:a] (html/content (str text))
    [:a] (ue/set-href href)
    [:a] (ue/set-id id)
    [:a] (ue/when-set-target open-in-new-window? "_blank"))

; Icon cell

(html/defsnippet ^:private cell-icon-snip-view template-filename (sel-for-cell :icon)
  [icon]
  [:span] (icons/set icon))

; Module version cell

(html/defsnippet ^:private cell-module-version-snip-view template-filename (sel-for-cell :module-version)
  [uri]
  [:span] (html/content (uc/last-path-segment uri))
  [:a]    (ue/set-href   (uc/trim-last-path-segment uri) "/" ))

; Help hint cell

(html/defsnippet ^:private cell-help-hint-snip-view template-filename (sel-for-cell :help-hint)
  [help-text]
  [:.ss-table-tooltip]  (ue/set-title help-text)
  [:.ss-table-tooltip]  (ue/remove-if-not (not-empty help-text)))

; Reference module cell snippets

(html/defsnippet ^:private cell-reference-module-snip-edit template-filename (sel-for-cell :reference-module :editable)
  [reference-module-uri]
  [:.ss-reference-module-name :a]  (ue/set-href reference-module-uri)
  [:.ss-reference-module-name :a]  (html/content reference-module-uri))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Cell snip dispatching on type, mode and content

(derive :content/map   :content/any)
(derive :content/plain :content/any)

(derive :mode/view  :mode/any)
(derive :mode/edit  :mode/any)

(defmulti ^:private cell-snip
  "See tests for an explicit list of cell types and an overview of their variants."
  (fn [{:keys [type content editable?] :as cell}]
    [type
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
  ; (cell-text-snip-view {:text (when pwd "*****")}))

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
                        :tooltip (str "Possible values: " (options-str enum))}))

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

(defmethod cell-snip [:cell/timestamp :mode/any :content/plain]
  [{timestamp :content}]
  (cell-text-snip-view {:text (or
                                (ut/format :human-readable-long timestamp)
                                (t :timestamp.unknown))
                        :tooltip (ut/format :relative timestamp)}))

(defmethod cell-snip [:cell/relative-timestamp :mode/any :content/plain]
  [{timestamp :content}]
  (cell-text-snip-view {:text (or
                                (ut/format :relative timestamp)
                                (t :timestamp.unknown))
                        :tooltip (ut/format :human-readable-long timestamp)}))

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

(defmethod cell-snip [:cell/map :mode/view :content/map]
  [{m :content}]
  (cell-map-snip-view m))

(defmethod cell-snip [:cell/map :mode/edit :content/map]
  [{m :content}]
  ; TODO: Unimplemented edit view
  (cell-map-snip-view m))

(defmethod cell-snip [:cell/link :mode/view :content/map]
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
  [{username :content}]
  (cell-link-snip-view {:text username
                        :href (str "/user/" username)
                        :id "username"}))

(defmethod cell-snip [:cell/icon :mode/any :content/plain]
  [{icon :content}]
  (cell-icon-snip-view icon))

(defmethod cell-snip [:cell/module-version :mode/any :content/plain]
  [{uri :content}]
  (cell-module-version-snip-view uri))

(defmethod cell-snip [:cell/module-name :mode/view :content/plain]
  [{module-name :content}]
  (cell-text-snip-view {:text module-name, :id "ss-module-name"}))

(defmethod cell-snip [:cell/module-name :mode/edit :content/plain]
  [{module-name :content}]
  (cell-text-snip-edit {:text module-name, :id "ss-module-name"}))

(defmethod cell-snip [:cell/help-hint :mode/view :content/plain]
  [{help-text :content}]
  (cell-help-hint-snip-view help-text))

(defmethod cell-snip [:cell/reference-module :mode/view :content/plain]
  [{reference-module-uri :content}]
  (cell-link-snip-view {:text reference-module-uri :href reference-module-uri}))

(defmethod cell-snip [:cell/reference-module :mode/edit :content/plain]
  [{reference-module-uri :content}]
  (cell-reference-module-snip-edit reference-module-uri))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Row

(html/defsnippet ^:private rows-snip template-filename table-row-sel
  [rows]
  ue/this (html/clone-for [{:keys [style cells]} rows]
           ue/this (html/content (map cell-snip cells))
           ue/this (ue/when-add-class style (name style))))


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
  [{:keys [headers rows] :as table}]
  table-head-sel (html/content (head-snip headers))
  table-body-sel (html/content (->> rows
                                    (remove :hidden?)
                                    rows-snip)))

(defn build
  [table]
  (table-snip table))
