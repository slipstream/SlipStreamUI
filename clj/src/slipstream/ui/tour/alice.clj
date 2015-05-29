(ns slipstream.ui.tour.alice
  "Tours for Alice."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]))

(def next-button-label
  "<button class='btn btn-primary btn-xs bootstro-next-btn' style='float:none;font-weight:bold;'>Next&nbsp;&raquo;</button>")

(def intro
  "Tour to lead Alice to the 'AHA!' moment."
  [
   :welcome
   [
      :#ss-section-group-0
      {:title "Main sections"
       :content (str "There are three main section on the welcome page of SlipStream. "
                     "<ol><li>AppStore</li><li>Projects</li><li>Service Catalog</li></ol>")}

      [:#ss-section-group-0 :> html/first-child]
      ; :#ss-section-app-store
      {:title "Applications"
       :content "Here you can find a curated list of deployable applications."}

      [:#ss-section-app-store :> :div :> :div :> [:div (ue/first-of-class "ss-example-app-in-tour")] :> :div]
      {:title "Application"
       :content "This is a plublished application. Click " next-button-label " to learn how to deploy it."}

      [:#ss-section-app-store :> :div :> :div :> [:div (ue/first-of-class "ss-example-app-in-tour")] :> :div :.ss-app-image-container]
      {:title "Deploy"
       :content "Click the <code><span class='glyphicon glyphicon-cloud-upload'></span> Deploy</code> button in the bottom right part of the application logo to deploy."
       :width "300px"}
    ]

   :deploying-wordpress
   [
      nil
      {:title "Run Dialog"
       :content (str "We are now in the page of the WordPress SlipStream image and the <span class='panel-primary'><h4 class='modal-header panel-heading'>Run image</h4></span> dialog behind has been automatically open."
                     " If you need to open it again, click on the menu action <span style='color:#54A9DE;background-color:#393939;font-weight:400;padding:2px 8px;'><span class='glyphicon glyphicon-cloud-upload' style='display:inline;'></span>&nbsp;Run...</span> in the left part of the page."
                     " In this dialog you can specify some parameters for the deployment."
                     "<br/><br/>"
                     "Click " next-button-label " to learn how to configure and launch WordPress.")}

      [:#ss-run-module-dialog #{:#parameter--cloudservice :#global-cloud-service}]
      {:title "Choose the cloud"
       :content "Please choose where you want WordPress to be deployed. Please note that you can only choose the clouds for which you have the corresponding credentials configured in your profile."
       :container-sel "#ss-run-module-dialog"
       :preserve-padding true
       :placement "right"
       :placement-distance "larger"}

      [:#ss-run-module-dialog :#tags]
      {:title "Give it a name"
       :content "You can assing some <code>tags</code> to the deployment. This will be useful to recognise it later on. Try something like <code>wp-tour-test</code>. Don't worry, you will be able to update it later on."
       :container-sel "#ss-run-module-dialog"
       :preserve-padding true
       :placement "right"
       :placement-distance "larger"}

      [:#ss-run-module-dialog :button.btn.btn-primary.ss-ok-btn]
      {:title "Ready to deploy"
       :content "Click on <code>Run image</code> when you are ready to go."
       :container-sel "#ss-run-module-dialog"
       :wrap-in-elem   [:span]
       :placement "right"
       :placement-distance "large"}
      ]

   :waiting-for-wordpress
   [
      :#header-content
      {:title "Welcome to the run page"
       :content "This is the page containing all the information about the run. In the header you can have an overview of the main info, like the state."
       :placement "bottom"}

      [:#ss-section-group-0 :> :div.panel.ss-section-selected.ss-section.panel-default]
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
   ]
  )

(def ^:private detour-to-set-up-cloud-credentials
  [
     :go-to-profile
     [
        nil
        {
         :title "First things first"
         :content (str "To begin using SlipStream you need to tell it where to deploy your applications to. "
                       "For that, you need to have an account with at least one cloud provider and set up your credentials in your SlipStream profile."
                       "<br/><br/>"
                       "Click " next-button-label " to begin the tour by configuring one (or more!) clouds.")
         }

        nil
        {
         :title "Cloud credentials"
         :content (str "If you already have an account with a cloud provider, just prepare its credentials (usually a <code>user/password</code> or a <code>key/secret</code> pair). "
                       "If not, please create one following the indications in our <a target='_blank' href='http://ssdocs.sixsq.com/documentation/advanced_tutorial/accounts.html#cloud-infrastructure-accounts'>documentation</a>. "
                       "<br/><br/>"
                       "When your cloud account is ready, go to the next step to learn how to set the credentials in your SlipStream user profile.")
         }

        :#ss-menubar-user-profile-anchor
        {:title "Go to your profile"
         :content "Let's configure your first cloud account. Go to your user profile clicking this menu here. Note that you are also able to logout from this menu."
         :placement "left"
         :container-sel "body"
         :preserve-padding true
         }

      ]

     :edit-profile
     [
        nil
        {:title "Your user profile"
         :content (str "This is your SlipStream user profile page. "
                       "First you will learn how to update your personal information, "
                       "and then you will be able to configure one or more clouds by providing the corresponding credentials. "
                       "Click 'Next' when you are ready to continue.")
         }

        [:#ss-section-group-0 :> :div.panel.ss-section-selected.ss-section.panel-default]
        {:title "Edit your personal information"
         :content "First of all, please take this moment to make sure that your information is correct."
         :placement "top"
         }

        [:#ss-section-group-1]
        {:title "Cloud credentials"
         :content "This is the configuration section for your cloud accounts. Please enter your credentials for at least one cloud here."
         :placement "top"
         }

        :#ss-secondary-menu-action-save
        {:title "Save your profile"
         :content "Click here to... exactly: save the chages you just made."
         :placement "bottom"
         :preserve-padding true
         }
      ]

     :navigate-back-to-welcome
     [
        [:#topbar :> :div :> :div :> :div.navbar-header :> :a]
        {:title "Back to the main page"
         :content "Now that you have at least one cloud configured, click on the logo to back to the AppStore on the main page and select your first app to deploy."
         :placement "bottom"
         :preserve-padding true
         }
      ]

     ])

(def intro-without-connectors
  "Tour to lead Alice to the 'AHA!' moment, but when she does not have any connector
  credentials configured."
  (concat detour-to-set-up-cloud-credentials
          intro))
