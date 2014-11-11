jQuery( function() { ( function( $$, $, undefined ) {

    // Configure save dialog

    var $saveDialog = $("#ss-save-dialog");

    $saveDialog.on("shown.bs.modal", function (e) {
        $("#ss-save-dialog #ss-commit-message").focus();
    });

    function hideSaveDialogAndSubmitSaveForm(){
        var $saveForm = $("#save-form");
        $$.util.form.addHiddenField($saveForm, "comment", $saveDialog.find("#ss-commit-message").val());
        $saveForm.submit();
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
        $$.request
            .delete($$.util.url.getCurrentURLBase())
            .onSuccessRedirectURL($$.util.url.getParentResourceURL())
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
            currentPathname = $iframe.get(0).contentWindow.location.pathname,
            currentImageName = $iframeContents.find("#ss-module-name").text(),
            currentImageVersion = $iframeContents.find(".ss-table-cell-module-version span").text(),
            currentViewName = $iframeContents.find("meta[name=ss-view-name]").attr("content").toLowerCase(),
            currentModuleCategory = $iframeContents.find("#category").text().toLowerCase();
        $moduleChooserDialog
            .data("currentPathname", currentPathname)
            .data("currentImageName", currentImageName)
            .data("currentExactVersionImageName", currentImageName + "/" + currentImageVersion)
            .find(".ss-select-btn, .ss-select-exact-version-btn")
            .enable(currentViewName === "module" && currentModuleCategory === "image");
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
        updateReferenceModuleCell($moduleChooserDialog.data("currentImageName"));
    });

    $("#ss-module-chooser-dialog .ss-select-exact-version-btn").click(function() {
        updateReferenceModuleCell($moduleChooserDialog.data("currentExactVersionImageName"));
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});


