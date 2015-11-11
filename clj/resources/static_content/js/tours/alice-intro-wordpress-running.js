jQuery( function() { ( function( $$, $, undefined ) {

    var currentTour         = $$.util.tour.current(),
        $documentationMenu  = $(".ss-action-documentation");

    $("body").on("ss-run-service-is-ready", function() {
        console.info("run is ready");
        $$.util.tour.goToStep(1);
    });

    $$.util.tour.setup({
        beforeStep: {
            2: function() {
                $$.util.tour.enableMouseShield();
            },
            3: function() {
                $documentationMenu.bsOpenDropdown();
            },
            4: function() {
                $$.util.urlQueryParams.remove("tour");
            },
        },
        onExitStep: {
            0: function() {
                $$.run.stopAutoupdatingRunPage();
            },
            2: function() {
                $$.util.tour.disableMouseShield();
            },
            3: function() {
                $documentationMenu.bsCloseDropdown();
            },
        },
        onExit: function() {
            $$.util.tour.disableMouseShield();
            $$.run.startAutoupdatingRunPage();
            $$.util.urlQueryParams.remove("tour");
        }
    });

    if ( $$.util.tour.shouldLaunch(currentTour) ) {
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
