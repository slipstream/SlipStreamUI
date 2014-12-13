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
        // TODO: Update image on logo url input validation event.
        .focusout(function(){
            if ($logoURLInput.isValidFormInput()) {
                $logoHeaderImg
                    .reloadImage($logoURLInput.val()
                    //   // Un-comment that to flash the logo after the change:
                    //     , function(isLoaded){
                    //     $logoHeaderImg
                    //         .parent()
                    //             .flash(isLoaded ? "success" : "warning");
                    // }
                    );
            }
        });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
