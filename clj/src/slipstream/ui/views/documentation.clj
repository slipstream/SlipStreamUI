(ns slipstream.ui.views.documentation
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.util.icons :as icons]
            [slipstream.ui.views.tables :as t]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.user :as user-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header]
                        [slipstream.ui.views.base :as base]))

(def documentation-template-html (common/get-template "documentation.html"))

(html/defsnippet header-snip documentation-template-html header/header-sel
  [user]
  header/header-top-bar-sel (html/substitute
                              (header/header-top-bar-snip
                                (user-model/attrs user))))

(html/defsnippet content-snip documentation-template-html common/content-sel
  []
  common/content-sel identity)

(defn page-legacy
  [user]
  (base/base 
    {:title (common/title "Documentation")
     :header (header-snip (user-model/user user))
     :content (content-snip)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
     :breadcrumbs [{:text "Documentation"}]
     :content [{:title "Documentation"
                :content (t/docs-table docs)}]}))
