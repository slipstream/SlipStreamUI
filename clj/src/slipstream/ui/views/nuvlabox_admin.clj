(ns slipstream.ui.views.nuvlabox-admin
    (:require [slipstream.ui.util.localization :as localization]
      [slipstream.ui.util.enlive :as ue]
      [slipstream.ui.views.base :as base]
      [slipstream.ui.util.icons :as icons]
      [net.cgrand.enlive-html :as html]
      [slipstream.ui.views.table :as table]

      [slipstream.ui.views.base :as base]))

(localization/def-scoped-t)

; This is the snippet .ss-usage-gauge-container for each one of the gauges.
(ue/def-blank-snippet ^:private gauge-snip [:div :div]
                      [cloud-usage]
                      ue/this     (ue/set-class   "ss-usage-gauge-container")
                      [:div :div] (ue/set-class   "ss-usage-gauge")
                      [:div :div] (ue/set-id      "ss-usage-gauge-"   (:cloud cloud-usage))
                      [:div :div] (html/set-attr  :data-quota-title   (:cloud cloud-usage))
                      [:div :div] (html/set-attr  :data-quota-max     (:quota cloud-usage))
                      [:div :div] (html/set-attr  :data-quota-current (:current-usage cloud-usage))
                      )

; This is the snippet .ss-usage-container containing all gauges.
(ue/def-blank-snippet ^:private usage-snip [:div :div]
                      [usage]
                      ue/this (ue/set-class "ss-usage-container")
                      ue/this (ue/content-for [:div :div] [cloud-usage usage]
                                              ue/this (html/substitute (gauge-snip cloud-usage))))

(defn- subsection-wan-config
       []
       (table/build
         {:headers [:description :value]
          :rows    [{:cells [{:type :cell/text, :content "Boot protocol"}
                             {:type    :cell/enum, :editable? true,
                              :content {:id   "WAN-bootproto-enum"
                                        :enum [{:value "dhcp" :text "dhcp"}
                                               {:value "static" :text "static"}]}}]}
                    {:class "WAN-ipaddr-row"
                     :cells [{:type :cell/text :content "IP Address"}
                             {:type :cell/text :content {:id "WAN-ipaddr-txt"} :editable? true}]}
                    {:class "WAN-netmask-row"
                     :cells [{:type :cell/text :content "Netmask"}
                             {:type :cell/text :content {:id "WAN-netmask-txt"} :editable? true}]}
                    {:class "WAN-gateway-row"
                     :cells [{:type :cell/text :content "Gateway"}
                             {:type :cell/text :content {:id "WAN-gateway-txt"} :editable? true}]}
                    {:cells [{:type :cell/text, :content "Interface state"}
                             {:type    :cell/enum, :editable? true,
                              :content {:id   "WAN-power-enum"
                                        :enum [{:value "yes" :text "Enable"}
                                               {:value "no" :text "Disable"}]}}]}
                    {:cells [{:type :cell/text :content "Save changes"}
                             {:type    :cell/action-button,
                              :content {:id "WAN-apply-btn" :text "Apply"}}]}
                    ]}))

(defn- subsection-lan-config
  [lan-id]
  (table/build
    {:headers [:description :value]
     :rows    [{:cells [{:type :cell/text, :content "Interface state"}
                        {:type    :cell/enum, :editable? true,
                         :content {:id   (str lan-id "-power-enum")
                                   :enum [{:value "yes" :text "Enable"}
                                          {:value "no" :text "Disable"}]}}]}
               {:cells [{:type :cell/text :content "Save changes"}
                        {:type    :cell/action-button,
                         :content {:id (str lan-id "-apply-btn") :text "Apply"}}]}
               ]}))

(defn- subsection-wlan-config
       []
       (table/build
         {:headers [:description :value]
          :rows    [{:cells [{:type :cell/text :content "SSID"}
                             {:type :cell/text :content {:id "WLAN-ssid-txt"} :editable? true}]}
                    {:cells [{:type :cell/text :content "Password"}
                             {:type :cell/text :content {:id "WLAN-pass-txt"} :editable? true}]}
                    {:cells [{:type :cell/text :content "Channel"}
                             {:type    :cell/enum :editable? true
                              :content {:id "WLAN-channel-enum"
                                        :enum (map (fn [x] {:value x :text x}) (range 1 12))}
                              }]}
                    {:cells [{:type :cell/text, :content "Interface state"}
                             {:type    :cell/enum, :editable? true,
                              :content {:id "WLAN-power-enum"
                                        :enum [{:value "yes" :text "Enable"} {:value "no" :text "Disable"}]}}]}
                    {:cells [{:type :cell/text :content "Save changes"}
                             {:type    :cell/action-button,
                              :content {:id "WLAN-apply-btn" :text "Apply"}}]}
                    ]}))

