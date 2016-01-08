(ns slipstream.ui.util.current-user
  (:refer-clojure :exclude [get get-in])
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.user :as user]))

(def  ^:private ^:dynamic *current-user*
  "The current-user is configured with a local thread-bound binding with the macro
  'current-user/with-user'. The default value of this dynamic var *current-user*
  is nil. To check the *current-user* use the 3 specific fns below instead of
  referencing this var."
  nil)

(def ^:private current-user-sel [html/root :> :user])

(defn- parse-current-user-from-metadata
  [metadata]
  (when-let [current-user-metadata (-> metadata
                                       (html/select current-user-sel)
                                       first
                                       not-empty)]
    (user/parse current-user-metadata)))

(defmacro with-user
  [user & body]
  `(binding [*current-user* ~user]
    ~@body))

(defn- symbol-present?
  [env sb]
  (-> env keys set (clojure.core/get sb) boolean))

(defn- check-symbol-present
  [env sb]
  (when-not (symbol-present? env sb)
    (throw (IllegalArgumentException. (str "with-current-user: Unable to find '" (name sb) "' symbol in this context")))))

(defmacro with-current-user
  [& body]
  (check-symbol-present &env 'metadata)
  `(with-user (or
                (~parse-current-user-from-metadata ~(symbol "metadata"))
                (:user ~(if (symbol-present? &env 'options)
                          (symbol "options")
                          nil)))
    ~@body))

(defn username
  []
  (:username *current-user*))

(defn is?
  [user-or-username]
  (let [username (if (string? user-or-username)
                   user-or-username
                   (:username user-or-username))]
    (-> *current-user* :username (= username))))

(def not-is?
  (complement is?))

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
  (-> *current-user*
      not-empty
      boolean))

(def not-logged-in?
  (complement logged-in?))

(defn github-user?
  []
  (:github-login *current-user*))

(defn get
  ([]
    *current-user*)
  ([k]
    (clojure.core/get *current-user* k)))

(defn get-in
  [ks]
  (clojure.core/get-in *current-user* ks))

(defn configuration
  [& ks]
  (get-in (into [:configuration] ks)))