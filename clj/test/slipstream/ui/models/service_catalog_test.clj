(ns slipstream.ui.models.service-catalog-test
  (:use [expectations])
  (:require [slipstream.ui.util.core :as u]
            [slipstream.ui.models.service-catalog :as model]))

(def raw-metadata-str
  "<serviceCatalogues>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/loco' cloud='loco' creation='2013-11-12 20:21:20.192 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='loco.loco.support.email' description='Support email adress for this cloud service' mandatory='true' type='String' readonly='false' category='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='loco.single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' mandatory='true' type='String' readonly='false' category='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='loco.overall.ram' description='Overall available RAM' mandatory='true' type='String' readonly='false' category='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='loco.overall.cpu' description='Overall available CPUs (cores)' mandatory='true' type='String' readonly='false' category='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='loco.single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' mandatory='false' type='String' readonly='false' category='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='loco.general.description' description='General description of the cloud service (including optional further links)' mandatory='true' type='String' readonly='false' category='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
      <serviceCatalog deleted='false' resourceUri='servicecatalog/toto-tata' cloud='toto-tata' creation='2013-11-12 20:21:20.98 CET'>
         <parameters class='org.hibernate.collection.PersistentMap'>
            <entry>
               <string><![CDATA[support.email]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='toto-tata.support.email' description='Support email adress for this cloud service' mandatory='true' type='String' readonly='false' category='General'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[mickey@disney.com]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='toto-tata.single.vm.max.cpu' description='Maximum number of CPUs (cores) for a single VM' mandatory='true' type='String' readonly='true' category='Single_VM'>
                  <instructions><![CDATA[You won't be able to provision more for a single VM]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='toto-tata.overall.ram' description='Overall available RAM' mandatory='true' type='String' readonly='false' category='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[overall.cpu]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='toto-tata.overall.cpu' description='Overall available CPUs (cores)' mandatory='true' type='String' readonly='false' category='Overall'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[single.vm.max.ram]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='toto-tata.single.vm.max.ram' description='Maximum number of RAM (GB) for a single VM' mandatory='true' type='String' readonly='false' category='Single_VM'>
                  <instructions><![CDATA[]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
            <entry>
               <string><![CDATA[general.description]]></string>
               <parameter class='com.sixsq.slipstream.persistence.ServiceCatalogParameter' name='toto-tata.general.description' description='General description of the cloud service (including optional further links)' mandatory='true' type='String' readonly='false' category='General'>
                  <instructions><![CDATA[This is an instruction]]></instructions>
                  <value><![CDATA[]]></value>
               </parameter>
            </entry>
         </parameters>
      </serviceCatalog>
   <user issuper='true' resourceUri='user/super' name='loco.super'></user>
   </serviceCatalogues>")

(def parsed-metadata
  {:items [{:cloud "loco"
            :creation "2013-11-12 20:21:20.192 CET"
            :uri "servicecatalog/loco"
            :parameters [{:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "General"
                          :description "General description of the cloud service (including optional further links)"
                          :type "String"
                          :name "loco.general.description"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "General"
                          :description "Support email adress for this cloud service"
                          :type "String"
                          :name "loco.loco.support.email"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "Overall"
                          :description "Overall available CPUs (cores)"
                          :type "String"
                          :name "loco.overall.cpu"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "Overall"
                          :description "Overall available RAM"
                          :type "String"
                          :name "loco.overall.ram"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "Single_VM"
                          :description "Maximum number of CPUs (cores) for a single VM"
                          :type "String"
                          :name "loco.single.vm.max.cpu"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "Single_VM"
                          :description "Maximum number of RAM (GB) for a single VM"
                          :type "String"
                          :name "loco.single.vm.max.ram"}]}
           {:cloud "toto-tata"
            :creation "2013-11-12 20:21:20.98 CET"
            :uri "servicecatalog/toto-tata"
            :parameters [{:help-hint "This is an instruction"
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "General"
                          :description "General description of the cloud service (including optional further links)"
                          :type "String"
                          :name "toto-tata.general.description"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value "mickey@disney.com"
                          :category "General"
                          :description "Support email adress for this cloud service"
                          :type "String"
                          :name "toto-tata.support.email"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "Overall"
                          :description "Overall available CPUs (cores)"
                          :type "String"
                          :name "toto-tata.overall.cpu"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "Overall"
                          :description "Overall available RAM"
                          :type "String"
                          :name "toto-tata.overall.ram"}
                         {:help-hint "You won't be able to provision more for a single VM"
                          :read-only? true
                          :order 2147483647
                          :value nil
                          :category "Single_VM"
                          :description "Maximum number of CPUs (cores) for a single VM"
                          :type "String"
                          :name "toto-tata.single.vm.max.cpu"}
                         {:help-hint nil
                          :read-only? false
                          :order 2147483647
                          :value nil
                          :category "Single_VM"
                          :description "Maximum number of RAM (GB) for a single VM"
                          :type "String"
                          :name "toto-tata.single.vm.max.ram"}]}]})

(expect
  parsed-metadata
  (-> raw-metadata-str u/clojurify-raw-metadata-str model/parse))
