(ns slipstream.ui.views.usages
  (:require [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.models.usages :as usages]))

(localization/def-scoped-t)

(defn- section []
       [{:content "<div id=\"usage\"></div>"
        :type    :flat-section}])

(defn page
      [metadata]
      (base/generate
        {:html-dependencies {:css-filenames         ["semantic-fix-conflicts.css" "semantic.min.css" "react-datepicker.min.css"]
                             :internal-js-filenames ["webui.js" "webui_init.js"]}
         :header            {:icon     icons/usage
                             :title    (t :header.title)
                             :subtitle (t :header.subtitle)}
         :content           (section)}))
