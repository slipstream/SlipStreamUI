jQuery( function() { ( function( $$, $, undefined ) {

    function toggleBaseImageFormInputs(isBase) {
        var disableOptions = {disableReason: "This attribute will be removed when on saving."};
        // Toggle as well the two form inputs in the section "Operating System Details" below.
        $(".cloudimageid_imageid_, select#platform, input#loginUser")
            .closest("tr")
                .enableRow(isBase,
                    {disableReason: "This can only be set up for native (aka 'base') images."});
        $("input#module-reference")
            .closest("tr")
                .enableRow(! isBase,
                    {disableReason: "A native (aka 'base') image does not need a reference module."});
    }

    var $isBaseCheckbox = $("input#isbase");

    $isBaseCheckbox.change(function() {
        var isBase = $(this).prop("checked");
        toggleBaseImageFormInputs(isBase);
    });

    // Trigger the 'change' event for set up the correct state of the forms.
    $isBaseCheckbox.change();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
