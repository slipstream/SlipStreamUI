(ns slipstream.ui.views.versions
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [slipstream.ui.models.common :as common-models]
            [slipstream.ui.models.modules :as modules]
            [slipstream.ui.models.module :as module-models]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.views.base :as base]
            [slipstream.ui.views.module-base :as module-base]
            [slipstream.ui.views.module :as module]
            [slipstream.ui.views.header :as header-views]
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
                  (:version (common-models/attrs %)))
               (modules/children versions))]
      [[:a]] (html/do->
               (html/set-attr :href (str "/" (:resourceuri (common-models/attrs child))))
               (html/content (:version (common-models/attrs child))))
      [[:td (html/nth-of-type 2)]] (html/content (module-models/module-commit-comment child))
      [[:td (html/nth-of-type 3)]] (html/content (module-models/module-commit-author child))
      [[:td (html/nth-of-type 4)]] (html/content (:lastmodified (common-models/attrs child)))))

(defn sanitize-module-name
  "Drop the leading 'module-models/' and the version number at the end"
  [module-name]
  (apply str 
         (drop common/drop-module-slash-no-of-chars 
               (string/join "/" 
                            (drop-last (string/split module-name #"/"))))))

(html/defsnippet content-snip versions-template-html common/content-sel
  [versions]
  common/breadcrumb-sel
  (module-base/breadcrumb
    (sanitize-module-name 
      (:resourceuri
        (common-models/attrs
          (first-item versions)))))

  versions-sel (html/substitute (items-snip versions)))

(defn title
  [versions]
   (:name (common-models/attrs (first-item versions))))

(defmulti js-scripts
  (fn [type]
    [type]))

(defmethod js-scripts ["chooser"]
  [type]
  (module/js-scripts-chooser))

(defmethod js-scripts :default
  [type]
  [])

(defn page [versions type]
  (base/base 
    {:js-scripts (js-scripts type)
     :title (common/title "Versions")
     :header (module-base/header versions type header-snip)
     :content (content-snip versions)
     :footer (module-base/footer type)}))
