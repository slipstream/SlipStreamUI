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

(localization/def-scoped-t)

(def ^:private views-with-allowed-html-in-header
  #{"error" "dashboard"})

(defn- safe-view?
  [view-name]
  (-> view-name
      views-with-allowed-html-in-header
      boolean))

(defn transform
  [{:keys [status-code title subtitle second-subtitle icon image-url] :as header} view-name]
  (let [content-fn (if (safe-view? view-name)
                     html/html-content
                     html/content)]
    (fn [match]
      (html/at match
               icon-sel             (when icon (icons/set (icon :tooltip-placement "bottom")))
               status-code-sel      (ue/content-when-not-nil (str status-code))
               title-sel            (ue/html-content-when-not-nil title)
               subtitle-sel         (ue/remove-if-not  subtitle)
               subtitle-sel         (content-fn subtitle)
               second-subtitle-sel  (ue/remove-if-not  second-subtitle)
               second-subtitle-sel  (content-fn second-subtitle)
               title-col-sel        (ue/when-replace-class (not image-url)
                                                          "col-sm-8"
                                                          "col-sm-11")
               image-preloader-sel  (ue/set-src image-url)
               image-col-sel        (ue/remove-if-not image-url)))))
