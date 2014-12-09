jQuery( function() { ( function( $$, $, undefined ) {

    if (! $$.util.meta.isPageType("edit")) {
        // nothing to do
        return;
    }

    var $inheritedGroupMembersCheckbox = $("input#inheritedGroupMembers");

    function toggleGroupMembersRow(areGroupMembersInherited) {
        $("input#groupmembers")
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

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
