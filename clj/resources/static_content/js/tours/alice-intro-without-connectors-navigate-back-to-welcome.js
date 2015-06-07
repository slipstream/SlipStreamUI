jQuery( function() { ( function( $$, $, undefined ) {

    if ( $$.util.tour.shouldLaunch($$.util.tour.current(), false) ) {
        $$.util.tour.muteNextOptInDialog();
        $("#topbar a.navbar-brand").attr("href", "/?tour=alice.intro-without-connectors.welcome");
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
