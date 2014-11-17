(ns slipstream.ui.views.documentation
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]))

(localization/def-scoped-t)

(defn- docs
  []
  [{:title (t :tutorial.title)              :basename "tutorial"}
   {:title (t :administrator-manual.title)  :basename "administrator-manual"}
   {:title (t :terms-of-service.title)      :basename "terms-of-service"}])

(defn page
  [metadata]
  (base/generate
    {:header {:icon icons/documentation
              :title (t :header.title)
              :subtitle (t :header.subtitle)}
     :resource-uri "/documentation"
     :content [{:title (t :content.title)
                :content (t/docs-table (docs))}]}))
