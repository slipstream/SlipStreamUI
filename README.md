# SlipStreamUI

Here is the list of the static pages currently available, including the static
features they support:

To evaluate them, clone the repository and point your favourite browser to this file.
The links here should just work.

A few observations:

* I've managed to only use accordions for the top level organisation of pages.
  When necessary, tabs inside accordions are used to provide further structure. 
* The accordions are configured such that they can all be closed and opened
  (as well as all closed) which makes navigation easier and less jumpy.
* The layout of all pages is the same, with the only exception of the login
  page, which has animated banner. Could do the same for the logout page.
* Most links don't work, except for a few that are explicitly mentioned below.

1. **Login [page](src/slipstream/ui/views/knockknock.html)**
  * This is the landing page for any unauthenticated access to any SlipStream resource
  * The banner will be animated, providing information relevant to SlipStream
    or a specific installation.
  * From this page, once logged-in, the user is normally redirected to the welcome
    page.

1. **Welcome [page](src/slipstream/ui/views/welcome.html)**:
  * The old dashboard and the initial project pages are merged into this new
    welcome page.
  * The top menu items are fewer, since the old 'dashboard' page is replaced
    by this page.
  * The *Consumption* section provides a current snapshot of the resource consumption
    for each configured cloud.
  * The *Metering* section provides a short historical daily summary of resource consumption
    for each configured cloud.
  * The *My Deployments, Runs, Builds and VMs* section provides the details of the
    currently active resources.  A new column *Note* shows the annotation a user
    can add to each individual run.
  * *My Modules* provides a list of all the modules I am owner of (will have
    to see if we only take the latest version of each module I am the owner of
    or only the module for which I am the last owner).
  * *Published Modules* contains modules we (super) flags as such. This is
    useful for promoting modules, like base images or certified deployments.
  * *Shared Projects* is the original root projects.
  * The lists, e.g. *My Modules* now includes an icon, which could replace
    the category.
    
1. **Projects (view) [page](src/slipstream/ui/views/project-view.html)**
  * The project (in view mode) includes a large icon corresponding to the category
    of the module.
  * The breadcrumb here is more interesting and useful
  
1. **Projects (edit) [page](src/slipstream/ui/views/project-edit.html)**
  * To get here, the user must click on a *New Project* button, available
    on the welcome page, to create a new root project, or on any page for
    which she has *create children* rights.
    
1. **Image (view) [page](src/slipstream/ui/views/image-view.html)**
  * The *Overview* section provides a graphical view of the inheritance relationships
    between the current image (highlighted with a bold border) and images it
    extends and the one (if any) it inherits from.
  * In the *Summary*, you can access to *history* (was *other versions* before)
    page (see below)
  * *Recipes* are grouped using tabs, and explicitly shown (no need to create
    them from drop down lists anymore)

1. **Image (edit) [page](src/slipstream/ui/views/image-edit.html)**
  * In the *Reference* section, the user can click on the *Choose Reference*
    button to show the new chooser window, which is now implemented as a
    jquery-ui dialog widget.  This means the user can move the window around
    and it has a nice look-and-feel.
  * *Parameters* are now sorted by tab. The *Add Parameter* works, so does the
    remove icon button.
  * The *Creation Recipes* and the *Deployment Scripts* (new names) are explicit
    as for the view page.

1. **Deployment (view) [page](src/slipstream/ui/views/deployment-view.html)**
  * The *Overview* section provides a graphical view of the nodes composing the
    deployment,as well as their reference image.  The graphics is similar to the
    run dashboard, such that the user has a more coherent user experience.  
  * The *Nodes* section uses more space and less packed, hopefully making its
    constituent clearer.
  * Clicking on the *Run* button will show an overlay message with a spinner. 

    1. **Deployment (edit) [page](src/slipstream/ui/views/deployment-edit.html)**
  * The *Nodes* section include functional remove buttons for parameters and nodes
  * The autocomplete is not active, but will be available for helping provide
    *Linked to* values, as with the current UI.
  * As for the image, the *Add Node* button shows the new chooser window.
  * The *remove buttons* also work for both nodes and parameter mappings.
  
1. **Versions/History [page](src/slipstream/ui/views/versions.html)**
  * From any *module* page, the *history* link will show the list of available
    versions.
  * It now includes comments

1. **Run [page](src/slipstream/ui/views/run.html)**
  * The third line in the header includes a *note*, where the user can annotate
    the run to better find it from the notes.
  * The *Overview* section contains the same graphical dashboard as the current
    version.
  * The *Overview* section also now shows the *state machine* as a graphical
    sequence, where the current state is highlighted, in green if all is good
    and in red (not shown) in case of errors. Note the boxes should probably be
    thiner.
  * Each VM now has its own section.
  * The last section is for the reports.
  
1. **Logout [page](src/slipstream/ui/views/logout.html)**
  * Nothing much to say here.

1. **Configurations [page](src/slipstream/ui/views/configurations.html)**
  * Each section corresponds to a configuration group.
  
1. **Documentation [page](src/slipstream/ui/views/documentation.html)**
  * Each type of document is available in a separate section, with corresponding
    link.
  
1. **Error [page](src/slipstream/ui/views/error.html)**
  * Oops... shot happens ;-)

1. **Users [page](src/slipstream/ui/views/Users.html)**
  * Lists all users, with an overview of their current user consumption
