jQuery( function() { ( function( $$, $, undefined ) {

    if ( $$.util.tour.shouldLaunch($$.util.tour.current(), false) ) {
        $$.util.tour.muteNextOptInDialog();
        $("#topbar .ss-action-appstore").attr("href", "appstore/?tour=alice.intro-without-connectors.welcome");
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
