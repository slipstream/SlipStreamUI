(ns slipstream.ui.models.pagination-test
  (:use [expectations])
  (:require [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.pagination :as model]))

(expect
  {:msg "91 − 100 of 430" ;; NOTE: the dash char is not a plain dash, but a "minus-dash" char
   :tooltip "Showing 10 items (from 91 to 100) from a total of 430."
   :params {:next-page {:offset 100
                        :limit 10}
            :previous-page {:offset 80
                            :limit 10}
            :pages [{:page-number 1  :offset 0,   :limit 10}
                    {:page-number 2  :offset 10,  :limit 10}
                    {:page-number 3  :offset 20,  :limit 10}
                    {:page-number 4  :offset 30,  :limit 10, :hidden true}
                    {:page-number 5  :offset 40,  :limit 10, :hidden true}
                    {:page-number 6  :offset 50,  :limit 10, :hidden true}
                    {:page-number 7  :offset 60,  :limit 10, :hidden true, :last-hidden true}
                    {:page-number 8  :offset 70,  :limit 10}
                    {:page-number 9  :offset 80,  :limit 10}
                    {:page-number 10 :offset 90,  :limit 10, :current-page true}
                    {:page-number 11 :offset 100, :limit 10}
                    {:page-number 12 :offset 110, :limit 10}
                    {:page-number 13 :offset 120, :limit 10, :hidden true}
                    {:page-number 14 :offset 130, :limit 10, :hidden true}
                    {:page-number 15 :offset 140, :limit 10, :hidden true}
                    {:page-number 16 :offset 150, :limit 10, :hidden true}
                    {:page-number 17 :offset 160, :limit 10, :hidden true}
                    {:page-number 18 :offset 170, :limit 10, :hidden true}
                    {:page-number 19 :offset 180, :limit 10, :hidden true}
                    {:page-number 20 :offset 190, :limit 10, :hidden true}
                    {:page-number 21 :offset 200, :limit 10, :hidden true}
                    {:page-number 22 :offset 210, :limit 10, :hidden true}
                    {:page-number 23 :offset 220, :limit 10, :hidden true}
                    {:page-number 24 :offset 230, :limit 10, :hidden true}
                    {:page-number 25 :offset 240, :limit 10, :hidden true}
                    {:page-number 26 :offset 250, :limit 10, :hidden true}
                    {:page-number 27 :offset 260, :limit 10, :hidden true}
                    {:page-number 28 :offset 270, :limit 10, :hidden true}
                    {:page-number 29 :offset 280, :limit 10, :hidden true}
                    {:page-number 30 :offset 290, :limit 10, :hidden true}
                    {:page-number 31 :offset 300, :limit 10, :hidden true}
                    {:page-number 32 :offset 310, :limit 10, :hidden true}
                    {:page-number 33 :offset 320, :limit 10, :hidden true}
                    {:page-number 34 :offset 330, :limit 10, :hidden true}
                    {:page-number 35 :offset 340, :limit 10, :hidden true}
                    {:page-number 36 :offset 350, :limit 10, :hidden true}
                    {:page-number 37 :offset 360, :limit 10, :hidden true}
                    {:page-number 38 :offset 370, :limit 10, :hidden true}
                    {:page-number 39 :offset 380, :limit 10, :hidden true}
                    {:page-number 40 :offset 390, :limit 10, :hidden true}
                    {:page-number 41 :offset 400, :limit 10, :hidden true, :last-hidden true}
                    {:page-number 42 :offset 410, :limit 10}
                    {:page-number 43 :offset 420, :limit 10}]}}
  (localization/with-lang :en
    (model/info {:offset 90
                 :limit 10
                 :count-shown 10
                 :count-total 430})))

