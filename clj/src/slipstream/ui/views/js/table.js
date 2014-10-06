jQuery( function() { ( function( $$, $, undefined ) {

    $(".ss-table-tooltip").tooltip();

    $(".ss-reference-module-chooser-button button").click( function() {
        console.log($(this).attr("id") + " in tables.js callback #100");
        $('#ss-module-chooser-dialog').modal('show');
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
