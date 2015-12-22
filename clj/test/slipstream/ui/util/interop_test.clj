(ns slipstream.ui.util.interop-test
  (:require [clojure.set :refer [union]])
  (:use [expectations]
        [slipstream.ui.util.interop]))

(defn get-class [^String s]
  (try 
    (Class/forName s)
    (catch ClassNotFoundException _
      nil)))

(defn class-set [coll]
  (->> coll
       (map get-class)
       (remove nil?)
       (set)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ->clj

;; Non Java things

(expect
  nil
  (->clj nil))

(expect
  :some-keyword
  (->clj :some-keyword))

(expect
  "some string"
  (->clj "some string"))

(expect
  1
  (->clj 1))

;; Understanding Clojure vs Java structures

(expect
  true
  (instance? clojure.lang.IPersistentMap {:a 1}))

(expect
  true
  (instance? clojure.lang.PersistentArrayMap {:a 1}))

(expect
  false
  (instance? clojure.lang.PersistentHashMap {:a 1}))

(expect
  true
  (instance? java.util.Map {:a 1}))

(expect
  false
  (instance? java.util.HashMap {:a 1}))

(expect
  clojure.lang.PersistentArrayMap
  (class {}))

(expect
  clojure.lang.PersistentArrayMap
  (class (into {} (->> 10 range (partition 2) (map vec)))))

(expect
  clojure.lang.PersistentHashMap
  (class (into {} (->> 1000 range (partition 2) (map vec)))))

(def ^:private i-map-ancestors
  #{clojure.lang.Associative
    clojure.lang.Counted
    clojure.lang.ILookup
    clojure.lang.IPersistentCollection
    clojure.lang.Seqable
    java.lang.Iterable})

(expect
  i-map-ancestors
  (ancestors clojure.lang.IPersistentMap))

(expect
 (union
  i-map-ancestors
  (class-set 
   #{"clojure.lang.AFn"
     "clojure.lang.APersistentMap"
     "clojure.lang.IEditableCollection"
     "clojure.lang.IFn"
     "clojure.lang.IHashEq"
     "clojure.lang.IMeta"
     "clojure.lang.IObj"
     "clojure.lang.IPersistentMap"
     "clojure.lang.MapEquivalence"
     "clojure.lang.IMapIterable"   ;; clojure 1.7.0
     "clojure.lang.IKVReduce"      ;; clojure 1.8.0
     "java.io.Serializable"
     "java.lang.Object"
     "java.lang.Runnable"
     "java.util.concurrent.Callable"
     "java.util.Map"}))
  (ancestors clojure.lang.PersistentArrayMap))

(expect
 (union
  i-map-ancestors
  (class-set 
   #{"clojure.lang.AFn"
     "clojure.lang.APersistentMap"
     "clojure.lang.IEditableCollection"
     "clojure.lang.IFn"
     "clojure.lang.IHashEq"
     "clojure.lang.IMeta"
     "clojure.lang.IObj"
     "clojure.lang.IPersistentMap"
     "clojure.lang.MapEquivalence"
     "clojure.lang.IMapIterable"   ;; clojure 1.7.0
     "clojure.lang.IKVReduce"      ;; clojure 1.8.0
     "java.io.Serializable"
     "java.lang.Object"
     "java.lang.Runnable"
     "java.util.concurrent.Callable"
     "java.util.Map"}))
  (ancestors clojure.lang.PersistentHashMap))

(expect
  nil
  (ancestors java.util.Map))

(expect
  #{java.lang.Cloneable
    java.lang.Object
    java.util.Map
    java.util.AbstractMap
    java.io.Serializable}
  (ancestors java.util.HashMap))

(expect
  true
  (instance? clojure.lang.IPersistentVector [:a :b :c]))

(expect
  true
  (instance? clojure.lang.PersistentVector [:a :b :c]))

(expect
  true
  (instance? java.util.List [:a :b :c]))

(def ^:private i-vector-ancestors
  #{clojure.lang.Associative
    clojure.lang.Counted
    clojure.lang.ILookup
    clojure.lang.Indexed
    clojure.lang.IPersistentCollection
    clojure.lang.IPersistentStack
    clojure.lang.Reversible
    clojure.lang.Seqable
    clojure.lang.Sequential})

(expect
  i-vector-ancestors
  (ancestors clojure.lang.IPersistentVector))


(expect
 (union
  i-vector-ancestors
  (class-set 
   #{"clojure.lang.AFn"
     "clojure.lang.APersistentVector"
     "clojure.lang.IEditableCollection"
     "clojure.lang.IFn"
     "clojure.lang.IHashEq"
     "clojure.lang.IMeta"
     "clojure.lang.IObj"
     "clojure.lang.IPersistentVector"
     "clojure.lang.IReduce"           ;; clojure 1.7.0
     "clojure.lang.IReduceInit"       ;; clojure 1.7.0
     "clojure.lang.IKVReduce"         ;; clojure 1.8.0
     "java.io.Serializable"
     "java.lang.Comparable"
     "java.lang.Iterable"
     "java.lang.Object"
     "java.lang.Runnable"
     "java.util.Collection"
     "java.util.concurrent.Callable"
     "java.util.List"
     "java.util.RandomAccess"}))
  (ancestors clojure.lang.PersistentVector))

;; Maps

(expect
  (hash-map :a 1)
  (->clj {:a 1}))

(expect
  {:a 1}
  (->clj {:a 1}))

(expect
  {"a" 1}
  (->clj {"a" 1}))

(expect
  map?
  (->clj {:a 1}))

(expect
  map?
  {:a 1})

;; Java HashMaps

(expect
  (hash-map :a 1)
  (->clj (java.util.HashMap. {:a 1})))

(expect
  {:a 1}
  (->clj (java.util.HashMap. {:a 1})))

(expect
  map?
  (->clj (java.util.HashMap. {:a 1})))

(expect
  map?
  (->clj (java.util.HashMap. {:a 1})))

(expect
  map?
  (->clj (java.util.HashMap. {(java.util.HashMap. {:a 1}) 1})))

(expect
  {{:a 1} 1}
  (->clj (java.util.HashMap. {(java.util.HashMap. {:a 1}) 1})))

(expect
  map?
  (ffirst (->clj (java.util.HashMap. {(java.util.HashMap. {:a 1}) 1}))))

(expect
  (complement map?)
  (java.util.HashMap. {:a 1}))

;; Java ArrayLists

(expect
  (vector :a 1)
  (->clj (java.util.ArrayList. [:a 1])))

(expect
  [:a 1]
  (->clj (java.util.ArrayList. [:a 1])))

(expect
  (complement vector?)
  (java.util.ArrayList. [:a 1]))

(expect
  vector?
  (->clj (java.util.ArrayList. [:a 1])))

;; Java composite structures

(expect
  vector?
  (->clj (java.util.ArrayList. [(java.util.HashMap. {:a 1})
                                (java.util.HashMap. {:b 2})
                                (java.util.HashMap. {:c 3})
                                (java.util.HashMap. {:d 4})])))

(expect
  (partial every? map?)
  (->clj (java.util.ArrayList. [(java.util.HashMap. {:a 1})
                                (java.util.HashMap. {:b 2})
                                (java.util.HashMap. {:c 3})
                                (java.util.HashMap. {:d 4})])))

(expect
  (partial every? map?)
  (map ->clj (java.util.ArrayList. [(java.util.HashMap. {:a 1})
                                    (java.util.HashMap. {:b 2})
                                    (java.util.HashMap. {:c 3})
                                    (java.util.HashMap. {:d 4})])))

(expect
  fn?
  ->clj)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
