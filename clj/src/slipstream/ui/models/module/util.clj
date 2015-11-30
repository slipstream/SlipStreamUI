(ns slipstream.ui.models.module.util
  (:require [net.cgrand.enlive-html :as html]))

(defn conj-recipe-target
  [v target-name metadata & [target-machine-type]]
  (let [recipe (-> metadata
                   (html/select [(keyword target-name) html/text-node])
                   first)]
    (conj v {:target-name target-name
             :target-type :script
             :target-machine-type (or target-machine-type :application-component)
             :context     #{:image-creation}
             :script      recipe})))

(defn- parse-package
  [package-metadata]
  (select-keys (:attrs package-metadata) [:key :name :repository]))

(defn conj-packages-target
  [v target-name metadata & [target-machine-type]]
  (let [packages (->> (html/select metadata [:package])
                      (map parse-package)
                      (sort-by :name)
                      vec)]
    (conj v {:target-name "packages"
             :target-type :packages
             :target-machine-type (or target-machine-type :application-component)
             :context     #{:image-creation}
             :packages    packages})))

(defn conj-script-target
  [v target-name metadata & [target-machine-type]]
  (let [script (-> metadata
                  (html/select [html/root :> :targets :> [:target (html/attr= :name target-name)] html/text-node])
                  first)]
    (conj v {:target-name target-name
             :target-type :script
             :target-machine-type (or target-machine-type :application-component)
             :context     (conj #{:ss-client-access}
                            (case target-name
                              ("execute"
                               "report")      :deployment
                              ("onvmadd"
                               "onvmremove"
                               "prescale"
                               "postscale")   :scaling))
             :script      script})))
