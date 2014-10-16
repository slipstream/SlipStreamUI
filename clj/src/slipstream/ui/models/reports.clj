(ns slipstream.ui.models.reports)

(defn- strip-domain
  [url]
  (if-let [[_ relative-uri] (re-matches #".*(/reports/.*)" url)]
    relative-uri
    url))

(defn- assoc-relative-uri
  [{:keys [uri] :as m}]
  (assoc m :relative-uri (strip-domain uri)))

(defn parse
  [metadata]
  (->> metadata
       :files
       (map assoc-relative-uri)))
