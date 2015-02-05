(ns slipstream.ui.models.pagination
  "Used by runs and vms models."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.enlive :as ue]
            [slipstream.ui.util.localization :as localization]))

(localization/def-scoped-t)

(def items-per-page
  "Default value."
  20)

(defn- parse-pagination
  [attrs]
  {:offset      (-> attrs :offset uc/parse-pos-int (or 0))
   :limit       (-> attrs :limit uc/parse-pos-int (or 0))
   :count-shown (-> attrs :count uc/parse-pos-int (or 0))
   :count-total (-> attrs :totalCount uc/parse-pos-int (or 0))
   :cloud-name  (-> attrs :cloud)})

(defn parse
  [metadata]
  (-> metadata
      (html/select ue/this)
      first
      :attrs
      parse-pagination))

;; Utils

(defn- pagination-type
  [first-item last-item count-shown count-total]
  (cond
    (= 1 count-shown count-total)     :showing-one
    (= 1 count-shown first-item)      :showing-first-from-many
    (and
      (= 1 count-shown)
      (= count-total last-item))      :showing-last-from-many
    (= 1 count-shown)                 :showing-one-from-many
    (= count-shown count-total)       :showing-all
    ; (<= count-total (+ 5 count-shown)) :showing-almost-all ;; NOTE: Outcommented until the server allow to a limit above 20
    (= count-shown last-item)         :showing-first-n-from-many
    (= count-total last-item)         :showing-last-n-from-many
    (= 2 count-shown)                 :showing-two-from-many
    :else                             :showing-range))

(defn- navigation-params
  [pagination-type offset limit count-total]
  (select-keys
    {:first-page     {:offset  0
                      :limit   limit}
     :previous-page  {:offset  (- offset limit)
                      :limit   limit}
     :next-page      {:offset  (+ offset limit)
                      :limit   limit}
     :last-page      {:offset  (->> limit (quot (dec count-total)) (* limit))
                      :limit   limit}
     :show-all       {:offset  0
                      :limit   (inc count-total)}}
    (case pagination-type
      :showing-one                []
      :showing-first-from-many    [:next-page :last-page]
      :showing-last-from-many     [:first-page :previous-page]
      :showing-one-from-many      [:first-page :previous-page :next-page :last-page]
      :showing-all                []
      :showing-almost-all         [:show-all]
      :showing-first-n-from-many  [:next-page :last-page]
      :showing-last-n-from-many   [:first-page :previous-page]
      :showing-two-from-many      [:first-page :previous-page :next-page :last-page]
      :showing-range              [:first-page :previous-page :next-page :last-page])))

(defn info
  [{:keys [offset limit count-shown count-total]}]
  (let [first-item          (inc offset)
        last-item           (+ offset count-shown)
        pagination-type     (pagination-type first-item last-item count-shown count-total)
        pagination-msg-key  (->> pagination-type name (str "msg.") keyword)
        pagination-msg      (t pagination-msg-key count-shown first-item last-item count-total)]
    {:msg     pagination-msg
     :params  (navigation-params pagination-type offset limit count-total)}))
