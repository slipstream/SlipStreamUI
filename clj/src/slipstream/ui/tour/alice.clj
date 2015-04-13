(ns slipstream.ui.tour.alice
  "Tours for Alice."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]))

(def intro
  "Tour to lead Alice to the 'AHA!' moment."
  {
   :welcome
   [
      :#ss-content
      {:title "Main sections"
       :content (str "There are three main section on the welcome page of SlipStream. "
                     "<ol><li>AppStore</li><li>Projects</li><li>Service Catalog</li></ol>")}

      [:#ss-section-group :> html/first-child]
      {:title "Applications"
       :content "Here you can find a curated list of deployable applications."}

      [:#ss-section-app-store :> :div :> :div :> [:div (ue/first-of-class "ss-example-app-in-tour")] :> :div]
      {:title "Application"
       :content "This is a plublished application. Click NEXT to learn how to deploy it."}

      [:#ss-section-app-store :> :div :> :div :> [:div (ue/first-of-class "ss-example-app-in-tour")] :> :div :.ss-app-image-container]
      {:title "Deploy"
       :content "Click the \"Deploy\" button in the bottom right part of the application logo to deploy."
       :width "300px"}
      ]

   :deploying-wordpress
   [
      [:#ss-run-module-dialog :> :div.modal-dialog]
      {:title "Input parameters"
       :content (str "There are three main section on the welcome page of SlipStream. ")
       :placement "left"}

      [:#ss-run-module-dialog :> :div.modal-dialog :div.ss-run-deployment-global-section-content :> :div :> :table :> :tbody :> [:tr (html/nth-child 4)]]
      {:title "Choose the cloud"
       :content "Here you can find a curated list of deployable applications."}

      [:#ss-run-module-dialog :> :div.modal-dialog :div.ss-run-deployment-global-section-content :> :div :> :table :> :tbody :> [:tr (html/nth-child 6)]]
      {:title "Give it a name"
       :content "This is a plublished application. Click NEXT to learn how to deploy it."}

      [:#ss-run-module-dialog :button.btn.btn-primary.ss-ok-btn.ss-build-btn]
      {:title "Ready to deploy"
       :content "Click the \"Deploy\" button in the bottom right part of the application logo to deploy."}
      ]
   })
