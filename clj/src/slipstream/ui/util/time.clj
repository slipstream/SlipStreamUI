(ns slipstream.ui.util.time
  "Util functions around time, date and timestamps using clj-time."
  (:refer-clojure :exclude [format])
  (require  [clojure.string :as s]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization])
  (:import [org.joda.time DateTimeZone]))

(localization/def-scoped-t)

(def ^:private ss-timestamp-pattern
  "Corresponds to the format currently returned by SlipStream server."
  #"\d\d\d\d-\d\d-\d\d \d\d:\d\d:\d\d.\d{1,3} \w{3,4}")

(def ^:private human-readable-long-pattern
  "Corresponds to 'Friday, 5 July 2013, 15:27:12 UTC'."
  (re-pattern (str "(?:Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday), "
                   "(?:[1-9]|[1-2][0-9]|3[0-1]) "
                   "(?:January|February|March|April|May|June|July|August|September|October|November|December) "
                   "\\d\\d\\d\\d, \\d\\d:\\d\\d:\\d\\d \\w{3,4}")))

(def ^:private human-readable-pattern
  "Corresponds to 'Fri, 5 Jul 2013, 15:27:12 UTC'."
  (re-pattern (str "(?:Sun|Mon|Tue|Wed|Thu|Fri|Sat), "
                   "(?:[1-9]|[1-2][0-9]|3[0-1]) "
                   "(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) "
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
      (s/replace "CET"  "Europe/Paris")
      (s/replace "CEST" "Europe/Paris")
      (s/replace "BST"  "Europe/London")
      (s/replace "GMT"  "Europe/London")))


(defn- formatters
  [formatter]
  (case formatter
    :ss-timestamp-format   (f/formatter "yyyy-MM-dd HH:mm:ss.SSS ZZZ")
    :iso8601               (f/formatters :date-time)
    :human-readable-long   (f/formatter "EEEE, d MMMM yyyy, HH:mm:ss zzz")
    :human-readable        (f/formatter "EEE, d MMM yyyy, HH:mm:ss zzz")
    :human-readable-short  (f/formatter "d MMM yyyy, HH:mm:ss zzz")
    :relative              :relative
    (throw (IllegalArgumentException.
               (str "formatter " formatter " not valid.")))))

(defn- find-format
  [s]
  (cond
    (re-matches ss-timestamp-pattern s)         :ss-timestamp-format
    (re-matches iso8601-timestamp-pattern s)    :iso8601
    (re-matches human-readable-long-pattern s)  :human-readable-long
    (re-matches human-readable-pattern s)       :human-readable
    (re-matches human-readable-short-pattern s) :human-readable-short
    :else (throw (IllegalArgumentException. (str "Unsupported timestamp format: " s)))))

(defn- find-timezone
  [s]
  (or
    (some->> s (re-matches #".* (\w{3,4})") second normalize-timezone t/time-zone-for-id)
    (some->> s (re-matches #".* ?([\+-]\d{2}):?(\d{2})") rest (map read-string) (apply t/time-zone-for-offset))))

(defn parse
  ([s]
    (parse s (find-format s)))
  ([s timestamp-format]
    (->> s
         normalize-timezone
         (f/parse (formatters timestamp-format)))))

(defn- formatter
  [timestamp-formatter timezone]
  (-> timestamp-formatter
      (f/with-locale (localization/locale))
      (f/with-zone timezone)))

(def ^:private time-periods
  [{:in-time-period t/in-years,   :name-key :year,    :unit-count 1000}
   {:in-time-period t/in-months,  :name-key :month,   :unit-count 12}
   {:in-time-period t/in-weeks,   :name-key :week,    :unit-count 4}
   {:in-time-period t/in-days,    :name-key :day,     :unit-count 7}
   {:in-time-period t/in-hours,   :name-key :hour,    :unit-count 24}
   {:in-time-period t/in-minutes, :name-key :minute,  :unit-count 60}
   {:in-time-period t/in-seconds, :name-key :second,  :unit-count 60}])

(defn- calculate-time-period-units
  [^org.joda.time.Interval interval]
  (doall ;; NOTE: If done lazily, the localization/*lang* is out of scope (i.e. nil) when realized (??!)
    (for [{:keys [in-time-period name-key unit-count]} time-periods
          :let [units (-> interval in-time-period (mod unit-count))
                name-localization-key (if (= 1 units) name-key (-> name-key name (str "s") keyword))]]
      (vector
        units
        (t name-localization-key)))))

(defn- significative-time-period-units
  [^org.joda.time.Interval interval]
  (->> interval
       calculate-time-period-units
       (remove #(-> % first (= 0)))
       (take 2)))

(localization/with-prefixed-t :relative-time.significative-time-periods
  (defn- relative-timestamp
  [^org.joda.time.DateTime datetime]
  (let [now (t/now)
        past-datetime? (t/before? datetime now)
        interval (if past-datetime?
                   (t/interval datetime now)
                   (t/interval now datetime))
        relative-periods (significative-time-period-units interval)]
    (case [past-datetime? (count relative-periods)]
      [true   0] (t :in-the-past.zero)
      [true   1] (apply t :in-the-past.one (flatten relative-periods))
      [true   2] (apply t :in-the-past.two (flatten relative-periods))
      [false  0] (t :in-the-future.zero)
      [false  1] (apply t :in-the-future.one (flatten relative-periods))
      [false  2] (apply t :in-the-future.two (flatten relative-periods))))))

(defn- format-as
  [^org.joda.time.DateTime datetime target-format source-time-zone]
  (let [target-formatter (formatters target-format)]
    (if (= :relative target-formatter)
      (relative-timestamp datetime)
      (f/unparse (formatter target-formatter source-time-zone) datetime))))

(defn format
  [target-format s]
  {:pre [(or (string? s) (nil? s))]}
  (when (not-empty s)
    (let [source-format (find-format s)]
      (if (= target-format source-format)
        s
        (-> s
            (parse source-format)
            (format-as target-format (find-timezone s)))))))