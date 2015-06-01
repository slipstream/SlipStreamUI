jQuery( function() { ( function( $$, $, undefined ) {

    var currentTour              = $$.util.tour.current(),
        tourBaseName             = $.type(currentTour) === "string" ? currentTour.trimFromLastIndexOf(".") : "alice.intro",
        hrefStringReplacement    = ["/dashboard", "/dashboard?tour=" + tourBaseName + ".wordpress-in-dashboard&wordpress-run-id=" + $$.model.getRun().getShortUUID() + "&cloud=" + $$.model.getRun().getClouds().first()];

    function updateHref(replacement) {
        $(".ss-action-dashboard")
            .updateAttr("href", function(s) {
                return s.replace.apply(s, replacement);
            });
    }

    function prepareButtonToContinueTour() {
        updateHref(hrefStringReplacement);
    }

    function unprepareButtonToContinueTour() {
        updateHref(hrefStringReplacement.getReversed());
    }

    $$.util.tour.setup({
        beforeStart: function() {
            $$.run.stopAutoupdatingRunPage();
        },

        beforeStep: {
            1: function() {
                $$.util.tour.enableMouseShield();
            },
            2: function() {
                $$.util.tour.enableMouseShield();
            },
            3: function() {
                $$.section.selectWithoutAnimation(1);
                $$.util.tour.disableMouseShield();
            },
            4: function() {
                $$.section.selectWithoutAnimation(2);
                $$.util.tour.disableMouseShield();
            },
            5: function() {
                $$.section.selectWithoutAnimation(3);
                $$.util.tour.disableMouseShield();
            },
            6: function() {
                unprepareButtonToContinueTour();
                $$.section.selectWithoutAnimation($$.section.count);
                $$.util.tour.disableMouseShield();
            },
            7: function() {
                prepareButtonToContinueTour();
                $$.util.tour.disableMouseShield();
            }
        },
        onExit: function() {
            unprepareButtonToContinueTour();
            $$.util.tour.disableMouseShield();
            $$.run.startAutoupdatingRunPage();
        }
    });

    if ( $$.util.tour.shouldLaunchAny($$.util.tour.alice.intro.waitingForWordpress,
                                      $$.util.tour.alice.introWithoutConnectors.waitingForWordpress)) {
        $$.util.tour.start();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
