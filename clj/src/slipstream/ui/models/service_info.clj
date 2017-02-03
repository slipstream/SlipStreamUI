(ns slipstream.ui.models.service-info
  (:require
    [clj-http.client  :as http]
    [slipstream.ui.util.core :as u]
    [slipstream.ui.util.model :as um]))

(defn- keyed-attribute
  [attribute]
  {(-> attribute :uri keyword) (select-keys attribute [:type :en])})

(defn- merge-attribute
  [m a]
  (merge m (keyed-attribute a)))

(defn attributes-to-map
  [attributes-resource]
  (->> attributes-resource
       u/clojurify-raw-metadata-str
       :serviceAttributes
      (reduce merge-attribute {})))

(defn read-attributes-resource
  []
  (-> (http/get "http://localhost:8201/api/service-attribute?$last=1000"
                {:headers {"slipstream-authn-info" "super ADMIN"}})
      :body))

(defn- name-plus-namespace
  [kw]
  (-> kw str (subs 1)))

(defn- alias-type?
  [attributes type]
  (contains? attributes type))

(defn- composite-value?
  [v]
  (map? v))

(defn meta-info
  [map-attributes uri]
  (when-let [attribute (uri map-attributes)]
    { :uri          (name-plus-namespace uri)
      :type         (-> attribute :type)
      :name         {:en (-> attribute :en :name)}
      :description  {:en (-> attribute :en :description)}
      :categories   {:en (-> attribute :en :categories)}}))

(defn decorate
  ([map-attributes [uri v]]
   (decorate map-attributes false [uri v]))

  ([map-attributes nested? [uri v]]
  (let [meta      (meta-info map-attributes uri)
        meta-type (:type meta)]
    (if (nil? meta)
      [{:name {:en (name-plus-namespace uri)} :value v :nested nested?}]
      (cond
        (alias-type? map-attributes meta-type)  (decorate map-attributes nested? [(get map-attributes meta-type) v])
        (composite-value? v)                    (into [(assoc meta :nested true)]
                                                      (flatten (map (partial decorate map-attributes true) v)))
        :else                                   [(-> meta
                                                     (assoc :value v)
                                                     (assoc :nested nested?))])))))

(defn- connector-name
  [service-info]
  (-> service-info :connector :href))

(defn- data-model->display-model
  [attributes service-info]
  {:id               (connector-name service-info)
   :decorated-values (->> (um/dissoc-CIMI service-info)
                          (map (partial decorate (attributes-to-map attributes)))
                          flatten)})

(defn parse
  ([metadata attributes]
    {:service-offer (map (partial data-model->display-model attributes) (:serviceOffers metadata))})
  ([metadata]
    (parse metadata (read-attributes-resource))))