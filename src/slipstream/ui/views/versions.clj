(ns slipstream.ui.views.versions
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.models.authz :as authz-model]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.module :as module]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.version :as version]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.header :as header-views]
            [slipstream.ui.views.footer :as footer]
            [slipstream.ui.views.project :as project]
            [slipstream.ui.views.common :as common]))

(def versions-sel [:#versions])

(def versions-template-html "slipstream/ui/views/versions.html")

(html/defsnippet header-snip header-views/header-template-html header-views/header-sel
  [versions]
  header-views/titles-sel (html/substitute 
                            (apply header-views/header-titles-snip 
                                   (modules/titles-from-versions versions)))
  header-views/header-top-bar-sel (html/substitute
                               (header-views/header-top-bar-snip
                                 (user/attrs versions))))

(defn first-item
  [versions]
  (first (modules/children versions)))

(html/defsnippet items-snip versions-template-html [versions-sel :> :table]
  [versions]
  [:tbody :> [:tr html/last-of-type]] nil
  [:tbody :> [:tr html/first-of-type]] 
    (html/clone-for 
      [child (sort-by
               #(read-string
                  (:version (common-model/attrs %)))
               (modules/children versions))]
      [[:a]] (html/do->
               (html/set-attr :href (str "/" (:resourceuri (common-model/attrs child))))
               (html/content (:version (common-model/attrs child))))
      [[:td (html/nth-of-type 2)]] (html/content (module/item-comment child))
      [[:td (html/nth-of-type 3)]] (html/content (user/username versions))
      [[:td (html/nth-of-type 4)]] (html/content (:lastmodified (common-model/attrs child)))))

(defn sanitize-module-name
  "Drop the leading 'module/' and the version number at the end"
  [module-name]
  (apply str 
         (drop 7 
               (string/join "/" 
                            (drop-last (string/split module-name #"/"))))))

(html/defsnippet content-snip versions-template-html common/content-sel
  [versions]
  common/breadcrumb-sel
  (module-base/breadcrumb
    (sanitize-module-name 
      (:resourceuri
        (common-model/attrs
          (first-item versions)))))

  versions-sel (html/substitute (items-snip versions)))

(defn title
  [versions]
   (:name (common-model/attrs (first-item versions))))

(defn page [versions type]
  (base/base 
    {;:js-scripts ["/js/welcome.js"]
     :title (title versions)
     :header (module-base/header versions type header-snip)
     :content (content-snip versions)
     :footer (module-base/footer type)}))
