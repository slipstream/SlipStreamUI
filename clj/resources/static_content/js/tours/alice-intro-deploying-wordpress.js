jQuery( function() { ( function( $$, $, undefined ) {

    $$.util.tour.queueLaunch($$.util.tour.alice.intro.waitingForWordpress);

    $("#ss-run-module-dialog").on("shown.bs.modal", function (e) {
        $$.util.tour.start();
    })
    .on("hide.bs.modal", function (e) {
        $$.util.tour.stop();
        $$.util.tour.dequeueLaunch($$.util.tour.alice.intro.waitingForWordpress);
    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
