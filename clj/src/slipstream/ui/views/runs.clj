(ns slipstream.ui.views.runs
  (:require [slipstream.ui.util.localization :as localization]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.run :as run-model]
            [slipstream.ui.views.header :as header]
                        [slipstream.ui.views.common :as common]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.run :as run]
            [slipstream.ui.views.module-base :as module-base]))

(def runs-template-html (common/get-template "runs-template.html"))

(def runs-sel [:#runs])
(def runs-fragment-sel [:#fragment-runs-somecloud])

(html/defsnippet header-snip header/header-template-html header/header-sel
  [runs]
  header/header-summary-sel
  (html/substitute
    (header/header-titles-snip
      "Runs"
      "View current runs"
      "This page provides you with an overview of the runs on each cloud you have access to"
      "Runs"))
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user-model/attrs runs))))

(html/defsnippet runs-for-cloud-snip runs-template-html [runs-fragment-sel :> :table]
  [runs _]
  [:tbody :> :tr] (html/clone-for
                    [run runs
                     :let
                     [attrs (module-model/attrs run)]]
                    [[:td (html/nth-of-type 1)] :> :i] (html/set-attr :class (common/type-to-icon-class (:type attrs)))
                    [[:td (html/nth-of-type 2)] :> :a] (html/do->
                                                         (html/set-attr :href (str "/" (:resourceuri attrs)))
                                                         (html/content (:uuid attrs)))
                                                         ; (html/content (run/shorten-runid (:uuid attrs))))
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

(defn js-scripts
  []
  )

(defn page-legacy [runs]
  (base/base
    {:js-scripts (js-scripts)
     :title (common/title "Runs")
     :header (header-snip runs)
     :content (runs-snip
                runs)}))
                ; (run-model/group-by-cloud runs))}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn page
  [metadata]
  (localization/with-lang-from-metadata
    (base/generate
      {:metadata metadata
       :placeholder-page? true
       :header {:icon icons/run
                :title "Runs"
                :subtitle "View current runs"}
       ; :resource-uri "/run/91aa79a"
       ; :secondary-menu-actions [action/terminate]
       :content nil})))