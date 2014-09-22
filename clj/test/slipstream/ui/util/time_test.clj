(ns slipstream.ui.util.time-test
  (:refer-clojure :exclude [format])
  (:use [expectations]
        [slipstream.ui.util.time])
  (:require [slipstream.ui.util.localization :as localization]))


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
