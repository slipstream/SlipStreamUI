jQuery( function() { ( function( $$, $, undefined ) {

    var module = $$.model.getModule();

    function newModuleRedirect(category) {
        var moduleURI = module.getURI() || "/module";
        $$.util.url.redirectTo(moduleURI + "/new?category=" + category + "#summary");
    }

    $("#ss-secondary-menu-action-new-project").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #1");
        newModuleRedirect("Project");
    });

    $("#ss-secondary-menu-action-new-image").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #2");
        newModuleRedirect("Image");
    });

    $("#ss-secondary-menu-action-new-deployment").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #3");
        newModuleRedirect("Deployment");
    });

    $("#ss-secondary-menu-action-import").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #4");
    });

    $("#ss-secondary-menu-action-new-user").clickWhenEnabled( function() {
        $$.util.url.redirectTo("/user/new");
    });

    $("#ss-secondary-menu-action-run").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #7");
    });

    $("#ss-secondary-menu-action-build").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #8");
    });

    $("#ss-secondary-menu-action-copy").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #9");
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
            $dateRow.fadeTo(200, 1);
            $dateRow.data("ss-date-row-inactive", false);
        } else {
            $dateRow.fadeTo(200, 0.3);
            $dateRow.data("ss-date-row-inactive", true);
        }
        return;
    }

    function publishActionAlertMessage(isPublished) {
        return "The " + module.getCategoryName().toLowerCase() +
               " <code class='text-success'>" + module.getBaseName() + "</code>" +
               " (v " + module.getVersion() + ") was successfully " +
               (isPublished ? "published in" : "removed from") +
               " the public <a href='/#app-store' class='text-success'>App Store</a>.";
    }

    $("#ss-secondary-menu-action-publish").clickWhenEnabled( function() {
        $$.request
            .put(module.getURIWithVersion() + "/publish")
            .onSuccess(togglePublishButton)
            .onSuccess(togglePublicationDateRow)
            .onSuccessAlert(module.getCategoryName() + " published",
                publishActionAlertMessage(true))
            .send();
    });

    $("#ss-secondary-menu-action-unpublish").clickWhenEnabled( function() {
        $$.request
            .delete(module.getURIWithVersion() + "/publish")
            .onSuccess(togglePublishButton)
            .onSuccess(togglePublicationDateRow)
            .onSuccessAlert(module.getCategoryName() + " unpublished",
                publishActionAlertMessage(false))
            .send();
    });


    // Terminate action

    $("#ss-secondary-menu-action-terminate").clickWhenEnabled( function() {
        $('#ss-terminate-deployment-dialog').askConfirmation(function(){
            console.log($(this).attr("id") + " in callback #12 after confirmation");
        });
    });

    $("#ss-secondary-menu-action-edit").clickWhenEnabled( function() {
        window.location.search = 'edit=true';
    });

    $("#ss-secondary-menu-action-save").clickWhenEnabled( function(event) {
        if (event.altKey ||
            $$.util.meta.isViewName("user") ||
            $$.util.meta.isViewName("configuration")) {
            // No commit message needed.
            $('#save-form').submit();
        } else {
            $('#ss-save-dialog').askConfirmation(function(){
                $("#save-form")
                    .addFormHiddenField("comment", $("#ss-commit-message").val())
                    .submit();
            });
        }
    });

    $("#ss-secondary-menu-action-create").clickWhenEnabled( function() {
        var $form = $("#create-form");
        if ($$.util.meta.isViewName("module")) {
            $form.addFormHiddenField("comment", "Initial version of this " + module.getCategoryName().toLowerCase() + ".");
        }
        $form.submit();
    });

    $("#ss-secondary-menu-action-cancel").clickWhenEnabled( function() {
        window.location = $$.util.url.getCurrentURLBase();
    });

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
            request
                .onErrorAlert("Unable to delete",
                    "Something wrong happened when trying to delete this resource." +
                    " Maybe the server is unreachable, or the connection is down." +
                    "Please try later again.")
                .send();
        });
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});


