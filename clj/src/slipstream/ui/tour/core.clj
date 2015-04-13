(ns slipstream.ui.tour.core
  "Util functions for the UI onboarding tour.
  Currently using bootstro.js - https://clu3.github.io/bootstro.js/#"
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.exception :as ex]
            [slipstream.ui.tour.alice :as alice]))

(defn- add-step-index
  [tour-scenes]
  (->> tour-scenes
       (partition 2)
       (map-indexed #(assoc-in (vec %2) [1 :step] %1))
       not-empty))

;; NOTE: Bootstro's data fields:
;;
;;    data-bootstro-title           Title of the popover [optional, defaulted to '']
;;    data-bootstro-content         Content of the popover [optional, defaulted to '']
;;    data-bootstro-placement       Placement position [top,bottom,left,right] of the popover [optional, defaulted to 'top']
;;    data-bootstro-width           If you like to bypass bootstrap's default popover un-settable width, specify it like data-bootstro-width='500px'. Don't forget the 'px' suffix [optional]
;;    data-bootstro-nextButtonText  Next button HTML text. For example, in stead of "Next", you can specify
;;    data-bootstro-html            true|false . Whether or not the popover content will be displayed as HTML or text. Refer to popovers html option.
;;    data-bootstro-step            The stack index of the intro'ed popover.

(defn- set-bootstro
  [bootstro-data-key step-info]
  {:pre (keyword? bootstro-data-key)}
  (ue/when-set-data (->> bootstro-data-key
                         name
                         (str "bootstro-"))
                    (-> step-info
                        bootstro-data-key
                        str)))


(defn- add
  "See tests for expectations."
  [tour-scenes]
  (fn [match]
    (let [tour-scenes-indexed (add-step-index tour-scenes)]
      ; For each of the scenes, we apply an enlive transformation
      (loop [m                match
             [sel step-info]  (first tour-scenes-indexed)
             next-scenes       (next tour-scenes-indexed)]
        (let [sel-v        (uc/ensure-vector sel)
              updated-node (html/transform (html/as-nodes m)
                              sel-v  (html/do->
                                       (html/add-class  "bootstro")
                                       (set-bootstro    :title          step-info)
                                       (set-bootstro    :content        step-info)
                                       (set-bootstro    :placement      step-info)
                                       (set-bootstro    :width          step-info)
                                       (set-bootstro    :nextButtonText step-info)
                                       (set-bootstro    :html           {:html true})
                                       (set-bootstro    :step           step-info)))]
          (if next-scenes
            (recur updated-node
                   (first next-scenes)
                   (next next-scenes))
            updated-node))))))

(defn when-add
  "Apply the transformations for the tour, if there is one. If not, don't change
  the matched nodes."
  [tour]
  (if-let [tour-scenes (-> tour :scenes not-empty)]
    (add tour-scenes)
    identity))

; (defn e [x]
;   (println)
;   (println ">>>>>>>>")
;   (prn x)
;   (println ">>>>>>>>")
;   (println)
;   x)

(defn- get-tour-name
  [context]
  (let [options (-> context :metadata meta)]
    (ex/guard "get the tour name"
      (case (:view-name context)
        "welcome"       "alice.intro.welcome"
        ; "module"       "alice.intro.deploying-wordpress"
        (-> options :request :query-parameters :tour not-empty)))))

(defn- extract-coordinates
  [tour-name]
   (some->> tour-name (re-matches  #"([^.]+)\.([^.]+)\.([^.]+)")))

(defn- acts
  [persona play]
  (ex/guard "get the map with the acts for the 'play' for the given 'persona'"
    (-> (str "slipstream.ui.tour." persona)
        (symbol play)
        resolve
        var-get)))

(defn- scenes
  [[_ persona play act]]
  (get (acts persona play) (keyword act)))

(defn- js-files
  [[_ persona play act]]
  [(format "tours/%s-%s-%s.js" persona play act)])


(defn tour
  "Returns the tour scenes and necessary JS and CSS files for the given context."
  [context]
  (ex/quietly-guard "get the tour for the given 'context'"
    (when-let [[tour-name :as coordinates] (some-> context get-tour-name extract-coordinates)]
      {:name      tour-name
       :scenes    (scenes coordinates)
       :js-files  (js-files coordinates)
       :css-files ["tour.css"]})))
