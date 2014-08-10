jQuery( function() { ( function( $$, $, undefined ) {

    $(document.body).css("padding-top", $(".navbar").height() || 0);

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

    // Remove secondary menu if no actions
    $(".ss-secondary-menu-main-action span:last-of-type:empty")
        .closest("#ss-secondary-menu")
        .remove();

    // Remove secondary menu dropdown caret if no extra actions
    if ( ! $(".ss-secondary-menu-extra-actions-container .ss-secondary-menu-extra-action:first-child a").length) {
        $(".ss-secondary-extra-actions-toggle").remove();
    }

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
}( window.SlipStream = window.SlipStream || {}, jQuery ));});
