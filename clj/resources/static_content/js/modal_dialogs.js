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


    // Configure reset password dialog

    $$.request
        .post("reset")
        .onSuccessAlert($("#ss-reset-password-form").data("success-message"))
        .always(function (){
            $("#ss-reset-password-dialog").modal("hide");
        })
        .useToSubmitForm("#ss-reset-password-form");


    // Configure chooser dialog

    var $imageChooserDialog = $("#ss-image-chooser-dialog");

    $("#ss-image-chooser-dialog iframe").bind('load', function(e) {
        var $this = $(this),
            $iframeContents = $this.contents(),
            module = $this.contents().getSlipStreamModel().module,
            isImage = module.isOfCategory("image");
        $imageChooserDialog
            .data("currentImageModule", isImage ? module : undefined)
            .find(".ss-select-btn, .ss-select-exact-version-btn")
            .enable(isImage);
        $this.enableBottomShadowAdjustOnScroll();

    });

    // Configure copy module dialog

    var $copyModuleDialog = $("#ss-copy-module-dialog"),
        $moduleCopyTargetName = $copyModuleDialog.find("#ss-module-copy-target-name");

    $copyModuleDialog.data("isProject", false);

    $moduleCopyTargetName
        .focusout(function() {
            // The form input validation is done with on 'bufferedtextinputchange' event
            // which triggers only when the input hasn't changed for a while (300ms).
            // Re-validating the input on 'focusout' allows to ensure that the users doesn't
            // tries to hit the submit (here: "copy") button during these 300ms.
            $moduleCopyTargetName.validateFormInput();
        });

    $$.request
        .post()
        .onSuccessFollowRedirectInResponseHeader()
        .always(function (){
            $copyModuleDialog.modal("hide");
        })
        .useToSubmitForm("#ss-module-copy-form");

    $copyModuleDialog.find("iframe").bind('load', function(e) {
        var $this = $(this),
            module = $this.contents().getSlipStreamModel().module,
            isProject = module.isOfCategory("project"),
            targetProject = isProject ? module.getFullName() : undefined,
            $moduleCopyTargetProject = $copyModuleDialog.find("#ss-module-copy-target-project");
        $copyModuleDialog
            .data("isProject", isProject)
            .find("form")
                .attr("action", "/module/" + targetProject)
                .find("input")
                    .toggleFormInputValidationState(isProject)
                    .enable(isProject);
        $this.enableBottomShadowAdjustOnScroll();
        if (targetProject) {
            $moduleCopyTargetProject.text(targetProject);
        } else {
            $moduleCopyTargetProject.append("/ ? ");
        }
    });


    // Configure image build, image run and deployment run dialogs

    // Tweak vertical alingment of the 'mutable' checkbox label on 'modal show'
    // TODO: Doesn't work when the modal is not shown, and on the 'shown' event, we see the label springing :(
    //       Doing it in table.css, but is not exact.
    // $(".ss-run-module-dialog").on("shown.bs.modal", function (e) {
    //     var leftOffset =  $(".ss-run-module-dialog .ss-inner-table tr td:first-of-type").position().left;
    //     $(".ss-run-module-dialog tr.ss-run-deployment-mutable-checkbox-row td:first-of-type")
    //         .css("padding-left", leftOffset);
    // });

    function updateRequestForRunDeployment(request, $form) {
        $("#ss-run-module-dialog input[type=text]")
            .each(function (){
                // TODO: Extract this function to $.fn.extend since it's also used in creation and save forms.
                var $this = $(this),
                    $form = $this.closest("form"),
                    rawValue = $this.val() || "",
                    fieldName = $this.id(); // These fields have the same attr for 'id' and 'name'.
                // We disable the fields by removing the 'name' attr, so that
                // they are not sent in the form:
                $this.removeAttr("name");
                if($this.is(".ss-value-must-be-single-quoted input")) {
                    // We add a hidden field with the quoted value
                    $form.addFormHiddenField(fieldName, rawValue.ensureSingleQuoted());
                } else {
                    // We add a hidden field with the value (without adding quotes)
                    $form.addFormHiddenField(fieldName, rawValue);
                }
            });
    }

    // Configure the request for both build and run actions on images
    // (therefore we use .classes in the selectors instead of #ids)
    $$.request
        .post("/run")
        .onSuccessFollowRedirectInResponseHeader()
        .always(function (){
            $(".ss-run-module-dialog").modal("hide");
        })
        .useToSubmitForm(".ss-run-module-form, .ss-build-module-form", updateRequestForRunDeployment);


    // Public functions

    $$.modalDialogs = {
        askForImageModule: function (callback){
            $imageChooserDialog.askConfirmation(function(){
                var $buttonPressed = $(this),
                    chosenImageModule = $imageChooserDialog.data("currentImageModule"),
                    chosenImage;
                if ($buttonPressed.hasClass("ss-select-exact-version-btn")) {
                    chosenImage = chosenImageModule.asExactVersionImage();
                } else if ($buttonPressed.hasClass("ss-select-btn")) {
                    chosenImage = chosenImageModule.asImage();
                } else {
                    throw "Unexpected button pressed";
                }
                callback.call(chosenImage, chosenImage.toString());
            });
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
