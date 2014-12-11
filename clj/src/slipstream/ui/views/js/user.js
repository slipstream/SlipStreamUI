jQuery( function() { ( function( $$, $, undefined ) {

    var $firstPasswordInput = $("#password1"),
        $secondPasswordInput = $("#password2");

    $firstPasswordInput
        .onTextInputChange(function (){
            $secondPasswordInput
                .setRequiredFormInput($firstPasswordInput.val().trim() !== "");
        });

    function isPasswordEqualToFirstPasswordInput(password) {
        return ($firstPasswordInput.val() === password);
    }

    $secondPasswordInput
        .addCustomFormFieldRequirement(isPasswordEqualToFirstPasswordInput);

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
