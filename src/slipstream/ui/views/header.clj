(ns slipstream.ui.views.header
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.models.user :as user]
            [slipstream.ui.views.common :as common]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.string :as string])
  (:import (java.util.regex Pattern)))

(def header-sel [:#header])
(def header-titles-sel [:#titles])

(def title-max-size 40)

; titles
; TODO: Refactor this
(def header-summary-sel [:#titles])

(def header-template-html "slipstream/ui/views/header.html")

(def header-top-bar-sel [:.menu_bar])

(html/defsnippet header-top-bar-snip header-template-html header-top-bar-sel
  [{username :name issuper :issuper}]
  [:#header-user :> :a] (html/do-> 
                          (html/html-content
                            (str username " <i class='icon-user icon-2x'>"))
                          (html/set-attr :href (str "/user/" username)))
  [:#header-user] (if (= nil username)
                    nil
                    identity)
  [#{:#header-users :#header-config}] (if (= "true" issuper)
                                        identity
                                        nil)
  [:#header-loginout :a] (if (= nil username)
                           (html/do-> 
                             (html/html-content "<i class='icon-signin icon-2x'>")
                             (html/set-attr :href "/login"))
                           identity))

(def titles-sel [:#titles])

(def category-map
  {"Image" "icon-desktop"
   "Project" "icon-folder-open"
   "Deployment" "icon-cloud-upload"
   "Error" "icon-cloud-error"
   "Users" "icon-group"
   "User" "icon-user"
   "Configuration" "icon-wrench"
   "Dashboard" "icon-dashboard"
   "Action" "icon-bolt"})

(html/defsnippet header-titles-snip header-template-html titles-sel
  [title title-sub title-desc category & extra]
  [:#header-title] (html/do->
                     (html/remove-class "project_category")
                     (html/add-class (common/to-css-class category))
                     (html/content (common/ellipse-left title title-max-size)))
  [:#header-title-sub] (html/html-content title-sub)
  [:#header-title-desc] (html/content title-desc)
  [:#header-title-extra] (if extra
                           (html/html-content (first extra))
                           identity))

(defn header-titles
  [title title-sub title-desc category extra]
  (header-titles-snip 
    {title :title title-sub :title-sub title-desc :title-desc category :category extra :extra}))

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

(html/defsnippet header-breadcrumb-snip header-template-html breadcrumb-sel
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

(defn- gen-titles [title sub desc category]
  {:title title 
   :title-sub sub 
   :title-desc desc 
   :category category})

(defn- gen-module-titles [module]
  (gen-titles 
    (:name (:attrs module))
    (str "Version: " (:version (:attrs module)))
    (:description (:attrs module))
    (:category  (:attrs module))))

(defn titles [root]
  (case (:tag root)
    :list (gen-titles 
            "Projects" 
            "All projects" 
            "This root project is shared with all HNX users"
            "Project")
    (gen-module-titles root)))

(defn root-uri [root]
  (if (or (= :list (:tag root)) (nil? root))
    "module"
    (first (string/split 
             (-> root :attrs :resourceuri)
             #"/"))))

(html/defsnippet header-top-only-snip header-template-html header-top-bar-sel
  [metadata]
  header-top-bar-sel (html/substitute
                       (header-top-bar-snip
                         (user/attrs metadata)))
  titles-sel nil
  breadcrumb-sel nil
  common/interaction-sel nil)
