(ns slipstream.ui.views.documentation
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]
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

(defn page [user]
  (base/generate
    {:metadata user
     :placeholder-page? true
     :header {:icon :book
              :title "Documentation"
              :subtitle "SlipStream technical documentation at a glance"}
     :content nil}))
