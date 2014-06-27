(ns slipstream.common.reload
  (:require [net.cgrand.enlive.reload :as enlive-reload]))

(defn- get-name
  "Extract the name as a string"
  [ns]
  (-> ns ns-name name))
  
(defn- ours?
  "Filter namespaces starting with 'slipstream.'"
  [nses]
  (filter
    #(.startsWith (get-name %) "slipstream.ui.views")
    nses))

;; TODO: fixme... works in the repl, but not as part of war deployment
(defn refresh-views
  "Reload all slipstream.ui.views.* modules. Use :reload and not :reload-all, otherwise the required modules are
   also reloaded, including the slipstream.ui.config module. This last module contains atom that must be altered
   for which views module must be reloaded."
  []
  (let [ours (ours? (all-ns))]
    (doseq [ns ours]
      (let [name (ns-name ns)]
        (try
          (enlive-reload/reload-ns ns)
          (catch Exception e (print "Error reloading clojure module " name " with detail: " e)))))))
