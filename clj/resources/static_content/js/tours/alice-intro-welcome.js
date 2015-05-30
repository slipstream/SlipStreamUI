jQuery( function() { ( function( $$, $, undefined ) {

    var buttonPreparedToContinueTourCls = "ss-button-prepared-to-continue-tour",
        queryParamTourName              = $$.util.urlQueryParams.getValue("tour"),
        tourBaseName                    = $.type(queryParamTourName) === "string" ? queryParamTourName.trimFromLastIndexOf(".") : "alice.intro",
        onclickStringReplacement        = ["run';", "run&tour=" + tourBaseName + ".deploying-wordpress';"];

    function prepareButtonToContinueTour() {
        $(".bootstro-highlight a[role=button]")
            .addClass(buttonPreparedToContinueTourCls)
            .updateAttr("onclick", function(s) {
                return s.replace.apply(s, onclickStringReplacement);
            });
    }

    function unprepareButtonToContinueTour() {
        $(buttonPreparedToContinueTourCls.asSel())
            .removeClass(buttonPreparedToContinueTourCls)
            .updateAttr("onclick", function(s) {
                return s.replace.apply(s, onclickStringReplacement.getReversed());
            });
    }

    // Uncomment following line to force asking for tour (for dev purposes), or play it on the browser JS console:
    // SlipStream.util.cookie.delete(SlipStream.util.meta.getUsername() + ".launch-tour.alice.intro.welcome");

    $$.util.tour.setup({
        beforeStart: function() {
            // NOTE: We want the user to explicitely say that she doesn't need the tour
            //       anymore, instead of disabling it the firt time it runs. Therefore,
            //       we explicitely do not run following line here:
            // $$.util.tour.persistDismissal('alice.intro.welcome');
            $$.section.collapseAll();
        },

        beforeStep: {
            0: function() {
                $$.section.collapseAll();
                $$.util.tour.disableMouseShield();
            },
            1: function() {
                $$.section.selectWithoutAnimation(1);
                $$.util.tour.enableMouseShield();
            },
            2: function() {
                $$.util.tour.enableMouseShield();
            },
            3: function() {
                prepareButtonToContinueTour();
                $$.util.tour.disableMouseShield();
            }
        },
        onExit: function(){
            unprepareButtonToContinueTour();
            $$.util.tour.disableMouseShield();
        }
    });

    if ( $$.util.tour.shouldLaunchAny($$.util.tour.alice.intro.welcome, $$.util.tour.alice.introWithoutConnectors.welcome, true) ) {
        if ( $$.util.tour.shouldShowOptInDialog() ) {
            $('#ss-start-tour-dialog').askConfirmation(function () {
                $$.util.tour.start();
            });
        } else {
            $$.util.tour.start();
        }
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
