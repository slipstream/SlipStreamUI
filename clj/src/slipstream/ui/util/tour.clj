(ns slipstream.ui.util.tour
  "Util functions for the UI onboarding tour.
  Currently using bootstro.js - https://clu3.github.io/bootstro.js/#"
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.clojure :as uc]))

(defn- add-step-index
  [tour-steps]
  (->> tour-steps
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


(defn add
  "See tests for expectations."
  [tour-steps]
  (fn [match]
    (if-let [tour-steps-indexed (add-step-index tour-steps)]
      ; For each of the steps, we apply an enlive transformation
      (loop [m                match
             [sel step-info]  (first tour-steps-indexed)
             next-steps       (next tour-steps-indexed)]
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
          (if next-steps
            (recur updated-node
                   (first next-steps)
                   (next next-steps))
            updated-node)))
      ; If no steps are defined, don't change the matched nodes.
      identity)))