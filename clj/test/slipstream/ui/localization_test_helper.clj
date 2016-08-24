(ns slipstream.ui.localization-test-helper
  "Test helper functions to navigate and compare localization files."
  (:require
   [clojure.java.io :as io]
   [superstring.core :as s]
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
        locale        (keyword basename)
        iso-language  (-> basename (uc/trim-from-last \-) keyword tower/iso-languages)]
    (when (and (= extension "edn") iso-language)
      [locale (->> filename (str "lang/") uc/read-resource uc/flatten-map)])))

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

(defn- dict-entry-str
  [with-base-locale-outcommented-strings? [k v]]
  (if-not (= v :missing)
    (format (str "<div class='dict-entry'>"
                   "<div class='key %2$s'>%3$s</div>"
                   (when with-base-locale-outcommented-strings?
                     "<div class='outcommented'>; Original: %1$s</div>")
                   "<div class='value %4$s'><xmp>%5$s</xmp></div>"
                 "</div>")
            (pr-str (get-in *all-dicts* [base-locale k]))
            (-> k class pr-str (s/replace \. \-))
            (pr-str k)
            (-> v class pr-str (s/replace \. \-))
            (pr-str v))
    (let [value-in-base-locale (get-in *all-dicts* [base-locale k])]
      (format (if (keyword? value-in-base-locale)
                (str "<div class='dict-entry'>"
                       "<div class='outcommented'>"
                         "; TODO: Missing localization entry. Pre-filled with the corresponding keyword in the base-locale dictionary."
                       "</div>"
                       "<div class='key clojure-lang-Keyword'>%s</div>"
                       "<div class='value clojure-lang-Keyword'><xmp>%s</xmp></div>"
                     "</div>")
                (str "<div class='outcommented'>"
                     "; TODO: Missing localization entry. Pre-filled with the corresponding string in the base-locale dictionary, but outcommented.<br>"
                     ";%s"
                     "<xmp>;%s</xmp>"
                     "</div>"))
              (pr-str k)
              (pr-str value-in-base-locale)))))

(defn- normalized-map
  [all-keys with-base-locale-outcommented-strings?]
  (let [current-lang-dict (get *all-dicts* *lang-to-display*)
        blank-complete-dict (zipmap (-> *all-dicts* :en keys) (repeat :missing))
        complete-lang-dict (merge blank-complete-dict current-lang-dict)]
    (->> complete-lang-dict
         (sort-by first)
         (map (partial dict-entry-str with-base-locale-outcommented-strings?))
         (s/join "<br><br>")
         (format "<pre>{<br><br><div class='outcommented'>;; This localization map was automatically normalized.</div><br><br>%s<br>}</pre>"))))

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
       "xmp{margin:0;}"
       ".dict-entry:hover{background-color:#eee;}"
       ".dict-entry .key{font-style:italic;}"
       ".outcommented{color:grey;}"
       ".java-lang-String{color:#d00;}"
       ".clojure-lang-Keyword{color:green;}"
       "</style>"
       "</head> "
       "<body>"
       (when-not (#{"normalized-map" "normalized-map-with-comments" "nested-map"} display-type)
         (str "<div>Locales: "
              (if *lang-to-display*
                "<a href='/localizations'>All</a> | "
                "<span style='background-color:black;color:white;'>All</span> | ")
              (->> all-locales
                    (map name)
                    (map #(if (= *lang-to-display* (keyword %))
                            (format "<span style='background-color:black;color:white;'>%1$s</span>" %)
                            (format "<a href='/localizations/%1$s'>%1$s</a>" %)))
                    (s/join " | "))
              "</div>"
              "<div><a href='ok'>show ok</a> | <a href='ok/keys'>only keys</a></div>"
              "<div><a href='missing'>show missing</a> | <a href='missing/keys'>only keys</a></div>"
              "<div><a href='unnecessary'>show unnecessary</a> | <a href='unnecessary/keys'>only keys</a></div>"
              (when *lang-to-display* "<div><a target='_blank' href='normalized-map'>normalized-map</a> <a target='_blank' href='normalized-map-with-comments'>(with original 'en' strings)</a> | <a target='_blank' href='nested-map'>nested-map</a></div>")))
       (case display-type
         "normalized-map" (normalized-map all-keys false)
         "normalized-map-with-comments" (normalized-map all-keys true)
         "nested-map"   (str "<pre>" (with-out-str (clojure.pprint/pprint (uc/deflatten-map (get *all-dicts* *lang-to-display*)))) "</pre>")
         (entries-table-html all-keys))
       "</body>"))

(defn html-str
  [url-segments]
  (let [diff-type       (some #{"ok" "missing" "unnecessary"}           url-segments)
        list-only-keys? (some (comp boolean #{"keys"})                  url-segments)
        display-type    (some #{"keys" "normalized-map" "normalized-map-with-comments" "nested-map"} url-segments)
        all-dicts       (read-locale-dict-files) ;; Read all dict files every time to catch updates.
        lang-to-display (some (-> all-dicts keys set) (map keyword url-segments))
        all-keys        (all-keys     all-dicts)
        all-locales     (all-locales  all-dicts)]
    (binding [*all-dicts*         all-dicts
              *lang-to-display*   lang-to-display
              *langs-to-display*  (cond
                                    (= (keyword lang-to-display) base-locale)  [base-locale]
                                    (not lang-to-display)                      all-locales
                                    :else                                      [base-locale (keyword lang-to-display)])]
      (localization-entries-page-html diff-type display-type list-only-keys? all-locales all-keys))))
