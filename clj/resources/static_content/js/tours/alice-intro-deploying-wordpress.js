jQuery( function() { ( function( $$, $, undefined ) {

    var currentTour = $$.util.tour.current();

    // TODO: Currently the server provides only the *introWithoutConnectors.waitingForWordpress tour,
    //       Since there is no way of passing which version must be used when launching the run.
    // if ( currentTour          == $$.util.tour.alice.intro                 .deployingWordpress) {
    //     $$.util.tour.queueLaunch($$.util.tour.alice.intro                 .waitingForWordpress);
    // } else if ( currentTour   == $$.util.tour.alice.introWithoutConnectors.deployingWordpress) {
    //     $$.util.tour.queueLaunch($$.util.tour.alice.introWithoutConnectors.waitingForWordpress);
    // }
    $$.util.tour.queueLaunch($$.util.tour.alice.introWithoutConnectors.waitingForWordpress);

    $("#ss-run-module-dialog").on("shown.bs.modal", function (e) {
        $$.util.tour.start();
    })
    .on("hide.bs.modal", function (e) {
        $$.util.tour.stop();
        $$.util.tour.dequeueLaunch($$.util.tour.alice.intro.waitingForWordpress);
    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
