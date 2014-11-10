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

    function updateRequest(request, $form) {
        switch ($$.util.meta.getViewName()) {
            case "user":
                console.log("in user view");
                if ($$.util.meta.isPageType("edit")) {
                    $$.util.form.addHiddenField($form, "name", $("#name").text());
                }
                request.settings.url = "/user/";
                if ($$.util.meta.isPageType("new")) {
                    request.settings.url += $("#name").val();
                } else {
                    request.settings.url += $("#name").text();
                }
                break;
            case "module":
                console.log("in module view");
                var moduleName,
                    category;
                if ($$.util.meta.isPageType("new")) {
                    moduleName = $form.find("#ss-module-name").val();
                    category = $$.util.urlQueryParams.getValue("category");
                    request.settings.url = $$.util.url.getParentResourceURL() + "/" + moduleName + "?new=true";
                    var moduleParent = $$.util.url.getCurrentURLBase().substring("/module/".length);
                    $$.util.form.addHiddenField($form, "name", moduleParent + "/" + moduleName);
                } else {
                    moduleName = $("#ss-module-name").text();
                    category = $("#category").text();
                    request.settings.url = "/module/" + moduleName;
                    $$.util.form.addHiddenField($form, "name", moduleName);
                }
                $$.util.form.addHiddenField($form, "category", category);
                break;
            default:
                console.log("in some other view");
                // nothing to do
                break;
        }
    }

    $$.request
        .put()
        .onSuccessFollowRedirectInResponseHeader()
        .useToSubmitForm("#save-form", updateRequest);

    $$.request
        .put()
        .onSuccessFollowRedirectInResponseHeader()
        .useToSubmitForm("#create-form", updateRequest);


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
