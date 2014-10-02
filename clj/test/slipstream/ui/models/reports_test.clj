(ns slipstream.ui.models.reports-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.reports :as model]))

(def raw-metadata
  "<ul>
    <li>
      <a href='parentDir'>..</a>
    </li>
    <li>
      <a href='report1.tgz'>report1.tgz</a>
    </li>
    <li>
      <a href='report2.tgz'>report2.tgz</a>
    </li>
  </ul>")

(expect
  "parsed-metadata"
  (-> raw-metadata u/clojurify-raw-metadata model/parse))