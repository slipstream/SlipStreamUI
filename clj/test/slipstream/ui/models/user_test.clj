(ns slipstream.ui.models.user-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.util.clojure :as uc]
            [slipstream.ui.util.localization :as localization]
            [slipstream.ui.models.user :as model]
            [slipstream.ui.models.parameters :as parameters-model]))

(def raw-metadata-str
  (uc/slurp-resource "slipstream/ui/mockup_data/metadata_user.xml"))

(expect
  {:username      "alice"
   :first-name    "Alice"
   :last-name     "Persona"
   :uri           "user/alice"
   :organization  "ACME"
   :email         "alice@example.com"
   :state         "ACTIVE"
   :creation      "2015-03-12 18:15:20.983 CET"
   :deleted?      false
   :super?        false
   :configuration {:available-clouds  [{:value "Cloud1", :text "Cloud1"}
                                       {:value "Cloud2", :text "Cloud2"}
                                       {:value "Cloud3", :text "Cloud3 *", :default? true, :selected? true}
                                       {:value "Cloud4", :text "Cloud4"}] ; General.default.cloud.service
                   :keep-running  :on-success    ; General.keep-running
                   :ssh-keys      "some-ssh-key" ; General.ssh.public.key
                  }}
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse (dissoc :parameters))))

(expect
  [{:category-type :general, :category "General", :parameters [{:help-hint "Select the cloud that you want to use as the default.", :mandatory? true, :read-only? false, :order 10, :value [{:selected? true, :value "Cloud3", :text "Cloud3"}
                                                            {:value "Cloud4", :text "Cloud4"}
                                                            {:value "Cloud1", :text "Cloud1"}
                                                            {:value "Cloud2", :text "Cloud2"}], :category "General", :description "Default Cloud", :type "Enum", :name "General.default.cloud.service"}
                                                            {:help-hint "Set the action to take in the Ready state. <br/>This parameter doesn't apply to <code>mutable</code> Runs and to <code>build image</code> Runs. <br/><strong>On success</strong> is useful for deployment or long tests. </br><strong>On error</strong> is useful to investigate an error if any.", :mandatory? true, :read-only? false, :order 15, :value [{:value "always", :text "Always"}
                                                            {:value "never", :text "Never"}
                                                            {:selected? true, :value "on-success", :text "On Success"}
                                                            {:value "on-error", :text "On Error"}], :category "General", :description "Auto-terminate application after deployment", :type "Enum", :name "General.keep-running"}
                                                            {:help-hint "This value is useful for deployment or long tests.", :mandatory? true, :read-only? false, :order 20, :value true, :category "General", :description "If NO errors occur, keep the execution running", :type "Boolean", :name "General.On Success Run Forever"}
                                                            {:help-hint "This value is useful to investigate the error.", :mandatory? true, :read-only? false, :order 30, :value false, :category "General", :description "If ANY error occurs, keep the execution running", :type "Boolean", :name "General.On Error Run Forever"}
                                                            {:help-hint "0 - Actions,  1 - Steps,  2 - Details data,  3 - Debugging", :mandatory? true, :read-only? false, :order 30, :value [{:selected? true, :value "0", :text "0 - Actions"}
                                                            {:value "1", :text "1 - Steps"}
                                                            {:value "2", :text "2 - Details data"}
                                                            {:value "3", :text "3 - Debugging"}], :category "General", :description "Level of verbosity", :type "Enum", :name "General.Verbosity Level"}
                                                            {:help-hint "If the execution stays in a transitional state for more than the value of this timeout, the execution is forcefully terminated.", :mandatory? true, :read-only? false, :order 40, :value "30", :category "General", :description "Execution timeout (in minutes)", :type "String", :name "General.Timeout"}
                                                            {:help-hint "Warning: Some clouds may take into account only the first key until SlipStream bootstraps the machine.", :mandatory? true, :read-only? false, :order 50, :value "some-ssh-key", :category "General", :description "SSH Public Key(s) (one per line)", :type "RestrictedText", :name "General.ssh.public.key"}]}
   {:category-type :global, :category "AmazonEC2", :parameters [{:help-hint "This is required in order to be able to deploy to this EC2 cloud. You can find the <code>access id</code> by logging into the AWS Console and going to the Security Credentials page.", :mandatory? true, :read-only? false, :order 0, :value "amazonec2_access_id", :category "AmazonEC2", :description "Access id", :type "String", :name "AmazonEC2.access.id"}
                                                              {:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "20", :category "AmazonEC2", :description "Number of VMs the user can start for this cloud", :type "String", :name "AmazonEC2.quota.vm"}
                                                              {:help-hint "This is required in order to be able to deploy to this EC2 cloud. You can find the <code>secret key</code> by logging into the AWS Console and going to the Security Credentials page.", :mandatory? true, :read-only? false, :order 0, :value "amazonec2_secret_key", :category "AmazonEC2", :description "Secret key", :type "Password", :name "AmazonEC2.secret.key"}]}
   {:category-type :global, :category "Cloud1", :parameters [{:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "20", :category "Cloud1", :description "Number of VMs the user can start for this cloud", :type "String", :name "Cloud1.quota.vm"}
                                                             {:help-hint nil, :mandatory? true, :read-only? false, :order 10, :value "cloud1_username", :category "Cloud1", :description "StratusLab account username", :type "RestrictedString", :name "Cloud1.username"}
                                                             {:help-hint nil, :mandatory? true, :read-only? false, :order 20, :value "cloud1_password", :category "Cloud1", :description "StratusLab account password", :type "Password", :name "Cloud1.password"}]}
   {:category-type :global, :category "Cloud2", :parameters [{:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "20", :category "Cloud2", :description "Number of VMs the user can start for this cloud", :type "String", :name "Cloud2.quota.vm"}
                                                             {:help-hint nil, :mandatory? true, :read-only? false, :order 10, :value "cloud2_key", :category "Cloud2", :description "Key", :type "RestrictedString", :name "Cloud2.username"}
                                                             {:help-hint nil, :mandatory? true, :read-only? false, :order 20, :value "cloud2_secret", :category "Cloud2", :description "Secret", :type "Password", :name "Cloud2.password"}]}
   {:category-type :global, :category "Cloud3", :parameters [{:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "20", :category "Cloud3", :description "Number of VMs the user can start for this cloud", :type "String", :name "Cloud3.quota.vm"}
                                                             {:help-hint nil, :mandatory? true, :read-only? false, :order 10, :value "cloud3_key", :category "Cloud3", :description "Key", :type "RestrictedString", :name "Cloud3.username"}
                                                             {:help-hint nil, :mandatory? true, :read-only? false, :order 20, :value nil, :category "Cloud3", :description "Secret", :type "Password", :name "Cloud3.password"}]}
   {:category-type :global, :category "Cloud4", :parameters [{:help-hint "This is required in order to be able to deploy to this EC2 cloud. You can find the <code>access id</code> by logging into the AWS Console and going to the Security Credentials page.", :mandatory? true, :read-only? false, :order 0, :value nil, :category "Cloud4", :description "Access id", :type "String", :name "Cloud4.access.id"}
                                                             {:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "20", :category "Cloud4", :description "Number of VMs the user can start for this cloud", :type "String", :name "Cloud4.quota.vm"}
                                                             {:help-hint "This is required in order to be able to deploy to this EC2 cloud. You can find the <code>secret key</code> by logging into the AWS Console and going to the Security Credentials page.", :mandatory? true, :read-only? false, :order 0, :value "cloud4_secret_key", :category "Cloud4", :description "Secret key", :type "Password", :name "Cloud4.secret.key"}]}
   {:category-type :global, :category "Exoscale", :parameters [{:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "12", :category "Exoscale", :description "Number of VMs the user can start for this cloud", :type "String", :name "Exoscale.quota.vm"}
                                                               {:help-hint nil, :mandatory? true, :read-only? false, :order 10, :value "keyforexoscale", :category "Exoscale", :description "Key", :type "RestrictedString", :name "Exoscale.username"}
                                                               {:help-hint nil, :mandatory? true, :read-only? false, :order 20, :value nil, :category "Exoscale", :description "Secret", :type "Password", :name "Exoscale.password"}]}
   {:category-type :global, :category "Exoscale-2", :parameters [{:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "20", :category "Exoscale-2", :description "Number of VMs the user can start for this cloud", :type "String", :name "Exoscale-2.quota.vm"}
                                                                 {:help-hint nil, :mandatory? true, :read-only? false, :order 10, :value nil, :category "Exoscale-2", :description "Key", :type "RestrictedString", :name "Exoscale-2.username"}
                                                                 {:help-hint nil, :mandatory? true, :read-only? false, :order 20, :value nil, :category "Exoscale-2", :description "Secret", :type "Password", :name "Exoscale-2.password"}]}
   {:category-type :global, :category "StratusLab", :parameters [{:help-hint nil, :mandatory? true, :read-only? true, :order 0, :value "20", :category "StratusLab", :description "Number of VMs the user can start for this cloud", :type "String", :name "StratusLab.quota.vm"}
                                                                 {:help-hint nil, :mandatory? true, :read-only? false, :order 10, :value nil, :category "StratusLab", :description "StratusLab account username", :type "RestrictedString", :name "StratusLab.username"}
                                                                 {:help-hint nil, :mandatory? true, :read-only? false, :order 20, :value nil, :category "StratusLab", :description "StratusLab account password", :type "Password", :name "StratusLab.password"}]}]
  (localization/with-lang :en
    (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse :parameters)))