(ns slipstream.ui.views.subsection
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.clojure :as uc]))

(def template-filename (u/template-path-for "subsection.html"))

(def ^:private subsection-selected-cls "active")

(def ^:private activator-group-sel    [:.ss-subsection-activator-group])
(def ^:private activator-sel          [[:li html/first-of-type]])
(def ^:private activator-xs-group-sel [:.ss-subsection-activator-xs-group])
(def ^:private activator-xs-sel       [[:option html/first-of-type]])
(def ^:private content-group-sel      [:.ss-subsection-content-group])
(def ^:private content-sel            [[:div.tab-pane html/first-of-type]])

(defn- activator-group
  [subsections]
  (ue/content-for activator-sel [{:keys [title selected? id]} subsections]
                  ue/this  (ue/enable-class selected? subsection-selected-cls)
                  [:a]    (ue/set-href "#" id)
                  [:a]    (html/content (str title))))

(defn- activator-xs-group
  [subsections]
  (ue/content-for activator-xs-sel [{:keys [title id]} subsections]
                  ue/this  (ue/set-value "#" id)
                  ue/this  (html/content (str title))))

(defn- content-group
  [subsections]
  (ue/content-for content-sel [{:keys [content selected? id]} subsections]
                  ue/this  (ue/enable-class selected? subsection-selected-cls)
                  ue/this  (ue/set-id id)
                  ue/this  (if (string? content)
                            (html/html-content content)
                            (html/content content))))

;; TODO: Refactor the 'subsection-group-snip-*' into one, since the body is identical.

(html/defsnippet subsection-group-snip-flat template-filename [:.ss-subsection-group]
  [subsections]
  activator-group-sel     (activator-group subsections)
  activator-xs-group-sel  (activator-xs-group subsections)
  content-group-sel       (content-group subsections))

(html/defsnippet subsection-group-snip-stacked template-filename [:.ss-subsection-group-stacked]
  [subsections]
  activator-group-sel     (activator-group subsections)
  activator-xs-group-sel  (activator-xs-group subsections)
  content-group-sel       (content-group subsections))

(def ^:private max-num-of-flat-sections 7)

(defn- subsection-group-snip
  [subsections]
  (if (-> subsections count (> max-num-of-flat-sections))
    (subsection-group-snip-stacked subsections)
    (subsection-group-snip-flat subsections)))

(defn- assoc-id
  [subsection-id-prefix subsection]
  (assoc subsection :id (->> subsection :title uc/keywordize name (str subsection-id-prefix "-"))))

(defn build
  [subsections]
  (html/content (subsection-group-snip (->> subsections
                                            vec
                                            u/ensure-one-selected
                                            (map (partial assoc-id (gensym "ss-subsection-")))))))
