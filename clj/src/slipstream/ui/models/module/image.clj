(ns slipstream.ui.models.module.image
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.runs :as runs]
            [slipstream.ui.models.parameters :as parameters]))

(localization/def-scoped-t)

;; Cloud image section

(defn- cloud-image-details
  [metadata]
  {:native-image?     (-> metadata :attrs :isBase uc/parse-boolean)
   :cloud-identifiers (->> (html/select metadata [:cloudImageIdentifier])
                           (map :attrs)
                           (map (juxt :cloudServiceName :cloudImageIdentifier))
                           (into (zipmap
                                   (->> (html/select metadata [:cloudNames :string html/text])
                                        (remove #{"default"}))
                                   (repeat nil))))
   :reference-image   (-> metadata :attrs :moduleReferenceUri (uc/trim-prefix "module/"))})


;; Os details section

(def ^:private platforms
  ["centos"
   "debian"
   "fedora"
   "opensuse"
   "redhat"
   "sles"
   "ubuntu"
   "windows"
   "other"])

(defn- os-details
  [metadata]
  {:platform         (->> metadata :attrs :platform (u/enum platforms :cloud-platforms))
   :login-username   (-> metadata :attrs :loginUser)})


;; Targets section

(defn- conj-recipe-target
  [v target-name metadata]
  (let [recipe (-> metadata
                   (html/select [(keyword target-name) html/text-node])
                   first)]
    (conj v {:target-name target-name
             :target-type :script
             :context     #{:image-creation}
             :script      recipe})))

(defn- parse-package
  [package-metadata]
  (select-keys (:attrs package-metadata) [:key :name :repository]))

(defn- conj-packages-target
  [v target-name metadata]
  (let [packages (->> (html/select metadata [:package])
                      (map parse-package)
                      (sort-by :name)
                      vec)]
    (conj v {:target-name "packages"
             :target-type :packages
             :context     #{:image-creation}
             :packages    packages})))

(defn- conj-script-target
  [v target-name metadata]
  (let [script (-> metadata
                  (html/select [[:target (html/attr= :name target-name)] html/text-node])
                  first)]
    (conj v {:target-name target-name
             :target-type :script
             :context     (conj #{:ss-client-access}
                            (case target-name
                              ("execute" "report")      :deployment
                              ("onvmadd" "onvmremove")  :scaling))
             :script      script})))

(defn- targets
  [metadata]
  (-> []
      ; Image creation recipes and packages
      (conj-recipe-target   "prerecipe"   metadata)
      (conj-packages-target "packages"    metadata)
      (conj-recipe-target   "recipe"      metadata)
      ; Deployment recipes
      (conj-script-target   "execute"     metadata)
      (conj-script-target   "report"      metadata)
      (conj-script-target   "onvmadd"     metadata)
      (conj-script-target   "onvmremove"  metadata)))


;; Deployment parameters section

(def ^:private default-parameters-order
  {"instanceid" 1
   "hostname"   2})

(defn- transform-default-parameters
  [parameter]
  (if-let [new-order (-> parameter :name default-parameters-order)]
    (assoc parameter
      :order new-order
      :disabled? true
      :description  (->> parameter :name (format "deployment.default-parameter.%s.description") t)
      :placeholder  (->> parameter :name (format "deployment.default-parameter.%s.placeholder") t)
      :help-hint    (->> parameter :name (format "deployment.default-parameter.%s.help-hint") t))
    parameter))

(defn- deployment-parameters
  [parameters]
  (->> (parameters/categories-of-type parameters :deployment)
       parameters/flatten
       (map transform-default-parameters)
       (sort-by (juxt :order :category :name))
       vec))


;; Metadata section build

(defn sections
  [metadata]
  (let [parameters (parameters/parse metadata)]
    (cond->
        {:cloud-image-details         (cloud-image-details    metadata)
         :os-details                  (os-details             metadata)
         :cloud-configuration         (parameters/categories-of-type parameters :global)
         :deployment-parameters       (deployment-parameters  parameters)
         :targets                     (targets                metadata)}
      (page-type/view?) (assoc :runs  (runs/parse metadata)))))

