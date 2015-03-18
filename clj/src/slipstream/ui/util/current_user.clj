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

(defn- parse-current-user
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

(defmacro with-user-from-metadata
  [& body]
  (when-not (-> &env keys set (clojure.core/get 'metadata))
    (throw (IllegalArgumentException. "with-user-from-metadata: Unable to find metadata symbol in this context")))
  `(with-user (~parse-current-user ~(symbol "metadata"))
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