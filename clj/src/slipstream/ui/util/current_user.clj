(ns slipstream.ui.util.current-user
  (:require [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.user.loggedin :as loggedin-user]))

(def ^:dynamic *current-user*
  "The current-user is configured with a local thread-bound binding with the macro
  'current-user/with-user'. The default value of this dynamic var *current-user*
  is nil. To check the *current-user* use the 3 specific fns below instead of
  referencing this var."
  nil)

(defmacro with-user
  [logged-user & body]
  `(binding [*current-user* ~logged-user]
    ~@body))

(defmacro with-user-from-metadata
  [& body]
  (when-not (-> &env keys set (get 'metadata))
    (throw (IllegalArgumentException. "with-user-from-metadata: Unable to find metadata symbol in this context")))
  `(with-user (loggedin-user/parse ~(symbol "metadata"))
    ~@body))

(defn username
  []
  (:username *current-user*))

(defn uri
  []
  (:uri *current-user*))

(defn super?
  []
  (:super? *current-user*))

(def not-super?
  (complement super?))

(defn type-name
  []
  (if (super?) "super" "regular"))

(defn logged-in?
  []
  (:logged-in? *current-user*))

(def not-logged-in?
  (complement logged-in?))