; content tables are generated dynamicly in nuvlabox-admin.js
(defn- section-services-status []
  {:title (t :section.srvs.title)})

(defn- section-sys-activity []
       (let [dashboard {:quota {:enabled? true, :usage [{:cloud "CPU usage [ % ]", :current-usage nil, :quota nil}
                                                        {:cloud "RAM [ MB ]", :current-usage nil, :quota nil}
                                                        {:cloud "System partition [ MB ]", :current-usage nil, :quota nil}
                                                        {:cloud "Images partition [ GB ]", :current-usage nil, :quota nil}
                                                        {:cloud "Boot partition [ MB ]", :current-usage nil, :quota nil}
                                                        ]}}]
            {:title   (t :section.perf.title)
             :content (-> dashboard :quota :usage usage-snip)
             :type    :flat-section
             }
            ))

(defn- section-ssh-tunnel []
       {:title   (t :section.ssh.title)
        :content [(table/build
                    {:headers [:description :port]
                     :rows    [{:cells [{:type :cell/text :content "Mothership endpoint"}
                                        {:type :cell/text :editable? true :content {:id "mothership-txt" :text "nuv.la"}}
                                        ]}
                               {:cells [{:type :cell/text :content "Mothership user"}
                                        {:type :cell/text :editable? true :content {:id "mothership-user-txt" :text "nuvlabox"}}
                                        ]}
                               {:cells [{:type :cell/text :content "SSH port"}
                                        {:type :cell/text :editable? true :content {:id "ssh-port-txt"}}
                                        ]}
                               {:cells [{:type :cell/text :content "SlipStream port"}
                                        {:type :cell/text :editable? true :content {:id "ss-port-txt"}}
                                        ]}
                               {:cells [{:type :cell/text :content "OpenNebula port"}
                                        {:type :cell/text :editable? true :content {:id "one-port-txt"}}
                                        ]}
                               {:cells [{:type :cell/text, :content "NuvlaBox SSH tunnel state"}
                                        {:type    :cell/enum, :editable? true,
                                         :content {:id   "ssh-tunnel-enum"
                                                   :enum [
                                                          {:value "yes" :text "Enable"}
                                                          {:value "no" :text "Disable"}]
                                                   }}]}
                               {:cells [{:type :cell/text :content "Save changes"}
                                        {:type    :cell/action-button,
                                         :content {:id "ssh-apply-btn" :text "Apply"}}]}
                               ]})]}
       )


; content tables are generated dynamicly in nuvlabox-admin.js
(defn- section-usb-devices-status []
  {:title (t :section.usb.title)})

(defn- section-networking []
       {:title   (t :section.net.title)
        :content [{:title "WAN" :content (subsection-wan-config)}
                  {:title "LAN" :content (subsection-lan-config "LAN")}
                  {:title "WLAN" :content (subsection-wlan-config)}
                  ]})

(defn- section-system-action []
       {:title   (t :section.system.title)
        :content (table/build
                   {:headers [:description :action]
                    :rows    [{:cells [{:type :cell/text :content "Restart the system"}
                                       {:type    :cell/action-button
                                        :content {:id "restart-system-btn" :text "Restart"}}]}
                              {:cells [{:type :cell/text :content "Poweroff the system"}
                                       {:type    :cell/action-button
                                        :content {:id "poweroff-system-btn" :text "Poweroff"}}]}
                              {:cells [{:type :cell/text :content "Erase all data in the system !"}
                                       {:type    :cell/action-button
                                        :content {:id "reset-system-btn" :text "Factory Reset" :action-type :danger}}]}]})}
       )



(defn page
      [metadata]
      (base/generate
        {:html-dependencies {:css-filenames         ["nuvlabox-admin.css"]
                             :external-js-filenames (concat
                                                      (map (partial format "jquery-flot/js/jquery.flot%s.min.js")
                                                           ["" ".pie" ".time" ".stack" ".tooltip" ".resize"])
                                                      (map (partial format "justgage/js/%s.min.js")
                                                           ["raphael.2.1.4" "justgage.1.1.0"]))
                             :internal-js-filenames ["nuvlabox_admin.js"]}
         :header            {:icon     icons/config-nuvlabox
                             :title    (t :header.title)
                             :subtitle (t :header.subtitle)}
         :content           [
                             (section-sys-activity)
                             (section-services-status)
                             (section-usb-devices-status)
                             (section-ssh-tunnel)
                             (section-networking)
                             (section-system-action)
                             ]}))
