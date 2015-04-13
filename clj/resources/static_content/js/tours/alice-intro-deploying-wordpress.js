jQuery( function() { ( function( $$, $, undefined ) {

    $("#ss-run-module-dialog").on("shown.bs.modal", function (e) {
        $$.util.tour.start();
    })
    .on("hide.bs.modal", function (e) {
        $$.util.tour.stop();
    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
