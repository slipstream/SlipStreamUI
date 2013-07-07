(ns slipstream.ui.views.reports
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.base :as base]))

(def reports-template-html "slipstream/ui/views/reports.html")

(def reports-sel [:#reports])

(defn extract-vm-name
  [file-name]
  (first (clojure.string/split (str file-name) #"_report")))

(html/defsnippet content-snip reports-template-html common/content-sel
  [reports]
  [reports-sel :> :ul :> [:li html/last-of-type]] nil
  [reports-sel :> :ul :> :li]
  (html/clone-for
    [report-entry (html/select reports [:a]) :when (not= ".." (common-model/content report-entry))]
    [:a] (html/do->
           (html/set-attr :href (:href (common-model/attrs report-entry)))
           (html/content (extract-vm-name (common-model/content report-entry))))))

(defn page [reports]
  (base/base 
    {:title (common/title "Reports")
     :header nil
     :content (content-snip reports)
     :footer nil}))
