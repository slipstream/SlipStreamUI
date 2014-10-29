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

(defn- expection-pseudo-hash
  "Returns a number (as a string) to help identifying exceptions. It is builded
  using the last line number of each stack trace element, from down to top, preceded
  by the count of the stack trace elements. Is not ensured to be unique for each
  exception, but close enough for purposes of support and debugging."
  [^Throwable t]
  (let [stack-trace-elements (.getStackTrace t)]
    (->> stack-trace-elements
         (into (list))
         (map str)
         (map #(re-matches #".*:(\d+)\).*" %))  ; get line numbers
         (map second)
         (map last)                             ; get last digit of line number
         reverse
         (take 20)
         (apply str "Exception id: "(count stack-trace-elements) "x"))))

(defn page-uncaught-exception
  [^Throwable t]
  (page nil (str t "<br><br>" (expection-pseudo-hash t)) 500))
