(ns slipstream.ui.models.module
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.models.common :as common]
            [slipstream.ui.models.parameters :as parameters]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.models.authz :as authz]))

(def module-root-uri "module/")
(def module-root-uri-length (count module-root-uri))
(def default-cloud-name "default")

(defn attrs
  [module]
  (common/attrs module))

(defn module-name
  [module]
  (common/elem-name module))

(defn module-category
  [module]
  (:category (attrs module)))

(defn module-description
  [module]
  (:description (attrs module)))

(defn module-version
  [module]
  (:version (attrs module)))

(defn module-logo-link
  [module]
  (:logolink (attrs module)))

(defn username
  [module]
  (-> module user/attrs :name))

(defn user
  [module]
  (user/attrs module))

(defn user-attrs
  [module]
  (-> module user attrs))

(defn owner
  [module]
  (-> (authz/authz module) authz/attrs :owner))

(defn module-commit
  [module]
  (first (html/select module [:commit])))

(defn module-commit-comment
  [module]
  (common/content (first (html/select (module-commit module) [:comment]))))

(defn module-commit-author
  [module]
  (:author (common/attrs (module-commit module))))

(defn module-latestversion?
  [module]
  (common/true-value? (:islatestversion (attrs module))))

(defn title-extra
  [module]
  (if (not (module-latestversion? module))
    (str "<i class='icon-warning-sign'></i>You are not on the latest version of this module!")
    (str "")))

(defn titles
  [module]
  (let
    [attrs (attrs module)
     {:keys [name description category]} attrs
     comment (module-commit-comment module)]
    [name description comment category]))

(defn titles-with-version
  [module]
  (let
    [attrs (attrs module)
     {:keys [name description category version]} attrs
     comment (module-commit-comment module)]
    [name (str "Version: " version " - " description) comment category (title-extra module)]))

(defn nodes
  [deployment]
  (html/select deployment [:node]))

(defn content
  [elem]
  (common/content elem))

(defn available-clouds
  [module]
  (flatten
    (map :content (html/select module [:cloudNames :string]))))

(defn published?
  [module]
  (not (empty? (html/select module [:published]))))

(defn parent-uri
  [module]
  (:parenturi (attrs module)))

(defn parent-name
  [module]
  (str
    (apply str
           (drop
             module-root-uri-length
             (parent-uri module)))
    "/"))

(defn new?
  [module]
  (= "new" (:shortname (attrs module))))

(defn base?
  [image]
  (= "true" (:isbase (attrs image))))

(defn image
  [metadata]
  (first (html/select metadata [:image])))

(defn runs
  [module]
  (first (html/select module [:runs])))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 (defn- parse-keyword
   [k]
   (let [[match group-name optional-create-str policy-name]
          (re-matches #"(owner|group|public)(create)?(\w+)" (name k))]
     (when match
       (let [group-key (keyword (str group-name "-access?"))
             optional-create-prefix (when optional-create-str (str optional-create-str "-"))
             policy-key (keyword (str optional-create-prefix policy-name))]
        [:access-rights policy-key group-key]))))

(defn- assoc-authz-setting
  "Assoc into the map 'm' the access rights transforming the original
  keyword into a path. E.g. will merge ':grouppost true' into {:post {:group-access? true}}"
  [m [k v]]
  (if-let [authz-setting-path (parse-keyword k)]
    (assoc-in m authz-setting-path (uc/parse-boolean v))
    m))

(defn- add-rights
  [m attrs]
  (reduce assoc-authz-setting m attrs))

(defn- group-members
  [authz]
  (-> authz
      (html/select [:groupMembers :string html/text-node])
      set))

(defn- authorization
  [metadata]
  (let [authz (first (html/select metadata [:authz]))
        attrs (:attrs authz)]
    (-> {}
        (assoc :inherited-group-members? (-> attrs :inheritedgroupmembers uc/parse-boolean)
               :group-members            (group-members authz))
        (add-rights attrs))))

; (defn- owner
;   [metadata]
;   (let [authz (first (html/select metadata [:authz]))]
;     (-> authz :attrs :owner)))

(defmulti other-sections (comp uc/keywordize :category :attrs))

(defmethod other-sections :project
  [metadata])

(defmethod other-sections :image
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

        )))

(defn assoc-other-sections
  [module-map metadata]
  (merge-with merge module-map (other-sections metadata)))

(defn parse
  [metadata]
  (let [attrs (:attrs metadata)]
    (-> {}
        (assoc-in [:summary :description]       (-> attrs :description))
        (assoc-in [:summary :category]          (-> attrs :category))
        (assoc-in [:summary :name]              (-> attrs :name))
        (assoc-in [:summary :creation]          (-> attrs :creation))
        (assoc-in [:summary :version]           (-> attrs :version uc/parse-pos-int))
        (assoc-in [:summary :short-name]        (-> attrs :shortname))
        (assoc-in [:summary :last-modified]     (-> attrs :lastmodified))
        (assoc-in [:summary :latest-version?]   (-> attrs :islatestversion uc/parse-boolean))
        (assoc-in [:summary :deleted?]          (-> attrs :deleted uc/parse-boolean))
        (assoc-in [:summary :uri]               (-> attrs :resourceuri))
        (assoc-in [:summary :parent-uri]        (-> attrs :parenturi))
        (assoc-in [:summary :owner]             (-> metadata (html/select [:authz]) first :attrs :owner))
        (assoc     :authorization    (authorization metadata))
        (assoc-other-sections metadata))))


;; Note: The parse function above will generate a map with the following structure:

; {:summary {:deleted? false
;            :creation "2013-05-16 17:04:39.113 CEST"
;            :name "Public/OtherProject"
;            :short-name "OtherProject"
;            :owner "sixsq"
;            :version 1
;            :uri "module/Public/OtherProject/1"
;            :latest-version? true
;            :last-modified "2013-05-16 17:04:39.113 CEST"
;            :parent-uri "module/Public"
;            :description "Another description..."
;            :category "Project"
;            }
;  :authorization {:access-rights {:create-children {:public-access? false
;                                                    :group-access? true
;                                                    :owner-access? true}
;                                  :delete {:owner-access? true
;                                           :public-access? false
;                                           :group-access? false}
;                                  :put {:owner-access? false
;                                        :public-access? false
;                                        :group-access? true}
;                                  :post {:group-access? false
;                                         :owner-access? true
;                                         :public-access? false}
;                                  :get {:group-access? true
;                                        :public-access? true
;                                        :owner-access? true}}
;                  :group-members #{"meb" "other" "konstan" "xxx"}
;                  :inherited-group-members? false}}


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