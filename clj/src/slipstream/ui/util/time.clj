(ns slipstream.ui.util.time
  "Util functions around time, date and timestamps using clj-time."
  (:refer-clojure :exclude [format])
  (require  [clojure.string :as s]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization])
  (:import [org.joda.time DateTimeZone]))

(def ^:private ss-timestamp-pattern
  "Corresponds to the format currently returned by SlipStream server."
  #"\d\d\d\d-\d\d-\d\d \d\d:\d\d:\d\d.\d{1,3} \w{3,4}")

(def ^:private human-readable-long-pattern
  "Corresponds to 'Friday, 5 July 2013, 15:27:12 UTC'."
  (re-pattern (str "(?:Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday), "
                   "(?:[1-9]|[1-2][0-9]|3[0-1]) "
                   "(?:January|February|March|April|May|June|July|August|September|October|November|December) "
                   "\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d \\w{3,4}")))

(def ^:private human-readable-short-pattern
  "Corresponds to '5 Jul 2013, 15:27:12 UTC'."
  (re-pattern (str "(?:[1-9]|[1-2][0-9]|3[0-1]) "
                   "(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) "
                   "\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d \\w{3,4}")))

(def ^:private iso8601-timestamp-pattern
  "Corresponds to (clj-time.format/formatters :date-time)."
  #"\d\d\d\d-\d\d-\d\dT\d\d:\d\d:\d\d.\d\d\d(?:Z|[\+-]\d\d(?:\:?\d\d)?)")

(defn- normalize-timezone
  "The timezone code CEST is not recognized as standard by org.joda.time.
  For a complete list of supported timezones run:
    (org.joda.time.DateTimeZone/getAvailableIDs)"
  [s]
  (-> s
      (s/replace "CEST" "CET")))


(def ^:private formatters
  {:ss-timestamp-format   (f/formatter "yyyy-MM-dd HH:mm:ss.SSS ZZZ")
   :iso8601               (f/formatters :date-time)
   :human-readable-long   (f/formatter "EEEE, d MMMM yyyy, HH:mm:ss zzz")
   :human-readable-short  (f/formatter "d MMM yyyy, HH:mm:ss zzz")})

(defn- find-format
  [s]
  (cond
    (re-matches ss-timestamp-pattern s)         :ss-timestamp-format
    (re-matches iso8601-timestamp-pattern s)    :iso8601
    (re-matches human-readable-long-pattern s)  :human-readable-long
    (re-matches human-readable-short-pattern s) :human-readable-short
    :else (throw (IllegalArgumentException. (str "Unsupported timestamp format: " s)))))

(defn- find-timezone
  [s]
  (or
    (some->> s (re-matches #".* (\w{3,4})") second normalize-timezone t/time-zone-for-id)
    (some->> s (re-matches #".* ?([\+-]\d{2}):?(\d{2})") rest (map read-string) (apply t/time-zone-for-offset))))

(defn parse
  ([s]
    (parse (find-format s) s))
  ([timestamp-format s]
    (->> s
         normalize-timezone
         (f/parse (formatters timestamp-format)))))

(defn- formatter
  [timestamp-format timezone]
  (-> timestamp-format
      formatters
      (f/with-locale (localization/locale))
      (f/with-zone timezone)))

(defn format
  [target-format s]
  {:pre [(or (string? s) (nil? s))]}
  (when (not-empty s)
    (let [source-format (find-format s)
          source-timezone (find-timezone s)]
      (if (= target-format source-format)
        s
        (->> s
             (parse source-format)
             (f/unparse (formatter target-format source-timezone)))))))