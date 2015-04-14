jQuery( function() { ( function( $$, $, undefined ) {

    $$.util.tour.setup({
        beforeStart: function() {
            $$.util.recurrentJob.stopAll();
        },
        onExit: function() {
            $$.util.recurrentJob.restartAll();
        }
    });

    if ( $$.util.tour.shouldLaunch($$.util.tour.alice.intro.waitingForWordpress) ) {
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
