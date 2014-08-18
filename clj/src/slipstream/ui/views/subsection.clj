(ns slipstream.ui.views.subsection
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.views.utils :as u]
            [slipstream.ui.views.common :as common]))

(def ^:private template-filename (common/get-template "subsection.html"))

(def ^:private subsection-selected-cls "active")

(def ^:private stacked-sections? false)

(def ^:private subsection-group-sel
  (if stacked-sections?
    [:.ss-subsection-group-stacked]
    [:.ss-subsection-group]))

(def ^:private activator-group-sel    [:.ss-subsection-activator-group])
(def ^:private activator-sel          [[:li html/first-of-type]])
(def ^:private activator-xs-group-sel [:.ss-subsection-activator-xs-group])
(def ^:private activator-xs-sel       [[:option html/first-of-type]])
(def ^:private content-group-sel      [:.ss-subsection-content-group])
(def ^:private content-sel            [[:div.tab-pane html/first-of-type]])

(defn- activator-group
  [subsections]
  (u/content-for activator-sel [{:keys [title selected? id]} subsections]
                  u/this  (u/enable-class selected? subsection-selected-cls)
                  [:a]    (u/set-href "#" id)
                  [:a]    (html/content (str title))))

(defn- activator-xs-group
  [subsections]
  (u/content-for activator-xs-sel [{:keys [title id]} subsections]
                  u/this  (u/set-value "#" id)
                  u/this  (html/content (str title))))

(defn- content-group
  [subsections]
  (u/content-for content-sel [{:keys [content selected? id]} subsections]
                  u/this  (u/enable-class selected? subsection-selected-cls)
                  u/this  (u/set-id id)
                  u/this  (html/content content)))

(html/defsnippet subsection-group-snip template-filename subsection-group-sel
  [subsections]
  activator-group-sel     (activator-group subsections)
  activator-xs-group-sel  (activator-xs-group subsections)
  content-group-sel       (content-group subsections))

(defn- assoc-id
  [subsection]
  (assoc subsection :id (gensym "ss-subsection")))

(defn build
  [subsections]
  (html/content (subsection-group-snip (map assoc-id subsections))))
