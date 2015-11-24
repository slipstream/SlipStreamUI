jQuery( function() { ( function( $$, $, undefined ) {

    $(".ss-login-error-on-mobile-alert")
        .click(function (e) {
            e.preventDefault();
            $(this).stop().fadeOut();
            return false;
        });

    $$.request
        .post("/login")
        .onErrorStatusCodeAlert(401, "Wrong credentials",
                                     "The provided username doesn't exist or the password is incorrect. Please try again.")
        .onErrorStatusCode(401, function(){
            var $loginErrorAlert = $(".ss-login-error-on-mobile-alert"),
                shouldFadeIn = $loginErrorAlert.prev().is(":visible");
            if ( shouldFadeIn ) {
                $(".ss-login-error-on-mobile-alert").fadeIn().delay(2000).fadeOut();
            }
        })
        .onErrorStatusCodeAlert(500, "Authentication internal error",
                                      "Error in contacting authentication server. Please contact administrator.")
        .onErrorStatusCode(500, function(){
            var $loginErrorAlert = $(".ss-login-error-on-mobile-alert"),
                shouldFadeIn = $loginErrorAlert.prev().is(":visible");
            if ( shouldFadeIn ) {
                $(".ss-login-error-on-mobile-alert").fadeIn().delay(2000).fadeOut();
            }
        })
        .onSuccessFollowRedirectInURL()
        .useToSubmitForm("#login");

    function logout () {
        $$.request
            .delete("/logout")
            .onSuccessRedirectTo("/login")
            .onErrorAlert("Unable to log out",
                "Something wrong happened when trying to log you out." +
                " Maybe the server is unreachable, or the connection is down." +
                "Please try later again.")
            .send();
    }
    $("a.ss-logout-action").on("click", logout);

}( window.SlipStream = window.SlipStream || {}, jQuery )); });
