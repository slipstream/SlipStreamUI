jQuery( function() { ( function( $$, $, undefined ) {

    var currentTour         = $$.util.tour.current(),
        $documentationMenu  = $(".ss-action-documentation");

    $("body").on("ss-run-service-is-ready", function() {
        console.info("run is ready");
        $$.util.tour.goToStep(1);
    });

    $$.util.tour.setup({
        beforeStep: {
            3: function() {
            $documentationMenu.bsOpenDropdown();
            },
        },
        onExitStep: {
            0: function() {
                $$.run.stopAutoupdatingRunPage();
            },
            3: function() {
            $documentationMenu.bsCloseDropdown();
            },
        },
        onExit: function() {
            $$.run.startAutoupdatingRunPage();
        }
    });

    if ( $$.util.tour.shouldLaunch(currentTour, true) ) {
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
