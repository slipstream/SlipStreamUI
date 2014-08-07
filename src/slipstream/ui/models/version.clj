(ns slipstream.ui.models.version)

(def slipstream-release-version (atom "1.2-345"))

(defn set-release-version! [version]
  (reset! slipstream-release-version version))
