(ns slipstream.ui.models.version
  (:require [superstring.core :as s]
            [clojure.java.shell :as sh]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]))

(def ^:private repos
  ["SlipStreamServer"
   "SlipStreamConnectors"
   "SlipStreamUI"])

(defn- slipstream-repo-version
  [repo]
  (try
    (-> (sh/sh "git" "describe" :dir (str "../../" repo))
        :out
        s/trim-newline
        (s/replace-first #"[a-zA-Z]+-" ""))
    (catch Throwable consumed
      "not found")))

(defn- slipstream-repo-html
  [repo]
  (format "<dt><strong>%s:</strong></dt><dd>%s</dd>"
          (uc/trim-prefix repo "SlipStream")
          (slipstream-repo-version repo)))

(def ^:private popover-html-content
  (->> repos
       (map slipstream-repo-html)
       (s/join "")
       (format "<dl class='dl-horizontal ss-footer-versions-popover'>%s</dl>")))

(defn- version-with-popover
  [version]
  (ue/text-with-popover-snip
    :text       version
    :title      "SlipStream Repo Tags"
    :content    popover-html-content
    :html       true
    :placement  "left"))


(def slipstream-release-version
  (atom (version-with-popover "x.y.z")))

(defn set-release-version
  [version]
  (reset! slipstream-release-version
          (if (re-matches #".*<dd>not found</dd>.*" popover-html-content)
            version
            (version-with-popover version))))
