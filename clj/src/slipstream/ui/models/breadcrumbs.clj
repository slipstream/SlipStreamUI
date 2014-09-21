(ns slipstream.ui.models.breadcrumbs
  (:require [slipstream.ui.util.clojure :as uc]))

(def ^:private blind-breadcrumb-segments
  #{"module"})

(defn parse
  "Transform a ressource-uri into the the breadcrumbs metadata. Note that if the
  ressource-uri ends with a slash, a last segment {:text \"\", :uri nil} will be
  included, so consider using uc/trim-last-slash on the ressource-uri. See tests
  for expectations."
  [ressource-uri]
  (let [uris (->> ressource-uri
                  (iterate uc/trim-last-path-segment)
                  (take-while not-empty)
                  reverse)]
    (into [] (for [uri uris
                   :let [uri-name (uc/last-path-segment uri)
                         is-inactive? (or (blind-breadcrumb-segments uri-name)
                                        (= uri (last uris)))
                         breadcrumb-base {:text uri-name}]]
               (if is-inactive?
                 breadcrumb-base
                 (assoc breadcrumb-base :uri uri))))))
