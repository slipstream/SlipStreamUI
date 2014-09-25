(ns slipstream.ui.util.core-test
  (:use [expectations]
        [slipstream.ui.util.core]))

(expect
  [{:selected? true} {}]
  (ensure-one-selected [{} {}]))

(expect
  [{:selected? true} {}]
  (ensure-one-selected [{:selected? true} {}]))

(expect
  [{:selected? true} {}]
  (ensure-one-selected [{:selected? false} {}]))

(expect
  [{} {:selected? true}]
  (ensure-one-selected [{} {:selected? true}]))