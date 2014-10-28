(ns slipstream.ui.views.header
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]))

(def status-code-sel [:.ss-header-error-status-code])
(def title-sel [:.ss-header-title])
(def subtitle-sel [:.ss-header-subtitle])
(def title-col-sel [:.ss-header-title-col])
(def icon-sel [:span.ss-header-icon])
(def image-preloader-sel [:img.ss-image-preloader])
(def image-col-sel [:.ss-header-image-col])

(defn transform
  [{:keys [status-code title subtitle icon image] :as header}]
  (fn [match]
    (html/at match
             icon-sel  (icons/set icon)
             status-code-sel      (ue/content-when-not-nil status-code)
             title-sel            (ue/content-when-not-nil title)
             subtitle-sel         (ue/html-content-when-not-nil subtitle)
             title-col-sel        (ue/when-replace-class (not image)
                                                        "col-sm-8"
                                                        "col-sm-11")
             image-preloader-sel  (ue/set-src image)
             image-col-sel        (ue/remove-if-not image))))
