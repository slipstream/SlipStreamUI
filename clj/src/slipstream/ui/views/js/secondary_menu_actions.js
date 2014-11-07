jQuery( function() { ( function( $$, $, undefined ) {

    function newModuleRedirect(category) {
        var moduleName = $('#ss-module-name').text();
        window.location = '/module/' + (moduleName && moduleName + "/") + "new?category=" + category;
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
        console.log($(this).attr("id") + " in callback #5");
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

    $("#ss-secondary-menu-action-save").click( function() {
        console.log($(this).attr("id") + " in callback #14");
        $('#ss-save-dialog').modal('show');
    });

    $("#ss-secondary-menu-action-create").click( function() {
        console.log($(this).attr("id") + " in callback #14 bis");
        $("#create-form").submit();
    });

    $("#ss-secondary-menu-action-cancel").click( function() {
        console.log($(this).attr("id") + " in callback #15");
        window.location = $$.util.url.getCurrentURLBase();
    });

    $("#ss-secondary-menu-action-delete").click( function() {
        console.log($(this).attr("id") + " in callback #16");
        $('#ss-delete-dialog').modal('show');
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});


