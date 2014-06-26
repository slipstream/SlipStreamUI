(ns slipstream.ui.views.common-test
  (:use
    [expectations])
  (:require
    [slipstream.ui.views.common :as common]))

(expect "...7890"
        (common/ellipse-left "1234567890" 7))

(expect "...567890"
        (common/ellipse-left "1234567890" 9))

(expect "1234567890"
        (common/ellipse-left "1234567890" 10))

(expect "1234567890"
        (common/ellipse-left "1234567890" 11))

(expect ""
        (common/ellipse-left "" 7))

(expect nil
        (common/ellipse-left nil 7))

(expect "1234..."
        (common/ellipse-right "1234567890" 7))

(expect "123456..."
        (common/ellipse-right "1234567890" 9))

(expect "1234567890"
        (common/ellipse-right "1234567890" 10))

(expect "1234567890"
        (common/ellipse-right "1234567890" 11))

(expect ""
        (common/ellipse-right "" 7))

(expect nil
        (common/ellipse-right nil 7))
