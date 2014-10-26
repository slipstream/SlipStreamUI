(ns slipstream.ui.models.breadcrumbs-test
  (:use [expectations]
        [slipstream.ui.models.breadcrumbs]))

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
