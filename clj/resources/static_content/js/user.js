jQuery( function() { ( function( $$, $, undefined ) {

    var $firstPasswordInput = $("#password1"),
        $secondPasswordInput = $("#password2");

    $firstPasswordInput
        .onTextInputChange(function (){
            $secondPasswordInput
                .setRequiredFormInput($firstPasswordInput.val().trim() !== "")
                .validateFormInput();
        });

    function isPasswordEqualToFirstPasswordInput(password) {
        if (password) {
            return ($firstPasswordInput.val() === password);
        } else {
            // There is nothing to check yet.
            return true;
        }
    }

    $secondPasswordInput
        .addCustomFormFieldRequirement(isPasswordEqualToFirstPasswordInput);

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
