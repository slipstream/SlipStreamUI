(ns slipstream.ui.models.user-test
  (:use [expectations])
  (:require [slipstream.ui.models.user :as user]
            [slipstream.ui.models.module.image-test :as image-data]))

(def image image-data/raw-metadata)

(expect "sky"
        (user/default-cloud image))
