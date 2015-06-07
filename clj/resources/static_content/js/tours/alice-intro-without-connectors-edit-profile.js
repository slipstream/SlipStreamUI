jQuery( function() { ( function( $$, $, undefined ) {

    $$.util.tour.setup({
        beforeStep: {
            1: function() {
                $$.section.selectWithoutAnimation(1);
            },
            2: function() {
                $$.section.selectWithoutAnimation(3);
            }
        },
        onStep: {
            3: function() {
                $$.util.tour.queueLaunch($$.util.tour.alice.introWithoutConnectors.navigateBackToWelcome);
            }
        }
    });

    if ( $$.util.tour.shouldLaunch($$.util.tour.current(), true) ) {
        $$.util.tour.start();
    }


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
