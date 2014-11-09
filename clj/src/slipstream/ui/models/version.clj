(ns slipstream.ui.models.version)

(def slipstream-release-version (atom "x.y.z"))

(defn set-release-version
  [version]
  (reset! slipstream-release-version version))
