(ns slipstream.ui.models.module.image
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.parameters :as parameters]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Runs metadata section

(defn- parse-run
  [run-metadata]
  (let [attrs (:attrs run-metadata)]
    (-> attrs
        (select-keys [:tags
                      :status
                      :uuid
                      :username])
        (assoc        :start-time  (:starttime attrs))
        (assoc        :module-uri  (:moduleresourceuri attrs))
        (assoc        :uri         (:resourceuri attrs))
        (assoc        :cloud-name  (:cloudservicename attrs)))))

(defn- group-runs
  [runs]
  (uc/coll-grouped-by :cloud-name runs
                      :items-keyword :runs))

(defn- runs
  [metadata]
  (->> (html/select metadata [:runs :item])
       (map parse-run)
       group-runs))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Image creation metadata section

(defn- parse-recipe
  [recipe-type metadata]
  {:code (-> metadata (html/select [recipe-type html/text-node]) first)})

(defn- parse-package
  [package-metadata]
  (let [attrs (:attrs package-metadata)]
    (-> attrs
        (select-keys [:key
                      :name
                      :repository]))))

(defn- image-creation
  [metadata]
  {:recipe      (parse-recipe :recipe metadata)
   :packages    (->> (html/select metadata [:package])
                     (map parse-package)
                     (sort-by :name))
   :pre-recipe  (parse-recipe :prerecipe metadata)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Deployment metadata section

(def ^:private target-names
  {:execute       "execute"
   :report        "report"
   :parameters    "parameters"
   :on-vm-add     "onvmadd"
   :on-vm-remove  "onvmremove"})

(defn- assoc-target
  [m target metadata]
  (let [target-metadata (-> metadata
                            (html/select [[:target (html/attr= :name (target-names target))]])
                            first)]
    (assoc-in m [:targets target]
        {:code              (-> target-metadata (html/select [html/text-node]) first)
         :run-in-background (-> target-metadata :attrs :runinbackground uc/parse-boolean)})))


(defn- deployment
  [metadata]
  (-> {}
      (assoc-target :execute metadata)
      (assoc-target :report metadata)
      (assoc-target :on-vm-add metadata)
      (assoc-target :on-vm-remove metadata)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Metadata section build

(defn sections
  [metadata]
  (let [parameters (parameters/parse metadata)]
    (-> {}
        (assoc-in [:summary :img-src] (-> metadata :attrs :logolink))

        (assoc-in [:cloud-image-details :native-image?]     (-> metadata :attrs :isbase uc/parse-boolean))
        (assoc-in [:cloud-image-details :cloud-identifiers] (->> (html/select metadata [:cloudImageIdentifier])
                                                                 (map :attrs)
                                                                 (map (juxt :cloudservicename :cloudimageidentifier))
                                                                 (into {})))
        (assoc-in [:cloud-image-details :reference-image]   (-> metadata :attrs :modulereferenceuri))

        (assoc-in [:os-details :platform]         (-> metadata :attrs :platform))
        (assoc-in [:os-details :login-username]   (-> metadata :attrs :loginuser))

        (assoc-in [:cloud-configuration]          (-> parameters
                                                      (parameters/categories-of-type :global)))

        (assoc-in [:image-creation]               (image-creation metadata))
        (assoc-in [:deployment]                   (deployment metadata))
        (assoc-in [:runs]                         (runs metadata))
        )))


;; Note: The parse function above will generate a map with the following structure:

; {:cloud-image-details {:reference-image nil
;                        :cloud-identifiers {"stratuslab" "HZTKYZgX7XzSokCHMB60lS0wsiv", "my-cloud" "abc"}
;                        :native-image? false}
;  :os-details {:login-username "donald"
;               :platform "debian"}
;  :cloud-configuration ({:category "Cloud"
;                         :parameters ({:order 2147483647
;                                       :value "Public"
;                                       :description "Network type"
;                                       :type "Enum"
;                                       :name "network"}
;                                       {:order 2147483647
;                                         :value "youshouldnotseethis"
;                                         :description "A password"
;                                         :type "Password"
;                                         :name "password"})}
;                         {:category "Output"
;                         :parameters ({:order 2147483647
;                                       :value "123.234.345"
;                                       :description "hostname/ip of the image"
;                                       :type "String"
;                                       :name "hostname"}
;                                       {:order 2147483647
;                                       :value nil
;                                       :description "Cloud instance id"
;                                       :type "String"
;                                       :name "instanceid"})}
;                         {:category "stratuslab"
;                         :parameters ({:order 2147483647
;                                       :value nil
;                                       :description "Requested CPUs"
;                                       :type "String"
;                                       :name "stratuslab.cpu"}
;                                       {:order 2147483647
;                                       :value "VIRTIO"
;                                       :description "VM disks bus type"
;                                       :type "Enum"
;                                       :name "stratuslab.disks.bus.type"}
;                                       {:order 2147483647
;                                       :value "M1_SMALL"
;                                       :description "Cloud instance type"
;                                       :type "Enum"
;                                       :name "stratuslab.instance.type"}
;                                       {:order 2147483647
;                                       :value nil
;                                       :description "Requested RAM (in GB)"
;                                       :type "String"
;                                       :name "stratuslab.ram"})})
; :runs ({:cloud-name "interoute"
;         :runs [{:cloud-name "interoute"
;                  :uri "run/e8d0b957-14a8-4e96-8677-85c7bd9eb64e"
;                  :module-uri "module/Mebster/word_press/simple_deployment/410"
;                  :start-time "2013-07-04 17:11:56.340 CEST"
;                  :username "mickey"
;                  :uuid "e8d0b957-14a8-4e96-8677-85c7bd9eb64e"
;                  :status "Aborting"
;                  :tags "this is a tag!"}]}
;         {:cloud-name "stratuslab"
;          :runs [{:cloud-name "stratuslab"
;                   :uri "run/638f04c3-44a1-41c7-90db-c81167fc6f19"
;                   :module-uri "module/Public/Tutorials/HelloWorld/client_server/11"
;                   :start-time "2013-07-05 17:27:12.471 CEST"
;                   :username "donald"
;                   :uuid "638f04c3-44a1-41c7-90db-c81167fc6f19"
;                   :status "Aborting"}]})
; :authorization {:access-rights {:create-children {:public-access? false
;                                                    :group-access? false
;                                                    :owner-access? true}
;                                  :delete {:owner-access? true
;                                           :public-access? false
;                                           :group-access? false}
;                                  :put {:owner-access? true
;                                        :public-access? false
;                                        :group-access? false}
;                                  :post {:group-access? true
;                                         :owner-access? true
;                                         :public-access? false}
;                                  :get {:group-access? true
;                                        :public-access? true
;                                        :owner-access? true}}
;                  :group-members #{}
;                  :inherited-group-members? true}
;  :summary {:deleted? false
;            :creation "2013-03-07 21:03:09.124 CET"
;            :name "Public/BaseImages/with-a-very-long-name/Ubuntu/12.04"
;            :short-name "12.04"
;            :img-src "http://s.w.org/about/images/logos/wordpress-logo-stacked-rgb.png"
;            :owner "sixsq"
;            :version 4
;            :uri "module/Public/BaseImages/Ubuntu/12.04"
;            :latest-version? nil
;            :last-modified "2013-03-07 21:03:09.337 CET"
;            :parent-uri "module/Public/BaseImages/Ubuntu/toto"
;            :description "Nice Ubuntu distro"
;            :category "Image"}}