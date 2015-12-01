(ns slipstream.ui.views.tables-test
  (:require
    [expectations :refer :all]
    [slipstream.ui.views.tables :as tables]
    [slipstream.ui.util.localization :as localization]))

(expect "4 April 2016"
        (localization/with-lang :en
                                (tables/format-period "daily"
                                                      "2016-04-04T00:00:00.000Z")))
(expect "25 Apr 2016 - 1 May 2016"
        (localization/with-lang :en
                                (tables/format-period "weekly"
                                                      "2016-04-25T00:00:00.000Z")))
(expect "April 2016"
        (localization/with-lang :en
                                (tables/format-period "monthly"
                                                      "2016-04-04T00:00:00.000Z")))