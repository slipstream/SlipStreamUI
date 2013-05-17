(ns slipstream.ui.views.representation
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.welcome :as welcome]
            [slipstream.ui.views.knockknock :as knockknock]
            [slipstream.ui.views.byebye :as byebye]
            [slipstream.ui.views.module :as module])
  (:gen-class
    :name slipstream.ui.views.Representation
    :methods [#^{:static true} [tohtml [String String String] String]]))

(defn- xml-string-to-map [metadata]
  (first (html/html-snippet metadata)))

(defn render [t]
  (apply str t))

(defmulti gen-page 
  (fn [metadata pagename type]
    pagename))

(defmethod gen-page "welcome"
  [metadata pagename type]
    (render (welcome/page (xml-string-to-map metadata))))

(defmethod gen-page "login"
  [metadata pagename type]
    (render (knockknock/page (xml-string-to-map metadata))))

(defmethod gen-page "logout"
  [metadata pagename type]
    (render (byebye/page (xml-string-to-map metadata))))

(defmethod gen-page "module"
  [metadata pagename type]
    (render (module/page (xml-string-to-map metadata) type)))

(defn -tohtml
  "Generate an HTML page from the metadata xml string"
  [metadata pagename type]
  (println (str "Calling connector"))
  (gen-page metadata pagename type))
