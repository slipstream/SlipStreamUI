(ns slipstream.ui.util.pattern
  "Util functions only related to the regex patterns to be fulfilled by SlipStream items
  like image names, user names or email addresses."
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.current-user :as current-user]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

(defn- str-pattern
  [pattern-key]
  (case pattern-key
    :not-empty                      ".+"
    :alpha-num                      "^[a-zA-Z0-9]+$"
    :alpha-num-underscore           "^\\w+$"
    :alpha-num-underscore-dash      "^[\\w-]+$"
    :alpha-num-underscore-dash-dot  "^[\\w.-]+$"
    :not-new                        "^(?!new$).*$" ; Other than the string 'new'.
    :not-include-username           (str "^((?!" (current-user/username) ").)*$") ; Does not contain username.
    :begin-with-letter              "^[a-zA-Z]"    ; Matches "asd..." but not "1asd..."
    :min-3-chars                    ".{3}"
    :min-6-chars                    ".{6}"
    :url                            "^https?://\\w+"
    :picture-file                   "\\.(?:png|jpg|jpeg|PNG|JPG|JPEG)$"
    :comma-separated-words          "^\\s*[\\w-]*(?:\\s*,\\s*[\\w-]*)*\\s*$"
    :dot-separated-words            "^[\\w-]*(?:\\.[\\w-]*)*[\\w-]+$"
    ; NOTE: The following regex is more precise and intended for a multiline match:
    ;       "^((([^ ]+ )?(ssh-(rsa|dss))|(ecdsa-sha2-nistp(256|384|512)) [^ ]+( .*)?)|(#.*))?$"
    ;       However 'RegExp(pattern, "gm").test(...)' in JavaScript returns true if any line matches.
    :ssh-public-keys                "^ssh-rsa \\S+(?: \\S.*)?[\n]*(?:[\n]ssh-rsa \\S+(?: \\S.*)?[\n]*)*$" ; simple regex for non-multiline match
    ; NOTE: As mentioned in http://stackoverflow.com/a/202528 the RFC of the
    ;       format of email address is so complex, that the only real way to
    ;       validate it is to send it an email. ;)
    ;       However we can perform a basic validation.
    :email                          "^.+@.+$"))

(defn- requirement
  [field pattern]
  (let [[pattern-key status] (if (vector? pattern)
                               pattern
                               [pattern {}])
        error-help-hint-t-key (str "error-help-hint." (name field) "." (name pattern-key))]
    {:pattern-name (name pattern-key)
     :pattern (str-pattern pattern-key)
     :help-hint {:when-false (t error-help-hint-t-key)}
     :status (merge
               {:when-true  "success", :when-false "error"}
               status)}))

(def ^:private patterns-for
  {:username      [:not-empty
                   :alpha-num-underscore
                   :not-new
                   :begin-with-letter
                   :min-3-chars]

   :email         [:not-empty
                   :email]

   :first-name    [:not-empty]

   :last-name     [:not-empty]

   :module-name   [:not-empty
                   :alpha-num-underscore-dash-dot
                   :not-new
                   :begin-with-letter]

   :parameter-name  [:not-empty
                     :dot-separated-words
                     :not-new
                     :begin-with-letter]

   :run-tags      [[:comma-separated-words  {:when-true   "warning"}]]

   :ssh-public-keys [:not-empty
                     [:ssh-public-keys      {:when-false   "warning"}]]

   :picture-url   [:url
                   [:picture-file           {:when-true   "validating"}]]

   :node-name     [:not-empty
                   :alpha-num-underscore
                   :not-new
                   :begin-with-letter]

   :user-password [:not-empty
                   :not-include-username
                   :min-6-chars]

   :user-password-confirmation [:not-empty]})

(defn requirements
  [field]
  (mapv (partial requirement field) (patterns-for field)))

(defn requires-not-empty?
  [requirements]
  (->> requirements
       (some #(-> % :pattern-name #{"not-empty"}))
       boolean))