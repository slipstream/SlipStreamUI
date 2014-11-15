jQuery( function() { ( function( $$, $, undefined ) {


    var $modalDialogs = $("div.modal");

    $modalDialogs.onAltEnterPress(function () {
        // Enable ALT-ENTER shortcut for all OK buttons (.ss-ok-btn)
        // If there is more than one OK button, we enable the shurtcut for the first one.
        $(this).find(".ss-ok-btn").first().click();
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


    // Configure copy module dialog

    var $copyModuleDialog = $("#ss-copy-module-dialog"),
        $moduleCopyTargetName = $copyModuleDialog.find("#ss-module-copy-target-name");

    $$.request
        .post()
        .onSuccessFollowRedirectInResponseHeader()
        .always(function (){
            $copyModuleDialog.modal("hide");
        })
        .useToSubmitForm("#ss-module-copy-form");

    function toggleFormValidation() {
        // BootstrapValidator needs now a comercial licence :(
        // Source: http://bootstrapvalidator.com/download/
        var isTargetNamePresent = $moduleCopyTargetName.val() ? true : false,
            isValidForm = isTargetNamePresent && $copyModuleDialog.data("isProject");
        $copyModuleDialog
            .find(".ss-ok-btn")
            .enable(isValidForm);
        $moduleCopyTargetName
            .toggleFormInputValidationState(isValidForm);
    }

    $moduleCopyTargetName.keyup(function(e) {
        // Backspace does not trigger 'keypress'.
        toggleFormValidation();
    });

    $copyModuleDialog.find("iframe").bind('load', function(e) {
        var module = $(this).contents().getSlipStreamModel().module,
            isProject = module.isOfCategory("project"),
            targetProject = isProject ? module.getFullName() : undefined;
        $copyModuleDialog
            .data("isProject", isProject)
            .find("form")
            .attr("action", "/module/" + targetProject)
            .find("input")
            .toggleFormInputValidationState(isProject)
            .enable(isProject);
        $copyModuleDialog
            .find("#ss-module-copy-target-project")
            .text(targetProject);
        toggleFormValidation();
    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});


