jQuery( function() { ( function( $$, $, undefined ) {
    $('form#register').bootstrapValidator();

    $$.request
        .post()
        .onErrorStatusCodeAlert(500, "Server Error",
            "Sorry, something unexpected happend while processing your registration request.")
        .onSuccessAlert("Registration successful",
            "You will receive in short an email with instructions to validate the account.")
        .useToSubmitForm("#register");
}( window.SlipStream = window.SlipStream || {}, jQuery )); });
