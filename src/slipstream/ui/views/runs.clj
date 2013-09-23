(ns slipstream.ui.views.runs
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.common :as module-common]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.module-base :as module-base]))

(def runs-template-html "slipstream/ui/views/runs-template.html")

(def runs-sel [:#runs])
(def runs-fragment-sel [:#fragment-runs-somecloud])

(html/defsnippet runs-for-cloud-snip runs-template-html [runs-fragment-sel :> :table]
  [runs _]
  [:tbody :> :tr] (html/clone-for
                    [run runs
                     :let 
                     [attrs (module-model/attrs run)]]
                    [[:td (html/nth-of-type 1)] :> :i] (html/set-attr :class (module-base/type-to-icon (:type attrs)))
                    [[:td (html/nth-of-type 2)] :> :a] (html/do->
                                                         (html/set-attr :href (str "/" (:resourceuri attrs)))
                                                         (html/content (apply str (take common/take-run-no-of-chars (:uuid attrs)))))
                    [[:td (html/nth-of-type 3)] :> :a] (html/do->
                                                         (html/set-attr :href (str "/" (:moduleresourceuri attrs)))
                                                         (html/content (apply str (drop common/drop-module-slash-no-of-chars (:moduleresourceuri attrs)))))
                    [[:td (html/nth-of-type 4)]] (html/content (:status attrs))
                    [[:td (html/nth-of-type 5)]] (html/content (:starttime attrs))
                    [[:td (html/nth-of-type 6)]] (html/content (:username attrs))
                    [[:td (html/nth-of-type 7)]] (html/content (:tags attrs))))

(html/defsnippet runs-snip runs-template-html runs-sel
  [grouped-by-cloud]
  [:ul :> [:li html/last-of-type]] nil 
  [:ul :> :li] 
  (common/tab-headers grouped-by-cloud "runs")
  [:ul] (if (empty? grouped-by-cloud)
          nil
          identity)

  runs-fragment-sel (if (empty? grouped-by-cloud)
                      (common/emtpy-section "No deployment, run or build currently available")
                      (common/tab-sections grouped-by-cloud "runs" runs-for-cloud-snip))
  [:#fragment-runs-cloudb] nil)

(html/deftemplate runs-template runs-template-html
  [runs]
  runs-sel (html/substitute (runs-snip runs)))

(defn page [runs]
  (runs-template
    (module-common/group-by-key
      :cloudservicename
      (module-common/children runs))))
