(ns slipstream.ui.models.user-test
  (:use [expectations])
  (:require [slipstream.ui.models.user :as user]
            [slipstream.ui.data.image :as image-data]))

(def image image-data/xml-image)

(expect "sky"
        (user/default-cloud image))
