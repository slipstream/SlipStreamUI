(ns slipstream.ui.views.header
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.utils :as utils]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.views.common :as common]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.string :as string])
  (:import (java.util.regex Pattern)))

(def header-sel [:#header])

(def header-menu [:.menu_bar])

(html/defsnippet header-top-bar-snip common/header-template-html header-menu
  [{username :username issuper :issuper}]
  [:#header-username :> :a] (html/do-> 
                                (html/content username)
                                (html/set-attr :href (str "/user/" username)))
  [:#header-username] (if (= nil username)
                          nil
                          identity)
  [#{:#header-users :#header-config}] (if (= "true" issuper)
                                            identity
                                            nil)
  [:#header-loginout :a] (if (= nil username)
                          (html/do-> 
                                (html/content "login")
                                (html/set-attr :href "/login"))
                          identity))

(def titles-sel [:#titles])

(html/defsnippet header-titles common/header-template-html titles-sel
  [{title :title title-sub :title-sub title-desc :title-desc}]
  [:#header-title] (html/content title)
  [:#header-title-sub] (html/content title-sub)
  [:#header-title-desc] (html/content title-desc))

(def breadcrumb-sel [:#breadcrumb])

(defn- breadcrumb-href
  "root-uri, e.g. 'module/' in the case of modules, or 'user/'
   in the case of users"
  [names index root-uri]
  (if (= "" (names index))
    root-uri
    (str 
      root-uri 
      "/" 
      (reduce 
        #(str %1 (if (= "" %1) "" "/") %2) 
        "" 
        (subvec names 0 (inc index))))))
                                 
(html/defsnippet header-breadcrumb common/header-template-html breadcrumb-sel
  [{name :name root-uri :root-uri}]
  [[:li (html/nth-of-type 2)] :> :a] (html/do->
                                       (html/content root-uri)
                                       (html/set-attr :href root-uri))
  [[:li (html/nth-of-type 3)] :> :a] (html/clone-for 
                                       [i (range (count (string/split name #"/")))] 
                                       (let 
                                         [names (string/split name #"/")
                                          href (breadcrumb-href names i root-uri)
                                          short-name (names i)]
                                         (html/do->
                                           (html/content (if (= "" short-name)
                                                           root-uri short-name))
                                           (html/set-attr :href href)))))

(defn- gen-titles [title sub desc]
  {:title title :title-sub sub :title-desc desc})

(defn- gen-module-titles [module]
  (gen-titles 
    (:name (:attrs module))
    (str "Version: " (:version (:attrs module)))
    (:description (:attrs module))))

(defn- titles [root]
  (case (:tag root)
    :list (gen-titles "Projects" "All projects" "This root project is shared with all SlipStream users")
    (gen-module-titles root)))

(defn- root-uri [root]
  (if (= :list (:tag root))
    "module"
    (first (string/split 
             (-> root :attrs :resourceuri)
             #"/"))))

(html/defsnippet header common/header-template-html header-sel
  [root edit?]
  header-menu (html/substitute
                (header-top-bar-snip
                  (user/attrs root)))
  titles-sel (html/substitute
               (header-titles
                 (titles root)))
  breadcrumb-sel (html/substitute
                   (header-breadcrumb
                     {:name (if (= :list (:tag root))
                              "" 
                              (-> root :attrs :name))
                      :root-uri (root-uri root)}))
  common/interaction-sel (html/substitute
                           (common/header-buttons
                             {:buttons (common/buttons root edit?)})))

(html/defsnippet header-top-only common/header-template-html header-sel
  []
  header-menu (html/substitute
                (header-top-bar-snip
                  (user/attrs nil)))
  titles-sel nil
  breadcrumb-sel nil
  common/interaction-sel nil)
