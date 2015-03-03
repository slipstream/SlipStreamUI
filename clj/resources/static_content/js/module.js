jQuery( function() { ( function( $$, $, undefined ) {

    var $inheritedGroupMembersCheckbox = $("input#inheritedGroupMembers"),
        $logoURLInput = $("input#logoLink"),
        $logoHeaderImg = $(".ss-header-image-container img");

    function toggleGroupMembersRow(areGroupMembersInherited) {
        $("#groupmembers")
            .closest("tr")
                .enableRow(! areGroupMembersInherited,
                    {disableReason: "This module will inherit the group members from its parent module."});
    }

    $inheritedGroupMembersCheckbox.change(function() {
        var areGroupMembersInherited = $(this).prop("checked");
        toggleGroupMembersRow(areGroupMembersInherited);
    });

    // Trigger the 'change' event for set up the correct state of the form on page load
    $inheritedGroupMembersCheckbox.change();

    $logoURLInput
        .onFormFieldValidation(function(isValidLogoURL, logoURLInputState){
            if (! $logoURLInput.hasFocus()) {
                // do nothing
                return;
            }
            if (! $logoURLInput.val() || logoURLInputState === undefined) {
                // Remove image
                $logoHeaderImg
                    .reloadImage("");
            } else if (logoURLInputState === "validating") {
                $logoHeaderImg
                    .reloadImage($logoURLInput.val(),
                        function(isLoaded){
                            $logoURLInput
                                .setFormInputValidationState(isLoaded ? "success" : "warning");
                        }
                    );
            }
        });


    // Hint on how to go to the latest version

    var $breadcrumbToLastestModuleVersion = $("#ss-breadcrumb-container li.ss-breadcrumb-item")
                                                .not(".disabled")
                                                    .last()
                                                        .find("a");

    // TODO: Retrieve text form clojure
    $breadcrumbToLastestModuleVersion.popover({ content:    "The module name in the breadcrumbs brings you always to the latest module version.",
                                                trigger:    "manual",
                                                placement:  "bottom"});
    $breadcrumbToLastestModuleVersion.tooltip({ title:      "Latest module version",
                                                trigger:    "hover focus",
                                                placement:  "bottom"});

    $(".alert-msg a[href^='module']")
        .hover(
            // TODO: Investigate why it doesn't work with 'partial()'
            //       $breadcrumbToLastestModuleVersion.popover.partial("show"),
            //       $breadcrumbToLastestModuleVersion.popover.partial("hide")
            function() {
                $breadcrumbToLastestModuleVersion.popover("show");
            },
            function() {
                $breadcrumbToLastestModuleVersion.popover("hide");
            }
        );


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
