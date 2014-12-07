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

    var discardedFormInputCls = "ss-discarded-form-input";

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
            // Disable deployment parameters without name to prevent
            // to be sent with the form request.
            $(".ss-table-with-blank-last-row tbody tr td:first-child input:first-child")
                .not(":disabled")
                    .blankInputs()
                        .closest("tr")
                            .find("select, input")
                                .addClass(discardedFormInputCls)
                                .disable();
            // Add scripts as hidden form fields
            $("pre.ss-code-editor").each(function (){
                var thisId = $(this).attr("id"),
                    code = $$.codeArea.getCode(thisId);
                $form.addFormHiddenField(thisId + "--script", code);
            });
        } else if (module.isOfCategory("deployment")) {
            // Disable deployment nodes without name or image or nodes flagges
            // to be removed to prevent to be sent with the form request.
            $("tr.ss-deployment-template-row")
                .add("tr.ss-deployment-node-unfinished-row")
                .add("tr.ss-disabled-row")
                    .find("select, input")
                        .addClass(discardedFormInputCls)
                        .disable();
            $(".ss-mapping-value input[type=text], .ss-mapping-value select")
                .filter(":visible")
                .not(":disabled")
                .not(discardedFormInputCls.asSel())
                    .each(function (){
                        var $this = $(this),
                            rawValue = $this.val() || "",
                            fieldName = $this.id(); // These fields have the same attr for 'id' and 'name'.
                        // We disable the fields by removing the 'name' attr, so that
                        // they are not sent in the form:
                        $this.removeAttr("name");
                        if($this.is("input[type=text]")) {
                            // We add a hidden field with the quoted valued:
                            $form.addFormHiddenField(fieldName, rawValue.ensureSingleQuoted());
                        } else if($this.is("select")) {
                            // We add a hidden field with the quoted valued:
                            $form.addFormHiddenField(fieldName, rawValue);
                        }
                    });
        }
    }

    function resetForm(){
        // reenable discarded text fields
        $(discardedFormInputCls.asSel())
            .removeClass(discardedFormInputCls)
            .enable();
        // Clean up the hidden fields that we added before submit
        $("form").cleanFormHiddenFields();
    }

    function usernameFromForm($form) {
        var username;
        if ($$.util.meta.isPageType("new")) {
            username = $("#name").val();
        } else {
            username = $("#name").text();
        }
        return username;
    }

    function updateRequestForUser(request, $form) {
        if ($$.util.meta.isPageType("edit")) {
            $form.addFormHiddenField("name", $("#name").text());
        }
        var username = usernameFromForm($form);
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

    function checkForm() {
        if ($$.util.meta.isViewName("configuration")) {
            // Nothing to validate
            return true;
        }
        var $saveForm = $("#save-form"),
            resourceName,
            suggestedName,
            isEmailMissing;
        if ($$.util.meta.isViewName("module")) {
            var module = $$.model.getModule();
            suggestedName = module.getBaseName();
            resourceName = module.getCategoryName();
            if (module.isOfCategory("deployment") &&
                $(".ss-deployment-node-unfinished-row").not(".ss-disabled-row").foundAny()) {
                $$.alert.showError("Unfinished deployment configuration",
                    "There are still some nodes without a reference image. Setup them correctly or remove them before saving.");
                return false;
            }
        } if ($$.util.meta.isViewName("user")) {
            suggestedName = usernameFromForm($saveForm);
            resourceName = "User";
            isEmailMissing = $saveForm.find("#email").val().mightBeAnEmailAddress().not();
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
        .always(resetForm)
        .validation(checkForm)
        .useToSubmitForm("#save-form", updateRequest);

    // Auto-open all dropdowns on mouseover.
    // The click action is still available for touch devices.
    $("body").bsOpenDropdownOnMouseOver();

    // Enable popovers
    $("[data-toggle='popover']").popover();

    // Enable tooltips
    $("[data-toggle='tooltip']").tooltip();


    // $("body").getSlipStreamModel().module.dump();
    // $("body").getSlipStreamModel().run.dump();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
