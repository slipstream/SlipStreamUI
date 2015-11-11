jQuery( function() { ( function( $$, $, undefined ) {

    var cloudUsedForTheTour = $$.util.urlQueryParams.getValue("cloud"),
        wordpressRunId      = $$.util.urlQueryParams.getValue("wordpress-run-id"),
        updatedRunIdLinkCls = "ss-updated-run-id",
        currentTour         = $$.util.tour.current(),
        tourBaseName        = $.type(currentTour) === "string" ? currentTour.trimFromLastIndexOf(".") : "alice.intro",
        nextTourName        = tourBaseName + ".wordpress-running";

    function disableIrrelevantLinks() {
        $(".ss-dynamic-content a")
            .not("[href^='run/" + wordpressRunId + "']")
                .removeAttr("href");
    }

    function updateRunHref() {
        $("a[href^='run/" + wordpressRunId + "']")
            .not(updatedRunIdLinkCls.asSel())
                .addClass(updatedRunIdLinkCls)
                .updateAttr("href", function(s) {
                    return s + "?tour=" + nextTourName ;
                });
        disableIrrelevantLinks();
    }

    $("#runs").on("ss-dynamic-content-updated", updateRunHref);

    if ( $$.util.tour.shouldLaunch(currentTour, true) ) {
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
