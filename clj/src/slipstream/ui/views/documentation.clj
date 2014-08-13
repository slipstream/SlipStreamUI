(ns slipstream.ui.views.documentation
  (:require [slipstream.ui.views.base :as base]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.tables :as t]))

(def ^:private docs
  [{:title "User Guide and Tutorial"  :basename "tutorial"}
   {:title "Administrator Manual"     :basename "administrator-manual"}
   {:title "Terms of Service"         :basename "terms-of-service"}])

(defn page [metadata]
  (base/generate
    {:metadata metadata
     :header {:icon icons/documentation
              :title "Documentation"
              :subtitle "SlipStream technical documentation at a glance"}
     :resource-uri "/documentation"
     :content [{:title "Documentation"
                :content (t/docs-table docs)}]}))
