(ns slipstream.ui.models.module.image
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.page-type :as page-type]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.module.util :as mu]
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

(defn- targets
  [metadata]
  (-> []
      ; Image creation recipes and packages
      (mu/conj-recipe-target   "prerecipe"   metadata)
      (mu/conj-packages-target "packages"    metadata)
      (mu/conj-recipe-target   "recipe"      metadata)
      ; Deployment recipes
      (mu/conj-script-target   "execute"     metadata)
      (mu/conj-script-target   "report"      metadata)
      (mu/conj-script-target   "onvmadd"     metadata)
      (mu/conj-script-target   "onvmremove"  metadata)
      ; NOTE: To display the prescale and postscale scripts, just decomment these 2 lines:
      ; (mu/conj-script-target   "prescale"    metadata)
      ; (mu/conj-script-target   "postscale"   metadata)
      ))


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

