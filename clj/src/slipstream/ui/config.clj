(ns slipstream.ui.config
  (:require [superstring.core :as string]
            [slipstream.common.reload :as reload])
  (:gen-class
  :name slipstream.ui.Config
  :methods [#^{:static true
               :doc "Set alternative namespace for HTML template. Must be slash separated."}
              [setHtmlTemplateNamespace [String] String]]))

;; This module must not be in a separate module as ui.views.representation, since it must
;; be loaded before the ui.views modules, which load and cache the enlive templates.

(def template-namespace (atom "slipstream/ui/views"))

(defn- trim-leading-trailing-double
  "Remove leading, trailing and double char c"
  [s c]
  (string/join c (filter not-empty (string/split s (re-pattern c)))))

(defn trim-slashes
  "Remove leading, trailing and double slashes"
  [s]
  (trim-leading-trailing-double s "/"))

(defn set-template!
  [ns]
  (let [template (trim-slashes ns)]
    (reset! template-namespace template)))

(defn -setHtmlTemplateNamespace
  "Set alternative namespace for HTML template. Must be slash separated."
  [ns]
  (set-template! ns)
  @template-namespace)
