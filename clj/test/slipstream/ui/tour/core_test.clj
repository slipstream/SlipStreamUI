(ns slipstream.ui.tour.core-test
  (:use [expectations]
        [slipstream.ui.tour.core])
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.localization :as localization]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def ^:private tour-steps
  [:#one {:title   "title1"
          :content "content1"}
   :#two {:title   "title2"
          :content "content2"}])

(expect
  (str "<span data-bootstro-step=\"0\" data-bootstro-html=\"true\" data-bootstro-content=\"content1\" data-bootstro-title=\"title1\" class=\"bootstro\" id=\"one\">i'm one</span>"
       "<span data-bootstro-step=\"1\" data-bootstro-html=\"true\" data-bootstro-content=\"content2\" data-bootstro-title=\"title2\" class=\"bootstro\" id=\"two\">i'm two</span>")
  (html/sniptest "<span id=one>i'm one</span><span id=two>i'm two</span>"
    (@#'slipstream.ui.tour.core/add tour-steps)))
