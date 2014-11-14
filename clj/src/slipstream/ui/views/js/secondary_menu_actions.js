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
            .onSuccessAlert(module.getCategoryName() + " published",
                publishActionAlertMessage(true))
            .send();
    });

    $("#ss-secondary-menu-action-unpublish").clickWhenEnabled( function() {
        $$.request
            .delete(module.getURIWithVersion() + "/publish")
            .onSuccess(togglePublishButton)
            .onSuccessAlert(module.getCategoryName() + " unpublished",
                publishActionAlertMessage(false))
            .send();
    });


    // Terminate action

    $("#ss-secondary-menu-action-terminate").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #12");
        $('#ss-terminate-deployment-dialog').modal('show');
    });

    $("#ss-secondary-menu-action-edit").clickWhenEnabled( function() {
        console.log($(this).attr("id") + " in callback #13");
        window.location.search = 'edit=true';
    });

    $("#ss-secondary-menu-action-save").clickWhenEnabled( function(event) {
        if ($$.util.meta.isViewName("user") || event.altKey) {
            // No commit message needed.
            $('#save-form').submit();
        } else {
            $('#ss-save-dialog').modal('show');
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
        $('#ss-delete-dialog').modal("show");
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});


