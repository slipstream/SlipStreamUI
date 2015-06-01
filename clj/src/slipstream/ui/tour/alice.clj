(ns slipstream.ui.tour.alice
  "Tours for Alice."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.enlive :as ue]))

(def next-button-label
  "<button class='btn btn-primary btn-xs bootstro-next-btn' style='float:none;font-weight:bold;'>Next&nbsp;&raquo;</button>")

(defn- query-param-value
  ;; TODO: Extract the context and this kind of util fn into a ns with a thread value
  ;;       in the same way than the *current-user* or the language.
  [context query-param-key]
  (some-> context :metadata meta :request :query-parameters query-param-key not-empty))

(defn intro
  "Tour to lead Alice to the 'AHA!' moment."
  [context]
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
       :content "This is a plublished application. Click 'next' to learn how to deploy it."}

      [:#ss-section-app-store :> :div :> :div :> [:div (ue/first-of-class "ss-example-app-in-tour")] :> :div :.ss-app-image-container]
      {:title "Deploy"
       :content "Click the <span style='color:#fff;background-color:#337ab7;padding: 4px 8px;font-weight:normal;'><span class='glyphicon glyphicon-cloud-upload'></span> Deploy</span> button in the bottom right part of the application logo to deploy."
       :width "300px"}
    ]

   :deploying-wordpress
   [
      nil
      {:title "Run Dialog"
       :content (str "We are now in the page of the WordPress SlipStream image and the <span class='panel-primary'><h4 class='modal-header panel-heading'>Run image</h4></span> dialog behind has been automatically open."
                     " If you need to open it again, click on the menu action <span style='color:#54A9DE;background-color:#393939;font-weight:400;padding:2px 8px;'><span class='glyphicon glyphicon-cloud-upload' style='display:inline;'></span>&nbsp;Run...</span> in the right part of the page."
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
       :content "Click on <span style='color:#fff;background-color:#337ab7;padding: 4px 8px;font-weight:normal;'>Run image</span> when you are ready to go."
       :container-sel "#ss-run-module-dialog"
       :wrap-in-elem   [:span]
       :placement "right"
       :placement-distance "large"}
      ]

   :waiting-for-wordpress
   [
      nil
      {:title "Welcome to the <code>Run</code> page"
       :content (str "This is the page containing all the information about the run you just launched."
                     "<br/><br/>"
                     "Click " next-button-label " to take a quick tour of this important page.")
       :placement "bottom"}

      :#header-content
      {:title "At a glance"
       :content "In the header you can have an overview of the main info like the run id, the state, who and when started it and the type of run."
       :placement "bottom"}

      :#ss-breadcrumb-container
      {:title "The breadcrumbs"
       :content (str "Across the whole application you can rely in the breadcrumbs to know where you are and navigate up the hierarchy."
                     " E.g. clicking on the <span class='glyphicon glyphicon-home'></span> will bring you always to the welcome page , with the App Store."
                     "<br/><br/>"
                     "In this case, you can see which module (i.e. <code>wordpress</code>) and version was deployed and where it's located in the project tree.")
       :width "100%"
       :container-sel "body"
       :placement "right"}

      [:#ss-section-group-0 :> :div.panel.ss-section-selected.ss-section.panel-default]
      {:title "Overview"
       :content (str "This offers a graphical overview of the running machines and global info about each one, like state, IP address and custom message."
                     "<br/><br/>"
                     "Hover on the different nodes to reveal more details.")
       :placement "top"}

      [:#ss-section-group-0 :> [:div.panel.ss-section.panel-default (html/nth-child 2)]]
      {:title "Summary"
       :content (str "This section summarizes the top level information about the run: who and when started what."
                     "<br/><br/>"
                     "Note that here you can add and modify the runs <code>tags</code>, as we promised in the previous page! &#x1F60E;") ;; &#x1F60E; is :sunglasses: emoji
       :placement "top"}

      [:#ss-section-group-0 :> [:div.panel.ss-section.panel-default (html/nth-child 3)]]
      {:title "Global run parameters"
       :content (str "This and the following sections contain a lot of detailed information about the run and the deployed machines. Here you can find out IP adresses, error causes and other parameters."
                     "<br/><br/>"
                     "Across the whole application you can hover on the <span class='glyphicon glyphicon-question-sign'></span> to reveal a detailed explanation of the corresponding item.")
       :placement "top"}

      [:#ss-section-group-0 :> [:div.panel.ss-section.panel-default html/last-child]]
      {:title "Reports"
       :content (str "For each run, SlipStream collects a series of reports that you will find here."
                     " You might want to come here and download them specially if your run didn't worked as expected."
                     "<br/><br/>"
                     "Note that you don't need to reload the page: the reports will automatically appear here when available.")
       :placement "top"}

      ; [:#ss-secondary-menu :> :div]
      ; {:title "Terminate run"
      ;  :content "Clicking here will ask the corresponding cloud infrastructure to stop the deployment and the running machines."
      ;  :placement "bottom"}

      [:#topbar :> :div :> :div :> :div.navbar-collapse.collapse :> :ul :> [:li (html/nth-child 1)]]
      {:title "Dashboard"
       :content (str "The deployment will take some minutes, depending on the cloud you selected and its current load."
                     "<br/><br/>"
                     "In the meantime we will visit the dashboard, also a very central point of SlipStream where you will have an overview of the applications you have running, including this WordPress you just launched."
                     "<br/><br/>"
                     "Click on the <span style='color:white;background-color:#a00;font-weight:normal;padding:2px 8px;'>Dashboard</span> above to discover in. We will come back here afterwards to see how the run finished.")
       :placement "bottom"}
    ]

    :wordpress-in-dashboard
   [
      nil
      {:title "Welcome to the Dashboard"
       :content (str "This page centralizes your activity on all clouds."
                     " You can check your resource usage per cloud and find all deployments and images you launched, including the WordPress images you just launched."
                     "<br/><br/>"
                     "Click " next-button-label " to take a quick tour of this important page.")}

      [[:div.panel.ss-section.panel-default (html/nth-child 1)]]
      {:title "Usage section"
       :content "Here you see all clouds that are you can have access to and your resource comsumption on each one."
       :placement "top"}

      (->> :cloud (query-param-value context) (str "#ss-usage-gauge-") keyword)
      {:title (str "Usage gauge")
       :content (str  "The big number indicates the number of VMs you have running in <code>" (query-param-value context :cloud) "</code>."
                      " The top limit is the quota you are allowed to consume at the same time for this cloud."
                      " Contact the administrator of this SlipStream instance if you need to change it.")
       :container-sel "body"
       :preserve-padding true
       :placement "top"}

      [[:div.panel.ss-section.panel-default (html/nth-child 3)]]
      {:title "VMs section"
       :content (str "This are all the individual VMs that are launched using your cloud credentials (i.e. a <code>describe-instance</code> kind of info, if it speaks to you)."
                     " Note that if some VMs were launched using with your same credentials, they will also be listed here, but without a <code>Run Id</code>, since SlipStream doesn't know them.")
       :placement "top"}

      [[:div.panel.ss-section.panel-default (html/nth-child 4)]]
      {:title "Metering section"
       :content "Here you can see the history of your consumption in several time frames."
       :placement "top"}

      [[:div.panel.ss-section.panel-default (html/nth-child 2)]]
      {:title "Runs section"
       :content (str "Here you can find all the runs grouped by cloud, including the WordPress you launched before."
                     " Click on its <code>Run Id</code> (we kindly noted it for you: <code>" (query-param-value context :wordpress-run-id) "</code>) to go back to the <code>Run page</code> we where before."
                     " Note that you can identify your run also with the <code>tag</code> you used.")
       :placement "top"}
    ]
   ]
  )

(defn- detour-to-set-up-cloud-credentials
  [context]
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
                       "<br/><br/>"
                       "Click " next-button-label " when you are ready to continue.")
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

(defn intro-without-connectors
  "Tour to lead Alice to the 'AHA!' moment, but when she does not have any connector
  credentials configured."
  [context]
  (concat (detour-to-set-up-cloud-credentials context)
          (intro context)))
