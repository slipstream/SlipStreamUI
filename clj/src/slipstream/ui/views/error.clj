(ns slipstream.ui.views.error
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

(defn page
  [metadata message code]
  (localization/with-lang-from-metadata
    (base/generate
      {:metadata metadata
       :page-title (t :page-title)
       :error-page? true
       :header {:status-code code
                :title nil
                :subtitle message}})))
