jQuery( function() { ( function( $$, $, undefined ) {

    $$.Request
        .post()
        .onErrorStatusCodeAlert(401, "Wrong credentials")
        .onSuccessFollowRedirectInURL()
        .useToSubmitForm("#login");

    function logout () {
        $$.Request
            .delete("/logout")
            .onSuccessRedirectURL("/login")
            .onErrorAlert("Something wrong happened when trying to log out.")
            .send();
    }
    $("a.ss-logout-action").on("click", logout);

}( window.SlipStream = window.SlipStream || {}, jQuery )); });
