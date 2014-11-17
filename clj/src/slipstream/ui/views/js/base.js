jQuery( function() { ( function( $$, $, undefined ) {

    // $(document.body).css("padding-top", $(".navbar").height() || 0);

    // $("#form-signin").bootstrapValidator();
    // $("#form-pwd-reset").bootstrapValidator();
    // $("#form-contact-us").bootstrapValidator();

    // $("#contact-request-textarea").maxlength({
    //     placement: "bottom",
    //     threshold: 50,
    //     showCharsTyped: false,
    //     warningClass: "label label-warning",
    //     limitReachedClass: "label label-danger",
    //     preText: "You can still write ",
    //     separator: " chars (",
    //     postText: " max)"
    // });

    if (window.navigator.standalone) {
        // The app is running in standalone mode on a mobile device.
        // Add 20px to the top to accomodate a translucent status bar by css.
        $("body").addClass("ss-standalone-webapp-mobile");
    }

    if ($("body").hasClass("ss-page-type-chooser")) {
        // Make secondary-menubar fixed to top directly
        $("#ss-secondary-menubar-container").affix({
            offset: {
                top: 0
            }
        });
        $("#ss-secondary-menubar-container").on("affixed.bs.affix", function () {
            $("#ss-secondary-menubar-container.affix").css("top",0);
            $("#ss-secondary-menubar-placeholder").show();
        });
        $("#ss-secondary-menubar-container").on("affixed-top.bs.affix", function () {
            $("#ss-secondary-menubar-placeholder").hide();
        });
    } else {
        // Make secondary-menubar fixed to below the topbar after scrolling past the header
        $("#ss-secondary-menubar-container").affix({
            offset: {
                top: function () {
                    return ($("#header").offset().top + $("#header").outerHeight() - $("#topbar").outerHeight());
                }
            }
        });

        $("#ss-secondary-menubar-container").on("affixed.bs.affix", function () {
            $("#ss-secondary-menubar-container.affix").css("top",$("#topbar").outerHeight());
            $("#ss-secondary-menubar-placeholder").show();
        });
        $("#ss-secondary-menubar-container").on("affixed-top.bs.affix", function () {
            $("#ss-secondary-menubar-container.affix-top").css("top",0);
            $("#ss-secondary-menubar-placeholder").hide();
        });
    }

    // Set up forms

    function updateRequestForModule(request, $form) {
        var module = $form.getSlipStreamModel().module;
        if ($$.util.meta.isPageType("new")) {
            request.url(module.getURI() + "?new=true");
        } else {
            request.url(module.getURI());
        }
        $form
            .addFormHiddenField("name", module.getFullName())
            .addFormHiddenField("category", module.getCategoryName());

        if (module.isOfCategory("image")) {
            // Add scripts as hidden form fields
            $("pre.ss-code-editor").each(function (){
                var thisId = $(this).attr("id"),
                    code = $$.codeArea.getCode(thisId);
                $form.addFormHiddenField(thisId + "--script", code);
            });
        }

        return;
    }

    function updateRequestForUser(request, $form) {
        if ($$.util.meta.isPageType("edit")) {
            $form.addFormHiddenField("name", $("#name").text());
        }
        var username;
        if ($$.util.meta.isPageType("new")) {
            username = $("#name").val();
        } else {
            username = $("#name").text();
        }
        request.url("/user/" + username);
    }

    function updateRequest(request, $form) {
        switch ($$.util.meta.getViewName()) {
            case "user":
                updateRequestForUser(request, $form);
                break;
            case "module":
                updateRequestForModule(request, $form);
                break;
            default:
                // nothing to do
                break;
        }
    }

    function checkCreationForm() {
        var $createForm = $("#create-form"),
            resourceName,
            suggestedName,
            isEmailMissing;
        if ($$.util.meta.isViewName("module")) {
            var module = $$.model.getModule();
            suggestedName = module.getBaseName();
            resourceName = module.getCategoryName();
        } else {
            // User is the only resource beyond module that can be created
            suggestedName = $createForm.find("#name").val();
            resourceName = "User";
            isEmailMissing = $createForm.find("#email").val().mightBeAnEmailAddress().not();
        }
        if (! suggestedName) {
            $$.alert.showError(resourceName + " name missing",
                "Please provide a name for the new " + resourceName.toLowerCase() + ".");
            return false;
        } else if ($$.util.string.caseInsensitiveEqual(suggestedName, "new")) {
            $$.alert.showError("Invalid " + resourceName + " name",
                "'new' is not a valid "  + resourceName.toLowerCase() + " name.");
            return false;
        } else if (isEmailMissing) {
            $$.alert.showError("Email missing",
                "Please provide a valid email address for the new " + resourceName.toLowerCase() + ".");
            return false;
        }
        return true;
    }

    $$.request
        .put()
        .onSuccessFollowRedirectInResponseHeader()
        .useToSubmitForm("#save-form", updateRequest);

    $$.request
        .put()
        .onSuccessFollowRedirectInResponseHeader()
        .validationCallback(checkCreationForm)
        .useToSubmitForm("#create-form", updateRequest);

    // Auto-open all dropdowns on mouseover.
    // The click action is still available for touch devices.
    $("body").bsOpenDropdownOnMouseOver();

    // $("body").getSlipStreamModel().module.dump();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
