(ns slipstream.ui.util.pattern
  "Util functions only related to the regex patterns to be fulfilled by SlipStream items
  like image names, user names or email addresses."
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

(def ^:private str-pattern
  {:not-empty                   ".+"
   :alpha-num                   "^[a-zA-Z0-9]+$"
   :alpha-num-underscore        "^\\w+$"
   :alpha-num-underscore-dash   "^[\\w-]+$"
   :not-new                     "^(?!new$).*$"    ; Other than the string 'new'
   :begin-with-letter           "^[a-zA-Z]"       ; Matches "asd..." but not "1asd..."
   :min-3-chars                 ".{3}"
   :url                         "^https?://\\w+"
   :picture-file                "\\.(?:png|jpg|jpeg|PNG|JPG|JPEG)$"
   ; NOTE: As mentioned in http://stackoverflow.com/a/202528 the RFC of the
   ;       format of email address is so complex, that the only real way to
   ;       validate it is to send it an email. ;)
   ;       However we can perform a basic validation.
   :email                       "^.+@.+$"})

(defn- requirement
  [field pattern-key]
  (let [error-help-hint-t-key (str "error-help-hint." (name field) "." (name pattern-key))]
    {:pattern (str-pattern pattern-key)
     :error-help-hint (t error-help-hint-t-key)}))

(def ^:private patterns-for
  {:username    [:not-empty
                 :alpha-num-underscore
                 :not-new
                 :begin-with-letter
                 :min-3-chars]

   :email       [:not-empty
                 :email]

   :first-name  [:not-empty]

   :last-name   [:not-empty]

   :module-name [:not-empty
                 :alpha-num-underscore-dash
                 :not-new
                 :begin-with-letter]

   :picture-url [:url
                 :picture-file]

   :node-name   [:not-empty
                 :alpha-num-underscore-dash
                 :not-new
                 :begin-with-letter]})

(defn requirements
  [field]
  (mapv (partial requirement field) (patterns-for field)))
