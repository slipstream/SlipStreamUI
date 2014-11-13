jQuery( function() { ( function( $$, $, undefined ) {

    function newModuleRedirect(category) {
        var moduleURI = $$.model.getModule().getURI() || "/module";
        $$.util.url.redirectTo(moduleURI + "/new?category=" + category + "#summary");
    }

    $("#ss-secondary-menu-action-new-project").click( function() {
        console.log($(this).attr("id") + " in callback #1");
        newModuleRedirect("Project");
    });

    $("#ss-secondary-menu-action-new-image").click( function() {
        console.log($(this).attr("id") + " in callback #2");
        newModuleRedirect("Image");
    });

    $("#ss-secondary-menu-action-new-deployment").click( function() {
        console.log($(this).attr("id") + " in callback #3");
        newModuleRedirect("Deployment");
    });

    $("#ss-secondary-menu-action-import").click( function() {
        console.log($(this).attr("id") + " in callback #4");
    });

    $("#ss-secondary-menu-action-new-user").click( function() {
        $$.util.url.redirectTo("/user/new");
    });

    $("#ss-secondary-menu-action-edit-user").click( function() {
        console.log($(this).attr("id") + " in callback #6");
    });

    $("#ss-secondary-menu-action-run").click( function() {
        console.log($(this).attr("id") + " in callback #7");
    });

    $("#ss-secondary-menu-action-build").click( function() {
        console.log($(this).attr("id") + " in callback #8");
    });

    $("#ss-secondary-menu-action-copy").click( function() {
        console.log($(this).attr("id") + " in callback #9");
    });

    $("#ss-secondary-menu-action-publish").click( function() {
        console.log($(this).attr("id") + " in callback #10");
    });

    $("#ss-secondary-menu-action-unpublish").click( function() {
        console.log($(this).attr("id") + " in callback #11");
    });

    $("#ss-secondary-menu-action-terminate").click( function() {
        console.log($(this).attr("id") + " in callback #12");
        $('#ss-terminate-deployment-dialog').modal('show');
    });

    $("#ss-secondary-menu-action-edit").click( function() {
        console.log($(this).attr("id") + " in callback #13");
        window.location.search = 'edit=true';
    });

    $("#ss-secondary-menu-action-save").click( function(event) {
        if ($$.util.meta.isViewName("user") || event.altKey) {
            // No commit message needed.
            $('#save-form').submit();
        } else {
            $('#ss-save-dialog').modal('show');
        }
    });

    $("#ss-secondary-menu-action-create").click( function() {
        $("#create-form").submit();
    });

    $("#ss-secondary-menu-action-cancel").click( function() {
        window.location = $$.util.url.getCurrentURLBase();
    });

    $("#ss-secondary-menu-action-delete").click( function() {
        $('#ss-delete-dialog').modal("show");
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});


