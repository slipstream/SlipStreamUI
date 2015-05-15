jQuery( function() { ( function( $$, $, undefined ) {

    if ( $$.util.tour.shouldLaunch("alice.intro-without-connectors.navigate-back-to-welcome", false) ) {
        $$.util.tour.muteNextOptInDialog();
        $("#topbar a.navbar-brand").attr("href", "/?tour=alice.intro-without-connectors.welcome");
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
