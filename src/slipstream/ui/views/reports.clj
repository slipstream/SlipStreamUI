(ns slipstream.ui.views.reports
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.base :as base]))

(def reports-template-html "slipstream/ui/views/reports.html")

(def reports-sel [:#reports])

(defn extract-vm-name-and-time
  [file-name]
  (clojure.string/replace-first
    (clojure.string/replace-first (str file-name) #".tgz" "")
    #"_report_" "  "))

(defn strip-domain
  [url]
  (let [reports "/reports/"]
    (str reports (last (clojure.string/split url (re-pattern reports))))))

(defn insert-reports
  [reports]
  (let
    [as (filter #(not= ".." (common-model/content %)) (html/select reports [:a]))]
    (if (empty? as)
      (html/content "No reports available yet")
      (html/clone-for
        [a as]
        [:a] (html/do->
               (html/set-attr :href (strip-domain (:href (common-model/attrs a))))
               (html/content (extract-vm-name-and-time (common-model/content a))))))))


(html/defsnippet content-snip reports-template-html common/content-sel
  [reports]
  [reports-sel :> :ul :> [:li html/last-of-type]] nil
  [reports-sel :> :ul :> :li]
  (insert-reports reports))

(defn page [reports]
  (base/base
    {:title (common/title "Reports")
     :header nil
     :content (content-snip reports)
     :footer nil}))
