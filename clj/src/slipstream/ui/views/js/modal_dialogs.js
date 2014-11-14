jQuery( function() { ( function( $$, $, undefined ) {


    var $modalDialogs = $("div.modal");

    $modalDialogs.onAltEnterPress(function () {
        // Enable ALT-ENTER shortcut for ss-save-btn buttons
        $(this).find(".ss-save-btn").click();
    });

    $modalDialogs.on("shown.bs.modal", function (e) {
        $(this).focusFirstInput();
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


