(ns slipstream.ui.models.breadcrumbs
  (:require [clojure.string :as s]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.current-user :as current-user]))

(defn- superfluous-breadcrumb
  [breadcrumb]
  (case (:text breadcrumb)
    "module" true
    "user"   (current-user/not-super?)
    false))

(defn- format-fn
  [uri-name]
  (case uri-name
    "run" #(s/replace % "run" "dashboard")
    identity))

(defn- parse-uri
  [uri]
  (let [uri-name (uc/last-path-segment uri)
        f (format-fn uri-name)]
    {:text (f uri-name), :uri (f uri)}))

(defn- disable-last-path-segment-link
  [breadcrumbs]
  (assoc-in breadcrumbs [0 :uri] nil))

(defn parse
  "Transform a ressource-uri into the the breadcrumbs metadata. Note that if the
  ressource-uri ends with a slash, a last segment {:text \"\", :uri nil} will be
  included, so consider using uc/trim-last-slash on the ressource-uri. See tests
  for expectations."
  [ressource-uri]
  (some->> ressource-uri
           (iterate uc/trim-last-path-segment)
           (take-while not-empty)
           (mapv parse-uri)
           disable-last-path-segment-link
           (remove superfluous-breadcrumb)
           reverse))
