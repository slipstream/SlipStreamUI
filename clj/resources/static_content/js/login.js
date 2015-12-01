jQuery( function() { ( function( $$, $, undefined ) {
    $('form#register').bootstrapValidator(); // TODO: Configure the validator via JS instead as inline in the HTML.

    $$.request
        .post()
        .onErrorStatusCodeAlert(500, "Server Error",
            "Sorry, something unexpected happend while processing your registration request.")
        .onSuccessAlert("Registration successful",
            "You will receive in short an email with instructions to validate the account.")
        .validation( function(){
          return $("form#register")
                      .bootstrapValidator("validate")
                      .find(".form-group.has-error")
                          .foundNothing();
        })
        .useToSubmitForm("#register");
}( window.SlipStream = window.SlipStream || {}, jQuery )); });
