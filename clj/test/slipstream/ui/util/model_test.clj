(ns slipstream.ui.util.model-test
  (:require
    [expectations :refer :all]
    [slipstream.ui.util.model :as um]))

(expect {} (um/dissoc-CIMI {}))
(expect {:a 1 :b 2} (um/dissoc-CIMI {:a 1 :b 2}))

(expect {:where "in a galaxy far away"}
        (um/dissoc-CIMI
          {:id            123
           :resourceURI   "uri"
           :created       "long time ago"
           :updated       "recently"
           :acl           "acl"
           :operations    {}
           :where         "in a galaxy far away"}))
