(ns slipstream.ui.util.theme
  "Util functions for the UI theme."
  (:require [superstring.core :as s]))

;; NOTE: Currently the theme is used in two parts: localization.clj and base.clj
;;       and the used theme is taken from the System Property 'slipstream.ui.util.theme.current-theme'.
;;       Look at the file SlipStreamServer/war/Makefile for example of how to launch different themes.
;;
;;       Later, the theme will be provided by the server in the metadata.

(def available-themes
  "See 'slipstream.ui.util.localization/available-languages' for documentation."
  #{:helixnebula
    :nuvla})

(def ^:dynamic ^:private *current-theme*
  "NB: If the theme declared in the property is not in the list above, the default
  SlipStream theme will be used. This allows e.g. to setup 'theme=default' to use
  the default theme."
  ; nil
  ; :helixnebula
  (let [theme-property  (System/getProperty "slipstream.ui.util.theme.current-theme")
        theme           (-> theme-property keyword available-themes)]
    (when (not-empty theme-property)
      (print "INFO: The system property 'slipstream.ui.util.theme.current-theme' has the value: ")
      (prn theme-property)
      (when (and (not theme) (not= theme-property "default"))
        (println
          (format "WARN: The requested theme \"%s\" is not available. Must be one of %s or \"default\"."
                  theme-property
                  (->> available-themes (map name) (map pr-str) (s/join ", "))))))
    (println "INFO: Using theme" (-> theme (or "default") name pr-str))
    theme))

(defn current
  []
  *current-theme*)

(def ^:private themes-resources-folder "themes")
(def ^:private static-content-resources-folder "static_content")

(def themable-sel [:.ss-themable])

(defn static-content-folder
  [theme]
  (when theme
    (str themes-resources-folder "/" (name theme) "/")))

(defn resources-folder
  [theme]
  (when theme
    (str static-content-resources-folder "/" (static-content-folder theme))))
