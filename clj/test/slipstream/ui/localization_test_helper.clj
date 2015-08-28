(ns slipstream.ui.localization-test-helper
  "Test helper functions to navigate and compare localization files."
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [taoensso.tower :as tower]
            [slipstream.ui.util.clojure :as uc]))

(def ^:private base-locale :en)

(def ^:private ^:dynamic *all-dicts* nil)
(def ^:private ^:dynamic *lang-to-display* nil)
(def ^:private ^:dynamic *langs-to-display* nil)

; (def ^:private lang-resources-dir "clj/resources/lang/")
(def ^:private lang-resources-dir "resources/lang/")

(defn- file->locale-entry
  [file]
  (let [filename      (.getName file)
        basename      (uc/trim-from-last filename \.)
        extension     (uc/trim-up-to-last filename \.)
        iso-language  (-> basename keyword tower/iso-languages)]
    (when (and (= extension "edn") iso-language)
      [iso-language (->> filename (str "lang/") uc/read-resource uc/flatten-map)])))

(defn- read-locale-dict-files
  []
  (if-let [all-dicts (into {} (->> lang-resources-dir
                                      io/file
                                      file-seq
                                      rest
                                      (map file->locale-entry)))]
    all-dicts
    (throw (IllegalStateException.
            (str "No lang locale files found in: " lang-resources-dir)))))

(defn- all-keys
  [all-dicts]
  (->> all-dicts
       vals
       (apply merge)
       keys
       sort))

(defn- all-locales
  [all-dicts]
  (-> all-dicts
      keys
      set
      (disj base-locale)
      seq
      (conj base-locale)))

(defn- values-for-locale
  [k locale]
  (let [dict (get *all-dicts* locale)
        value (get dict k)]
    (format "<li class='%1$s'><b><code>%1$s:</code></b> %2$s</li>"
            (name locale)
            (if (keyword? value)
              (format "<code>%s =></code> <span class='redirected-value'><em>%s</em></span>"
                      value
                      (get dict value))
              (str value)))))

(defn- values-for-langs-to-display
  [k]
  (->> *langs-to-display*
       (map (partial values-for-locale k))
       s/join))

(defn- css-class-for-tr
  [k]
  (let [[base-locale-value & extra-locale-values]  (map (comp k *all-dicts*) *langs-to-display*)
        base-locale-present?        (-> base-locale-value str not-empty boolean)
        base-locale-missing?        (not base-locale-present?)
        all-extra-locales-present?  (every? (comp     not-empty str) extra-locale-values)
        all-extra-locales-missing?  (every? (comp not not-empty str) extra-locale-values)
        some-extra-locales-present? (some   (comp     not-empty str) extra-locale-values)
        some-extra-locales-missing? (some   (comp not not-empty str) extra-locale-values)]
    (cond
      (and base-locale-present? all-extra-locales-present?)  "locales-ok"
      (and base-locale-missing? all-extra-locales-missing?)  "locales-ok"
      (and base-locale-present? some-extra-locales-missing?) "locales-missing"
      (and base-locale-missing? some-extra-locales-present?) "locales-unnecessary")))

(defn- langs-to-display-entry-row-html
  [k]
  (format "<tr class='%s'><td><code>%s</code></td><td><ul>%s</ul></td></tr>\n"
          (css-class-for-tr k)
          (str k)
          (values-for-langs-to-display k)))

(defn- entries-table-html
  [all-keys]
  (str "<table><thead><th>key</th><th>string</th></thead><tbody>"
       (s/join (map langs-to-display-entry-row-html all-keys))
       "</tbody></table>"))

(defn- localization-entries-page-html
  [diff-type display-type list-only-keys? all-locales all-keys]
  (str "<head>"
       (format "<base href='/localizations/%s' />"
               (condp = (count *langs-to-display*)
                 2 (-> *langs-to-display* last  name (str "/"))
                 1 (-> *langs-to-display* first name (str "/"))
                 ""))
       "<style>"
       "ul{list-style-type:none;}"
       "tr:hover{background-color:#eee;}"
       "tr.locales-missing{background-color:#faa;}"
       "tr.locales-missing:hover{background-color:#f88;}"
       "tr.locales-unnecessary{background-color:#ffa;}"
       "tr.locales-unnecessary:hover{background-color:#ff8;}"
       (when diff-type (format "tr:not(.locales-%s){display:none;}" diff-type))
       (format "li:not(.%s){color:blue;}" (name base-locale))
       (when list-only-keys? "td:nth-child(2){display:none;}")
       "</style>"
       "</head> "
       "<body>"
       "<div>Locales: "
       "<a href='/localizations'>All</a> | "
       (->> all-locales
            (map name)
            (map #(str "<a href='/localizations/" % "'>" % "</a>"))
            (s/join " | "))
       "</div>"
       "<div><a href='ok'>show ok</a> | <a href='ok/keys'>only keys</a>"
       (when *lang-to-display* " | <a href='ok/flat-map'>flat-map</a> | <a href='ok/nested-map'>nested-map</a>")
       "</div>"
       "<div><a href='missing'>show missing</a> | <a href='missing/keys'>only keys</a>"
       (when *lang-to-display* " | <a href='missing/flat-map'>flat-map</a> | <a href='missing/nested-map'>nested-map</a>")
       "</div>"
       "<div><a href='unnecessary'>show unnecessary</a> | <a href='unnecessary/keys'>only keys</a>"
       (when *lang-to-display* " | <a href='unnecessary/flat-map'>flat-map</a> | <a href='unnecessary/nested-map'>nested-map</a>")
       "</div>"
       (case display-type
         "flat-map"     (str "<pre>" (with-out-str (clojure.pprint/pprint (get *all-dicts* *lang-to-display*))) "</pre>")
         "nested-map"   (str "<pre>" (with-out-str (clojure.pprint/pprint (uc/deflatten-map (get *all-dicts* *lang-to-display*)))) "</pre>")
         ; "nested-map"   (str "<code>" (uc/deflatten-map (get *all-dicts* *lang-to-display*)) "</code>")
         ; "nested-map"   (get *all-dicts* *lang-to-display*)
         (entries-table-html all-keys))
       "</body>"))

(defn html-str
  [url-segments]
  (let [diff-type       (some #{"ok" "missing" "unnecessary"}           url-segments)
        lang-to-display (some tower/iso-languages                       (map keyword url-segments))
        list-only-keys? (some (comp boolean #{"keys"})                  url-segments)
        display-type    (some #{"keys" "flat-map" "nested-map"}         url-segments)
        all-dicts       (read-locale-dict-files) ;; Read all dict files every time to catch updates.
        all-keys        (all-keys     all-dicts)
        all-locales     (all-locales  all-dicts)]
    (binding [*all-dicts*         all-dicts
              *lang-to-display*   lang-to-display
              *langs-to-display*  (cond
                                    (= (keyword lang-to-display) base-locale)  [base-locale]
                                    (not lang-to-display)                      all-locales
                                    :else                                      [base-locale (keyword lang-to-display)])]
      (localization-entries-page-html diff-type display-type list-only-keys? all-locales all-keys))))