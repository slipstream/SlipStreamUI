(ns slipstream.ui.util.theme
  "Util functions for the UI theme.")

;; NOTE: Currently the theme is used in two parts: localization.clj and base.clj
;;       and the used theme is taken from the System Property 'slipstream.ui.util.theme.current-theme'.
;;       Look at the file SlipStreamServer/war/Makefile for example of how to launch different themes.
;;
;;       Later, the theme will be provided by the server in the metadata.

(def ^:dynamic ^:private *current-theme*
  ; nil
  ; "helixnebula"
  (System/getProperty "slipstream.ui.util.theme.current-theme")
  )

(defn current
  []
  *current-theme*)

(def ^:private themes-resources-folder "themes")
(def ^:private static-content-resources-folder "static_content")

(def themable-sel [:.ss-themable])

(defn static-content-folder
  [theme]
  (when (not-empty theme)
    (str themes-resources-folder "/" (name theme) "/")))

(defn resources-folder
  [theme]
  (when (not-empty theme)
    (str static-content-resources-folder "/" (static-content-folder theme))))
