jQuery( function() { ( function( $$, $, undefined ) {

    $$.request
        .post()
        .onErrorStatusCodeAlert(401, "Wrong credentials")
        .onSuccessFollowRedirectInURL()
        .useToSubmitForm("#login");

    function logout () {
        $$.request
            .delete("/logout")
            .onSuccessRedirectURL("/login")
            .onErrorAlert("Unable to log out",
                "Something wrong happened when trying to log you out." +
                " Maybe the server is unreachable, or the connection is down." +
                "Please try later again.")
            .send();
    }
    $("a.ss-logout-action").on("click", logout);

}( window.SlipStream = window.SlipStream || {}, jQuery )); });
