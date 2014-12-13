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
            if (isValidLogoURL && logoURLInputState !== "warning" && $logoURLInput.hasFocus()) {
                $logoHeaderImg
                    .reloadImage($logoURLInput.val(),
                        function(isLoaded){
                            if (!isLoaded && $logoURLInput.val()) {
                                $logoURLInput
                                    .setFormInputValidationState("warning");
                            }
                      // // Un-comment that to flash the logo after the change:
                      //   $logoHeaderImg
                      //       .parent()
                      //           .flash(isLoaded ? "success" : "warning");
                    }
                    );
            }
        });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
