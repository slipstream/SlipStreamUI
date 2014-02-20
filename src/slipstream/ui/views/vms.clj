(ns slipstream.ui.views.vms
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.module :as module-model]
            [slipstream.ui.models.common :as common-model]
            [slipstream.ui.views.common :as common]
            [slipstream.ui.views.run :as run]
            [slipstream.ui.views.base :as base]))

(def vms-template-html "slipstream/ui/views/vms-template.html")

(def vms-sel [:#vms])
(def vms-fragment-sel [:#fragment-vms-somecloud])

(html/defsnippet vms-for-cloud-snip vms-template-html [vms-fragment-sel :> :table]
  [vms _]
  [:tbody :> :tr] (html/clone-for
                    [vm vms
                     :let 
                     [attrs (module-model/attrs vm)]]
                    [[:a]] (html/do->
                             (html/set-attr :href (str "/run/" (:runuuid attrs)))
                             (html/content (run/shorten-runid (:runuuid attrs))))
                    [[:td (html/nth-of-type 2)]] (html/content (:state attrs))
                    [[:td (html/nth-of-type 3)]] (html/content (:user attrs))
                    [[:td (html/nth-of-type 4)]] (html/content (:instanceid attrs))))

(html/defsnippet vms-snip vms-template-html vms-sel
  [grouped-by-cloud]
  [:ul :> [:li html/last-of-type]] nil
  [:ul :> :li] 
  (common/tab-headers grouped-by-cloud "vms")
  [:ul] (if (empty? grouped-by-cloud)
          nil
          identity)

  vms-fragment-sel (if (empty? grouped-by-cloud)
                     (common/emtpy-section "No virtual machine currently available")
                     (common/tab-sections grouped-by-cloud "vms" vms-for-cloud-snip))
  [:#fragment-vms-cloudb] nil)

(html/deftemplate vms-template vms-template-html
  [vms]
  vms-sel (html/substitute (vms-snip vms))
  [vms-sel [:a (html/attr= :href "/run/Unknown")]] (html/add-class "inactive"))

(defn page [vms]
  (vms-template
    (common-model/group-by-key
      :cloud
      (html/select vms [:vm]))))
