(ns slipstream.ui.util.model)

(def ^:private CIMI-common-attributes
  [:id, :resourceURI, :created, :updated, :acl, :operations])

(defn dissoc-CIMI
  "Dissoc CIMI attributes from given map"
  [m]
  (apply dissoc m CIMI-common-attributes))
