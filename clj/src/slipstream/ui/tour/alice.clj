(ns slipstream.ui.tour.alice
  "Tours for Alice."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]))

(def intro
  "Tour to lead Alice to the 'AHA!' moment."
  {
   :welcome
   [
      :#ss-section-group
      {:title "Main sections"
       :content (str "There are three main section on the welcome page of SlipStream. "
                     "<ol><li>AppStore</li><li>Projects</li><li>Service Catalog</li></ol>")}

      [:#ss-section-group :> html/first-child]
      ; :#ss-section-app-store
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
      [:#ss-run-module-dialog :> :div.modal-dialog :div.modal-content ]
      {:title "Input parameters"
       :content (str "There are three main section on the welcome page of SlipStream. ")
       :placement "left"
       :container-sel "#ss-run-module-dialog"}

      [:#ss-run-module-dialog :> :div.modal-dialog :div.ss-run-deployment-global-section-content :> :div :> :table :> :tbody :> [:tr (html/nth-child 4)] :> [:td (html/nth-child 2)]]
      {:title "Choose the cloud"
       :content "Here you can find a curated list of deployable applications."
       :container-sel "#ss-run-module-dialog"}

      [:#ss-run-module-dialog :> :div.modal-dialog :div.ss-run-deployment-global-section-content :> :div :> :table :> :tbody :> [:tr (html/nth-child 6)] :> [:td (html/nth-child 2)]]
      {:title "Give it a name"
       :content "This is a plublished application. Click NEXT to learn how to deploy it."
       :container-sel "#ss-run-module-dialog"}

      [:#ss-run-module-dialog :button.btn.btn-primary.ss-ok-btn.ss-build-btn]
      {:title "Ready to deploy"
       :content "Click the \"Deploy\" button in the bottom right part of the application logo to deploy."
       :container-sel "#ss-run-module-dialog"
       :wrap-in-elem   [:span]}
      ]

   :waiting-for-wordpress
   [
      :#header-content
      {:title "Welcome to the run page"
       :content "This is the page containing all the information about the run. In the header you can have an overview of the main info, like the state."
       :placement "bottom"}

      [:#ss-section-group :> :div.panel.ss-section-selected.ss-section.panel-default]
      {:title "Run overview"
       :content "This offers you a graphical overview of the running machines and global info about each one, like state, IP address and custom message."
       :placement "top"}

      [:#ss-secondary-menu :> :div]
      {:title "Terminate run"
       :content "Clicking here will ask the corresponding cloud infrastructure to stop the deployment and the running machines."
       :placement "bottom"}

      [:#topbar :> :div :> :div :> :div.navbar-collapse.collapse :> :ul :> [:li (html/nth-child 1)]]
      {:title "Dashboard"
       :content "Go to the dashboard to have an overview of the applications you have running."
       :placement "bottom"}
    ]
   }
  )
