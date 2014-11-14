jQuery( function() { ( function( $$, $, undefined ) {

    // Configure save dialog

    var $saveDialog = $("#ss-save-dialog");

    $saveDialog.on("shown.bs.modal", function (e) {
        $("#ss-save-dialog #ss-commit-message").focus();
    });

    function hideSaveDialogAndSubmitSaveForm(){
        $("#save-form")
            .addFormHiddenField("comment", $("#ss-commit-message").val())
            .submit();
        $saveDialog.modal("hide");
    }

    $saveDialog.onAltEnterPress(function () {
        hideSaveDialogAndSubmitSaveForm();
    });

    $saveDialog.find(".ss-save-btn").click(function() {
        hideSaveDialogAndSubmitSaveForm();
    });


    // Configure delete dialog

    $("#ss-delete-dialog .ss-delete-btn").click(function() {
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


    // Configure chooser dialog

    var $moduleChooserDialog = $("#ss-module-chooser-dialog");

    $("#ss-module-chooser-dialog iframe").bind('load', function(e) {
        var $iframe = $(this),
            $iframeContents = $iframe.contents(),
            module = $iframe.contents().getSlipStreamModel().module;
        $moduleChooserDialog
            .data("currentModule", module)
            .find(".ss-select-btn, .ss-select-exact-version-btn")
            .enable(module.isOfCategory("image"));
    });

    function updateReferenceModuleCell(imageName) {
        $(".ss-table-cell-reference-module-editable #module-reference")
            .val(imageName);                        // The form field value
        $(".ss-table-cell-reference-module-editable .ss-reference-module-name a")
            .attr("href", "/module/" + imageName)   // The link to the image module
            .text(imageName);                       // The string to display
        $moduleChooserDialog.modal("hide");
        return;
    }

    $("#ss-module-chooser-dialog .ss-select-btn").click(function() {
        updateReferenceModuleCell($moduleChooserDialog.data("currentModule").getFullName());
    });

    $("#ss-module-chooser-dialog .ss-select-exact-version-btn").click(function() {
        updateReferenceModuleCell($moduleChooserDialog.data("currentModule").getFullNameWithVersion());
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});


