(ns slipstream.ui.models.breadcrumbs-test
  (:use [expectations]
        [slipstream.ui.models.breadcrumbs])
  (:require [slipstream.ui.util.current-user :as current-user]))

(expect
  nil
  (parse nil))

(expect
  [{:text "a" :uri "a"}
   {:text "b" :uri "a/b"}
   {:text "c" :uri "a/b/c"}
   {:text "d" :uri "a/b/c/d"}
   {:text "e", :uri nil}]
  (parse "a/b/c/d/e"))

(expect
  [{:text "examples" :uri "module/examples"}
   {:text "tutorials" :uri "module/examples/tutorials"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress/wordpress"}
   {:text "180", :uri nil}]
  (parse "module/examples/tutorials/wordpress/wordpress/180"))

(expect
  [{:text "examples" :uri "/module/examples"}
   {:text "tutorials" :uri "/module/examples/tutorials"}
   {:text "wordpress" :uri "/module/examples/tutorials/wordpress"}
   {:text "wordpress" :uri "/module/examples/tutorials/wordpress/wordpress"}
   {:text "180", :uri nil}]
  (parse "/module/examples/tutorials/wordpress/wordpress/180"))

(expect
  [{:text "examples" :uri "module/examples"}
   {:text "tutorials" :uri "module/examples/tutorials"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress/wordpress"}
   {:text "180" :uri "module/examples/tutorials/wordpress/wordpress/180"}
   {:text "", :uri nil}]
  (parse "module/examples/tutorials/wordpress/wordpress/180/"))

(expect
  [{:text "dashboard" :uri "/dashboard"}
   {:text "38497716-5f75-44ba-802b-6b464431d802", :uri nil}]
  (current-user/with-user {:super? true}
    (parse "/run/38497716-5f75-44ba-802b-6b464431d802")))

(expect
  [{:text "dashboard" :uri "dashboard"}
   {:text "38497716-5f75-44ba-802b-6b464431d802", :uri nil}]
  (current-user/with-user {:super? true}
    (parse "run/38497716-5f75-44ba-802b-6b464431d802")))

(expect
  [{:text "user" :uri "/user"}
   {:text "foo", :uri nil}]
  (current-user/with-user {:super? true}
    (parse "/user/foo")))

(expect
  [{:text "foo", :uri nil}]
  (current-user/with-user {:super? false}
    (parse "/user/foo")))
