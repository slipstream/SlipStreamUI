(ns slipstream.ui.tour.core
  "Util functions for the UI onboarding tour.
  Currently using bootstro.js - https://clu3.github.io/bootstro.js/#"
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.exception :as ex]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.current-user :as current-user]
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

(def ^:private bootstro-info-fields
  #{:title
    :content
    :placement
    :placement-distance
    :width
    :nextButtonText
    :count
    :offset
    :step
    :hide-prev-button
    :hide-next-button})

(defmacro  ^:private process-step-node
  [step-info]
  `(html/do->
    (ue/when-wrap     (-> ~step-info :wrap-in-elem not-empty))
    (html/add-class   "bootstro")
    ~@(for [field bootstro-info-fields]
        `(set-bootstro ~field ~step-info))
    (ue/set-data      :html                true)
    (ue/enable-class  (:preserve-padding ~step-info)  "preserve-padding")
    (ue/when-set-data :container             (:container-sel ~step-info))))

(ue/def-blank-snippet ^:private orphan-step-placeholder-snip :div
  [step-info]
  ue/this   (process-step-node (assoc step-info :container "body"))
  ue/this   (ue/set-data      :bootstro-orphan-step true)
  ue/this   (html/add-class   "bootstro-orphan-step-placeholder"))

(defn- add
  "See tests for expectations."
  [tour-scenes]
  (fn [match]
    (let [tour-scenes-indexed (add-step-index tour-scenes)]
      ; For each of the scenes, we apply an enlive transformation
      (loop [m                match
             [sel step-info]  (first tour-scenes-indexed)
             next-scenes       (next tour-scenes-indexed)]
        (let [orphan-step? (nil? sel)
              sel-v        (-> sel (or :body) uc/ensure-vector)
              updated-node (if orphan-step?
                             (html/transform (html/as-nodes m)
                                sel-v  (html/prepend (orphan-step-placeholder-snip step-info)))
                             (html/transform (html/as-nodes m)
                                sel-v (process-step-node step-info)))]
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

(defn- tour-in-query-param
  [context]
  (some-> context :metadata meta :request :query-parameters :tour not-empty))

(defmulti ^:private get-tour-name
  (fn [context] [(:view-name context) (page-type/current)]))

(defmethod get-tour-name ["appstore" :page-type/view]
  [context]
  (if-let [tour (tour-in-query-param context)]
    tour
    (if (current-user/configuration :configured-clouds)
      "alice.intro.welcome"
      "alice.intro-without-connectors.go-to-profile")))

(defmethod get-tour-name ["user" :page-type/view]
  [context]
  "alice.intro-without-connectors.navigate-back-to-welcome")

(defmethod get-tour-name ["run" :page-type/any]
  [context]
  ;; TODO: Determine which tour to launch: if the 'intro' version or the 'intro-without-connectors' version.
  ;;       The step content will be the same in both cases, but the total number of steps of the whole tour
  ;;       is different, so that the numbering of the steps and the progress bar will differ.
  (or
    (tour-in-query-param context)
    "alice.intro-without-connectors.waiting-for-wordpress"))
  ; "alice.intro.waiting-for-wordpress")

(defmethod get-tour-name :default
  [context]
  (tour-in-query-param context))

(defn- extract-coordinates
  [tour-name]
   (some->> tour-name (re-matches  #"([^.]+)\.([^.]+)\.([^.]+)")))

(defn- call
  ;; NOTE: Middle fn to inverse the order of the arguments in the ->> usage below.
  [args tour-fn-var]
  (tour-fn-var args))

(defn- acts
  [context persona play]
  (ex/guard "get the map with the acts for the 'play' for the given 'persona'"
    (->> play
        (symbol (str "slipstream.ui.tour." persona))
        resolve
        var-get
        (call context)
        (partition 2)
        (map vec))))

(defn- count-scenes
  [acts]
  (->> acts
       (mapcat second)
       (partition 2)
       count))

(defn- scenes-count-offset
  [acts act-keyword]
  (->> acts
       (take-while (comp not #{act-keyword} first))
       count-scenes))

(defn- inject-scenes-count
  [scenes-count scenes-count-offset sel-or-step-info]
  (if (map? sel-or-step-info)
    (assoc sel-or-step-info
      :count  scenes-count
      :offset scenes-count-offset)
    sel-or-step-info))

(defn- scenes
  [context [_ persona play act]]
  (ex/guard "get scenes"
    (let [acts (into {} (acts context persona play))
          act-keyword (keyword act)
          scenes-count (count-scenes acts)
          scenes-count-offset (scenes-count-offset acts act-keyword)]
      (->> act-keyword
           (get acts)
           (map (partial inject-scenes-count scenes-count scenes-count-offset))))))

(defn- js-filenames-aliases
  [js-filename]
  (case js-filename
    "tours/alice-intro-without-connectors-welcome.js"                 "tours/alice-intro-welcome.js"
    "tours/alice-intro-without-connectors-deploying-wordpress.js"     "tours/alice-intro-deploying-wordpress.js"
    "tours/alice-intro-without-connectors-waiting-for-wordpress.js"   "tours/alice-intro-waiting-for-wordpress.js"
    "tours/alice-intro-without-connectors-wordpress-in-dashboard.js"  "tours/alice-intro-wordpress-in-dashboard.js"
    "tours/alice-intro-without-connectors-wordpress-running.js"       "tours/alice-intro-wordpress-running.js"
    js-filename))

(defn- js-files
  [[_ persona play act]]
  (-> (format "tours/%s-%s-%s.js" persona play act)
      js-filenames-aliases
      vector))

(defn- help-menu-action?
  [{:keys [view-name]}]
  (= view-name "welcome"))

(defn tour
  "Returns the tour scenes and necessary JS and CSS files for the given context."
  [context]
  (ex/quietly-guard "get the tour for the given 'context'"
    (when-let [[tour-name :as coordinates] (some-> context get-tour-name extract-coordinates)]
      {:name      tour-name
       :scenes    (scenes context coordinates)
       :js-files  (js-files coordinates)
       :css-files ["tour.css"]
       :help-menu-action? (help-menu-action? context)})))
