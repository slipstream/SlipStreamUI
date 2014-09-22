(ns slipstream.ui.models.breadcrumbs-test
  (:use [expectations]
        [slipstream.ui.models.breadcrumbs]))

(expect
  [{:text "module"}
   {:text "examples" :uri "module/examples"}
   {:text "tutorials" :uri "module/examples/tutorials"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress/wordpress"}
   {:text "180"}]
  (parse "module/examples/tutorials/wordpress/wordpress/180"))

(expect
  [{:text "module"}
   {:text "examples" :uri "/module/examples"}
   {:text "tutorials" :uri "/module/examples/tutorials"}
   {:text "wordpress" :uri "/module/examples/tutorials/wordpress"}
   {:text "wordpress" :uri "/module/examples/tutorials/wordpress/wordpress"}
   {:text "180"}]
  (parse "/module/examples/tutorials/wordpress/wordpress/180"))

(expect
  [{:text "module"}
   {:text "examples" :uri "module/examples"}
   {:text "tutorials" :uri "module/examples/tutorials"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress"}
   {:text "wordpress" :uri "module/examples/tutorials/wordpress/wordpress"}
   {:text "180" :uri "module/examples/tutorials/wordpress/wordpress/180"}
   {:text ""}]
  (parse "module/examples/tutorials/wordpress/wordpress/180/"))
