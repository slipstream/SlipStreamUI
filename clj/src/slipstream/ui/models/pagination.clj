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

(defn- pages
  [total-number-of-items items-per-page]
  (let [number-of-pages (-> total-number-of-items (quot items-per-page) inc)]
    (for [page (range number-of-pages)]
      {:page-number (inc page)
       :offset      (* page items-per-page)
       :limit       items-per-page})))

(defn- flag-current-page
  [pages current-offset]
  (map #(if (= (:offset %) current-offset)
          (assoc % :current-page true)
          %)
       pages))

(defn- unhide-pages-with-index
  [pred n pages]
  (map-indexed (fn [i page]
                 (if (pred i n)
                   (dissoc page :hidden)
                   page))
               pages))

(defn- unhide-first-n-pages
  [n pages]
  (unhide-pages-with-index < n pages))

(defn- unhide-last-n-pages
  [n pages]
  (unhide-pages-with-index >= (-> pages count (- n)) pages))

(defn- partition-prev-next
  "Returns pages partitioned as [prev-pages current-page next-pages]"
  [pages]
  (let [[first-group second-group third-group] (->> pages
                                                    (map #(assoc % :hidden true))
                                                    (partition-by :current-page))]
    (cond
      (not-empty third-group)               [first-group  second-group  third-group]
      (-> first-group first :current-page)  [nil          first-group   second-group]
      :else                                 [first-group  second-group  nil])))

(defn- flag-hidden-pages
  [pages & {:keys [num-of-head-pages
                   num-of-tail-pages
                   num-of-context-pages]}]
  (let [[prev-pages current-page next-pages] (partition-prev-next pages)]
    (concat
      (->> prev-pages
           (unhide-first-n-pages num-of-head-pages)
           (unhide-last-n-pages num-of-context-pages))
      (->> current-page
           (unhide-first-n-pages 1))
      (->> next-pages
           (unhide-first-n-pages num-of-context-pages)
           (unhide-last-n-pages num-of-tail-pages)))))


(defn- flag-last-hidden-page
  [pages]
  (let [[last-page first-pages] (->> pages
                                     reverse
                                     ((juxt first next)))
        updated-last-page (if (:hidden last-page)
                            (assoc last-page :last-hidden true)
                            last-page)]
    (-> updated-last-page
        (cons first-pages)
        reverse)))

(defn- flag-last-hidden-pages
  [pages]
  (->> pages
       (partition-by :hidden)
       (mapcat flag-last-hidden-page)))

(defn- navigation-params
  [pagination-type offset items-per-page count-total]
  (select-keys
        {:first-page     {:offset  0
                          :limit   items-per-page}
         :previous-page  {:offset  (- offset items-per-page)
                          :limit   items-per-page}
         :next-page      {:offset  (+ offset items-per-page)
                          :limit   items-per-page}
         :last-page      {:offset  (->> items-per-page (quot (dec count-total)) (* items-per-page))
                          :limit   items-per-page}
         :show-all       {:offset  0
                          :limit   (inc count-total)}
         :pages          (-> count-total
                           (pages items-per-page)
                           (flag-current-page offset)
                           (flag-hidden-pages :num-of-head-pages    3
                                              :num-of-tail-pages    2
                                              :num-of-context-pages 2)
                           flag-last-hidden-pages)}
    (case pagination-type
      :showing-one                [:pages]
      :showing-first-from-many    [:pages :next-page]
      :showing-last-from-many     [:pages :previous-page]
      :showing-one-from-many      [:pages :previous-page :next-page]
      :showing-all                [:pages]
      :showing-almost-all         [:pages :next-page :show-all]
      :showing-first-n-from-many  [:pages :next-page]
      :showing-last-n-from-many   [:pages :previous-page]
      :showing-two-from-many      [:pages :previous-page :next-page]
      :showing-range              [:pages :previous-page :next-page])))

(defn info
  [{:keys [offset limit count-shown count-total]}]
  (let [first-item          (inc offset)
        last-item           (+ offset count-shown)
        pagination-type     (pagination-type first-item last-item count-shown count-total)
        pagination-msg-key  (->> pagination-type name (str "msg.") keyword)
        pagination-msg      (t pagination-msg-key count-shown first-item last-item count-total)]
    {:msg     pagination-msg
     :params  (navigation-params pagination-type offset limit count-total)}))
