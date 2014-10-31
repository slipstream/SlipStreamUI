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

    // Remove secondary menu if no actions
    $(".ss-secondary-menu-main-action span:last-of-type:empty")
        .closest("#ss-secondary-menu")
        .remove();

    // Remove secondary menu dropdown caret if no extra actions
    if ( ! $(".ss-secondary-menu-extra-actions-container .ss-secondary-menu-extra-action:first-child a").length) {
        $(".ss-secondary-extra-actions-toggle").remove();
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

    function includeUsernameToForm(request, $form) {
        $("<input>")
            .attr("type", "hidden")
            .attr("name", "name")
            .attr("value", $("#name").text())
            .appendTo($form);
    }

    $$.request
        .put($$.util.url.getCurrentURLBase())
        .onErrorAlert("Sorry, something went wrong with the update.")
        .onSuccessReloadPageWithoutQueryParamsInURL()
        // .onSuccessAlert("Your changes are saved.")
        .useToSubmitForm("#save-form", includeUsernameToForm);

    function updateRequestURLWithUsername(request, $form) {
        request.settings.url = "/user/" + $("#name").val();
    }

    $$.request
        .put()
        .onErrorAlert("Sorry, something went wrong with the update.")
        .onSuccessFollowRedirectInResponseHeader()
        .useToSubmitForm("#create-form", updateRequestURLWithUsername);


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
