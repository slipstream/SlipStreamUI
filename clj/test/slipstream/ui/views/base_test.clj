(ns slipstream.ui.views.base-test
  (:use [expectations]
        [slipstream.ui.views.base])
  (:require [net.cgrand.enlive-html :as html]))

;; NOTE: To access the private symbol 'x' in the namespace 'foo.bar',  we use following notation:
;;
;;       @#'foo.bar/x
;;
;; Source: https://github.com/bbatsov/clojure-style-guide#access-private-var


;; bottom-external-scripts-snip

(expect
  ""
  (->> (@#'slipstream.ui.views.base/bottom-external-scripts-snip nil)
       html/emit*
       (apply str)))

(expect
  ""
  (->> (@#'slipstream.ui.views.base/bottom-external-scripts-snip [])
       html/emit*
       (apply str)))

(expect
  "<script src=\"external/blah.js\"></script>"
  (->> (@#'slipstream.ui.views.base/bottom-external-scripts-snip ["blah.js"])
       html/emit*
       (apply str)))

(expect
  "<script src=\"external/foo.js\"></script><script src=\"external/foo/bar.js\"></script>"
  (->> (@#'slipstream.ui.views.base/bottom-external-scripts-snip ["foo.js" "foo/bar.js"])
       html/emit*
       (apply str)))


;; bottom-internal-scripts-snip

(expect
  ""
  (->> (@#'slipstream.ui.views.base/bottom-internal-scripts-snip nil)
       html/emit*
       (apply str)))

(expect
  ""
  (->> (@#'slipstream.ui.views.base/bottom-internal-scripts-snip [])
       html/emit*
       (apply str)))

(expect
  "<script src=\"js/blah.js\"></script>"
  (->> (@#'slipstream.ui.views.base/bottom-internal-scripts-snip ["blah.js"])
       html/emit*
       (apply str)))

(expect
  "<script src=\"js/foo.js\"></script><script src=\"js/foo/bar.js\"></script>"
  (->> (@#'slipstream.ui.views.base/bottom-internal-scripts-snip ["foo.js" "foo/bar.js"])
       html/emit*
       (apply str)))


;; css-links-snip

(expect
  ""
  (->> (@#'slipstream.ui.views.base/css-links-snip nil)
       html/emit*
       (apply str)))

(expect
  ""
  (->> (@#'slipstream.ui.views.base/css-links-snip [])
       html/emit*
       (apply str)))

(expect
  "<link href=\"css/blah.js\" type=\"text/css\" rel=\"stylesheet\" />"
  (->> (@#'slipstream.ui.views.base/css-links-snip ["blah.js"])
       html/emit*
       (apply str)))

(expect
  "<link href=\"css/foo.js\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"css/foo/bar.js\" type=\"text/css\" rel=\"stylesheet\" />"
  (->> (@#'slipstream.ui.views.base/css-links-snip ["foo.js" "foo/bar.js"])
       html/emit*
       (apply str)))

