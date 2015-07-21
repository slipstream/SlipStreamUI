(ns slipstream.ui.tour.alice
  "Tours for Alice."
  (:require [net.cgrand.enlive-html :as html]
            [slipstream.ui.util.clojure :as uc]
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
       :content (if (->> context :content (remove nil?) count (= 3))
                  (str "There are three main sections on the welcome page of SlipStream."
                            "<ol><li>AppStore</li><li>Projects</li><li>Service Catalog</li></ol>")
                  (str "There are two main sections on the welcome page of SlipStream."
                            "<ol><li>AppStore</li><li>Projects</li></ol>"))}

      [:#ss-section-group-0 :> html/first-child]
      ; :#ss-section-app-store
      {:title "Applications"
       :content "Here you can find a curated list of deployable applications."}

      [:#ss-section-app-store :> :div :> :div :> [:div (ue/first-of-class "ss-example-app-in-tour")] :> :div]
      {:title "Application"
       :content "This is a published application. Click 'next' to learn how to deploy it."}

      [:#ss-section-app-store :> :div :> :div :> [:div (ue/first-of-class "ss-example-app-in-tour")] :> :div :.ss-app-image-container]
      {:title "Deploy"
       :content "Click the <span style='color:#fff;background-color:#337ab7;padding: 4px 8px;font-weight:normal;'><span class='glyphicon glyphicon-cloud-upload'></span> Deploy</span> button in the bottom right part of the application logo."
       :width "300px"}
    ]

   :deploying-wordpress
   [
      nil
      {:title "Run dialog"
       :content (str "We are now on the WordPress SlipStream image page and the <span class='panel-primary'><h4 class='modal-header panel-heading'>Run image</h4></span> dialog behind has been automatically opened."
                     " If you need to open it again, click on the menu action <span style='color:#54A9DE;background-color:#393939;font-weight:400;padding:2px 8px;'><span class='glyphicon glyphicon-cloud-upload' style='display:inline;'></span>&nbsp;Run...</span> in the right part of the page."
                     " In this dialog you can specify the values of some parameters for the deployment."
                     "<br/><br/>"
                     "Click " next-button-label " to learn how to configure and launch WordPress.")}

      [:#ss-run-module-dialog #{:#parameter--cloudservice :#global-cloud-service}]
      {:title "Choose the cloud"
       :content "Please choose where you want WordPress to be deployed. Please note that you can only choose those clouds for which you have provided credentials in your profile."
       :container-sel "#ss-run-module-dialog"
       :preserve-padding true
       :placement "right"
       :placement-distance "larger"}

      [:#ss-run-module-dialog :#tags]
      {:title "Give it a name"
       :content "You can assign a <code>tag</code> to the deployment. This will be useful to recognise it later on. Try something like <code>wp-tour-test</code>. Don't worry, you can change it later."
       :container-sel "#ss-run-module-dialog"
       :preserve-padding true
       :placement "right"
       :placement-distance "larger"}

      [:.ss-run-image-input-parameters-section]
      {:title "Input parameters"
       :content (str "Each application might define one or more input parameters to customise the deployment."
                     " You might modify these values or leave them with the default values."
                     " They are not related to SlipStream but to the application being deployed."
                     "<br/><br/>"
                     " In this case, you might personalise the <strong>title</strong> and the <strong>email</strong> of the administrator of the Wordpress that your are deploying.")
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
       :content "In the header you have an overview with information like the run id, the state, who started it, when it was started, and the type of run."
       :placement "bottom"}

      :#ss-breadcrumb-container
      {:title "The breadcrumbs"
       :content (str "Throughout SlipStream you can rely on the breadcrumbs to know where you are and navigate up the hierarchy."
                     " E.g. clicking on the <span class='glyphicon glyphicon-home'></span> will always bring you to the welcome page containing the App Store."
                     "<br/><br/>"
                     "In this case, you can see which module (i.e. <code>wordpress</code>) and version was deployed and where it's located in the project tree.")
       :width "100%"
       :container-sel "body"
       :placement "right"}

      [:#ss-section-group-0 :> :div.panel.ss-section-selected.ss-section.panel-default]
      {:title "Overview"
       :content (str "This offers a graphical overview of the running machines and global information about each one, like state, IP address and custom message."
                     " Hovering over the different nodes will reveal more details as they become available."
                     "<br/><br/>"
                     " Note that now you might not see much information yet, since the deployment is still initializing.")
       :placement "top"}

      [:#ss-section-group-0 :> [:div.panel.ss-section.panel-default (html/nth-child 2)]]
      {:title "Summary"
       :content (str "This section summarizes the top level information about the run."
                     "<br/><br/>"
                     "Note that here you can add and modify the <code>tags</code>, as we promised previously! &#x1F60E;") ;; &#x1F60E; is :sunglasses: emoji
       :placement "top"}

      [:#ss-section-group-0 :> [:div.panel.ss-section.panel-default (html/nth-child 3)]]
      {:title "Global run parameters"
       :content (str "This and the following sections contain a lot of detailed information about the run and the deployed machines. Here you can find machine IP addresses, deployment errors, and other parameters."
                     "<br/><br/>"
                     "Throughout SlipStream you can hover over the <span class='glyphicon glyphicon-question-sign'></span> to reveal a detailed explanation of the corresponding item.")
       :placement "top"}

      [:#ss-section-group-0 :> [:div.panel.ss-section.panel-default html/last-child]]
      {:title "Reports"
       :content (str "For each run, SlipStream collects a series of reports that you will find here."
                     " You might want to come here and download them, especially if your run didn't work as expected."
                     "<br/><br/>"
                     "Note that you don't need to reload the page: the reports will automatically appear here when available.")
       :placement "top"}

      ; [:#ss-secondary-menu :> :div]
      ; {:title "Terminate run"
      ;  :content "Clicking here will ask the corresponding cloud infrastructure to stop the deployment and the running machines."
      ;  :placement "bottom"}

      [:#topbar :> :div :> :div :> :div.navbar-collapse.collapse :> :ul :> [:li (html/nth-child 1)]]
      {:title "Dashboard"
       :content (str "The deployment may take some time, depending on the cloud you selected and its current load."
                     "<br/><br/>"
                     "In the meantime we will visit the dashboard, also a very central point of SlipStream where you will have an overview of the applications you have running, including the WordPress instance you just launched."
                     "<br/><br/>"
                     "Click on the <span style='color:white;background-color:#a00;font-weight:normal;padding:2px 8px;'>Dashboard</span> above to discover it. We will come back here afterwards to see how the run finished.")
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

      [:.panel-group :> (html/nth-child 1)]
      {:title "Usage section"
       :content "Here you see all of the accessible clouds and your resource comsumption on each one."
       :placement "top"}

      (->> :cloud (query-param-value context) (str "#ss-usage-gauge-") keyword)
      {:title (str "Usage gauge")
       :content (str  "The big number indicates the number of VMs you have running in <code>" (query-param-value context :cloud) "</code>."
                      " The top limit is the quota you are allowed to consume at the same time for this cloud."
                      " Contact the administrator of this SlipStream instance if you need to change it."
                      "<br/><br/>"
                      "Click here, and then click next, to see the runs you did on this cloud.")
       :container-sel "body"
       :preserve-padding true
       :placement "top"}

      [:.panel-group :> (html/nth-child 2)]
      {:title "Runs section"
       :content (str "Here you can find all the runs grouped by cloud, including the WordPress you launched before."
                     " Click on its <code>Run Id</code> (we kindly noted it for you: <code>" (query-param-value context :wordpress-run-id) "</code>) to go back to the <code>Run page</code>."
                     " Note that you can identify your run also with the <code>tag</code> you used.")
       :placement "top"}
    ]

    :wordpress-running
   (if (->> context :alerts (remove nil?) (map :type) (some #{:error}))
     [
         [:#ss-alert-container-fixed]
         {:title "Oups! "
          :content (str "Err... something went wrong! &#x1F631;"
                        "<br/><br/>"
                        "Please verify that the credentials you added for the cloud used are valid and launch the tour again."
                        "<br/><br/>"
                        "If the problem persists please do not hesitate to contact us!")
          :preserve-padding true
          :placement "bottom"
          }
     ]
     [
         nil
         {:title "Please wait"
          :content (str "<div style='font-size:42px;text-align:center;font-weight:900;opacity:0.8'><span class='glyphicon glyphicon-refresh'></span></div>"
                        "Please wait until Wordpress is being deployed. It should take less than a couple of minutes.")
          :hide-prev-button true
          :hide-next-button true
          }


         [:#ss-alert-container-fixed]
         {:title "Yeah!"
          :content "Your Wordpress is up and running! &#x1F60E; You can now click this service URL to see it live."
          :placement "top"
          :container-sel "body"
          :preserve-padding true
          :hide-prev-button true
          }

         [:#ss-secondary-menu-action-terminate]
         {:title "Terminate the deployment"
          :content "Click here to terminate your Wordpress instance and stop using resources in this cloud."
          :placement "bottom"
          :width "300px"
          :preserve-padding true
          }

         :.ss-action-documentation
         {:title "Before we leave"
          :content "Remember that you have access to the SlipStream documentation and other help pointers on all pages."
          :placement "left"
          :container-sel "body"
          :preserve-padding true
          :placement-distance "larger"}

         nil
         {:title "Congratulations!"
          :content (str "<div style='font-size: 64px;text-align:center;'>&#x1F680</div>"
                        "You completed your first deployment! Now you are all set to go and simply your deployments with SlipStream."
                        "<br/><br/>"
                        "We hope you well enjoy using SlipStream. And don't hesitate to contact us if you have any question or concern"
                        " using the support tabs at the bottom and right parts of any page in the application for a message or a live chat."
                        "<br/><br/>"
                        "We would greatly appreciate if you <b><a href='mailto:support@sixsq.com?Subject=%5Balice-tour%5D%20Toughts%20after%20completion&Body=Dear%20SixSq%20Team%2C%0A%0AI%20just%20finished%20the%20discovery%20tour%20in%20SlipStream%20and%20successfully%20deployed%20a%20Wordpress%20application.%0A%0AI%20think%20that%20'>tell us what you think about this tour</a></b>.")
          }

       ])
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
         :content (str "To begin using SlipStream you need to tell it where to deploy your applications. "
                       "For that, you need to have an account with at least one cloud provider and set up your credentials in your SlipStream profile."
                       "<br/><br/>"
                       "Click " next-button-label " to begin the tour by configuring one (or more!) clouds.")
         }

        nil
        {
         :title "Cloud credentials"
         :content (str "If you already have an account with a cloud provider, have your credentials handy (usually a <code>user/password</code> or a <code>key/secret</code> pair). "
                       "If not, please create one following the procedure in our <a target='_blank' href='http://ssdocs.sixsq.com/documentation/advanced_tutorial/accounts.html#cloud-infrastructure-accounts'>documentation</a>. "
                       "<br/><br/>"
                       "When your cloud account is ready, go to the next step to learn how to set the credentials in your SlipStream user profile.")
         }

        :#ss-menubar-user-profile-anchor
        {:title "Go to your profile"
         :content "Let's configure your first cloud account. Go to your user profile clicking this menu item. Note that you are also able to logout from this menu."
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
         :content "First of all, please take a moment to make sure that your information is correct."
         :placement "top"
         }

        [:#ss-section-group-1]
        {:title "Cloud credentials"
         :content "This is the configuration section for your cloud accounts. Please enter credentials for at least one cloud here."
         :placement "top"
         }

        :#ss-secondary-menu-action-save
        {:title "Save your profile"
         :content "Click here to... exactly: save the changes you just made."
         :placement "bottom"
         :preserve-padding true
         }
      ]

     :navigate-back-to-welcome
     [
        [:#topbar :> :div :> :div :> :div.navbar-header :> :a]
        {:title "Back to the main page"
         :content "Now that you have at least one cloud configured, click on the SlipStream logo to back to the AppStore on the main page."
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
