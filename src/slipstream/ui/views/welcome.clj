(ns slipstream.ui.views.welcome
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.authz :as authz]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.header :as header]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.models.version :as version]))

(def welcome-template-html "slipstream/ui/views/welcome.html")

(html/defsnippet welcome-snip welcome-template-html common/content-sel
  [projects]
  [:#root_projects :> :table :> :tbody :> [:tr (html/nth-of-type 1)]] (html/clone-for 
                    [module (modules/modules projects)]
                    [[:a]] (html/do->
                             (html/set-attr :href (:resourceuri (module/attrs module)))
                             (html/content (:name (module/attrs module))))
                    [[:td (html/nth-of-type 2)]] (html/content (:description (module/attrs module)))
                    [[:td (html/nth-of-type 3)]] (html/content (:owner (authz/attrs module)))
                    [[:td (html/nth-of-type 4)]] (html/content (:version (module/attrs module))))
  [:#root_projects :> :table :> :tbody :> [:tr (html/nth-last-of-type 1)]] nil)

(html/defsnippet header-titles-snip welcome-template-html header/titles-sel
  [metadata]
  header/titles-sel (html/substitute
                      (header/header-titles-snip
                        (header/titles metadata))))

(html/defsnippet header-titles-snip welcome-template-html header/titles-sel
  [{title :title title-sub :title-sub title-desc :title-desc}]
  header/titles-sel identity)

(html/defsnippet header-snip welcome-template-html header/header-sel
  [metadata]
  header/header-sel (html/content (header/header-snip metadata))
  header/titles-sel (html/substitute (header-titles-snip metadata)))

(html/deftemplate base welcome-template-html
  [{:keys [title header content footer]}]
  [:#title]  (html/content title)
  header/header-sel (html/content header)
  common/content-sel (html/content content)
  footer/footer-sel (html/content footer)
)

(defn page [metadata]
  (base 
    {:title (common/title "Welcome")
     :header (header-snip metadata)
     :content (welcome-snip metadata)
     :footer (footer/footer-snip @version/slipstream-release-version)}))
