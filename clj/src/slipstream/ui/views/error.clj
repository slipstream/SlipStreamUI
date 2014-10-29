(ns slipstream.ui.views.error
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn page
  [metadata message code]
  (base/generate
    {:metadata metadata
     :page-title (t :page-title)
     :error-page? true
     :header {:status-code code
              :title (->> (or code 500) (str "header.title.status-code-") keyword t)
              :subtitle (case code
                          (404 500 nil) (str (t :header.subtitle) "<br><br>" message)
                          message)}}))

(defn page-uncaught-exception
  [^Throwable t]
  (page nil (str t) 500))
