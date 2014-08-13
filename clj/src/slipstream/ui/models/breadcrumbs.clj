(ns slipstream.ui.models.breadcrumbs
  (:require [slipstream.ui.views.utils :as u]))

(def ^:private blind-breadcrumb-segments
  #{"module"})

(defn parse
  "Transform a ressource-uri into the the breadcrumbs metadata.
  E.g. From this uri 'module/examples/tutorials/wordpress/wordpress/180'
    it will build this breadcrumbs:
    [{:text 'module'}
     {:text 'examples' :uri 'module/examples'}
     {:text 'tutorials' :uri 'module/examples/tutorials'}
     {:text 'wordpress' :uri 'module/examples/tutorials/wordpress'}
     {:text 'wordpress' :uri 'module/examples/tutorials/wordpress/wordpress'}
     {:text '180'}]
  Note that if the ressource-uri ends with a slash, a last segment {:text \"\", :uri nil}
  will be included, so consider using u/trim-last-slash on the ressource-uri."
  [ressource-uri]
  (let [uris (->> ressource-uri
                  (iterate u/trim-last-path-segment)
                  (take-while not-empty)
                  reverse)]
    (into [] (for [uri uris
                   :let [uri-name (u/get-last-path-segment uri)
                         is-inactive? (or (blind-breadcrumb-segments uri-name)
                                        (= uri (last uris)))
                         breadcrumb-base {:text uri-name}]]
               (if is-inactive?
                 breadcrumb-base
                 (assoc breadcrumb-base :uri uri))))))