(expect
  {:msg "1 − 10 of 430" ;; NOTE: the dash char is not a plain dash, but a "minus-dash" char
   :tooltip "Showing first 10 items from a total of 430."
   :params {:next-page {:offset 10
                        :limit 10}
            :pages [{:page-number 1  :offset 0,   :limit 10, :current-page true}
                    {:page-number 2  :offset 10,  :limit 10}
                    {:page-number 3  :offset 20,  :limit 10}
                    {:page-number 4  :offset 30,  :limit 10, :hidden true}
                    {:page-number 5  :offset 40,  :limit 10, :hidden true}
                    {:page-number 6  :offset 50,  :limit 10, :hidden true}
                    {:page-number 7  :offset 60,  :limit 10, :hidden true}
                    {:page-number 8  :offset 70,  :limit 10, :hidden true}
                    {:page-number 9  :offset 80,  :limit 10, :hidden true}
                    {:page-number 10 :offset 90,  :limit 10, :hidden true}
                    {:page-number 11 :offset 100, :limit 10, :hidden true}
                    {:page-number 12 :offset 110, :limit 10, :hidden true}
                    {:page-number 13 :offset 120, :limit 10, :hidden true}
                    {:page-number 14 :offset 130, :limit 10, :hidden true}
                    {:page-number 15 :offset 140, :limit 10, :hidden true}
                    {:page-number 16 :offset 150, :limit 10, :hidden true}
                    {:page-number 17 :offset 160, :limit 10, :hidden true}
                    {:page-number 18 :offset 170, :limit 10, :hidden true}
                    {:page-number 19 :offset 180, :limit 10, :hidden true}
                    {:page-number 20 :offset 190, :limit 10, :hidden true}
                    {:page-number 21 :offset 200, :limit 10, :hidden true}
                    {:page-number 22 :offset 210, :limit 10, :hidden true}
                    {:page-number 23 :offset 220, :limit 10, :hidden true}
                    {:page-number 24 :offset 230, :limit 10, :hidden true}
                    {:page-number 25 :offset 240, :limit 10, :hidden true}
                    {:page-number 26 :offset 250, :limit 10, :hidden true}
                    {:page-number 27 :offset 260, :limit 10, :hidden true}
                    {:page-number 28 :offset 270, :limit 10, :hidden true}
                    {:page-number 29 :offset 280, :limit 10, :hidden true}
                    {:page-number 30 :offset 290, :limit 10, :hidden true}
                    {:page-number 31 :offset 300, :limit 10, :hidden true}
                    {:page-number 32 :offset 310, :limit 10, :hidden true}
                    {:page-number 33 :offset 320, :limit 10, :hidden true}
                    {:page-number 34 :offset 330, :limit 10, :hidden true}
                    {:page-number 35 :offset 340, :limit 10, :hidden true}
                    {:page-number 36 :offset 350, :limit 10, :hidden true}
                    {:page-number 37 :offset 360, :limit 10, :hidden true}
                    {:page-number 38 :offset 370, :limit 10, :hidden true}
                    {:page-number 39 :offset 380, :limit 10, :hidden true}
                    {:page-number 40 :offset 390, :limit 10, :hidden true}
                    {:page-number 41 :offset 400, :limit 10, :hidden true, :last-hidden true}
                    {:page-number 42 :offset 410, :limit 10}
                    {:page-number 43 :offset 420, :limit 10}]}}
  (localization/with-lang :en
    (model/info {:offset 0
                 :limit 10
                 :count-shown 10
                 :count-total 430})))

(expect
  {:msg "11 − 11 of 11" ;; NOTE: the dash char is not a plain dash, but a "minus-dash" char
   :tooltip "Showing last item from a total of 11."
   :params {:previous-page {:offset 0
                            :limit 10}
            :pages [{:page-number 1, :offset 0,  :limit 10}
                    {:page-number 2, :offset 10, :limit 10, :current-page true}]}}
  (localization/with-lang :en
    (model/info {:offset 10
                 :limit 10
                 :count-shown 1
                 :count-total 11})))

