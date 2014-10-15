(ns slipstream.ui.models.breadcrumbs
  (:require [slipstream.ui.util.clojure :as uc]))

(def ^:private alias-uri-segments
  {"module" nil
   "run"    "dashboard"})

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
    (remove nil?
      (into [] (for [uri uris
                       :let [uri-name (uc/last-path-segment uri)
                             last-segment?  (= uri (last uris))]]
                   (if-not (contains? alias-uri-segments uri-name)
                     {:text uri-name, :uri (when-not last-segment? uri)}
                     (when-let [alias-uri (get alias-uri-segments uri-name)]
                       {:text alias-uri, :uri alias-uri})))))))
