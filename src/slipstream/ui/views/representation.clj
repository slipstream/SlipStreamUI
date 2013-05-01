(ns slipstream.ui.views.representation
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.welcome :as welcome])
  (:gen-class
    :name slipstream.ui.views.representation
    :methods [#^{:static true} [tohtml [String String] String]]))

(defn- xml-string-to-map [metadata]
  (first (html/html-snippet metadata)))

(defn render [t]
  (apply str t))

(defn welcome-page [metadata]
  (welcome/page (xml-string-to-map metadata)))

(defn gen-page [metadata pagename]
  (render (welcome-page metadata)))

(defn tohtml
  "Generate an HTML page from the metadata xml string"
  [metadata pagename]
  (println (str "Calling connector"))
  (gen-page metadata pagename))

(defn -tohtml [metadata pagename]
  (gen-page metadata pagename))

