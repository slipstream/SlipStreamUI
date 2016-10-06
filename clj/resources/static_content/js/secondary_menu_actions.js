jQuery( function() { ( function( $$, $, undefined ) {

    var module = $$.model.getModule();

    function newModuleRedirect(category) {
        var moduleURI = module.getURI() || "/module";
        $$.util.url.redirectTo(moduleURI + "/new?category=" + category + "#summary");
    }


    // New-project action

    $("#ss-secondary-menu-action-new-project").clickWhenEnabled( function() {
        newModuleRedirect("Project");
    });


    // New-image action

    $("#ss-secondary-menu-action-new-image").clickWhenEnabled( function() {
        newModuleRedirect("Image");
    });


    // New-deployment action

    $("#ss-secondary-menu-action-new-deployment").clickWhenEnabled( function() {
        newModuleRedirect("Deployment");
    });


    // Import action

    $("#ss-secondary-menu-action-import").clickWhenEnabled( function() {
        console.error("not yet implemented");
    });


    // New-user action

    $("#ss-secondary-menu-action-new-user").clickWhenEnabled( function() {
        $$.util.url.redirectTo("/user/new");
    });


    // User-export action

    $("#ss-secondary-menu-action-export-users").clickWhenEnabled( function() {
        $$.util.url.redirectTo("/user?media=csv");
    });


    // Run action

    $("#ss-secondary-menu-action-run").clickWhenEnabled( function() {
        $('#ss-run-module-dialog').modal("show");
    });


    // Build action

    $("#ss-secondary-menu-action-build").clickWhenEnabled( function() {
        $('#ss-build-module-dialog').modal("show");
    });


    // Copy action

    $("#ss-secondary-menu-action-copy").clickWhenEnabled( function() {
        $('#ss-copy-module-dialog').modal("show");
    });


    // Publish and unpublish actions

    function togglePublishButton() {
        $("#ss-secondary-menu-action-publish, #ss-secondary-menu-action-unpublish")
            .toggleClass("hidden");
        return;
    }

    function togglePublicationDateRow() {
        var $dateCell = $("#ss-publication-date"),
            $dateRow = $dateCell.closest("tr");
            isDateRowInactive = $dateRow.data("ss-date-row-inactive");
        if (! $dateRow.length){
            // If the module was originally unpublished, the row will not be there.
            // The user will see the publication dat row on page reload only.
            return;
        }
        if (isDateRowInactive) {
            // If the module was originally published, and the users re-publishes it
            // after unpublishing it, we can toggle this row back in without reloading
            // the page.
            $dateCell.text("Now");
            $dateRow.enableRow();
            $dateRow.data("ss-date-row-inactive", false);
        } else {
            $dateRow.disableRow();
            $dateRow.data("ss-date-row-inactive", true);
        }
        return;
    }

    function publishActionAlertMessage(isPublished) {
        return "The " + module.getCategoryName().toLowerCase() +
               " <code class='text-success'>" + module.getFullName() + "</code>" +
               " (v " + module.getVersion() + ") was successfully " +
               (isPublished ? "published in" : "removed from") +
               " the public <a href='/appstore' class='text-success'>App Store</a>.";
    }

    $("#ss-secondary-menu-action-publish").clickWhenEnabled( function() {
        $('#ss-publish-module-confirmation-dialog').askConfirmation(function () {
            $$.request
                .put(module.getURIWithVersion() + "/publish")
                .onSuccess(togglePublishButton)
                .onSuccess(togglePublicationDateRow)
                .onSuccessAlert(module.getCategoryName() + " published",
                    publishActionAlertMessage(true))
                .send();
            });
    });

    $("#ss-secondary-menu-action-unpublish").clickWhenEnabled( function() {
        $('#ss-unpublish-module-confirmation-dialog').askConfirmation(function () {
            $$.request
                .delete(module.getURIWithVersion() + "/publish")
                .onSuccess(togglePublishButton)
                .onSuccess(togglePublicationDateRow)
                .onSuccessAlert(module.getCategoryName() + " unpublished",
                    publishActionAlertMessage(false))
                .send();
            });
    });


    // Terminate action

    $("#ss-secondary-menu-action-terminate").clickWhenEnabled( function() {
        $('#ss-terminate-deployment-dialog').askConfirmation(function(){
            $$.request
                .delete($$.util.url.getCurrentURLBase())
                .onSuccessAlert("The request to terminate the run was successfully received. You can stay on this page, the Run state will be updated automatically.")
                .send();
        });
    });


    // Edit action

    $("#ss-secondary-menu-action-edit").clickWhenEnabled( function() {
        window.location.search = 'edit=true';
    });


    // Save action

    var $saveForm = $("#save-form");

    $("#ss-secondary-menu-action-save").clickWhenEnabled( function(event) {
        if (! $saveForm.isValidForm()) {
            $$.alert.showError($saveForm.getGenericFormAlertMsg("error"));
        } else if (event.altKey ||
            $$.util.meta.isViewName("user configuration service-catalog")) {
            // No commit message needed.
            $saveForm.submit();
        } else {
            $('#ss-save-dialog').askConfirmation(function(){
                $saveForm
                    .addFormHiddenField("comment", $("#ss-commit-message").val())
                    .submit();
            });
        }
    });


    // Create action

    $("#ss-secondary-menu-action-create").clickWhenEnabled( function() {
        var $form = $("#save-form");
        if ($$.util.meta.isViewName("module")) {
            $form.addFormHiddenField("comment", "Initial version of this " + module.getCategoryName().toLowerCase() + ".");
        }
        $form.submit();
    });


    // Cancel action

    $("#ss-secondary-menu-action-cancel").clickWhenEnabled( function() {
        $$.util.url.redirectToCurrentURLBase(true);
    });


    // Delete action

    $("#ss-secondary-menu-action-delete").clickWhenEnabled( function() {
        $('#ss-delete-dialog').askConfirmation(function () {
            var request = $$.request.delete($$.util.url.getCurrentURLBase());
            if ($$.model.getModule().isRootModule()){
                // There is a (known but untracked) bug on the server which returns
                // a wrong redirect header when deleting root projects.
                request.onSuccessRedirectTo("/");
            } else if ($$.util.meta.isViewName("user")) {
                // There is a (known but untracked) bug on the server which returns
                // a wrong redirect header when deleting users.
                request.onSuccessRedirectTo("/user");
            } else {
                request.onSuccessFollowRedirectInResponseHeader();
            }
            $$.util.leavingConfirmation.reset();
            request
                .onErrorAlert("Unable to delete",
                    "Something wrong happened when trying to delete this resource." +
                    " Maybe the server is unreachable, or the connection is down." +
                    "Please try later again.")
                .send();
        });
    });

    // Delete all action

    $("#ss-secondary-menu-action-delete-all").clickWhenEnabled( function() {
        var $verification = $('#ss-delete-all-dialog-verification');
        $verification.enableLiveInputValidation();
        $verification.focusout(function() {$verification.validateFormInput();});
        $verification.val('');

        $('#ss-delete-all-dialog').askConfirmation(function () {
            var request = $$.request.delete($$.util.url.getParentResourceURL()+"/");
            if ($$.model.getModule().isRootModule()){
                // There is a (known but untracked) bug on the server which returns
                // a wrong redirect header when deleting root projects.
                request.onSuccessRedirectTo("/");
            } else if ($$.util.meta.isViewName("user")) {
                // There is a (known but untracked) bug on the server which returns
                // a wrong redirect header when deleting users.
                request.onSuccessRedirectTo("/user");
            } else {
                request.onSuccessFollowRedirectInResponseHeader();
            }
            $$.util.leavingConfirmation.reset();
            request
                .onErrorAlert("Unable to delete",
                    "Something wrong happened when trying to delete this resource." +
                    " Maybe the server is unreachable, or the connection is down." +
                    "Please try later again.")
                .send();
        });
    });

    // Check if URL asks to trigger an action on pageload
    var actionNameToTrigger = $$.util.urlQueryParams.getValue("action");
    $("#ss-secondary-menu-action-" + actionNameToTrigger).first().click();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});


