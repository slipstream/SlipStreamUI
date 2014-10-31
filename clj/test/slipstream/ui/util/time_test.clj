(ns slipstream.ui.util.time-test
  (:refer-clojure :exclude [format])
  (:use [expectations]
        [slipstream.ui.util.time])
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [slipstream.ui.util.localization :as localization]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; parse SlipStream timestamp format

(expect
  org.joda.time.DateTime
  (parse "2013-07-05 00:27:12.471 CEST"))

; CEST – Central European Summer Time: +0200
(expect
  (org.joda.time.DateTime. 2013 07 05 00 27 12 471 (org.joda.time.DateTimeZone/UTC))
  (parse "2013-07-05 02:27:12.471 CEST"))

; CET – Central European Time: +0100
(expect
  (org.joda.time.DateTime. 2013 01 05 00 27 12 471 (org.joda.time.DateTimeZone/UTC))
  (parse "2013-01-05 01:27:12.471 CET"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; parse iso8601 timestamp format

(expect
  org.joda.time.DateTime
  (parse "2013-07-05T00:27:12.471Z"))

; CEST – Central European Summer Time: +0200
(expect
  (org.joda.time.DateTime. 2013 07 05 00 27 12 471 (org.joda.time.DateTimeZone/UTC))
  (parse "2013-07-05T02:27:12.471+0200"))

; CET – Central European Time: +0100
(expect
  (org.joda.time.DateTime. 2013 01 05 00 27 12 471 (org.joda.time.DateTimeZone/UTC))
  (parse "2013-01-05T01:27:12.471+0100"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; test format localization

(expect
  IllegalStateException ;; Missing lang environment
  (format :human-readable-long "2013-07-05 00:27:12.471 CEST"))

(expect
  "Friday, 5 July 2013, 02:27:12 CEST"
  (localization/with-lang :en
    (format :human-readable-long "2013-07-05 02:27:12.471 CEST")))

(expect
  "vendredi, 5 juillet 2013, 02:27:12 CEST"
  (localization/with-lang :fr
    (format :human-readable-long "2013-07-05 02:27:12.471 CEST")))

(expect
  "divendres, 5 juliol 2013, 02:27:12 CEST"
  (localization/with-lang :ca
    (format :human-readable-long "2013-07-05 02:27:12.471 CEST")))

(expect
  "金曜日, 5 7月 2013, 02:27:12 CEST"
  (localization/with-lang :ja
    (format :human-readable-long "2013-07-05 02:27:12.471 CEST")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; format SlipStream timestamp format

(expect
  String
  (localization/with-lang :en
    (format :human-readable-long "2013-07-05 00:27:12.471 CEST")))

; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "Saturday, 5 January 2013, 00:27:12 UTC"
  (localization/with-lang :en
    (format :human-readable-long "2013-01-05 00:27:12.471 UTC")))

; CEST – Central European Summer Time: +0200
(expect
  "Friday, 5 July 2013, 02:27:12 CEST"
  (localization/with-lang :en
    (format :human-readable-long "2013-07-05 02:27:12.471 CEST")))

; CET – Central European Time: +0100
(expect
  "Saturday, 5 January 2013, 01:27:12 CET"
  (localization/with-lang :en
    (format :human-readable-long "2013-01-05 01:27:12.471 CET")))

; EST – Eastern Standard Time: -0500
(expect
  "Saturday, 5 January 2013, 01:27:12 EST"
  (localization/with-lang :en
    (format :human-readable-long "2013-01-05 01:27:12.471 EST")))


; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "5 Jan 2013, 00:27:12 UTC"
  (localization/with-lang :en
    (format :human-readable-short "2013-01-05 00:27:12.471 UTC")))

; CEST – Central European Summer Time: +0200
(expect
  "5 Jul 2013, 02:27:12 CEST"
  (localization/with-lang :en
    (format :human-readable-short "2013-07-05 02:27:12.471 CEST")))

; CET – Central European Time: +0100
(expect
  "5 Jan 2013, 01:27:12 CET"
  (localization/with-lang :en
    (format :human-readable-short "2013-01-05 01:27:12.471 CET")))


; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "2013-01-05T00:27:12.471Z"
  (localization/with-lang :en
    (format :iso8601 "2013-01-05 00:27:12.471 UTC")))

; CEST – Central European Summer Time: +0200
(expect
  "2013-07-05T02:27:12.471+02:00"
  (localization/with-lang :en
    (format :iso8601 "2013-07-05 02:27:12.471 CEST")))

; CET – Central European Time: +0100
(expect
  "2013-01-05T01:27:12.471+01:00"
  (localization/with-lang :en
    (format :iso8601 "2013-01-05 01:27:12.471 CET")))


; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "2013-01-05 00:27:12.471 UTC"
  (localization/with-lang :en
    (format :ss-timestamp-format "2013-01-05 00:27:12.471 UTC")))

; CEST – Central European Summer Time: +0200
(expect
  "2013-07-05 02:27:12.471 CEST"
  (localization/with-lang :en
    (format :ss-timestamp-format "2013-07-05 02:27:12.471 CEST")))

; CET – Central European Time: +0100
(expect
  "2013-01-05 01:27:12.471 CET"
  (localization/with-lang :en
    (format :ss-timestamp-format "2013-01-05 01:27:12.471 CET")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; format iso8601 timestamp format

(expect
  String
  (localization/with-lang :en
    (format :human-readable-long "2013-07-05T00:27:12.471Z")))

; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "Saturday, 5 January 2013, 00:27:12 UTC"
  (localization/with-lang :en
    (format :human-readable-long "2013-01-05T00:27:12.471Z")))

; CEST – Central European Summer Time: +0200
(expect
  "Friday, 5 July 2013, 02:27:12 +02:00"
  (localization/with-lang :en
    (format :human-readable-long "2013-07-05T02:27:12.471+0200")))

; CET – Central European Time: +0100
(expect
  "Saturday, 5 January 2013, 01:27:12 +01:00"
  (localization/with-lang :en
    (format :human-readable-long "2013-01-05T01:27:12.471+0100")))

; EST – Eastern Standard Time: -0500
(expect
  "Saturday, 5 January 2013, 01:27:12 -05:00"
  (localization/with-lang :en
    (format :human-readable-long "2013-01-05T01:27:12.471-0500")))


; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "5 Jan 2013, 00:27:12 UTC"
  (localization/with-lang :en
    (format :human-readable-short "2013-01-05T00:27:12.471Z")))

; CEST – Central European Summer Time: +0200
(expect
  "5 Jul 2013, 02:27:12 +02:00"
  (localization/with-lang :en
    (format :human-readable-short "2013-07-05T02:27:12.471+0200")))

; CET – Central European Time: +0100
(expect
  "5 Jan 2013, 01:27:12 +01:00"
  (localization/with-lang :en
    (format :human-readable-short "2013-01-05T01:27:12.471+0100")))


; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "2013-01-05T00:27:12.471Z"
  (localization/with-lang :en
    (format :iso8601 "2013-01-05T00:27:12.471Z")))

; CEST – Central European Summer Time: +0200
(expect
  "2013-07-05T02:27:12.471+0200"
  (localization/with-lang :en
    (format :iso8601 "2013-07-05T02:27:12.471+0200")))

; CET – Central European Time: +0100
(expect
  "2013-01-05T01:27:12.471+0100"
  (localization/with-lang :en
    (format :iso8601 "2013-01-05T01:27:12.471+0100")))


; UTC - Coordinated Universal Time: (Z) +00:00
(expect
  "2013-01-05 00:27:12.471 UTC"
  (localization/with-lang :en
    (format :ss-timestamp-format "2013-01-05T00:27:12.471Z")))

; CEST – Central European Summer Time: +0200
(expect
  "2013-07-05 02:27:12.471 +02:00"
  (localization/with-lang :en
    (format :ss-timestamp-format "2013-07-05T02:27:12.471+0200")))

; CET – Central European Time: +0100
(expect
  "2013-01-05 01:27:12.471 +01:00"
  (localization/with-lang :en
    (format :ss-timestamp-format "2013-01-05T01:27:12.471+0100")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; relative

(expect
  String
  (localization/with-lang :en
    (format :relative "2013-07-05T00:27:12.471Z")))

(defn- date-periods-ago
  [units period]
  (->> units period t/ago
       (f/unparse (f/formatters :date-time))))

(expect
  "1 minute ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 60 t/seconds))))

(expect
  "1 minute and 10 seconds ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 70 t/seconds))))

(expect
  "5 minutes ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 5 t/minutes))))

(expect
  "59 minutes ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 59 t/minutes))))

(expect
  "1 hour ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 60 t/minutes))))

(expect
  "1 hour and 1 minute ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 61 t/minutes))))

(expect
  "1 hour and 2 minutes ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 62 t/minutes))))

(expect
  "1 year and 3 months ago"
  (localization/with-lang :en
    (format :relative (date-periods-ago 15 t/months))))


(defn- date-periods-from-now
  [units period]
  (->> units period t/from-now
       (f/unparse (f/formatters :date-time))))

(expect
  "in 1 minute"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 60 t/seconds))))

(expect
  "in 1 minute and 10 seconds"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 70 t/seconds))))

(expect
  "in 5 minutes"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 5 t/minutes))))

(expect
  "in 59 minutes"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 59 t/minutes))))

(expect
  "in 1 hour"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 60 t/minutes))))

(expect
  "in 1 hour and 1 minute"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 61 t/minutes))))

(expect
  "in 1 hour and 2 minutes"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 62 t/minutes))))

(expect
  "in 1 year and 3 months"
  (localization/with-lang :en
    (format :relative (date-periods-from-now 15 t/months))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Wrong formats, empty string and nil

(expect
  IllegalArgumentException
  (localization/with-lang :en
    (format :human-readable-long "2013-07-05T00:27:")))

(expect
  AssertionError
  (localization/with-lang :en
    (format :human-readable-long 123)))

(expect
  nil
  (localization/with-lang :en
    (format :human-readable-long "")))

(expect
  nil
  (localization/with-lang :en
    (format :human-readable-long nil)))

