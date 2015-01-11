(ns slipstream.ui.views.header
  "The header is the top section of each page, just below the top menubar and
  above the secondary menu bar (with the breadcrumbs and the secondary menu). It
  contains the title and subtitle of the page, and optional icon and image."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.icons :as icons]
            [slipstream.ui.util.localization :as localization]))

(def status-code-sel      [:.ss-header-error-status-code])
(def title-sel            [:.ss-header-title])
(def subtitle-sel         [:.ss-header-subtitle])
(def second-subtitle-sel  [:.ss-header-second-subtitle])
(def title-col-sel        [:.ss-header-title-col])
(def icon-sel             [:span.ss-header-icon])
(def image-preloader-sel  [:img.ss-image-preloader])
(def image-col-sel        [:.ss-header-image-col])
(def noscript-title-sel   [:.ss-header-noscript-title])
(def noscript-subtitle-sel [:.ss-header-noscript-subtitle])

(localization/def-scoped-t)

(defn transform
  [{:keys [status-code title subtitle second-subtitle icon image-url] :as header}]
  (fn [match]
    (html/at match
             icon-sel             (when icon (icons/set (icon :tooltip-placement "bottom")))
             status-code-sel      (ue/content-when-not-nil (str status-code))
             title-sel            (ue/html-content-when-not-nil title)
             subtitle-sel         (ue/remove-if-not  subtitle)
             subtitle-sel         (html/html-content subtitle)
             second-subtitle-sel  (ue/remove-if-not  second-subtitle)
             second-subtitle-sel  (html/html-content second-subtitle)
             title-col-sel        (ue/when-replace-class (not image-url)
                                                        "col-sm-8"
                                                        "col-sm-11")
             image-preloader-sel  (ue/set-src image-url)
             image-col-sel        (ue/remove-if-not image-url)
             noscript-title-sel   (ue/when-content (t :noscript-title))
             noscript-subtitle-sel (ue/when-content (t :noscript-subtitle)))))
