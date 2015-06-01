jQuery( function() { ( function( $$, $, undefined ) {

    var cloudUsedForTheTour = $$.util.urlQueryParams.getValue("cloud"),
        wordpressRunId      = $$.util.urlQueryParams.getValue("wordpress-run-id"),
        updatedRunIdLinkCls = "ss-updated-run-id",
        currentTour         = $$.util.tour.current(),
        tourBaseName        = $.type(currentTour) === "string" ? currentTour.trimFromLastIndexOf(".") : "alice.intro",
        nextTourName        = tourBaseName + ".wordpress-running";

    function disableIrrelevantLinks() {
        $(".ss-dynamic-subsection a")
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

    $("#runs-" + cloudUsedForTheTour).on("ss-dynamic-subsection-updated", updateRunHref);

    $$.util.tour.setup({

        beforeStep: {
            1: function() {
                $$.section.selectWithoutAnimation(1);
            },
            2: function() {
                $$.section.selectWithoutAnimation(1);
            },
            3: function() {
                var $vmsSection = $$.section.selectWithoutAnimation(3);
                $$.subsection.selectByTitle($vmsSection, cloudUsedForTheTour);
            },
            4: function() {
                $$.section.selectWithoutAnimation(4);
            },
            5: function() {
                // We visit the runs section at the end, so that we can get back to the WordPress run:
                var $runsSection = $$.section.selectWithoutAnimation(2);
                $$.subsection.selectByTitle($runsSection, cloudUsedForTheTour);
            }
        }
    });

    if ( $$.util.tour.shouldLaunchAny($$.util.tour.alice.intro                 .wordpressInDashboard,
                                      $$.util.tour.alice.introWithoutConnectors.wordpressInDashboard),
                                      true) {
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
